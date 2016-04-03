package com.mehdiii.duelgame.views.activities.quiz.fragments.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.quiz.fragments.QuizDetailFragment;
import com.mehdiii.duelgame.views.activities.register.fragments.AvatarWaveFragment;

/**
 * Created by frshd on 4/3/16.
 */
public class QuizSliderAdapter extends FragmentStatePagerAdapter{
    OnCompleteListener onCompleteListener;
    int pages;

    public QuizSliderAdapter(FragmentManager fm) {
        super(fm);
    }



    public void setOnCompleteListener(OnCompleteListener onCompleteListener, int pages) {
        this.onCompleteListener = onCompleteListener;
        this.pages = pages;
    }

    @Override
    public int getCount() {
        return pages;
    }

    @Override
    public Fragment getItem(int position) {
        QuizDetailFragment quizDetailFragment = new QuizDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(quizDetailFragment.ARGS_START_INDEX, position);
        quizDetailFragment.setArguments(bundle);
        return quizDetailFragment;
    }
}
