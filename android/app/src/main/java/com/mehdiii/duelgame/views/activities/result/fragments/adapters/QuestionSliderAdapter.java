package com.mehdiii.duelgame.views.activities.result.fragments.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.result.fragments.ShowQuestionFragment;

import java.util.ArrayList;

/**
 * Created by MeHdi on 5/25/2015.
 */
public class QuestionSliderAdapter extends FragmentStatePagerAdapter {

    int pages;
    ArrayList<Integer> correctOptions = new ArrayList<Integer>();

    public QuestionSliderAdapter(FragmentManager fm, int pages, ArrayList<Integer> correctOptions) {
        super(fm);
        this.pages = pages;
        this.correctOptions = correctOptions;
        Log.d("trace", "constructor QuestionSliderAdapter");
    }

    @Override
    public int getCount() {
        Log.d("trace", "getCount QuestionSliderAdapter");
        return pages;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("trace", "getItem QuestionSliderAdapter " + position);

        ShowQuestionFragment fragment = new ShowQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(ShowQuestionFragment.ARGUMENT_OPTIONS, ParentActivity.questionsToAsk.get(position).getOptions());
        bundle.putString(ShowQuestionFragment.ARGUMENT_STATEMENT, ParentActivity.questionsToAsk.get(position).getQuestionText());
        bundle.putInt(ShowQuestionFragment.ARGUMENT_CORRECT_OPTION, correctOptions.get(position));
        fragment.setArguments(bundle);

        return fragment;
    }
}
