package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.result.GameResultActivity;
import com.mehdiii.duelgame.views.activities.result.fragments.adapters.QuestionSliderAdapter;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by MeHdi on 5/25/2015.
 */
public class ReviewQuestionsDialog extends android.support.v4.app.DialogFragment {

    ViewPager viewPager;
    CirclePageIndicator indicator;
    QuestionSliderAdapter pagerAdapter;

    int screenWidth;
    int screenHeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("trace", "onCreateView ReviewQuestionsDialog");
        return inflater.inflate(R.layout.dialog_review_questions, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d("trace", "onStart ReviewQuestionsDialog");

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        getDialog().getWindow().setLayout((int) (screenWidth * 0.95), (int) (screenHeight * 0.95));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("trace", "onCreateDialog ReviewQuestionsDialog");
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("trace", "onViewCreated ReviewQuestionsDialog");

        find(view);
        configure();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("trace", "onCreate ReviewQuestionsDialog");
    }

    private void find(View view) {
        Log.d("trace", "find ReviewQuestionsDialog");
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_questions);
        indicator = (CirclePageIndicator) view.findViewById(R.id.indicator_questions);
    }

    private void configure() {
        Log.d("trace", "configure ReviewQuestionsDialog");
        pagerAdapter = new QuestionSliderAdapter(getChildFragmentManager(), ParentActivity.NUMBER_OF_QUESTIONS, getArguments().getIntegerArrayList(GameResultActivity.ARGUMENT_CORRECT_OPTIONS));
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOffscreenPageLimit(ParentActivity.NUMBER_OF_QUESTIONS);
        indicator.setViewPager(viewPager);
    }
}
