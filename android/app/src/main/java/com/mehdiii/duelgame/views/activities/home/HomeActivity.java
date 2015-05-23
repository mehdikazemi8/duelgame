package com.mehdiii.duelgame.views.activities.home;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.android.vending.billing.IInAppBillingService;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.PurchaseManager;
import com.mehdiii.duelgame.models.BuyNotification;
import com.mehdiii.duelgame.models.ChangePage;
import com.mehdiii.duelgame.models.DuelOpponentRequest;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnSoundStateChanged;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.utils.ScoreHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.friends.FriendsFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.home.HomeFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.ranking.RankingFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.settings.SettingsFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.store.StoreFragment;
import com.mehdiii.duelgame.views.custom.ToggleButton;
import com.mehdiii.duelgame.views.dialogs.AnswerOfChallengeRequestDialog;
import com.mehdiii.duelgame.views.dialogs.ConfirmDialog;
import com.mehdiii.duelgame.views.dialogs.ScoresDialog;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!PurchaseManager.getInstance().handleActivityResult(resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        find();
        configure();

        PurchaseManager.init(HomeActivity.this, REQUEST_CODE_PURCHASE);
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

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, DuelApp.getInstance().getIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_CHALLENGE_REQUEST) {
                DuelOpponentRequest request = BaseModel.deserialize(json, DuelOpponentRequest.class);
                AnswerOfChallengeRequestDialog dialog = new AnswerOfChallengeRequestDialog(HomeActivity.this, request);
                dialog.setCancelable(false);
                dialog.show();

            }
        }
    });
}
