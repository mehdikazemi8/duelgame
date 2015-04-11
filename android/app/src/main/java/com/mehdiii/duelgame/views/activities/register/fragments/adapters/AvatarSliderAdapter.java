package com.mehdiii.duelgame.views.activities.register.fragments.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.register.fragments.AvatarWaveFragment;

/**
 * Created by omid on 4/6/2015.
 */
public class AvatarSliderAdapter extends FragmentStatePagerAdapter {

    OnCompleteListener onCompleteListener;
    int pages;

    public void setOnCompleteListener(OnCompleteListener onCompleteListener, int pages) {
        this.onCompleteListener = onCompleteListener;
        this.pages = pages;
    }

    public AvatarSliderAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return pages;
    }

    @Override
    public Fragment getItem(int position) {
        AvatarWaveFragment fragment = new AvatarWaveFragment();
        fragment.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                if (onCompleteListener != null)
                    onCompleteListener.onComplete(data);
            }
        });
        Bundle bundle = new Bundle();
        bundle.putInt(AvatarWaveFragment.ARGS_START_INDEX, position);
        fragment.setArguments(bundle);
        return fragment;
    }
}
