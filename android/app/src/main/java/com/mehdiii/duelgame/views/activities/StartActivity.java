package com.mehdiii.duelgame.views.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.UpdateVersion;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.home.HomeActivity;
import com.mehdiii.duelgame.views.activities.register.RegisterActivity;
import com.mehdiii.duelgame.views.dialogs.UpdateDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class StartActivity extends ParentActivity {
    String userId;
    /**
     * UPDATE DIALOG
     */
    private static long lastCheck = 0;

    BroadcastReceiver commandListener = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {

            if (CommandType.RECEIVE_LOGIN_INFO == type) {
                User user = BaseModel.deserialize(json, User.class);
//                if (user.getUpdateVersion() != null) {
//                    displayUpdateDialog(user.getUpdateVersion());
//                    return;
//                }

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

//        Intent svc = new Intent(this, MusicPlayer.class);
//        startService(svc);
    }

    private void displayUpdateDialog(UpdateVersion version) {
        // if previous check is displayed in less than ten minutes, do not interrupt user again.
        long now = Calendar.getInstance().getTime().getTime();
        if (lastCheck != 0 && lastCheck > now - 360000) {
            return;
        } else lastCheck = now;

        if (version != null) {
            try {
                int currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
                if (currentVersion < version.getVersion()) {

                    boolean force = version.getMinSupportedVersion() > currentVersion;

                    UpdateDialog dialog = new UpdateDialog(this, version, force);
                    dialog.setCancelable(!force);
                    dialog.show();
                }
            } catch (PackageManager.NameNotFoundException ex) {
                ex.printStackTrace();
            }
        }
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