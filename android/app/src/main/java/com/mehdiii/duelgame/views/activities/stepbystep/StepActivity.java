package com.mehdiii.duelgame.views.activities.stepbystep;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.managers.PurchaseManager;
import com.mehdiii.duelgame.models.BoughtQuiz;
import com.mehdiii.duelgame.models.QuestionForQuiz;
import com.mehdiii.duelgame.models.Quiz;
import com.mehdiii.duelgame.models.UseDiamond;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnStepCompleted;
import com.mehdiii.duelgame.utils.CategoryManager;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.StepManager;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.stepbystep.fragments.StepFragment;
import com.mehdiii.duelgame.views.dialogs.AlertDialog;
import com.mehdiii.duelgame.views.dialogs.BuyQuizDialog;
import com.mehdiii.duelgame.views.dialogs.RetakeStepDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by frshd on 2/24/16.
 */
public class StepActivity extends ParentActivity implements View.OnClickListener {
    private final int RETAKE_DIAMOND = 290;

    Quiz quiz;
    Map<Integer, TextView> stepStars;
    Map<Integer, LinearLayout> stepHolders;
    Map<String, Integer> questionArchive;
    Fragment stepFragment;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        find();
        configure();
    }

    private void find() {

        setStepHolders();
        setStepStars();
        setQuestionArchive();
    }

    private void configure() {
        putStepStars();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cbc1000411_holder:
                checkRetake("1000411");
                break;
            case R.id.cbc1000412_holder:
                checkRetake("1000412");
                break;
            case R.id.cbc1000413_holder:
                checkRetake("1000413");
                break;
            case R.id.cbc1000414_holder:
                checkRetake("1000414");
                break;
            case R.id.cbc1000415_holder:
                checkRetake("1000415");
                break;
            case R.id.cbc1000416_holder:
                checkRetake("1000416");
                break;
            case R.id.cbc1000421_holder:
                checkRetake("1000421");
                break;
            case R.id.cbc1000422_holder:
                checkRetake("1000422");
                break;
            case R.id.cbc1000423_holder:
                checkRetake("1000423");
                break;
            case R.id.cbc1000424_holder:
                checkRetake("1000424");
                break;
            case R.id.cbc1000425_holder:
                checkRetake("1000425");
                break;
            case R.id.cbc1000426_holder:
                checkRetake("1000426");
                break;
            case R.id.cbc1000427_holder:
                checkRetake("1000427");
                break;
            case R.id.cbc1000428_holder:
                checkRetake("1000428");
                break;

        }
    }

    private void checkRetake(final String s) {
        int stars = GlobalPreferenceManager.readInteger(this, s, -1);
        if (stars > 0) {
            final RetakeStepDialog retakeStepDialog = new RetakeStepDialog(this,
                    String.format(getResources().getString(R.string.caption_diamond_cnt), AuthManager.getCurrentUser().getDiamond()) +
                            getResources().getString(R.string.caption_retake_step));

            retakeStepDialog.setOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(Object data) {
                    if ((boolean) data) {
                        if (AuthManager.getCurrentUser().getDiamond() >= RETAKE_DIAMOND) {
                            UseDiamond ud = new UseDiamond();
                            AuthManager.getCurrentUser().decreaseDiamond(RETAKE_DIAMOND);
                            ud.setAmount(RETAKE_DIAMOND);
                            ud.setCommand(CommandType.USE_DIAMOND);
                            DuelApp.getInstance().sendMessage(ud.serialize());
                            Log.d("TAG", "used diamond" + ud.serialize() + AuthManager.getCurrentUser().getDiamond());
                            startStep(s);
                        } else {
                            String message = getResources().getString(R.string.not_enough_diamond);
                            AlertDialog ad = new AlertDialog(StepActivity.this, message);
                            ad.show();
                        }
                    }
                }
            });
            retakeStepDialog.show();
        }
        else
            startStep(s);
    }

    private void startStep(String s) {
        Bundle bundle = new Bundle();
        ArrayList<QuestionForQuiz> parsedQuestions = new ArrayList<>();
        parsedQuestions = getQuestions(questionArchive.get(s));
        quiz = new Quiz();
        quiz.setQuestions(parsedQuestions);
        bundle.putString("quiz", quiz.serialize());
        bundle.putString("stepName", StepManager.getStep(this, s));
        bundle.putString("stepId", s);
        stepFragment = StepFragment.getInstance();
        stepFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().
                add(R.id.step_fragment_holder, stepFragment).
                addToBackStack(null).commit();
    }

    public ArrayList<QuestionForQuiz> getQuestions(int stepId) {
        String[] questions = getResources().getStringArray(stepId);
        ArrayList<QuestionForQuiz> parsedQuestions = new ArrayList<>();
        for (String question : questions) {
            String[] splitted = question.split("###");
            QuestionForQuiz newquestion = new QuestionForQuiz();
            ArrayList<String> options = new ArrayList<>();
            for (int iterator = 0; iterator < splitted.length; iterator++) {
                if (iterator == 0)
                    newquestion.setQuestionText(splitted[iterator]);
                else if (iterator == 1)
                    newquestion.setAnswer(splitted[iterator]);
                if (iterator > 0)
                    options.add(splitted[iterator]);
            }
            newquestion.setOptions(options);
            newquestion.setCategory("10004");
            newquestion.setCourseName(CategoryManager.getCategory(this, 10004));
            newquestion.setDescription("bello");
            parsedQuestions.add(newquestion);
        }
        return parsedQuestions;
    }

    void setStepHolders() {
        stepHolders = new HashMap<>();
        stepHolders.put(1000411, (LinearLayout) findViewById(R.id.cbc1000411_holder));
        stepHolders.put(1000412, (LinearLayout) findViewById(R.id.cbc1000412_holder));
        stepHolders.put(1000413, (LinearLayout) findViewById(R.id.cbc1000413_holder));
        stepHolders.put(1000414, (LinearLayout) findViewById(R.id.cbc1000414_holder));
        stepHolders.put(1000415, (LinearLayout) findViewById(R.id.cbc1000415_holder));
        stepHolders.put(1000416, (LinearLayout) findViewById(R.id.cbc1000416_holder));
        stepHolders.put(1000421, (LinearLayout) findViewById(R.id.cbc1000421_holder));
        stepHolders.put(1000422, (LinearLayout) findViewById(R.id.cbc1000422_holder));
        stepHolders.put(1000423, (LinearLayout) findViewById(R.id.cbc1000423_holder));
        stepHolders.put(1000424, (LinearLayout) findViewById(R.id.cbc1000424_holder));
        stepHolders.put(1000425, (LinearLayout) findViewById(R.id.cbc1000425_holder));
        stepHolders.put(1000426, (LinearLayout) findViewById(R.id.cbc1000426_holder));
        stepHolders.put(1000427, (LinearLayout) findViewById(R.id.cbc1000427_holder));
        stepHolders.put(1000428, (LinearLayout) findViewById(R.id.cbc1000428_holder));

    }

    void setStepStars() {
        stepStars = new HashMap<>();
        stepStars.put(1000411, (TextView) findViewById(R.id.cbc1000411_star));
        stepStars.put(1000412, (TextView) findViewById(R.id.cbc1000412_star));
        stepStars.put(1000413, (TextView) findViewById(R.id.cbc1000413_star));
        stepStars.put(1000414, (TextView) findViewById(R.id.cbc1000414_star));
        stepStars.put(1000415, (TextView) findViewById(R.id.cbc1000415_star));
        stepStars.put(1000416, (TextView) findViewById(R.id.cbc1000416_star));
        stepStars.put(1000421, (TextView) findViewById(R.id.cbc1000421_star));
        stepStars.put(1000422, (TextView) findViewById(R.id.cbc1000422_star));
        stepStars.put(1000423, (TextView) findViewById(R.id.cbc1000423_star));
        stepStars.put(1000424, (TextView) findViewById(R.id.cbc1000424_star));
        stepStars.put(1000425, (TextView) findViewById(R.id.cbc1000425_star));
        stepStars.put(1000426, (TextView) findViewById(R.id.cbc1000426_star));
        stepStars.put(1000427, (TextView) findViewById(R.id.cbc1000427_star));
        stepStars.put(1000428, (TextView) findViewById(R.id.cbc1000428_star));
        for (int stepId = 0; stepId < stepIds.length; stepId++) {
            stepStars.get(stepIds[stepId]).setTypeface(FontHelper.getIcons(this));
        }
    }

    void setQuestionArchive() {

        questionArchive = new HashMap<>();
        questionArchive.put("1000411", R.array.cbc1000411);
        questionArchive.put("1000412", R.array.cbc1000412);
        questionArchive.put("1000413", R.array.cbc1000413);
        questionArchive.put("1000414", R.array.cbc1000414);
        questionArchive.put("1000415", R.array.cbc1000415);
        questionArchive.put("1000416", R.array.cbc1000416);
        questionArchive.put("1000421", R.array.cbc1000421);
        questionArchive.put("1000422", R.array.cbc1000422);
        questionArchive.put("1000423", R.array.cbc1000423);
        questionArchive.put("1000424", R.array.cbc1000424);
        questionArchive.put("1000425", R.array.cbc1000425);
        questionArchive.put("1000426", R.array.cbc1000426);
        questionArchive.put("1000427", R.array.cbc1000427);
        questionArchive.put("1000428", R.array.cbc1000428);
    }

    void putStepStars() {
        for (int stepId = 0; stepId < stepIds.length; stepId++) {
            stepHolders.get(stepIds[stepId]).setOnClickListener(this);
            int stars = -1;
            if (stepId == 0){
                stars = GlobalPreferenceManager.readInteger(this, String.valueOf(stepIds[stepId]), -1);
                if(stars == -1) {
                    GlobalPreferenceManager.writeInt(this, String.valueOf(stepIds[stepId]), 0);
                    stars = 0;
                }
            }
            else
                stars = GlobalPreferenceManager.readInteger(this, String.valueOf(stepIds[stepId]), -1);

            int nextStars = -1;
            if (stepId + 1 < stepIds.length)
                nextStars = GlobalPreferenceManager.readInteger(this, String.valueOf(stepIds[stepId + 1]), -1);

            switch (stars) {
                case -1:
                    stepHolders.get(stepIds[stepId]).setBackgroundResource(R.drawable.step_box_deactive);
                    stepStars.get(stepIds[stepId]).setText("Z");
                    stepHolders.get(stepIds[stepId]).setClickable(false);
                    break;
                case 0:
                    stepHolders.get(stepIds[stepId]).setBackgroundResource(R.drawable.step_box_active);
                    stepStars.get(stepIds[stepId]).setText("aaa");
                    stepHolders.get(stepIds[stepId]).setClickable(true);
                    break;
                case 1:
                    stepHolders.get(stepIds[stepId]).setBackgroundResource(R.drawable.step_box_passed);
                    stepStars.get(stepIds[stepId]).setText("caa");
                    if (nextStars == -1 && stepId + 1 < stepIds.length) {
                        GlobalPreferenceManager.writeInt(this, String.valueOf(stepIds[stepId + 1]), 0);
                    }
                    break;
                case 2:
                    stepHolders.get(stepIds[stepId]).setBackgroundResource(R.drawable.step_box_passed);
                    stepStars.get(stepIds[stepId]).setText("cca");
                    if (nextStars == -1 && stepId + 1 < stepIds.length)
                        GlobalPreferenceManager.writeInt(this, String.valueOf(stepIds[stepId + 1]), 0);
                    break;
                case 3:
                    stepHolders.get(stepIds[stepId]).setBackgroundResource(R.drawable.step_box_passed);
                    stepStars.get(stepIds[stepId]).setText("ccc");
                    if (nextStars == -1 && stepId + 1 < stepIds.length) {
                        GlobalPreferenceManager.writeInt(this, String.valueOf(stepIds[stepId + 1]), 0);
                    }
                    break;
            }
        }

    }

    public boolean canHandleChallengeRequest() {
        return true;
    }

    public void onEvent(OnStepCompleted event) {
        putStepStars();
    }
}
