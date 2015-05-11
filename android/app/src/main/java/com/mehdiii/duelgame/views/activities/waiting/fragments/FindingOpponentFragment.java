package com.mehdiii.duelgame.views.activities.waiting.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.FontHelper;

/**
 * Created by MeHdi on 5/11/2015.
 */
public class FindingOpponentFragment extends Fragment {

    private TextView noticeTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_waiting_finding_opponent, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        find(view);
        FontHelper.setKoodakFor(getActivity(), noticeTextView);
    }

    private void find(View view) {
        noticeTextView = (TextView) view.findViewById(R.id.finding_opponent_notice);
    }
}
