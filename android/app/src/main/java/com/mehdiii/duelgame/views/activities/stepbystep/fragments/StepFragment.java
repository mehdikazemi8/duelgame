package com.mehdiii.duelgame.views.activities.stepbystep.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.OneCourseAnswer;
import com.mehdiii.duelgame.models.QuestionForQuiz;
import com.mehdiii.duelgame.models.Quiz;
import com.mehdiii.duelgame.models.QuizAnswer;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.mehdiii.duelgame.views.custom.CustomTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    CustomButton nextQuestion;
    CustomButton submitAnswer;
    CustomTextView result;
    TextView starsView;

    String lastQShuffle;
    QuizAnswer quizAnswer;

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
        starsView = (TextView) view.findViewById(R.id.stars);
    }

    private void configure() {

        // set onclick for options
        for(int k = 0; k < NOPTIONS; k ++) {
            options[k].setOnClickListener(this);
        }
        nextQuestion.setOnClickListener(this);
        submitAnswer.setOnClickListener(this);

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
//        int lastQuestionAnsweredIdx = GlobalPreferenceManager.readInteger(getActivity(), quiz.getId() + "idx", -1);
        if(lastQuestionAnsweredIdx == -1) {
            currentQuestionIdx = 0;
            quizAnswer = new QuizAnswer(quiz.getId());
        } else {
            currentQuestionIdx = lastQuestionAnsweredIdx;
//            String previousAnswersJson = GlobalPreferenceManager.readString(getActivity(), quiz.getId()+"result", null);
//            GlobalPreferenceManager.remove(getActivity(), quiz.getId()+"result");
//            Log.d("TAG", "previous " + previousAnswersJson);
//            if(previousAnswersJson != null) {
//                quizAnswer = QuizAnswer.deserialize(previousAnswersJson, QuizAnswer.class);
//            } else {
                quizAnswer = new QuizAnswer(quiz.getId());
//            }
        }

        lastQShuffle = null;
        showNextQuestion();
    }

    private void submitAnswer() {
        Log.d("TAG", "func submitAnswer " + quizAnswer.serialize());
//        GlobalPreferenceManager.writeString(getActivity(), quiz.getId()+"answers", quizAnswer.serialize());

        GlobalPreferenceManager.writeInt(getActivity(), getArguments().getString("stepId"), stars);
        getActivity().getSupportFragmentManager().popBackStack();


    }

    private void showNextQuestion() {
        Log.d("TAG", "func showNextQuestion");

        if(currentQuestionIdx > 0) {
            addAnswer(quiz.getQuestions().get(currentQuestionIdx - 1).getCategory());
        }

        if(currentQuestionIdx == quiz.getQuestions().size()) {
            // TODO
            nextQuestion.setVisibility(View.GONE);
            questionText.setVisibility(View.GONE);
            for(int k = 0; k < NOPTIONS; k++) {
                options[k].setVisibility(View.GONE);
            }
            int trues = 0;
            int total = 0;
            for(OneCourseAnswer answer: quizAnswer.getAnswers()){
                for (String ans : answer.getData()){
                    String answerRes = ans.substring(ans.length()-1);
                    total += 1;
                    if (answerRes.equals("0"))
                        trues += 1;
                }
            }

            score = (trues*100.0)/total;
            Log.d("total score", "" + (int) Math.round(score));
            if (score > 80)
                stars = 3;
            else if(score > 60)
                stars = 2;
            else if(score > 40)
                stars = 1;
            if (stars >= 0){
                switch (stars){
                    case 0:
                        starsView.setText("XXX");
                        break;
                    case 1:
                        starsView.setText("WXX");
                        break;
                    case 2:
                        starsView.setText("WWX");
                        break;
                    case 3:
                        starsView.setText("WWW");
                        break;

                }
            }
            starsView.setVisibility(View.VISIBLE);
            title.setText("ارسال جواب ها");
            submitAnswer.setVisibility(View.VISIBLE);
            result.setText("درصد پاسخ‌های درست : " + score);
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
        title.setText(String.valueOf(currentQuestionIdx+1) + " - " + getArguments().getString("stepName"));
        questionText.setText(question.getQuestionText());
        for(int k = 0; k < NOPTIONS; k ++) {
            options[k].setText(question.getOptions().get(Integer.valueOf("" + lastQShuffle.charAt(k))));
        }

        currentQuestionIdx ++;
        if(currentQuestionIdx == quiz.getQuestions().size()) {
            nextQuestion.setText("پایان آزمون و ارسال جواب ها");
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
//                    GlobalPreferenceManager.writeString(getActivity(), quiz.getId() + "result", quizAnswer.serialize());
                    return;
                }
            }
        }

        OneCourseAnswer oneCourseAnswer = new OneCourseAnswer(category);
        oneCourseAnswer.addAnswer(lastQShuffle);
        quizAnswer.addOneCourseAnswer(oneCourseAnswer);
//        GlobalPreferenceManager.writeString(getActivity(), quiz.getId() + "result", quizAnswer.serialize());
    }
}
