package com.mehdiii.duelgame.views.activities.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.managers.PurchaseManager;
import com.mehdiii.duelgame.models.BuyNotification;
import com.mehdiii.duelgame.models.ChangePage;
import com.mehdiii.duelgame.models.CourseMap;
import com.mehdiii.duelgame.models.DrawerItem;
import com.mehdiii.duelgame.models.SendGcmCode;
import com.mehdiii.duelgame.models.StepCourse;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnPurchaseResult;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.utils.UserFlowHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.addquestion.AddQuestionFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.duelhour.DuelHourFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.friends.FriendsFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.home.HomeFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.onlineusers.OnlineUsersFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.settings.SettingsFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.store.StoreFragment;
import com.mehdiii.duelgame.views.custom.ToggleButton;
import com.mehdiii.duelgame.views.dialogs.AlertDialog;
import com.mehdiii.duelgame.views.dialogs.ConfirmDialog;
import com.mehdiii.duelgame.views.dialogs.OptionsMenuDialog;
import com.mehdiii.duelgame.views.dialogs.ScoresDialog;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends ParentActivity {
    public static final String TAG = "TAG_GCM";
    private static final int REQUEST_CODE_PURCHASE = 10001;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static GoogleCloudMessaging gcm;
    static Context context;
    static String regid;
    static String SENDER_ID = "753066845572";
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    ToggleButton onlineUsersButton;
    ToggleButton duelHourButton;
    ToggleButton moreOptionsButton;
    ToggleButton homeButton;
    ToggleButton friendsButton;
    ToggleButton previous;

    FlippableFragment homeFragment;
    FlippableFragment onlineUsersFragment;
    FlippableFragment friendsFragment;
    FlippableFragment duelHourFragment;

    List<Fragment> childFragments;
//    ScoresDialog scoresDialog;

    private View.OnClickListener pageSelectorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(view.getId() == R.id.button_more_options) {
//                if (!UserFlowHelper.gotDuel()){
//                    setHomeButtonsState(homeButton, "برای اینکه بتونی گزینه‌های بیشتر رو ببینی باید اول در یک \n\nدوئل\n\n شرکت کنی.");
//                    return;
//                } else
                if (!UserFlowHelper.gotQuiz()){
                    setHomeButtonsState(homeButton, "برای اینکه بتونی گزینه‌های بیشتر رو ببینی باید اول در یک \n\nآزمون\n\n شرکت کنی.");
                    return;
                } else {
                    OptionsMenuDialog menuDialog = new OptionsMenuDialog();
                    menuDialog.setContext(HomeActivity.this);
                    menuDialog.show(getSupportFragmentManager(), "OPTIONS_MENU");
                    return;}
            }

            if (previous != null)
                previous.toggle();
            ((ToggleButton) view).toggle();
            previous = (ToggleButton) view;

            switch (view.getId()) {
                case R.id.button_home:
                    viewPager.setCurrentItem(3, true);
                    break;

                case R.id.button_friends:
//                    if (!UserFlowHelper.gotDuel()){
//                        setHomeButtonsState(friendsButton, "برای اینکه بتونی لیست دوستانت رو ببینی باید اول در یک \n\nدوئل\n\n شرکت کنی.");
//                        break;
//                    } else
                    if (!UserFlowHelper.gotQuiz()){
                        setHomeButtonsState(friendsButton, "برای اینکه بتونی لیست دوستانت رو ببینی باید اول در یک \n\nآزمون\n\n شرکت کنی.");
                        break;
                    } else {
                        viewPager.setCurrentItem(2, true);
                        break;
                    }

                case R.id.button_duel_hour:
//                    if(!UserFlowHelper.gotDuel()){
//                        setHomeButtonsState(duelHourButton, "برای اینکه بتونی  رتبه‌های ساعت دوئل رو ببینی باید اول در یک \n\nدوئل\n\n شرکت کنی.");
//                        break;
//                    } else
                    if (!UserFlowHelper.gotQuiz()){
                        setHomeButtonsState(duelHourButton, "برای اینکه بتونی  رتبه‌های ساعت ( رو ببینی باید اول در یک \n\nآزمون\n\n شرکت کنی.");
                        break;
                    } else {
                        viewPager.setCurrentItem(1, true);
                        break;
                    }

                case R.id.button_online_users:
//                    if(!UserFlowHelper.gotDuel()){
//                        setHomeButtonsState(onlineUsersButton, "برای اینکه بتونی دیگران رو به لیست دوستات اضافه کنی باید اول در یک \n\nدوئل\n\n شرکت کنی.");
//                        break;
//                    } else
                    if (!UserFlowHelper.gotQuiz()){
                        setHomeButtonsState(onlineUsersButton, "برای اینکه بتونی دیگران رو به لیست دوستات اضافه کنی باید اول در یک \n\nآزمون\n\n شرکت کنی.");
                        break;
                    } else {
                        viewPager.setCurrentItem(0, true);
                        break;
                    }
            }
        }
    };

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }


        @Override
        public void onPageSelected(int position) {

            if (previous != null)
                previous.toggle();

            ToggleButton selection = null;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++)
                        getSupportFragmentManager().popBackStack();}
            });

            switch (position) {
                case 0:
                    selection = onlineUsersButton;
                    onlineUsersFragment.onBringToFront();
                    break;
                case 1:
                    selection = duelHourButton;
                    duelHourFragment.onBringToFront();
                    break;
                case 2:
                    selection = friendsButton;
                    friendsFragment.onBringToFront();
                    break;
                case 3:
                    selection = homeButton;
                    homeFragment.onBringToFront();
                    break;
            }


            if (selection != null) {
                selection.toggle();
                previous = selection;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

    };

    private void setHomeButtonsState(ToggleButton toggledButton, String msg){
        AlertDialog dialog = new AlertDialog(HomeActivity.this, msg);
        dialog.show();
        homeButton.toggle();
        toggledButton.toggle();
        previous = homeButton;
    }

    public static void registerInBackground() {
        registerInBackground(null);
    }

    public static void registerInBackground(final OnCompleteListener onComplete) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg;
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }

                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            }
        }.execute(null, null, null);
    }

    public static void sendRegistrationIdToBackend() {
        DuelApp.getInstance().sendMessage(new SendGcmCode(regid).serialize(CommandType.SEND_GCM_CODE));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!PurchaseManager.getInstance().handleActivityResult(resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = getApplicationContext();



        setAlarmForDuelHour(this);
        setAlarmForFlashCards(this);
        cancelDuelHourNotification();

        find();
        configure();

        // Check device for Play Services APK.
        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = DuelApp.getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, DuelApp.getInstance().getIntentFilter());
        PurchaseManager.changeActivity(this);

        if(! DuelApp.getInstance().isConnected())
            DuelApp.getInstance().connectToWs();

        DuelApp.getInstance().sendMessage(new BaseModel().serialize(CommandType.GET_COURSE_MAP));
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;

    }

    public void viewLevels(View view) {
        //scoresDialog.show();
    }

    private void find() {
        this.viewPager = (ViewPager) findViewById(R.id.viewpager_main);
        this.moreOptionsButton = (ToggleButton) findViewById(R.id.button_more_options);
        this.homeButton = (ToggleButton) findViewById(R.id.button_home);
        this.friendsButton = (ToggleButton) findViewById(R.id.button_friends);
        this.onlineUsersButton = (ToggleButton) findViewById(R.id.button_online_users);
        this.duelHourButton = (ToggleButton) findViewById(R.id.button_duel_hour);
    }

    private void configure() {
        //scoresDialog = new ScoresDialog(HomeActivity.this);

        createChildFragments();

        if (previous == null)
            previous = this.homeButton.toggle();

//        this.storeButton.setOnClickListener(pageSelectorClickListener);
        this.friendsButton.setOnClickListener(pageSelectorClickListener);
        this.onlineUsersButton.setOnClickListener(pageSelectorClickListener);
        this.duelHourButton.setOnClickListener(pageSelectorClickListener);
        this.homeButton.setOnClickListener(pageSelectorClickListener);
//        this.rankingButton.setOnClickListener(pageSelectorClickListener);
        this.moreOptionsButton.setOnClickListener(pageSelectorClickListener);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), childFragments, null);
        viewPager.setAdapter(adapter);
        this.viewPager.setOnPageChangeListener(pageChangeListener);
        this.viewPager.setCurrentItem(3);
        viewPager.setOffscreenPageLimit(4);
    }

    private void createChildFragments() {
        childFragments = new ArrayList<>();

        friendsFragment = (FlippableFragment) Fragment.instantiate(this, FriendsFragment.class.getName(), null);
        duelHourFragment = (FlippableFragment) Fragment.instantiate(this, DuelHourFragment.class.getName(), null);
        onlineUsersFragment = (FlippableFragment) Fragment.instantiate(this, OnlineUsersFragment.class.getName(), null);
        homeFragment = (FlippableFragment) Fragment.instantiate(this, HomeFragment.class.getName(), null);

        childFragments.add(onlineUsersFragment);
        childFragments.add(duelHourFragment);
        childFragments.add(friendsFragment);
        childFragments.add(homeFragment);
    }

    @Override
    public void onBackPressed() {
        Log.d("TAG", "onBackPressed " + getSupportFragmentManager().getFragments().size());

        if( getSupportFragmentManager().findFragmentByTag(ParentActivity.STORE_FRAGMENT) != null ) {
            getSupportFragmentManager().popBackStack();
            return;
        }

        if( getSupportFragmentManager().findFragmentByTag(ParentActivity.DUEL_HOUR_TOTAL_FRAGMENT) != null ) {
            getSupportFragmentManager().popBackStack();
            return;
        }

        if( getSupportFragmentManager().findFragmentByTag(ParentActivity.SETTINGS_FRAGMENT) != null ) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        if( getSupportFragmentManager().findFragmentByTag(ParentActivity.ADD_QUESTION_FRAGMENT) != null ) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        ConfirmDialog dialog = new ConfirmDialog(this, getResources().getString(R.string.message_are_you_sure_to_exit));
        dialog.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                if ((Boolean) data) {
                    //HomeActivity.super.onBackPressed();
                    // TODO: don't disconnect, add notification instead
                    DuelApp.getInstance().disconnect();
                    finish();
                    System.exit(0);
                }
            }
        });
        dialog.show();
    }

//    public void onEvent(BuyNotification notif) {
//
//        switch (notif.getType()) {
//            case 1:
//                PurchaseManager.getInstance().startPurchase(notif.getId());
//                break;
//            case 2:
//                PurchaseManager.getInstance().useDiamond(notif);
//                break;
//        }
//    }

    public void onEvent(ChangePage change) {
        Log.d("TAG", "onEvent ChangePage " + change.getPage());

        // store fragment
        if(change.getPage() == ParentActivity.STORE_PAGE) {
            FlippableFragment storeFragment = (FlippableFragment) Fragment.instantiate(this, StoreFragment.class.getName(), null);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, storeFragment, ParentActivity.STORE_FRAGMENT)
                    .addToBackStack(null)
                    .commit();
        }

        // settings fragment
        if(change.getPage() == ParentActivity.SETTINGS_PAGE) {
            FlippableFragment settingsFragment = (FlippableFragment) Fragment.instantiate(this, SettingsFragment.class.getName(), null);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, settingsFragment, ParentActivity.SETTINGS_FRAGMENT)
                    .addToBackStack(null)
                    .commit();

            settingsFragment.onBringToFront();
        }

        // add question fragment
        if(change.getPage() == ParentActivity.ADD_QUESTION_PAGE) {
            FlippableFragment addQuestionFragment = (FlippableFragment) Fragment.instantiate(this, AddQuestionFragment.class.getName(), null);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, addQuestionFragment, ParentActivity.ADD_QUESTION_FRAGMENT)
                    .addToBackStack(null)
                    .commit();

            addQuestionFragment.onBringToFront();
        }
    }

    @Override
    public void onEvent(OnPurchaseResult alert) {
        if(alert == null) {
            Log.d("TAG", "alert is null");
        } else if(alert.getStatus() == null) {
            Log.d("TAG", "alert.getStatus is null");
        }

        if (alert != null && alert.getStatus() != null && alert.getStatus().equals("invalid"))
            DuelApp.getInstance().toast(R.string.message_invalid_purchase, Toast.LENGTH_LONG);
//        else
//            scoresDialog.setUserScore(AuthManager.getCurrentUser().getScore());
    }

    public void clickSample(View view) {

    }

    @Override
    public boolean canHandleChallengeRequest() {
        return true;
    }

    private BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_COURSE_MAP) {
                CourseMap cm = CourseMap.deserialize(json, CourseMap.class);
//                Log.d("TAG", "course map received" + cm.serialize());
                User user = AuthManager.getCurrentUser();
                if(user != null) {
                    user.setCourseMap(cm);
                }
//                Log.d("TAG", "course map received" + user.getCourseMap().serialize());
            }
        }
    });


//    TODO GCM Receive
//    BroadcastReceiver commandListener = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
//        @Override
//        public void onReceive(String json, CommandType type) {
//            if (CommandType.GET_COURSE_MAP == type && !isFinishing()) {
////                final User user = BaseModel.deserialize(json, User.class);
//                Log.d("taggg", "" + (json));
//            }
//        }
//    });

}
