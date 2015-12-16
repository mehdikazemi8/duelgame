package com.mehdiii.duelgame.views.activities.splash;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.UpdateVersion;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.receivers.PokeDuelReceiver;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.home.HomeActivity;
import com.mehdiii.duelgame.views.activities.register.RegisterActivity;
import com.mehdiii.duelgame.views.dialogs.UpdateDialog;

import java.util.Calendar;

public class StartActivity extends ParentActivity {
    private static final int ANIMATION_DURATION_PER_CIRCLE_IN_MILLS = 3000;
    private static final int CIRCLES_COUNT = 1;

    boolean isSent = false;
    RelativeLayout layout;
    int[] splashColors;
    boolean stopCircles = false;
    long startingTime, currentTime;
    ImageView[] circlesImage;
    long[] offsets;
    int counter = 0;
    /**
     * UPDATE DIALOG
     */
    private static long lastCheck = 0;

    BroadcastReceiver commandListener = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {

            if (CommandType.RECEIVE_LOGIN_INFO == type && !isFinishing()) {
                final User user = BaseModel.deserialize(json, User.class);
                if (user != null && user.getUpdateVersion() != null) {
                    displayUpdateDialog(user.getUpdateVersion(), new OnCompleteListener() {
                        @Override
                        public void onComplete(Object data) {
                            loginOrRegisterUser(user);
                        }
                    });
                } else
                    loginOrRegisterUser(user);
            }
        }
    });

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    public void createShortCut() {
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutintent.putExtra("duplicate", false);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext(), StartActivity.class));
        sendBroadcast(shortcutintent);
    }

    private void loginOrRegisterUser(User user) {

        GlobalPreferenceManager.remove(this, PokeDuelReceiver.PREFERENCE_PREVIOUS_CHECK_IN);
        GlobalPreferenceManager.remove(this, PokeDuelReceiver.PREFERENCE_POKE_SEED);

        isSent = false;
        stopCircles = true;
        if (user.getId() == null)
            // register user
            startActivity(new Intent(StartActivity.this, RegisterActivity.class));
        else {
            // login user
            startActivity(new Intent(StartActivity.this, HomeActivity.class));
        }
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Log.d("TAG", "onEvent onCreate");

        cancelDuelHourNotification();

//        try {
//            PurchaseManager.getInstance().consumePurchase();
//        } catch (Exception e) {
//
//        }

        layout = (RelativeLayout) findViewById(R.id.container_wrapper);
        splashColors = SplashColors.getArray(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(commandListener, DuelApp.getInstance().getIntentFilter());

        if (AuthManager.isLoggedin()) {
            loginOrRegisterUser(AuthManager.getCurrentUser());
        }
    }


    private void addAnimations(final ImageView image) {
        if (counter >= CIRCLES_COUNT)
            counter = 0;
        long offset = offsets[counter++];

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0, 70f,
                0, 70f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);

        final AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new LinearInterpolator());
        set.addAnimation(scaleAnimation);
        set.addAnimation(alphaAnimation);
        scaleAnimation.setStartOffset(offset);
        alphaAnimation.setStartOffset(offset);

        set.setDuration(ANIMATION_DURATION_PER_CIRCLE_IN_MILLS);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                addAnimations(image);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        image.setAnimation(set);
    }


    public void addCircles() {
        circlesImage = new ImageView[CIRCLES_COUNT];
        offsets = new long[CIRCLES_COUNT];

        long gap = (ANIMATION_DURATION_PER_CIRCLE_IN_MILLS / CIRCLES_COUNT);
        long delay = 0;
        for (int i = 0; i < CIRCLES_COUNT; i++) {
            ImageView imageView = new ImageView(StartActivity.this);
            RelativeLayout.LayoutParams setting = new RelativeLayout.LayoutParams(50, 50);
            setting.addRule(RelativeLayout.CENTER_IN_PARENT);
            imageView.setLayoutParams(setting);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.round_tv));
            imageView.setColorFilter(splashColors[rand.nextInt(splashColors.length)]);
            layout.addView(imageView);
            circlesImage[i] = imageView;
            offsets[i] = delay;
            delay += gap;
        }

        for (int i = 0; i < CIRCLES_COUNT; i++) {
            addAnimations(circlesImage[i]);
        }

//        long diffFromLastLoginRequest = System.currentTimeMillis() - lastLoginRequestTime;
//        if (lastLoginRequestTime != -1 && diffFromLastLoginRequest > WAIT_BEFORE_RECONNECT) {
//            DuelApp.getInstance().toast(R.string.message_connection_unstable, Toast.LENGTH_LONG);
//        }
//
//        long diff = System.currentTimeMillis() - startingTime;
//        if (DuelApp.getInstance().getSocket().isConnected() && (lastLoginRequestTime != -1 && diffFromLastLoginRequest > WAIT_BEFORE_RECONNECT || (!isSent && diff > WAIT_BEFORE_LOGIN))) {
//            DuelApp.getInstance().sendMessage(new LoginRequest(CommandType.SEND_USER_LOGIN_REQUEST, DeviceManager.getDeviceId(this)).serialize());
//            lastLoginRequestTime = System.currentTimeMillis();
//            isSent = true;
//        }
    }

    public void clickedLogo(View v) {
        currentTime = System.currentTimeMillis();
//        if (currentTime - startingTime < RECREATE_CIRCLE_GAP)
//            return;
//        addCircle(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        startingTime = System.currentTimeMillis();
        addCircles();
    }

    private void displayUpdateDialog(UpdateVersion version, final OnCompleteListener onCompleteListener) {
        //    if previous check is displayed in less than ten minutes, do not interrupt user again.
        long now = Calendar.getInstance().getTime().getTime();
        if (lastCheck != 0 && lastCheck > now - 360000) {
            onCompleteListener.onComplete(false);
            return;
        } else lastCheck = now;

        if (version != null) {
            try {
                int currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
                if (currentVersion < version.getVersion()) {

                    final boolean force = version.getMinSupportedVersion() > currentVersion;
                    final UpdateDialog dialog = new UpdateDialog(this, version, force);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            onCompleteListener.onComplete(force);
                        }
                    });
                    dialog.setCancelable(!force);
                    dialog.show();
                } else onCompleteListener.onComplete(false);
            } catch (PackageManager.NameNotFoundException ex) {
                ex.printStackTrace();
                onCompleteListener.onComplete(false);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCircles = true;
//        LocalBroadcastManager.configure(this).unregisterReceiver(commandListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean showConnectingToServerDialog() {
        return false;
    }
}