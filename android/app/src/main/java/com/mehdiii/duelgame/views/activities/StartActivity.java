package com.mehdiii.duelgame.views.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.MusicPlayer;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.views.activities.home.HomeActivity;
import com.mehdiii.duelgame.views.activities.register.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends MyBaseActivity {

    protected class TitleBarListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("MESSAGE")) {
                User user = User.deserialize(intent.getExtras().getString("inputMessage"),
                        User.class);
                if (user.getCommandType() == User.CommandType.GET_INFO) {
                    if (user.getId() == null)
                        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                    else {
                        AuthManager.authenticate(user);
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    }
                    finish();
                }
            }
        }
    }

    TitleBarListener mListener;
    String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mListener = new TitleBarListener();

        LocalBroadcastManager.getInstance(this).registerReceiver(mListener, new IntentFilter("MESSAGE"));

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

                        Log.d("--- Start Activity", query.toString());

                        DuelApp.getInstance().sendMessage(query.toString());
                    } catch (JSONException e) {
                        Log.d("---- START ACTIVITY", e.toString());
                    }
                }
            }
        }, 2000);
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent svc = new Intent(this, MusicPlayer.class);
        startService(svc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mListener);
    }

}