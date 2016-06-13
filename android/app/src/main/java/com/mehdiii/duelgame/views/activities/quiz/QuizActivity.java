package com.mehdiii.duelgame.views.activities.quiz;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.PurchaseManager;
import com.mehdiii.duelgame.models.BoughtQuiz;
import com.mehdiii.duelgame.models.Quiz;
import com.mehdiii.duelgame.models.Quizzes;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.responses.TookQuiz;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.utils.TellFriendManager;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.quiz.fragments.QuizInfoFragment;
import com.mehdiii.duelgame.views.activities.quiz.fragments.adapters.QuizCardAdapter;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.mehdiii.duelgame.views.dialogs.ConfirmDialog;
import com.mehdiii.duelgame.views.dialogs.QuizDetailDialog;

import java.util.ArrayList;
import java.util.List;

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
    private final int NUMBER_OF_TABS = 3;
    boolean[] isFocused = new boolean[NUMBER_OF_TABS];
    int focusedColor;
    int notFocusedColor;
    CustomTextView[] quizMenu = new CustomTextView[NUMBER_OF_TABS];
    String[] sendWhat = new String[]{"due", "running", "future"};
    int whichTabToShow;
    ProgressBar progressBar;
    ImageButton refreshButton;
    ListView quizzesListView;
    QuizCardAdapter adapter;
    Quizzes quizzes;
    Button nextTen;
    Button previousTen;
    CustomTextView fromTo;
    CustomButton subscribeButton;
    CustomTextView invitedUsersCnt;
    CustomTextView freeQuizCnt;
    ImageButton infoButton;
    LinearLayout infoHolder;
    private int offset = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        find();
        configure();
    }

    private void find() {
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        nextTen = (Button) findViewById(R.id.next_ten);
        previousTen = (Button) findViewById(R.id.previous_ten);
        fromTo = (CustomTextView) findViewById(R.id.from_to);

        refreshButton = (ImageButton) findViewById(R.id.refresh_button);
        quizzesListView = (ListView) findViewById(R.id.quizzes_list_view);

        quizMenu[0] = (CustomTextView) findViewById(R.id.menu_due);
        quizMenu[1] = (CustomTextView) findViewById(R.id.menu_running);
        quizMenu[2] = (CustomTextView) findViewById(R.id.menu_future);

        subscribeButton = (CustomButton) findViewById(R.id.subscribe_button);
        invitedUsersCnt = (CustomTextView) findViewById(R.id.invited_users_cnt);
        freeQuizCnt = (CustomTextView) findViewById(R.id.free_quiz_cnt);
        infoButton = (ImageButton) findViewById(R.id.info_button);

        infoHolder = (LinearLayout) findViewById(R.id.quiz_info_holder);
    }

    private void configureTabs() {
        for (int i = 0; i < NUMBER_OF_TABS; i++) {
            quizMenu[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int titleIndex = Integer.parseInt(v.getContentDescription().toString());
                    if (isFocused[titleIndex])
                        return;

                    if(adapter != null) {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                    }

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

        // configure TABS
        configureTabs();

        subscribeButton.setOnClickListener(this);
        infoButton.setOnClickListener(this);
        infoHolder.setOnClickListener(this);

        updateInfo();
    }

    private void updateInfo() {
        if(AuthManager.getCurrentUser().isSubscribedForExam()) {
            freeQuizCnt.setText(getString(R.string.caption_free_quiz_cnt_infinity));
            subscribeButton.setVisibility(View.INVISIBLE);
        } else {
            freeQuizCnt.setText(String.format(getString(R.string.caption_free_quiz_cnt),
                    AuthManager.getCurrentUser().getFreeExamCount()));
        }

        invitedUsersCnt.setText(String.format(getString(R.string.caption_invited_users_cnt),
                AuthManager.getCurrentUser().getInvitedByMe()));
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
        progressBar.setVisibility(View.VISIBLE);
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage(getResources().getString(R.string.please_wait_message));
//        progressDialog.show();
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

        Log.d("TAG", "QuizActivity onPause");

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void handleInfoButton() {
        ConfirmDialog tellFriendDialog = new ConfirmDialog(this, getResources().getString(R.string.caption_invite_friends_info), R.layout.dialog_invite_friends);
        tellFriendDialog.setCancelable(true);
        tellFriendDialog.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                if((boolean)data) {
                    TellFriendManager.tellFriends(QuizActivity.this);
                }
            }
        });
        tellFriendDialog.show();
    }



    private void handleSubscription() {
//        ConfirmDialog subscriptionDialog = new ConfirmDialog(this,
//                getString(R.string.caption_subscribe_description),
//                R.layout.dialog_subscribe, AuthManager.getCurrentUser().getSubscriptionPrice(), true);
//        subscriptionDialog.show();

        QuizDetailDialog quizDetailDialog = new QuizDetailDialog();
        quizDetailDialog.show(getSupportFragmentManager(), "TAG");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.info_button:
            case R.id.quiz_info_holder:
                handleInfoButton();
                break;

            case R.id.subscribe_button:
                handleSubscription();
                break;

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
                if(progressBar.isShown()) {
                    progressBar.setVisibility(View.GONE);
                }

                quizzes = Quizzes.deserialize(json, Quizzes.class);
                if(quizzes.getQuizzes().size() == 0) {
                    // TODO
                    return;
                }

                bindListViewData(quizzes);
            } else if(type == CommandType.RECEIVE_SUBSCRIPTION_PURCHASE_CONFIRMATION) {
                AuthManager.getCurrentUser().setSubscribedForExam(true);
                updateInfo();
                sendFetchRequest();
            }
        }
    });
}
