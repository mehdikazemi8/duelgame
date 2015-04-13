package com.mehdiii.duelgame.views.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.register.fragments.AvatarWaveFragment;
import com.mehdiii.duelgame.views.activities.register.fragments.adapters.AvatarSliderAdapter;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by omid on 4/6/2015.
 */
public class AvatarSelectionDialog extends DialogFragment {

    TextView titleTextView;
    TextView avatarTextView;
    ViewPager viewPager;

    AvatarSliderAdapter pagerAdapter;
    CirclePageIndicator indicator;

    int screenW;
    int screenH;


    OnCompleteListener onCompleteListener = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_avatar_selection, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        screenW = metrics.widthPixels;
        screenH = metrics.heightPixels;

        getDialog().getWindow().setLayout((int) (screenW * 0.9), (int) (screenH * 0.95));
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
        titleTextView = (TextView) view.findViewById(R.id.textView_title);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_avatars);
        indicator = (CirclePageIndicator) view.findViewById(R.id.indicator_avatars);
    }

    private void configure() {
        FontHelper.setKoodakFor(getActivity(), titleTextView);
        pagerAdapter = new AvatarSliderAdapter(getChildFragmentManager());
        pagerAdapter.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                getDialog().dismiss();
                if (onCompleteListener != null)
                    onCompleteListener.onComplete(data);
            }
        }, AvatarHelper.getCount(getActivity()) / AvatarWaveFragment.ROWS / AvatarWaveFragment.COLUMNS);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(4);

        indicator.setViewPager(viewPager);

    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

}
