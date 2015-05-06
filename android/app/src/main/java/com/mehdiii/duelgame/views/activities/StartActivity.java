package com.mehdiii.duelgame.views.activities;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.LoginRequest;
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
    boolean isSent = false;
    long lastLoginRequestTime = -1;
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

                isSent = false;
                stopCircles = true;
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

        centralImage = (ImageView) findViewById(R.id.start_picture);
        layout = (FrameLayout) findViewById(R.id.start_layout);

        // Reads splash colors from resources
        if (splashColorsStr == null) {
            splashColorsStr = getResources().getStringArray(R.array.splash_colors_array);
            splashColors = new int[splashColorsStr.length];
            for (int i = 0; i < splashColorsStr.length; i++)
                splashColors[i] = Color.parseColor(splashColorsStr[i]);
        }

        // TODO organize these lines a bit.
        final String deviceId, simSerialNumber;
        TelephonyManager teleManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        deviceId = teleManager.getDeviceId();
        simSerialNumber = teleManager.getSimSerialNumber();
        userId = deviceId + simSerialNumber;

        LocalBroadcastManager.getInstance(this).registerReceiver(commandListener, DuelApp.getInstance().getIntentFilter());
    }

    private FrameLayout layout;
    private int[] splashColors;
    private String[] splashColorsStr = null;
    ImageView centralImage;
    long startingTime, currentTime;
    final long WAIT_BEFORE_LOGIN = 5000;
    final long WAIT_BEFORE_RECONNECT = 10000;

    private boolean stopCircles = false;

    public void addCircle(final boolean userClicked) {

        ImageView tmp = new ImageView(StartActivity.this);
        tmp.setImageDrawable(getResources().getDrawable(R.drawable.round_tv));
        tmp.setScaleX(centralImage.getWidth() / 2);
        tmp.setScaleY(centralImage.getHeight() / 2);

        tmp.setColorFilter(splashColors[rand.nextInt(splashColors.length)]);

        layout.addView(tmp);

        ObjectAnimator scalex = ObjectAnimator.ofFloat(tmp, "scaleX", 0f, 2f);
        scalex.setDuration(3000);
        scalex.setInterpolator(new LinearInterpolator());
        scalex.start();

        ObjectAnimator scaley = ObjectAnimator.ofFloat(tmp, "scaleY", 0f, 2f);
        scaley.setInterpolator(new LinearInterpolator());
        scaley.setDuration(3000);
        scaley.start();

        ObjectAnimator fadeout = ObjectAnimator.ofFloat(tmp, "alpha", 1f, 0f);
        fadeout.setInterpolator(new LinearInterpolator());
        fadeout.setDuration(3000);
        fadeout.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userClicked)
                    return;

                if (stopCircles == false) {
                    addCircle(false);
                }
            }
        }, 500);
        long diffFromLastLoginRequest = System.currentTimeMillis() - lastLoginRequestTime;
        if (lastLoginRequestTime != -1 && diffFromLastLoginRequest > WAIT_BEFORE_RECONNECT) {
            DuelApp.getInstance().toast(R.string.message_connection_unstable, Toast.LENGTH_LONG);
        }

        long diff = System.currentTimeMillis() - startingTime;
        if (DuelApp.getInstance().getSocket().isConnected() && (lastLoginRequestTime != -1 && diffFromLastLoginRequest > WAIT_BEFORE_RECONNECT || (!isSent && diff > WAIT_BEFORE_LOGIN))) {
            DuelApp.getInstance().sendMessage(new LoginRequest(CommandType.SEND_USER_LOGIN_REQUEST, userId).serialize());
            lastLoginRequestTime = System.currentTimeMillis();
            isSent = true;
        }
    }

    public void clickedLogo(View v) {
        currentTime = System.currentTimeMillis();
        if (currentTime - startingTime < 500)
            return;

        addCircle(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        startingTime = System.currentTimeMillis();
        addCircle(false);

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
//        LocalBroadcastManager.configure(this).unregisterReceiver(commandListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}