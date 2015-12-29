package com.mehdiii.duelgame.views.dialogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.ChangePage;
import com.mehdiii.duelgame.views.activities.category.CategoryActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.store.StoreFragment;

import de.greenrobot.event.EventBus;

/**
 * Created by mehdiii on 12/29/15.
 */
public class OptionsMenuDialog extends DialogFragment implements View.OnClickListener {

    Context context;

    LinearLayout coursesRankings;
    LinearLayout store;

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
        coursesRankings = (LinearLayout) view.findViewById(R.id.option_courses_rankings);
        store = (LinearLayout) view.findViewById(R.id.option_store);
    }

    private void configure() {
        coursesRankings.setOnClickListener(this);
        store.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        dismiss();

        switch (view.getId()) {
            case R.id.option_courses_rankings:
                context.startActivity(new Intent(context, CategoryActivity.class));
                break;

            case R.id.option_store:
                EventBus.getDefault().post(new ChangePage(10));
                break;
        }
    }
}
