package com.mehdiii.duelgame.views.activities.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter /*implements PagerSlidingTabStrip.IconTabProvider */ {

    private List<Fragment> fragments;
    public List<String> categories = null;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> headlines) {
        super(fm);
        this.fragments = fragments;
        this.categories = headlines;
    }

    public List<Fragment> exportList() {
        return fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.categories.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}