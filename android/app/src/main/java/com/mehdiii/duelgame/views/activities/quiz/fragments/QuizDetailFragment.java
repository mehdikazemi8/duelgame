package com.mehdiii.duelgame.views.activities.quiz.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.FontHelper;

/**
 * Created by frshd on 4/3/16.
 */
public class QuizDetailFragment extends Fragment {
    public static String ARGS_START_INDEX = "ARGS_START_INDEX";
    int startIndex;
    TextView details;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        readArgs();
        if (startIndex==0){
            return inflater.inflate(R.layout.fragment_quiz_detail_overal, container, false);
        }
        else if(startIndex==1){
            return inflater.inflate(R.layout.fragment_quiz_detail_ghalamchi, container, false);
        }
        else if(startIndex==2){
            return inflater.inflate(R.layout.fragment_quiz_detail_sanjesh, container, false);
        }
        else if(startIndex==3){
            return inflater.inflate(R.layout.fragment_quiz_detail_daily, container, false);
        }
        else if(startIndex==4) {
            return inflater.inflate(R.layout.fragment_quiz_detail_heart, container, false);
        }
        else{
            return inflater.inflate(R.layout.fragment_quiz_detail_diamond, container, false);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        details = (TextView) view.findViewById(R.id.textView_caption);
        FontHelper.setKoodakFor(getActivity(), details);
    }

    private void readArgs() {
        Bundle bundle = getArguments();
        startIndex = bundle.getInt(ARGS_START_INDEX, 0);
    }

}
