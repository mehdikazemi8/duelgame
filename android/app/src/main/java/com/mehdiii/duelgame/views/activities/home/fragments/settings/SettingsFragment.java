package com.mehdiii.duelgame.views.activities.home.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.views.activities.home.fragments.FlipableFragment;

/**
 * Created by omid on 4/5/2015.
 */
public class SettingsFragment extends FlipableFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }
}