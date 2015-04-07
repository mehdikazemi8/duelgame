package com.mehdiii.duelgame.views.activities.register.fragments.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mehdiii.duelgame.views.activities.register.fragments.AvatarWaveFragment;

/**
 * Created by omid on 4/6/2015.
 */
public class AvatarSliderAdapter extends FragmentStatePagerAdapter {

    public AvatarSliderAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new AvatarWaveFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AvatarWaveFragment.ARGS_START_INDEX, position);
        fragment.setArguments(bundle);
        return fragment;
    }
}
