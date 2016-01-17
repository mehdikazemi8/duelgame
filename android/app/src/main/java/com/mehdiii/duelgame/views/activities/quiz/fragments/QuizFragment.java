package com.mehdiii.duelgame.views.activities.quiz.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;

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
import java.util.Random;
import java.util.Timer;

/**
 * Created by mehdiii on 1/14/16.
 */
public class QuizFragment extends Fragment implements View.OnClickListener {

    final int NOP = 4;
    Quiz quiz;
    long remainingTime;
    int currentQuestionIdx;
    CustomTextView quizTimerTextView;
    CountDownTimer countDownTimer;

    CustomButton[] options = new CustomButton[NOP];
    CustomTextView questionText;
    CustomTextView title;
    CustomButton nextQuestion;
    CustomButton submitAnswer;
    EditText comment;

    String lastQShuffle;
    QuizAnswer quizAnswer;

    public QuizFragment() {
        super();
    }

    public static QuizFragment getInstance() { return new QuizFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        quiz = Quiz.deserialize(getArguments().getString("quiz"), Quiz.class);
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        find(view);
        configure();
    }

    private void find(View view) {
        quizTimerTextView = (CustomTextView) view.findViewById(R.id.quiz_timer);
        options[0] = (CustomButton) view.findViewById(R.id.option_0);
        options[1] = (CustomButton) view.findViewById(R.id.option_1);
        options[2] = (CustomButton) view.findViewById(R.id.option_2);
        options[3] = (CustomButton) view.findViewById(R.id.option_3);
        questionText = (CustomTextView) view.findViewById(R.id.question_text);
        nextQuestion = (CustomButton) view.findViewById(R.id.next_question_button);
        submitAnswer = (CustomButton) view.findViewById(R.id.submit_answer_button);
        title = (CustomTextView) view.findViewById(R.id.title);
        comment = (EditText) view.findViewById(R.id.comment);
    }

    private void configure() {
        FontHelper.setKoodakFor(getActivity(), comment);

        // calculating remaining time of the quiz and setting start of the quiz if needed
        long startTime = GlobalPreferenceManager.readLong(getActivity(), quiz.getId(), -1l);
        remainingTime = quiz.getDuration();
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis()/1000;
        if(startTime != -1) {
            remainingTime -= now - startTime;
        } else {
            GlobalPreferenceManager.writeLong(getActivity(), quiz.getId(), now);
        }

        // setting timer
        quizTimerTextView.setText(calculateRemainingTime(remainingTime));
        countDownTimer = new CountDownTimer(remainingTime*1000, 1000) {
            @Override
            public void onTick(long l) {
                remainingTime -= 1;
                quizTimerTextView.setText(calculateRemainingTime(remainingTime));
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();

        // set onclick for options
        for(int k = 0; k < NOP; k ++) {
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
                for(int k = 0; k < NOP; k ++) {
                    options[k].setTextColor(getResources().getColor(R.color.play_game_option_btn_text));
                }
                ((CustomButton)view).setTextColor(getResources().getColor(R.color.blue));
                break;

            case R.id.next_question_button:
                showNextQuestion();
                break;

            case R.id.submit_answer_button:
                submitAnswerToServer();
                break;
        }
    }

    private void submitAnswerToServer() {
        Log.d("TAG", "func submitAnswerToServer");
    }

    private void startQuiz() {
        int lastQuestionAnsweredIdx = GlobalPreferenceManager.readInteger(getActivity(), quiz.getId() + "idx", -1);
        if(lastQuestionAnsweredIdx == -1) {
            currentQuestionIdx = 0;
            quizAnswer = new QuizAnswer(quiz.getId());
        } else {
            currentQuestionIdx = lastQuestionAnsweredIdx;
            String previousAnswersJson = GlobalPreferenceManager.readString(getActivity(), quiz.getId()+"result", null);
            Log.d("TAG", "previous " + previousAnswersJson);
            if(previousAnswersJson != null) {
                quizAnswer = QuizAnswer.deserialize(previousAnswersJson, QuizAnswer.class);
            } else {
                quizAnswer = new QuizAnswer(quiz.getId());
            }
        }

        lastQShuffle = null;
        showNextQuestion();
    }

    private void addAnswer(String category) {
        if(lastQShuffle == null) {
            return;
        }

        if(lastQShuffle.length() == 4) {
            lastQShuffle += "9";
        }

        Log.d("TAG", "answer " + lastQShuffle);

        if(quizAnswer.getAnswers() != null) {
            for(OneCourseAnswer oneCourseAnswer : quizAnswer.getAnswers()) {
                if(oneCourseAnswer.getCategory().equals(category)) {
                    oneCourseAnswer.addAnswer(lastQShuffle);
                    GlobalPreferenceManager.writeString(getActivity(), quiz.getId() + "result", quizAnswer.serialize());
                    return;
                }
            }
        }

        OneCourseAnswer oneCourseAnswer = new OneCourseAnswer(category);
        oneCourseAnswer.addAnswer(lastQShuffle);
        quizAnswer.addOneCourseAnswer(oneCourseAnswer);
        GlobalPreferenceManager.writeString(getActivity(), quiz.getId()+"result", quizAnswer.serialize());
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
            for(int k = 0; k < NOP; k++) {
                options[k].setVisibility(View.GONE);
            }
            title.setText("ارسال جواب ها");
            submitAnswer.setVisibility(View.VISIBLE);
            comment.setVisibility(View.VISIBLE);
            return;
        }

        lastQShuffle = "0123";
        lastQShuffle = shuffleString(lastQShuffle);
        Log.d("TAG", "shuff " + lastQShuffle);

        GlobalPreferenceManager.writeInt(getActivity(), quiz.getId() + "idx", currentQuestionIdx);
        for(int k = 0; k < NOP; k ++) {
            options[k].setTextColor(getResources().getColor(R.color.play_game_option_btn_text));
        }
        QuestionForQuiz question = quiz.getQuestions().get(currentQuestionIdx);
        title.setText(String.valueOf(currentQuestionIdx+1) + " - " + question.getCourseName());
        questionText.setText(question.getQuestionText());
        for(int k = 0; k < NOP; k ++) {
            options[Integer.valueOf(""+lastQShuffle.charAt(k))].setText(question.getOptions().get(k));
        }

        currentQuestionIdx ++;
    }

    private String calculateRemainingTime(long sec) {
        return String.format("%d:%d", sec / 60, sec % 60);
    }

    private String shuffleString(String input){
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
}
