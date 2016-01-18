package com.mehdiii.duelgame.views.activities.quiz.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.BoughtQuiz;
import com.mehdiii.duelgame.models.Quiz;
import com.mehdiii.duelgame.models.QuizCourse;
import com.mehdiii.duelgame.models.GetBuyQuizRequest;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.responses.TookQuiz;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.mehdiii.duelgame.views.dialogs.AlertDialog;
import com.mehdiii.duelgame.views.dialogs.BuyQuizDialog;
import com.mehdiii.duelgame.views.dialogs.ConfirmDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by mehdiii on 1/14/16.
 */

public class QuizInfoFragment extends Fragment implements View.OnClickListener {

    private Quiz quiz;
    CustomTextView title;
    CustomTextView fromTo;
    CustomTextView duration;
    ListView coursesListView;
    ProgressDialog progressDialog;

    CustomButton attendQuiz;
    CustomButton registerQuiz;
    CustomButton reviewQuiz;
    CustomButton reviewResults;

    public QuizInfoFragment() {
    }

    public static QuizInfoFragment getInstance() {
        return new QuizInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.d("TAG", "QuizInfoFragment " + getArguments().getString("quiz"));
        this.quiz = Quiz.deserialize(getArguments().getString("quiz"), Quiz.class);
        return inflater.inflate(R.layout.fragment_quiz_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("TAG", "onEvent onViewCreated QuizInfoFragment");

        find(view);
        configure();
    }

    private void find(View view) {
        title = (CustomTextView) view.findViewById(R.id.title);
        fromTo = (CustomTextView) view.findViewById(R.id.from_to);
        duration = (CustomTextView) view.findViewById(R.id.duration);
        coursesListView = (ListView) view.findViewById(R.id.courses_list_view);

        attendQuiz = (CustomButton) view.findViewById(R.id.attend_quiz_button);
        registerQuiz = (CustomButton) view.findViewById(R.id.register_quiz_button);
        reviewQuiz = (CustomButton) view.findViewById(R.id.review_quiz_button);
        reviewResults = (CustomButton) view.findViewById(R.id.review_results_button);
    }

    private List<String> getCoursesQuestionsCount(List<QuizCourse> quizCourses) {
        List<String> res = new ArrayList<>();
        for(QuizCourse quizCourse : quizCourses) {
            res.add(quizCourse.getCourseName() + ": " + quizCourse.getCount() + " سوال");
        }
        return res;
    }

    private void configure() {
        Log.d("TAG", "onEvent QuizInfoFragment configure");

        title.setText(quiz.getTitle());
        fromTo.setText("از " + quiz.getStart() + " تا " + quiz.getEnd() + " فرصت دارید در این آزمون شرکت کنید.");
        duration.setText("از زمانی که آزمون رو شروع کنید "
                + String.valueOf(quiz.getDuration() / 60)
                + " دقیقه فرصت دارید سوالات آزمون رو پاسخ بدید.");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.template_quiz_course_questions,
                getCoursesQuestionsCount(quiz.getCourses()));
        coursesListView.setAdapter(adapter);

        attendQuiz.setOnClickListener(this);
        reviewResults.setOnClickListener(this);
        reviewQuiz.setOnClickListener(this);
        registerQuiz.setOnClickListener(this);

        attendQuiz.setVisibility(View.GONE);
        reviewResults.setVisibility(View.GONE);
        reviewQuiz.setVisibility(View.GONE);
        registerQuiz.setVisibility(View.GONE);

        // configuring buttons
        if(quiz.getStatus().equals("future")) {
            if(quiz.getOwned()) {
                attendQuiz.setVisibility(View.VISIBLE);
            } else {
                registerQuiz.setVisibility(View.VISIBLE);
            }
        } else if(quiz.getStatus().equals("running")) {
            if(quiz.getOwned()) {
                if(quiz.getTaken()) {
                    reviewQuiz.setVisibility(View.VISIBLE);
                    reviewResults.setVisibility(View.VISIBLE);
                } else {
                    attendQuiz.setVisibility(View.VISIBLE);
                }
            } else {
                registerQuiz.setVisibility(View.VISIBLE);
            }
        } else if(quiz.getStatus().equals("due")) {
            if(quiz.getOwned()) {
                if(quiz.getTaken()) {
                    reviewQuiz.setVisibility(View.VISIBLE);
                    reviewResults.setVisibility(View.VISIBLE);
                } else {
                    reviewQuiz.setVisibility(View.VISIBLE);
                    reviewResults.setVisibility(View.VISIBLE);
                }
            } else {
                reviewResults.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        Log.d("TAG", "onEvent QuizInfoFragment pasued");
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, DuelApp.getInstance().getIntentFilter());
        Log.d("TAG", "onEvent QuizInfoFragment resumed " + quiz.getOwned() + " " + quiz.getId());

//        if(GlobalPreferenceManager.readString(getActivity(), quiz.getId()+"taken", null) != null) {
//            quiz.setTaken(true);
//            configure();
//        }
    }

    private void showProgressDialog() {
        // set progress dialog and then send request
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("لطفا کمی صبر کنید...");
        progressDialog.show();
    }

    private void sendGetQuestionsRequest() {
        // if we have saved data of quiz, then there is no need to request it again
        String quizJson = GlobalPreferenceManager.readString(getActivity(), quiz.getId()+"quiz", null);
        if(quizJson != null) {
            startQuizFragment(quizJson);
            return;
        }

        showProgressDialog();
        // send request for getting questions and then wait in broadcast receiver
        GetBuyQuizRequest request = new GetBuyQuizRequest(quiz.getId());
        request.setCommand(CommandType.GET_QUIZ_QUESTIONS);
        DuelApp.getInstance().sendMessage(request.serialize());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.attend_quiz_button:
                Log.d("TAG", "onEvent attendQuiz " + quiz.getOwned() + " " + quiz.getId());

                if(quiz.getStatus().equals("future")) {
                    AlertDialog dialog = new AlertDialog(getActivity(), "از " + quiz.getStart() + " تا " + quiz.getEnd() + " فرصت داری توی این آزمون شرکت کنی.");
                    dialog.show();
                    break;
                }
                if(GlobalPreferenceManager.readInteger(getActivity(), quiz.getId() + "idx", -1) != -1) {
                    sendGetQuestionsRequest();
                    break;
                }
                ConfirmDialog dialog = new ConfirmDialog(getActivity(),
                        String.format(getResources().getString(R.string.confirm_start_quiz), String.valueOf(quiz.getDuration() / 60)));
                dialog.setOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Object data) {
                        if((boolean)data) {
                            sendGetQuestionsRequest();
                        }
                    }
                });
                dialog.show();
                break;

            case R.id.review_quiz_button:
                break;

            case R.id.review_results_button:
                break;

            case R.id.register_quiz_button:
                BuyQuizDialog buyQuizDialog = new BuyQuizDialog(
                        getActivity(),
                        String.valueOf(quiz.getPrice()),
                        String.valueOf(quiz.getDiscount()),
                        String.valueOf(quiz.getPrice()-quiz.getPrice()*quiz.getDiscount()/100)
                );
                buyQuizDialog.setOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Object data) {
                        if((boolean)data) {
                            GetBuyQuizRequest request = new GetBuyQuizRequest(quiz.getId());
                            request.setCommand(CommandType.GET_BUY_QUIZ);
                            DuelApp.getInstance().sendMessage(request.serialize());
                            showProgressDialog();
                        }
                    }
                });
                buyQuizDialog.show();
                break;
        }
    }

    private void startQuizFragment(String json) {
        QuizFragment fragment = QuizFragment.getInstance();
        Bundle bundle = new Bundle();
        Quiz quizQuestions = Quiz.deserialize(json, Quiz.class);

        quiz.setQuestions(quizQuestions.getQuestions());
        bundle.putString("quiz", quiz.serialize());
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_holder, fragment, ParentActivity.QUIZ_INFO_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    public void onEvent(TookQuiz tookQuiz) {
        Log.d("TAG", "onEvent QuizInfoFragment TookQuiz " + tookQuiz.getId());
        if(quiz.getId().equals(tookQuiz.getId())) {
            quiz.setTaken(true);
            configure();
        }
    }

    private BroadcastReceiver broadcastReceiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if(progressDialog != null) {
                progressDialog.dismiss();
            }

            if (type == CommandType.RECEIVE_QUIZ_QUESTIONS) {
                // write data of quiz so you won't ask for it again and again
                GlobalPreferenceManager.writeString(getActivity(), quiz.getId()+"quiz", json);
                startQuizFragment(json);
            } else if(type == CommandType.RECEIVE_BUY_QUIZ) {
                try {
                    if(new JSONObject(json).get("id").equals(quiz.getId())) {
                        quiz.setOwned(true);
                        Log.d("TAG", "onEvent RECEIVE_BUY_QUIZ " + quiz.getOwned() + " " + quiz.getId());
                        configure();
                        EventBus.getDefault().post(new BoughtQuiz(quiz.getId()));

                        AlertDialog dialog = new AlertDialog(getActivity(), getResources().getString(R.string.caption_quiz_bought_successfully));
                        dialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });
}
