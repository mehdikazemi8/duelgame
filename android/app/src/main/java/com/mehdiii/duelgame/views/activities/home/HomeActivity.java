package com.mehdiii.duelgame.views.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.DuelMusicPlayer;
import com.mehdiii.duelgame.views.activities.CategoryActivity;
import com.mehdiii.duelgame.views.activities.MyBaseActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.FlipableFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.friends.FriendsFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.home.HomeFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.ranking.RankingFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.settings.SettingsFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.store.StoreFragment;
import com.mehdiii.duelgame.views.custom.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends MyBaseActivity {

    ViewPager viewPager;
    ViewPagerAdapter adapter;

    ToggleButton storeButton;
    ToggleButton rankingButton;
    ToggleButton settingsButton;
    ToggleButton homeButton;
    ToggleButton friendsButton;
    ToggleButton previous;

    FlipableFragment storeFragment;
    FlipableFragment rankingFragment;
    FlipableFragment settingsFragment;
    FlipableFragment homeFragment;
    FlipableFragment friendsFagment;


    List<Fragment> childFragments;

    DuelMusicPlayer musicPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        musicPlayer = new DuelMusicPlayer(HomeActivity.this, R.raw.music, true);
        musicPlayer.execute();

        find();
        configure();

//        readData();
//
//        setData();
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
                    friendsFagment.onBringToFront();
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

        settingsFragment = (FlipableFragment) Fragment.instantiate(this, SettingsFragment.class.getName(), null);
        rankingFragment = (FlipableFragment) Fragment.instantiate(this, RankingFragment.class.getName(), null);
        friendsFagment = (FlipableFragment) Fragment.instantiate(this, FriendsFragment.class.getName(), null);
        storeFragment = (FlipableFragment) Fragment.instantiate(this, StoreFragment.class.getName(), null);
        homeFragment = (FlipableFragment) Fragment.instantiate(this, HomeFragment.class.getName(), null);

        childFragments.add(settingsFragment);
        childFragments.add(storeFragment);
        childFragments.add(rankingFragment);
        childFragments.add(friendsFagment);
        childFragments.add(homeFragment);
    }


    public void setTextView(int id, String str) {
        ((TextView) findViewById(id)).setText(str);
    }

    private void readData() {
//        try {
//            JSONObject parser = new JSONObject(loginInfo);
//            myAvatarIndex = parser.getInt("avatar");
//            userDiamond = parser.getInt("time");
//            myOstanInt = parser.getInt("ostan");
//            myScore = parser.getInt("score");
//            myUserNumber = parser.getString("user_number");
//            myElo = (int) parser.getDouble("elo");
//            myName = parser.getString("name");
//
//        } catch (JSONException e) {
//
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_start, menu);

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
    public void onResume() {
        super.onResume();
//        setData();

        musicPlayer.playSound();
//        Intent svc = new Intent(this, MusicPlayer.class);
//        startService(svc);
    }

    @Override
    public void onBackPressed() {
//        Intent svc = new Intent(this, MusicPlayer.class);
//        stopService(svc);
        finish();
    }

    public void wantToPlay(View v) {
        Log.d("&&&&", "aaaaaaaaaaaaaaaa");
        musicPlayer.pauseSound();
        startActivity(new Intent(this, CategoryActivity.class));
    }

    // ******************************** HOME BUTTONE PRESSED

    @Override
    public void onPause() {
        super.onPause();

        musicPlayer.pauseSound();
//        Intent svc = new Intent(this, MusicPlayer.class);
//        stopService(svc);
    }

    @Override
    public void onStop() {
        super.onStop();

//        Intent svc = new Intent(this, MusicPlayer.class);
//        stopService(svc);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        Intent svc = new Intent(this, MusicPlayer.class);
//        stopService(svc);
    }
}
