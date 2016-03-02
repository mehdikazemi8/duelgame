package com.mehdiii.duelgame.views.activities.stepbystep.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.OneCourseAnswer;
import com.mehdiii.duelgame.models.QuestionForQuiz;
import com.mehdiii.duelgame.models.Quiz;
import com.mehdiii.duelgame.models.QuizAnswer;
import com.mehdiii.duelgame.models.StepResult;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnStepCompleted;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.mehdiii.duelgame.views.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by frshd on 2/25/16.
 */
public class StepFragment extends Fragment implements View.OnClickListener {

    final int NOPTIONS = 4;
    Quiz quiz;
    int currentQuestionIdx;
    double score = 0;
    int stars = 0;
    CustomButton[] options = new CustomButton[NOPTIONS];
    CustomTextView questionText;
    CustomTextView title;
    CustomTextView truesView;
    CustomTextView falsesView;
    CustomTextView nonesView;
    CustomButton nextQuestion;
    CustomButton submitAnswer;
    CustomTextView result;
    ProgressDialog progressDialog;
    TextView star1View;
    TextView star2View;
    TextView star3View;
    String lastQShuffle;
    QuizAnswer quizAnswer;
    StepResult stepResult;

    int viewWidth = 1000;

    public StepFragment() {
        super();
    }

    public static StepFragment getInstance() {
        return new StepFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        quiz = Quiz.deserialize(getArguments().getString("quiz"), Quiz.class);
        return inflater.inflate(R.layout.fragment_step_quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        viewWidth = metrics.widthPixels;

        find(view);
        configure();
    }

    private void find(View view) {
        options[0] = (CustomButton) view.findViewById(R.id.option_0);
        options[1] = (CustomButton) view.findViewById(R.id.option_1);
        options[2] = (CustomButton) view.findViewById(R.id.option_2);
        options[3] = (CustomButton) view.findViewById(R.id.option_3);
        questionText = (CustomTextView) view.findViewById(R.id.question_text);
        nextQuestion = (CustomButton) view.findViewById(R.id.next_question_button);
        submitAnswer = (CustomButton) view.findViewById(R.id.submit_answer_button);
        title = (CustomTextView) view.findViewById(R.id.title);
        result = (CustomTextView) view.findViewById(R.id.result);
        star1View = (TextView) view.findViewById(R.id.star1);
        star2View = (TextView) view.findViewById(R.id.star2);
        star3View = (TextView) view.findViewById(R.id.star3);
        falsesView = (CustomTextView) view.findViewById(R.id.falses);
        truesView = (CustomTextView) view.findViewById(R.id.trues);
        nonesView = (CustomTextView) view.findViewById(R.id.nones);
    }

    private void configure() {

        // set onclick for options
        for(int k = 0; k < NOPTIONS; k ++) {
            options[k].setOnClickListener(this);
        }

        star1View.setTypeface(FontHelper.getIcons(getActivity()));
        star2View.setTypeface(FontHelper.getIcons(getActivity()));
        star3View.setTypeface(FontHelper.getIcons(getActivity()));

        nextQuestion.setOnClickListener(this);
        submitAnswer.setOnClickListener(this);
        stepResult = new StepResult();
        stepResult.setCategory(10004);
        String sid = getArguments().getString("stepId");
        stepResult.setBook(Integer.valueOf(sid.substring(sid.length() - 3, sid.length() - 1)));
        stepResult.setChapter(Integer.valueOf(sid.substring(sid.length() - 1)));
        stepResult.setCommand(CommandType.LOG_COURSE_STEP_UP);
        // start quiz
        startQuiz();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.option_0:
                lastQShuffle += lastQShuffle.charAt(0);
                break;
            case R.id.option_1:
                lastQShuffle += lastQShuffle.charAt(1);
                break;
            case R.id.option_2:
                lastQShuffle += lastQShuffle.charAt(2);
                break;
            case R.id.option_3:
                lastQShuffle += lastQShuffle.charAt(3);
                break;
        }

        switch (view.getId()) {
            case R.id.option_0:
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
                for(int k = 0; k < NOPTIONS; k ++) {
                    options[k].setTextColor(getResources().getColor(R.color.play_game_option_btn_text));
                }
                ((CustomButton)view).setTextColor(getResources().getColor(R.color.blue));
                break;

            case R.id.next_question_button:
                showNextQuestion();
                break;

            case R.id.submit_answer_button:
                submitAnswer();
                break;
        }

    }

    private void startQuiz() {

        int lastQuestionAnsweredIdx = 0;
        if(lastQuestionAnsweredIdx == -1) {
            currentQuestionIdx = 0;
            quizAnswer = new QuizAnswer(quiz.getId());
        } else {
            currentQuestionIdx = lastQuestionAnsweredIdx;
            quizAnswer = new QuizAnswer(quiz.getId());
        }

        lastQShuffle = null;
        showNextQuestion();
    }

    private void submitAnswer() {
        Log.d("TAG", "func submitAnswer " + stepResult.serialize());
        int lastRes = GlobalPreferenceManager.readInteger(getActivity(), getArguments().getString("stepId"), -1);
        if (lastRes < stars)
            GlobalPreferenceManager.writeInt(getActivity(), getArguments().getString("stepId"), stars);
        DuelApp.getInstance().sendMessage(stepResult.serialize());
        EventBus.getDefault().post(new OnStepCompleted());
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public void animateStars() {
        animateStar(star1View, 500, 0);
        animateStar(star2View, 500, 1000);
        animateStar(star3View, 500, 1500);
    }

    public void animateStar(final TextView tv, int duration, int delay) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tv, "translationX", viewWidth, 0);

        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                tv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        objectAnimator.setStartDelay(delay);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }


    private void showNextQuestion() {

        Log.d("TAG", "func showNextQuestion");
        if(currentQuestionIdx > 0) {
            addAnswer(quiz.getQuestions().get(currentQuestionIdx - 1).getCategory());
        }

        if(currentQuestionIdx == quiz.getQuestions().size()) {
            nextQuestion.setVisibility(View.GONE);
            questionText.setVisibility(View.GONE);
            for(int k = 0; k < NOPTIONS; k++) {
                options[k].setVisibility(View.GONE);
            }
            calculateScore();

//            star1View.setVisibility(View.VISIBLE);
//            star2View.setVisibility(View.VISIBLE);
//            star3View.setVisibility(View.VISIBLE);

            animateStars();

            nonesView.setVisibility(View.VISIBLE);
            truesView.setVisibility(View.VISIBLE);
            falsesView.setVisibility(View.VISIBLE);
            title.setText("ثبت جواب‌ها");
            submitAnswer.setVisibility(View.VISIBLE);

            result.setVisibility(View.VISIBLE);
            return;
        }

        lastQShuffle = "0123";
        lastQShuffle = shuffleString(lastQShuffle);
        Log.d("TAG", "shuff " + lastQShuffle);

        GlobalPreferenceManager.writeInt(getActivity(), quiz.getId()+"idx", currentQuestionIdx);
        for(int k = 0; k < NOPTIONS; k ++) {
            options[k].setTextColor(getResources().getColor(R.color.play_game_option_btn_text));
        }
        QuestionForQuiz question = quiz.getQuestions().get(currentQuestionIdx);

        // TODO DELETE THIS
        question.getOptions().set(0, "abcd");

        title.setText("سوال " + String.valueOf(currentQuestionIdx+1) + " - " + getArguments().getString("stepName"));
        questionText.setText(question.getQuestionText());
        for(int k = 0; k < NOPTIONS; k ++) {
            options[k].setText(question.getOptions().get(Integer.valueOf("" + lastQShuffle.charAt(k))));
        }

        currentQuestionIdx ++;
        if(currentQuestionIdx == quiz.getQuestions().size()) {
            nextQuestion.setText("پایان آزمون و ثبت جواب ها");
        }
    }

    private String shuffleString(String input) {
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!= 0) {
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }

    private void addAnswer(String category) {
        if(lastQShuffle == null) {
            return;
        }

        if(lastQShuffle.length() == 4) {
            lastQShuffle += "9";
        } else if(lastQShuffle.length() > 5) {
            lastQShuffle = lastQShuffle.substring(0, 4) + lastQShuffle.charAt(lastQShuffle.length()-1);
        }

        Log.d("TAG", "answer " + lastQShuffle);

        if(quizAnswer.getAnswers() != null) {
            for(OneCourseAnswer oneCourseAnswer : quizAnswer.getAnswers()) {
                if(oneCourseAnswer.getCategory().equals(category)) {
                    oneCourseAnswer.addAnswer(lastQShuffle);
                    return;
                }
            }
        }

        OneCourseAnswer oneCourseAnswer = new OneCourseAnswer(category);
        oneCourseAnswer.addAnswer(lastQShuffle);
        quizAnswer.addOneCourseAnswer(oneCourseAnswer);
    }

    void calculateScore(){

        int trues = 0;
        int total = 0;
        int nones = 0;
        int falses = 0;

        for(OneCourseAnswer answer: quizAnswer.getAnswers()){
            for (String ans : answer.getData()){
                String answerRes = ans.substring(ans.length()-1);
                total += 1;
                if (answerRes.equals("0"))
                    trues += 1;
                else if (answerRes.equals("9"))
                    nones += 1;
                else
                    falses += 1;
            }
        }
        score = (trues*100.0)/total;
        stepResult.setCorrect(trues);
        stepResult.setAll(total);
        result.setText("درصد پاسخ‌های درست : " + (int) Math.round(score) + " %");
        truesView.setText("تعداد پاسخ‌های درست: " + trues);
        falsesView.setText("تعداد پاسخ‌های نادرست: " + falses);
        nonesView.setText("تعداد سوالات بی پاسخ: " + nones);
        Log.d("total score", "" + (int) Math.round(score));
        if (score >= 80)
            stars = 3;
        else if(score >= 60)
            stars = 2;
        else if(score >= 40)
            stars = 1;
        else
            stars = 0;
        if (stars >= 0){
            switch (stars){
                case 0:
                    star1View.setText("a");
                    star2View.setText("a");
                    star3View.setText("a");
                    break;
                case 1:
                    star1View.setText("c");
                    star2View.setText("a");
                    star3View.setText("a");
                    break;
                case 2:
                    star1View.setText("c");
                    star2View.setText("c");
                    star3View.setText("a");
                    break;
                case 3:
                    star1View.setText("c");
                    star2View.setText("c");
                    star3View.setText("c");
                    break;
            }
        }
    }
}
