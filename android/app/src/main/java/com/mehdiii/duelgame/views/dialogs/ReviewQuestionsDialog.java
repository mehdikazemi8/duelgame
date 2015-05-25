package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mehdiii.duelgame.R;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by MeHdi on 5/25/2015.
 */
public class ReviewQuestionsDialog extends DialogFragment {

    ViewPager viewPager;
    CirclePageIndicator indicator;

    int screenWidth;
    int screenHeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_review_questions, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        getDialog().getWindow().setLayout((int) (screenWidth * 0.95), (int) (screenHeight * 0.95));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        find(view);
        configure();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void find(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_questions);
        indicator = (CirclePageIndicator) view.findViewById(R.id.indicator_questions);
    }

    private void configure() {
        // TODO add pager adapter

        viewPager.setOffscreenPageLimit(6);
        indicator.setViewPager(viewPager);
    }
}
