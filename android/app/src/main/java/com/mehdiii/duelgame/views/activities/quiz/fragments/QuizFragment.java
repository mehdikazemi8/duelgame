package com.mehdiii.duelgame.views.activities.quiz.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.QuestionForQuiz;
import com.mehdiii.duelgame.models.Quiz;
import com.mehdiii.duelgame.views.custom.CustomTextView;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;

/**
 * Created by mehdiii on 1/14/16.
 */
public class QuizFragment extends Fragment {

    Quiz quiz;
    long remainingTime;
    CustomTextView quizTimerTextView;
    CountDownTimer countDownTimer;

    public QuizFragment() {
        super();
    }

    public static QuizFragment getInstance() { return new QuizFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        quiz = Quiz.deserialize(getArguments().getString("quiz"), Quiz.class);
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        find(view);
        configure();
    }

    private void find(View view) {
        quizTimerTextView = (CustomTextView) view.findViewById(R.id.quiz_timer);
    }

    private void configure() {
        // calculating remaining time of the quiz and setting start of the quiz if needed
        long startTime = GlobalPreferenceManager.readLong(getActivity(), quiz.getId(), -1l);
        remainingTime = quiz.getDuration();
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis()/1000;
        if(startTime != -1) {
            remainingTime -= now - startTime;
        } else {
            GlobalPreferenceManager.writeLong(getActivity(), quiz.getId(), now);
        }

        // setting timer
        quizTimerTextView.setText(calculateRemainingTime(remainingTime));
        countDownTimer = new CountDownTimer(remainingTime*1000, 1000) {
            @Override
            public void onTick(long l) {
                remainingTime -= 1;
                Log.d("TAG", "onTick " + remainingTime);
                quizTimerTextView.setText(calculateRemainingTime(remainingTime));
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();

    }

    private String calculateRemainingTime(long sec) {
        return String.format("%d:%d", sec/60, sec%60);
    }
}
