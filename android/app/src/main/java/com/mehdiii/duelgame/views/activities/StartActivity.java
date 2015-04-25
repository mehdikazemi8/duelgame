package com.mehdiii.duelgame.views.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.MusicPlayer;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.home.HomeActivity;
import com.mehdiii.duelgame.views.activities.register.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends MyBaseActivity {
    String userId;

    BroadcastReceiver commandListener = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {

            if (CommandType.RECEIVE_LOGIN_INFO == type) {
                User user = BaseModel.deserialize(json, User.class);
                if (user.getId() == null)
                    startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                else {
                    AuthManager.authenticate(user);
                    startActivity(new Intent(StartActivity.this, HomeActivity.class));
                }
                finish();
            }
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // TODO organize these lines a bit.
        final String deviceId, simSerialNumber;

        TelephonyManager teleManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        deviceId = teleManager.getDeviceId();
        simSerialNumber = teleManager.getSimSerialNumber();
        userId = deviceId + simSerialNumber;

        if (!DuelApp.getInstance().getSocket().isConnected()) {
            Log.d("DUELAPP", "not connected");
            startActivity(new Intent(this, TryToConnectActivity.class));
        }

        ViewCompat.postOnAnimationDelayed(new TextView(this), new Runnable() {
            public void run() {
                if (DuelApp.getInstance().getSocket().isConnected()) {
                    JSONObject query = new JSONObject();
                    try {
                        query.put("code", "UL");
                        query.put("user_id", userId);
                        DuelApp.getInstance().sendMessage(query.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 2000);
        LocalBroadcastManager.getInstance(this).registerReceiver(commandListener, DuelApp.getInstance().getIntentFilter());
    }

    @Override
    public void onResume() {
        super.onResume();


        Intent svc = new Intent(this, MusicPlayer.class);
        startService(svc);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(commandListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}