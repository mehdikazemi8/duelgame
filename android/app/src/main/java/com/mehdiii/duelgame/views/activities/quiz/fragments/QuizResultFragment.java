package com.mehdiii.duelgame.views.activities.quiz.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.Quiz;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.mehdiii.duelgame.views.custom.CustomTextView;

/**
 * Created by mehdiii on 1/14/16.
 */
public class QuizResultFragment extends Fragment {
    public static QuizResultFragment getInstance() {
        return new QuizResultFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        find(view);
        configure();
    }

    private void find(View view) {
    }

    private void configure() {

    }
}
