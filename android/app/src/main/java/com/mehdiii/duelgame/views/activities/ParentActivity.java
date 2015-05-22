package com.mehdiii.duelgame.views.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.LoginRequest;
import com.mehdiii.duelgame.models.Question;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnConnectionStateChanged;
import com.mehdiii.duelgame.utils.DeviceManager;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.dialogs.ConnectionLostDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.greenrobot.event.EventBus;

public class ParentActivity extends ActionBarActivity {

    protected BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_LOGIN_INFO) {
                User user = BaseModel.deserialize(json, User.class);
                if (user != null) {
                    AuthManager.authenticate(ParentActivity.this, user);
                }
            }
        }
    });

    protected static Random rand = new Random();
    static String category;
    static int NUMBER_OF_QUESTIONS = 6;
    protected static List<Question> questionsToAsk = new ArrayList<>();
    static int userPoints;
    static int opponentPoints;
    static final int NUMBER_OF_AVATARS = 6;

    static void shuffleArray(List<String> ar) {
        for (int i = ar.size() - 1; i > 0; i--) {
            int index = rand.nextInt(i) + 1;
            String a = ar.get(index);
            ar.set(index, ar.get(i));
            ar.set(i, a);
        }

        int index = rand.nextInt(ar.size());
        String a = ar.get(0);
        ar.set(0, ar.get(index));
        ar.set(index, a);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, DuelApp.getInstance().getIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    ProgressDialog dialog;

    public void onEvent(OnConnectionStateChanged status) {
        if (status.getState() == OnConnectionStateChanged.ConnectionState.DISCONNECTED ||
                status.getState() == OnConnectionStateChanged.ConnectionState.CONNECTED) {
            if (dialog != null)
                dialog.dismiss();
        }

        if (status.getState() == OnConnectionStateChanged.ConnectionState.DISCONNECTED) {
            ConnectionLostDialog dialog = new ConnectionLostDialog(this);
            dialog.setCancelable(false);
            dialog.show();
        } else if (status.getState() == OnConnectionStateChanged.ConnectionState.CONNECTED) {
            DuelApp.getInstance().sendMessage(new LoginRequest(CommandType.SEND_USER_LOGIN_REQUEST, DeviceManager.getDeviceId(this)).serialize());
        } else if (status.getState() == OnConnectionStateChanged.ConnectionState.CONNECTING) {
            if (showConnectingToServerDialog()) {
                dialog = new ProgressDialog(this);
                dialog.setMessage(getResources().getString(R.string.message_connecting_to_server));
                dialog.setCancelable(false);
                dialog.show();
            }
        }
    }

    // sub classes can change this configuration by overriding this method. default value is `true`
    public boolean showConnectingToServerDialog() {
        return true;
    }
}