package com.mehdiii.duelgame.views.activities.quiz;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.PurchaseManager;
import com.mehdiii.duelgame.models.BoughtQuiz;
import com.mehdiii.duelgame.models.Quiz;
import com.mehdiii.duelgame.models.Quizzes;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.responses.TookQuiz;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.offlineduellists.fragments.ViewOfflineDuelsFragment;
import com.mehdiii.duelgame.views.activities.quiz.fragments.QuizInfoFragment;
import com.mehdiii.duelgame.views.activities.quiz.fragments.adapters.QuizCardAdapter;
import com.mehdiii.duelgame.views.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by mehdiii on 1/14/16.
 * quizId : start of the quiz
 * quizId + idx : last index of the question user has answered
 * quizId + quiz : data of the quiz without user's answers
 * quizId + result : the result of user (in the middle of exam)
 * quizId + quizresult : the data of quiz and user's answers
 */
public class QuizActivity extends ParentActivity implements View.OnClickListener {

    private final int LIMIT = 10;
    private int offset = 0;

    private final int NUMBER_OF_TABS = 3;

    boolean[] isFocused = new boolean[NUMBER_OF_TABS];
    int focusedColor;
    int notFocusedColor;
    CustomTextView[] quizMenu = new CustomTextView[NUMBER_OF_TABS];
    String[] sendWhat = new String[]{"due", "running", "future"};
    int whichTabToShow;

    ProgressDialog progressDialog;
    ImageButton refreshButton;
    ImageButton infoButton;
    ListView quizzesListView;
    QuizCardAdapter adapter;
    Quizzes quizzes;

    Button nextTen;
    Button previousTen;
    CustomTextView fromTo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        find();
        configure();
    }

    private void find() {
        nextTen = (Button) findViewById(R.id.next_ten);
        previousTen = (Button) findViewById(R.id.previous_ten);
        fromTo = (CustomTextView) findViewById(R.id.from_to);

        refreshButton = (ImageButton) findViewById(R.id.refresh_button);
        infoButton = (ImageButton) findViewById(R.id.info_button);
        quizzesListView = (ListView) findViewById(R.id.quizzes_list_view);

        quizMenu[0] = (CustomTextView) findViewById(R.id.menu_due);
        quizMenu[1] = (CustomTextView) findViewById(R.id.menu_running);
        quizMenu[2] = (CustomTextView) findViewById(R.id.menu_future);
    }

    private void configure() {
        previousTen.setOnClickListener(this);
        nextTen.setOnClickListener(this);

        previousTen.setTypeface(FontHelper.getIcons(this));
        nextTen.setTypeface(FontHelper.getIcons(this));

        // back colors
        focusedColor = getResources().getColor(R.color.purple);
        notFocusedColor = getResources().getColor(R.color.purple_dark);

        // initial tab is running quizzes
        whichTabToShow = 1;

        // setting focused initiate state boolean
        setFocusInitialState(1);
        setBackColor();

        // sending request to get running quizzes
        sendFetchRequest();

        for (int i = 0; i < NUMBER_OF_TABS; i++) {
            quizMenu[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.clear();
                    adapter.notifyDataSetChanged();

                    int titleIndex = Integer.parseInt(v.getContentDescription().toString());
                    if (isFocused[titleIndex])
                        return;

                    offset = 0;

                    for (int j = 0; j < NUMBER_OF_TABS; j++)
                        isFocused[j] = false;
                    isFocused[titleIndex] = true;
                    setBackColor();

                    whichTabToShow = titleIndex;

                    sendFetchRequest();
                }
            });
        }
    }

    private void setBackColor() {
        for (int i = 0; i < NUMBER_OF_TABS; i++)
            if (isFocused[i])
                quizMenu[i].setBackgroundColor(focusedColor);
            else
                quizMenu[i].setBackgroundColor(notFocusedColor);
    }

    private void setFocusInitialState(int focusedTab) {
        for(int k = 0; k < NUMBER_OF_TABS; k ++) {
            if(k == focusedTab) {
                isFocused[k] = true;
            } else {
                isFocused[k] = false;
            }
        }
    }

    private void sendFetchRequest() {
        // setting progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait_message));
        progressDialog.show();
        DuelApp.getInstance().sendMessage(new Quizzes(offset, 10, sendWhat[whichTabToShow]).serialize(CommandType.GET_QUIZ_LIST_PAGE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        PurchaseManager.changeActivity(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, DuelApp.getInstance().getIntentFilter());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!PurchaseManager.getInstance().handleActivityResult(resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_ten:
                offset += 10;
                sendFetchRequest();
                break;

            case R.id.previous_ten:
                if(offset == 0)
                    return;
                offset -= 10;
                sendFetchRequest();
                break;
        }
    }

    private List<String> getQuizzesTitles(Quizzes quizzes) {
        List<String> titles = new ArrayList<>();
        for(Quiz quiz : quizzes.getQuizzes())
            titles.add(quiz.getTitle());
        return titles;
    }

    private void bindListViewData(Quizzes quizzes) {
        fromTo.setText(String.valueOf(offset+1) + " - " + String.valueOf(offset+10));

        adapter = new QuizCardAdapter(QuizActivity.this, R.layout.template_quiz_card, quizzes.getQuizzes(), quizzes.getStatus());
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
            Log.d("TAG", "QuizActivity onReceive " + type);
            if(type == CommandType.RECEIVE_QUIZ_LIST_PAGE) {
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
