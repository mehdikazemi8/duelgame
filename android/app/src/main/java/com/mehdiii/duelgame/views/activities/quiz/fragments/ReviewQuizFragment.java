package com.mehdiii.duelgame.views.activities.quiz.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.async.GetQuizQuestions;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.CourseResult;
import com.mehdiii.duelgame.models.GetBuyQuizRequest;
import com.mehdiii.duelgame.models.OneCourseAnswer;
import com.mehdiii.duelgame.models.QuestionForQuiz;
import com.mehdiii.duelgame.models.Quiz;
import com.mehdiii.duelgame.models.QuizAnswer;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.responses.TookQuiz;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.utils.TellFriendManager;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.mehdiii.duelgame.views.dialogs.AlertDialog;
import com.mehdiii.duelgame.views.dialogs.ConfirmDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by mehdiii on 1/14/16.
 */
public class ReviewQuizFragment extends Fragment implements View.OnClickListener {

    final int NOPTIONS = 4;
    String quizId;
    boolean isQuizTaken;
    Quiz quiz;
    int currentQuestionIdx;

    CustomButton[] options = new CustomButton[NOPTIONS];
    CustomTextView questionText;
    CustomTextView title;
    CustomTextView description;
    CustomTextView verdictText;
    CustomButton nextQuestion;
    String lastQShuffle;
    ProgressBar progressBar;
    RelativeLayout body;

    public ReviewQuizFragment() {
        super();
    }

    public static ReviewQuizFragment getInstance() { return new ReviewQuizFragment(); }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, DuelApp.getInstance().getIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        quizId = getArguments().getString("id");
        isQuizTaken = getArguments().getBoolean("isQuizTaken");
        return inflater.inflate(R.layout.fragment_review_quiz, container, false);
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
        title = (CustomTextView) view.findViewById(R.id.title);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        body = (RelativeLayout) view.findViewById(R.id.body);
        description = (CustomTextView) view.findViewById(R.id.description);
        verdictText = (CustomTextView) view.findViewById(R.id.verdict_text);
    }

    private void configure() {
        // set onclick for options
        for(int k = 0; k < NOPTIONS; k ++) {
            options[k].setClickable(false);
        }
        nextQuestion.setOnClickListener(this);

        // setting text color of options
        for(int k = 0; k < NOPTIONS; k ++) {
            options[k].setTextColor(getResources().getColor(R.color.play_game_option_btn_text));
        }

        // check if we have results in clients mobile or not, and request server if necessary
        String quizJson = GlobalPreferenceManager.readString(getActivity(), quizId+"quizresult", null);
        if(quizJson == null) {
            // show progressbar and request server for quiz
            progressBar.setVisibility(View.VISIBLE);

            // send request for getting questions with answers and then wait in broadcast receiver
//            GetBuyQuizRequest request = new GetBuyQuizRequest(quizId);
//            request.setCommand(CommandType.GET_QUIZ_QUESTIONS);
//            DuelApp.getInstance().sendMessage(request.serialize());
            Log.d("TAG", "boro be GetQuizQuestions ");
            new GetQuizQuestions(getActivity(), quizId).execute();
        } else {
            startReview(quizJson);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_question_button:
                showNextQuestion();
                break;
        }
    }

    private void startReview(String quizJson) {
        Log.d("TAG", quizJson);

        if(progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        body.setVisibility(View.VISIBLE);

        quiz = Quiz.deserialize(quizJson, Quiz.class);
        currentQuestionIdx = 0;
        showNextQuestion();
    }

    private void showNextQuestion() {
        if(currentQuestionIdx == quiz.getQuestions().size()) {
            getActivity().getSupportFragmentManager().popBackStack();
            return;
        }

        QuestionForQuiz question = quiz.getQuestions().get(currentQuestionIdx);
        lastQShuffle = question.getAnswer();
        if(question.getDescription() == null)
            description.setText("");
        else
            description.setText(question.getDescription());

        if(lastQShuffle.charAt(4) == '9') {
            verdictText.setText("نزدی");
            verdictText.setTextColor(getResources().getColor(R.color.purple_dark3));
        } else if(lastQShuffle.charAt(4) == '0') {
            verdictText.setText("درست");
            verdictText.setTextColor(getResources().getColor(R.color.correct_answer));
        } else {
            verdictText.setText("نادرست");
            verdictText.setTextColor(getResources().getColor(R.color.wrong_answer));
        }
        title.setText(String.valueOf(currentQuestionIdx+1) + " - " + question.getCourseName());
        questionText.setText(question.getQuestionText());
        for(int k = 0; k < NOPTIONS; k ++) {
            options[k].setText(question.getOptions().get(Integer.valueOf("" + lastQShuffle.charAt(k))));

            if(lastQShuffle.charAt(k) == '0') {
                options[k].setTextColor(getResources().getColor(R.color.correct_answer));
            } else if(lastQShuffle.charAt(k) == lastQShuffle.charAt(4)) {
                options[k].setTextColor(getResources().getColor(R.color.wrong_answer));
            } else {
                options[k].setTextColor(getResources().getColor(R.color.play_game_option_btn_text));
            }
        }

        currentQuestionIdx ++;
        if(currentQuestionIdx == quiz.getQuestions().size()) {
            nextQuestion.setText("پایان مرور");
        }
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

    private BroadcastReceiver broadcastReceiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if(type == CommandType.RECEIVE_QUIZ_QUESTIONS) {
                quiz = Quiz.deserialize(json, Quiz.class);

                if(!isQuizTaken) {
                    for(QuestionForQuiz question : quiz.getQuestions()) {
                        String shuff = "0123";
                        shuff = shuffleString(shuff);
                        shuff += '9';
                        question.setAnswer(shuff);
                    }
                    Log.d("TAG", "mehdiii444 " + quiz.serialize());
                    GlobalPreferenceManager.writeString(getActivity(), quizId + "quizresult", quiz.serialize());
                    startReview(quiz.serialize());
                } else {
                    GlobalPreferenceManager.writeString(getActivity(), quizId+"quizresult", json);
                    startReview(json);
                }
            }
        }
    });
}
