package com.mehdiii.duelgame.views.activities.quiz;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.BoughtQuiz;
import com.mehdiii.duelgame.models.ChangePage;
import com.mehdiii.duelgame.models.Quiz;
import com.mehdiii.duelgame.models.Quizzes;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.responses.TookQuiz;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.quiz.fragments.QuizInfoFragment;
import com.mehdiii.duelgame.views.activities.quiz.fragments.adapters.QuizCardAdapter;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by mehdiii on 1/14/16.
 * quizId : start of the quiz
 * quizId + idx : last index of the question user has answered
 * quizId + quiz : data of the quiz without user's answers
 * quizId + result : the result of user (in the middle of exam)
 * quizId + quizresult : the data of quiz and user's answers
 */
public class QuizActivity extends ParentActivity {

    ProgressDialog progressDialog;
    ImageButton refreshButton;
    ImageButton infoButton;
    ListView quizzesListView;
    Quizzes quizzes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        find();
        configure();
    }

    private void find() {
        refreshButton = (ImageButton) findViewById(R.id.refresh_button);
        infoButton = (ImageButton) findViewById(R.id.info_button);
        quizzesListView = (ListView) findViewById(R.id.quizzes_list_view);
    }

    private void configure() {
        DuelApp.getInstance().sendMessage(new BaseModel(CommandType.GET_QUIZ_LIST).serialize());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait_message));
        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, DuelApp.getInstance().getIntentFilter());
    }

    public void onEvent(TookQuiz tookQuiz) {
        Log.d("TAG", "onEvent quizActivity TookQuiz " + tookQuiz.getId());
        if(quizzes.getQuizzes().size() == 0)
            return;

        for(Quiz quiz : quizzes.getQuizzes()) {
            if(quiz.getId().equals(tookQuiz.getId())) {
                quiz.setTaken(true);
            }
        }
    }

    public void onEvent(BoughtQuiz boughtQuiz) {
        Log.d("TAG", "onEvent " + boughtQuiz.getId());
        if(quizzes.getQuizzes().size() == 0)
            return;

        for(Quiz quiz : quizzes.getQuizzes()) {
            if(quiz.getId().equals(boughtQuiz.getId())) {
                quiz.setOwned(true);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private List<String> getQuizzesTitles(Quizzes quizzes) {
        List<String> titles = new ArrayList<>();
        for(Quiz quiz : quizzes.getQuizzes())
            titles.add(quiz.getTitle());
        return titles;
    }

    private void bindListViewData(Quizzes quizzes) {
        QuizCardAdapter adapter = new QuizCardAdapter(QuizActivity.this, R.layout.template_quiz_card, quizzes.getQuizzes());
        quizzesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TAG", "quizzesListView " + adapterView.getAdapter().getItem(i));
            }
        });
        quizzesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                QuizInfoFragment fragment = QuizInfoFragment.getInstance();
                Bundle bundle = new Bundle();
                bundle.putString("quiz", ((Quiz)adapterView.getItemAtPosition(i)).serialize());
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_holder, fragment, ParentActivity.QUIZ_INFO_FRAGMENT)
                        .addToBackStack(null)
                        .commit();
            }
        });
        quizzesListView.setAdapter(adapter);
    }

    @Override
    public boolean isInQuizActivity() {
        return true;
    }

    private BroadcastReceiver broadcastReceiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            Log.d("TAG", "QuizActivity onReceive");
            if(type == CommandType.RECEIVE_QUIZ_LIST) {
                if(progressDialog != null) {
                    progressDialog.dismiss();
                }

                quizzes = Quizzes.deserialize(json, Quizzes.class);
                    if(quizzes.getQuizzes().size() == 0) {
                    // TODO
                    return;
                }

                bindListViewData(quizzes);
            }
        }
    });
}
