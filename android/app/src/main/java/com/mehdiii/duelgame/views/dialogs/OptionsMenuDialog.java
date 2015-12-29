package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.Category;
import com.mehdiii.duelgame.views.activities.category.CategoryActivity;
import com.mehdiii.duelgame.views.activities.category.fragments.RankPlayFragment;

/**
 * Created by mehdiii on 12/29/15.
 */
public class OptionsMenuDialog extends DialogFragment implements View.OnClickListener {

    Context context;
    LinearLayout coursesRankings;

    public OptionsMenuDialog() {
        super();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        getDialog().getWindow().setAttributes(p);

        return inflater.inflate(R.layout.dialog_options_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        find(view);
        configure();
    }

    private void find(View view) {
        coursesRankings = (LinearLayout) view.findViewById(R.id.courses_rankings);
    }

    private void configure() {
        coursesRankings.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.courses_rankings:
                context.startActivity(new Intent(context, CategoryActivity.class));
                dismiss();
                break;
        }
    }
}
