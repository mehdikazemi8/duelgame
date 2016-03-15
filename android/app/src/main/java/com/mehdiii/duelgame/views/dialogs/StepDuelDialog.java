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
import com.mehdiii.duelgame.models.WannaChallenge;
import com.mehdiii.duelgame.utils.StepManager;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.adapters.StepListAdapter;
import com.mehdiii.duelgame.views.activities.waiting.WaitingActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frshd on 3/6/16.
 */
public class StepDuelDialog extends Dialog {

    Category category;
    ListView steps;
    String opponentUserNumeber;
    boolean isMaster = false;
    boolean challengeDirectInZaban = false;

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

    public StepDuelDialog(Context context, Category category) {
        super(context);
        this.category = category;

        Log.d("TAG", "abcd StepDuelDialog");
    }

    public StepDuelDialog(Context context, WannaChallenge challenge, String opponentUserNumeber, boolean isMaster) {
        super(context);
        this.category = new Category();

        category.setCategory(challenge.getCategory());

        this.opponentUserNumeber = opponentUserNumeber;
        this.isMaster = isMaster;
        this.challengeDirectInZaban = true;

        Log.d("TAG", "abcd StepDuelDialog");
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
        for (int s = 0; s < stepIds.length; s++) {
            StepForDuel temp = new StepForDuel();
            if (s == 0){
                temp.setStars(GlobalPreferenceManager.readInteger(getContext(), String.valueOf(stepIds[s]), -1));
                if (temp.getStars()==-1)
                    GlobalPreferenceManager.writeInt(getContext(), String.valueOf(stepIds[s]), 0);
            }
            temp.setName(StepManager.getStep(getContext(), String.valueOf(stepIds[s])));
            temp.setStars(GlobalPreferenceManager.readInteger(getContext(), String.valueOf(stepIds[s]), -1));
            temp.setStepId(stepIds[s]);
            stepList.add(temp);
        }

        StepListAdapter adapter = new StepListAdapter(getContext(), R.layout.template_step_list_dialog, stepList);
        steps.setAdapter(adapter);
        steps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("TAG", "abcd challenge in zaban 11111");

                if (stepList.get(i).getStars() <= 0){
                    String message = getContext().getResources().getString(R.string.step_duel_not_passed_yet);
                    AlertDialog ad = new AlertDialog(getContext(), message);
                    ad.show();
                }else {

                    String sid = String.valueOf(stepList.get(i).getStepId());
                    category.setBook( sid.substring(sid.length() - 3, sid.length() - 1) );
                    category.setChapter( sid.substring(sid.length() - 1) );

                    if(challengeDirectInZaban == false) {

                        ParentActivity.book = "0";
                        ParentActivity.chapter = "0";

                        Log.d("TAG", "category to server" + category.serialize());
                        DuelApp.getInstance().sendMessage(category.serialize());
                        getContext().startActivity(new Intent(getContext(), WaitingActivity.class));
                    } else {
                        Log.d("TAG", "abcd challenge in zaban " + category.getCategory());
                        Log.d("TAG", "abcd challenge in zaban " + category.getBook());
                        Log.d("TAG", "abcd challenge in zaban " + category.getChapter());

                        ParentActivity.book = category.getBook();
                        ParentActivity.chapter = category.getChapter();

                        Intent i2 = new Intent(getContext(), WaitingActivity.class);
                        i2.putExtra("user_number", opponentUserNumeber);
                        i2.putExtra("category", category.getCategory());
                        i2.putExtra("book", category.getBook());
                        i2.putExtra("chapter", category.getChapter());
                        i2.putExtra("message", getContext().getString(R.string.message_duel_with_friends_default));
                        i2.putExtra("master", true);

                        getContext().startActivity(i2);
                    }

                    dismiss();
                }
            }
        });

    }

    private void findControls() {
        steps = (ListView) findViewById(R.id.steps_list);
    }

}
