package com.mehdiii.duelgame.views.activities.category.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mehdiii.duelgame.R;

/**
 * Created by mehdiii on 7/12/15.
 */
public class RankPlayFragment extends Fragment {

    private int category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        category = getArguments().getInt("category", 0);

        return inflater.inflate(R.layout.fragment_rank_play, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ((TextView)view.findViewById(R.id.play)).setContentDescription(String.valueOf(category));
        ((TextView)view.findViewById(R.id.ranking)).setContentDescription(String.valueOf(category));
    }
}
