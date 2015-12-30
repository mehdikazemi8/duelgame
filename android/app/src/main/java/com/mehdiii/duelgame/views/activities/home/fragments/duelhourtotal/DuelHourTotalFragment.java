package com.mehdiii.duelgame.views.activities.home.fragments.duelhourtotal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehdiii.duelgame.R;

/**
 * Created by mehdiii on 12/30/15.
 */
public class DuelHourTotalFragment extends Fragment {

    public static DuelHourTotalFragment getInstance() {
        return new DuelHourTotalFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_duel_hour_total, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
