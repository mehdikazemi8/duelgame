xpackage com.mehdiii.duelgame.views.activities.home;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.PurchaseManager;
import com.mehdiii.duelgame.models.BuyNotification;
import com.mehdiii.duelgame.models.ChangePage;
import com.mehdiii.duelgame.models.events.OnUserSettingsChanged;
import com.mehdiii.duelgame.models.SendGcmCode;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.friends.FriendsFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.home.HomeFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.ranking.RankingFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.settings.SettingsFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.store.StoreFragment;
import com.mehdiii.duelgame.views.custom.ToggleButton;
import com.mehdiii.duelgame.views.dialogs.ConfirmDialog;
import com.mehdiii.duelgame.views.dialogs.ScoresDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends ParentActivity {
    private static final int REQUEST_CODE_PURCHASE = 10001;

    ViewPager viewPager;
    ViewPagerAdapter adapter;

    ToggleButton storeButton;
    ToggleButton rankingButton;
    ToggleButton settingsButton;
    ToggleButton homeButton;
    ToggleButton friendsButton;
    ToggleButton previous;

    FlippableFragment storeFragment;
    FlippableFragment rankingFragment;
    FlippableFragment settingsFragment;
    FlippableFragment homeFragment;
    FlippableFragment friendsFragment;

    List<Fragment> childFragments;


//    public DuelMusicPlayer musicPlayer;

    static GoogleCloudMessaging gcm;
    static Context context;
    static String regid;
    static String SENDER_ID = "753066845572";

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

        find();
        configure();

        PurchaseManager.init(HomeActivity.this, REQUEST_CODE_PURCHASE);

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

    public static void registerInBackground() {
        registerInBackground(null);
    }

    public static void registerInBackground(final OnCompleteListener onComplete) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
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

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String TAG = "TAG_GCM";

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

    ScoresDialog scoresDialog;

    public void viewLevels(View view) {
        scoresDialog.show();
    }

    private void find() {
        this.viewPager = (ViewPager) findViewById(R.id.viewpager_main);
        this.storeButton = (ToggleButton) findViewById(R.id.button_store);
        this.rankingButton = (ToggleButton) findViewById(R.id.button_ranking);
        this.settingsButton = (ToggleButton) findViewById(R.id.button_settings);
        this.homeButton = (ToggleButton) findViewById(R.id.button_home);
        this.friendsButton = (ToggleButton) findViewById(R.id.button_friends);
    }

    private void configure() {
        scoresDialog = new ScoresDialog(HomeActivity.this);

        createChildFragments();

        if (previous == null)
            previous = this.homeButton.toggle();

        this.storeButton.setOnClickListener(pageSelectorClickListener);
        this.friendsButton.setOnClickListener(pageSelectorClickListener);
        this.homeButton.setOnClickListener(pageSelectorClickListener);
        this.rankingButton.setOnClickListener(pageSelectorClickListener);
        this.settingsButton.setOnClickListener(pageSelectorClickListener);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), childFragments, null);
        viewPager.setAdapter(adapter);
        this.viewPager.setOnPageChangeListener(pageChangeListener);
        this.viewPager.setCurrentItem(4);
        viewPager.setOffscreenPageLimit(5);
    }


    private View.OnClickListener pageSelectorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (previous != null)
                previous.toggle();
            ((ToggleButton) view).toggle();
            previous = (ToggleButton) view;

            switch (view.getId()) {
                case R.id.button_home:
                    viewPager.setCurrentItem(4, true);
                    break;
                case R.id.button_friends:
                    viewPager.setCurrentItem(3, true);
                    break;
                case R.id.button_ranking:
                    viewPager.setCurrentItem(2, true);
                    break;
                case R.id.button_store:
                    viewPager.setCurrentItem(1, true);
                    break;
                case R.id.button_settings:
                    viewPager.setCurrentItem(0, true);
                    break;
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
                        getSupportFragmentManager().popBackStack();
                }
            });

            switch (position) {
                case 0:
                    selection = settingsButton;
                    settingsFragment.onBringToFront();
                    break;
                case 1:
                    selection = storeButton;
                    storeFragment.onBringToFront();
                    break;
                case 2:
                    selection = rankingButton;
                    rankingFragment.onBringToFront();
                    break;
                case 3:
                    selection = friendsButton;
                    friendsFragment.onBringToFront();
                    break;
                case 4:
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

    private void createChildFragments() {
        childFragments = new ArrayList<>();

        settingsFragment = (FlippableFragment) Fragment.instantiate(this, SettingsFragment.class.getName(), null);
        rankingFragment = (FlippableFragment) Fragment.instantiate(this, RankingFragment.class.getName(), null);
        friendsFragment = (FlippableFragment) Fragment.instantiate(this, FriendsFragment.class.getName(), null);
        storeFragment = (FlippableFragment) Fragment.instantiate(this, StoreFragment.class.getName(), null);
        homeFragment = (FlippableFragment) Fragment.instantiate(this, HomeFragment.class.getName(), null);

        childFragments.add(settingsFragment);
        childFragments.add(storeFragment);
        childFragments.add(rankingFragment);
        childFragments.add(friendsFragment);
        childFragments.add(homeFragment);
    }

    @Override
    public void onBackPressed() {
        ConfirmDialog dialog = new ConfirmDialog(this, getResources().getString(R.string.message_are_you_sure_to_exit));
        dialog.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                if ((Boolean) data)
                    HomeActivity.super.onBackPressed();
            }
        });
        dialog.show();
    }

    public void onEvent(BuyNotification buyNotification) {

        switch (buyNotification.getType()) {
            case 1:
                PurchaseManager.getInstance().initiatePurchase(buyNotification);
                break;
            case 2:
                PurchaseManager.getInstance().useDiamond(buyNotification);
                break;
        }
    }

    public void onEvent(ChangePage change) {
        viewPager.setCurrentItem(change.getPage());
    }

    public void onEvent(OnUserSettingsChanged settings) {
        scoresDialog.setUserScore(AuthManager.getCurrentUser().getScore());
    }
}
