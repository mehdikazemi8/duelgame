package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.Category;
import com.mehdiii.duelgame.models.StepForDuel;
import com.mehdiii.duelgame.models.events.OnFinishActivity;
import com.mehdiii.duelgame.utils.StepManager;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.duelofflinewaiting.DuelOfflineWaitingActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.adapters.StepListAdapter;
import com.mehdiii.duelgame.views.activities.waiting.WaitingActivity;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by frshd on 3/8/16.
 */

public class StepOfflineDuelDialog extends Dialog {

    Category category;
    ListView steps;
    String opponentUserNumber;

    int stepIds[] = new int[]{
            1000411,
            1000412,
            1000413,
            1000414,
            1000415,
            1000416,
            1000421,
            1000422,
            1000423,
            1000424,
            1000425,
            1000426,
            1000427,
            1000428
    };

    public StepOfflineDuelDialog(Context context,Category category, String opponentUserNumber) {
        super(context);
        this.category = category;
        this.opponentUserNumber = opponentUserNumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_step_duel);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        getWindow().setLayout((int) (metrics.widthPixels * 0.9),(int) (metrics.heightPixels * 0.9) );

        findControls();
        configure();
    }


    private void configure() {
        final List<StepForDuel> stepList = new ArrayList<>();
        for (int s : stepIds){
            StepForDuel temp = new StepForDuel();
            temp.setName(StepManager.getStep(getContext(), String.valueOf(s)));
            temp.setStars(GlobalPreferenceManager.readInteger(getContext(), String.valueOf(s), -1));
            temp.setStepId(s);
            stepList.add(temp);
        }
        StepListAdapter adapter = new StepListAdapter(getContext(), R.layout.template_step_list_dialog, stepList);
        steps.setAdapter(adapter);
        steps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (stepList.get(i).getStars() <= 0){
                    String message = getContext().getResources().getString(R.string.step_duel_not_passed_yet);
                    AlertDialog ad = new AlertDialog(getContext(), message);
                    ad.show();
                }else {
                    String sid = String.valueOf(stepList.get(i).getStepId());

                    Log.d("TAG", "category to server" + category.serialize());
                    Intent intent = new Intent(getContext(), DuelOfflineWaitingActivity.class);
                    intent.putExtra("opponent_user_number", opponentUserNumber);
                    intent.putExtra("category", ParentActivity.category);
                    intent.putExtra("book", sid.substring(sid.length() - 3, sid.length() - 1));
                    intent.putExtra("chapter", sid.substring(sid.length() - 1));
                    intent.putExtra("master", true);
                    getContext().startActivity(intent);
                    dismiss();
                    EventBus.getDefault().post(new OnFinishActivity());
                }

            }
        });

    }

    private void findControls() {
        steps = (ListView) findViewById(R.id.steps_list);
    }

}

