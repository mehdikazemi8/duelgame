package com.mehdiii.duelgame.views.activities.stepbystep;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.QuestionForQuiz;
import com.mehdiii.duelgame.models.Quiz;
import com.mehdiii.duelgame.utils.CategoryManager;
import com.mehdiii.duelgame.utils.StepManager;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.stepbystep.fragments.StepFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by frshd on 2/24/16.
 */
public class StepActivity extends ParentActivity implements View.OnClickListener {
    Quiz quiz;
    Map<Integer, TextView> stepStars;
    Map<Integer, LinearLayout> stepHolders;
    Map<String, Integer> questionArchive;
    Fragment stepFragment;
    int stepIds[] = new int []{
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

    private void find(){

        stepFragment = StepFragment.getInstance();
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

    private void configure(){
        GlobalPreferenceManager.writeInt(this, String.valueOf(stepIds[0]), 3);
        for (int stepId = 0; stepId<stepIds.length ; stepId++){
            stepHolders.get(stepIds[stepId]).setOnClickListener(this);
            int stars = -1;
            if (stepId == 0)
                stars = GlobalPreferenceManager.readInteger(this, String.valueOf(stepIds[stepId]), 0);
            else
                stars = GlobalPreferenceManager.readInteger(this, String.valueOf(stepIds[stepId]), -1);
            int nextStars = -1;
            if (stepId+1 < stepIds.length)
                nextStars = GlobalPreferenceManager.readInteger(this, String.valueOf(stepIds[stepId+1]), -1);
            Log.d("Stars", ""+stars);
            switch (stars){
                case -1:
                    stepHolders.get(stepIds[stepId]).setBackground(getResources().getDrawable(R.drawable.home_rank_deactive));
                    stepHolders.get(stepIds[stepId]).setClickable(false);
                    break;
                case 0:
                    stepHolders.get(stepIds[stepId]).setBackground(getResources().getDrawable(R.drawable.home_rank_active));
                    stepHolders.get(stepIds[stepId]).setClickable(true);
                    break;
                case 1:
                    stepHolders.get(stepIds[stepId]).setBackground(getResources().getDrawable(R.drawable.home_rank_passed));
                    stepStars.get(stepIds[stepId]).setText("WXX");
                    if(nextStars==-1) {
                        GlobalPreferenceManager.writeInt(this, String.valueOf(stepIds[stepId+1]), 0);
                    }
                    break;
                case 2:
                    stepHolders.get(stepIds[stepId]).setBackground(getResources().getDrawable(R.drawable.home_rank_passed));
                    stepStars.get(stepIds[stepId]).setText("WWX");
                    if(nextStars==-1)
                        GlobalPreferenceManager.writeInt(this, String.valueOf(stepIds[stepId+1]), 0);
                    break;
                case 3:
                    stepHolders.get(stepIds[stepId]).setBackground(getResources().getDrawable(R.drawable.home_rank_passed));
                    stepStars.get(stepIds[stepId]).setText("WWW");
                    if(nextStars==-1) {
                        GlobalPreferenceManager.writeInt(this, String.valueOf(stepIds[stepId+1]), 0);
                    }
                    break;

            }

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cbc1000411_holder:
                startStep("1000411");
                break;
            case R.id.cbc1000412_holder:
                startStep("1000412");
                break;
            case R.id.cbc1000413_holder:
                startStep("1000413");
                break;
            case R.id.cbc1000414_holder:
                startStep("1000414");
                break;
            case R.id.cbc1000415_holder:
                startStep("1000415");
                break;
            case R.id.cbc1000416_holder:
                startStep("1000416");
                break;
            case R.id.cbc1000421_holder:
                startStep("1000421");
                break;
            case R.id.cbc1000422_holder:
                startStep("1000422");
                break;
            case R.id.cbc1000423_holder:
                startStep("1000423");
                break;
            case R.id.cbc1000424_holder:
                startStep("1000424");
                break;
            case R.id.cbc1000425_holder:
                startStep("1000425");
                break;
            case R.id.cbc1000426_holder:
                startStep("1000426");
                break;
            case R.id.cbc1000427_holder:
                startStep("1000427");
                break;
            case R.id.cbc1000428_holder:
                startStep("1000428");
                break;

        }
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
        stepFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().
                add(R.id.step_fragment_holder, stepFragment).
                addToBackStack(null).commit();
        configure();
    }

    public ArrayList<QuestionForQuiz> getQuestions(int stepId){
        String[] questions = getResources().getStringArray(stepId);
        ArrayList<QuestionForQuiz> parsedQuestions = new ArrayList<>();
        for (String question : questions){
            String[] splitted = question.split("###");
            QuestionForQuiz newquestion = new QuestionForQuiz();
            ArrayList<String> options = new ArrayList<>();
            for (int iterator=0 ; iterator< splitted.length ; iterator++ ){
                if (iterator==0)
                    newquestion.setQuestionText(splitted[iterator]);
                else if(iterator==1)
                    newquestion.setAnswer(splitted[iterator]);
                if(iterator>0)
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

}
