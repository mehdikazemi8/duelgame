package com.mehdiii.duelgame.views.activities.home.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.HeartTracker;
import com.mehdiii.duelgame.managers.PurchaseManager;
import com.mehdiii.duelgame.models.ChangePage;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.events.OnDiamondChangeNotice;
import com.mehdiii.duelgame.models.events.OnHeartChangeNotice;
import com.mehdiii.duelgame.models.events.OnPurchaseResult;
import com.mehdiii.duelgame.models.events.OnSyncDataReceived;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.CategoryManager;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.ScoreHelper;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.activities.offlineduellists.OfflineDuelsListsActivity;
import com.mehdiii.duelgame.views.activities.quiz.QuizActivity;
import com.mehdiii.duelgame.views.activities.ranking.RankingActivity;
import com.mehdiii.duelgame.views.activities.stepbystep.StepActivity;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.mehdiii.duelgame.views.dialogs.AlertDialog;
import com.mehdiii.duelgame.views.dialogs.ConfirmDialog;
import com.mehdiii.duelgame.views.dialogs.DuelDialog;
import com.mehdiii.duelgame.views.dialogs.HeartLowDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import de.greenrobot.event.EventBus;

public class HomeFragment extends FlippableFragment implements View.OnClickListener {

    CustomButton quizButton;

    CustomTextView pendingOfflineDuels;
    CustomTextView openExamNotTaken;

    TextView diamondCount;
    ImageView avatarImageView;
    TextView titleTextView;
    TextView textViewCounter;
    Button refillButton;
    Button buyDiamondButton;
    Button offlineDuelsListsButton;
    Button duel2Button;
    CustomButton stepButton;
    TextView textViewHearts;
    ImageView heartsImageView;
    LinearLayout containerHearts;
    LinearLayout rankingsHolder;
    Map<Integer, CustomTextView> courseRanks;
    Map<Integer, CustomTextView> courseScores;
    Map<Integer, LinearLayout> courseHolders;
    int courseIds[] = new int []{
        10001,
        10002,
        10003,
        10004,
        10005,
        10006
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("TAG", "onResume onViewCreated");

        /**
         * find controls and bind view data
         */
        find(view);

        /**
         * configure click listeners and setup typeface
         */
        configure(view);

//        AppRater ar = new AppRater();
//        ar.show(getActivity(), true);

        if(AuthManager.getCurrentUser() != null && AuthManager.getCurrentUser().getPendingOfflineChallenges() > 0) {
            // TODO Loading baraye accept kardane challenge
            Log.d("TAG", "hhjj " + AuthManager.getCurrentUser().getPendingOfflineChallenges());
            AlertDialog dialog = new AlertDialog(getActivity(),
                    String.format(getString(R.string.caption_number_of_pending_duels),
                            AuthManager.getCurrentUser().getPendingOfflineChallenges()));
            dialog.show();
//            AuthManager.getCurrentUser().setPendingOfflineChallenges(0);
        }
    }

    private void configureCourseHolders() {
        for(final int courseId : courseIds) {
            courseHolders.get(courseId).setClickable(true);
            courseHolders.get(courseId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParentActivity.category = String.valueOf(courseId);
                    getActivity().startActivity(new Intent(getActivity(), RankingActivity.class));
                }
            });
        }
    }

    private void configure(View view) {

        configureCourseHolders();

        refillButton.setOnClickListener(this);
        buyDiamondButton.setOnClickListener(this);
        offlineDuelsListsButton.setOnClickListener(this);
        duel2Button.setOnClickListener(this);

        // set font-face
        FontHelper.setKoodakFor(view.getContext(), offlineDuelsListsButton, duel2Button, textViewHearts, /*textViewSendReport,*/
                diamondCount, titleTextView/*, levelText, totalRankingText,
                totalRanking, friendsRankingText, friendsRanking,
                provinceRanking, provinceRankingText*/, textViewCounter
        );

        buyDiamondButton.setTypeface(FontHelper.getIcons(getActivity()));
        refillButton.setTypeface(FontHelper.getIcons(getActivity()));

        quizButton.setOnClickListener(this);
        stepButton.setOnClickListener(this);
    }

    private void setPendingOfflineDuels() {
        if(AuthManager.getCurrentUser().getPendingOfflineChallenges() == 0) {
            pendingOfflineDuels.setVisibility(View.GONE);
        } else {
            pendingOfflineDuels.setVisibility(View.VISIBLE);
            pendingOfflineDuels.setText( String.valueOf(AuthManager.getCurrentUser().getPendingOfflineChallenges()));
        }
    }

    private void setOpenExamNotTaken(){
        if(AuthManager.getCurrentUser().getOpenExamNotTaken() == 0 || AuthManager.getCurrentUser().getOpenExamNotTaken() == null) {
            openExamNotTaken.setVisibility(View.GONE);
        } else {
            openExamNotTaken.setVisibility(View.VISIBLE);
            openExamNotTaken.setText( String.valueOf(AuthManager.getCurrentUser().getOpenExamNotTaken()));
        }
    }
    /**
     * return number of visible course stats of home page
     *
     **/
    private int getNumberOfVisibleCourses(){
        User user = AuthManager.getCurrentUser();
        if (user == null)
            return 0;
        int totalRanks = 0;
        for (int courseId : courseIds){
            if (user.getRank(String.valueOf(courseId))>0) {
                courseRanks.get(courseId).setText(String.valueOf(user.getRank(String.valueOf(courseId))));
                totalRanks += 1;
            } else {
//                courseHolders.get(courseId).setVisibility(View.GONE);

                courseRanks.get(courseId).setText("-");
            }
        }
        return totalRanks;
    }

    private void setCoursesRank(){
        User user = AuthManager.getCurrentUser();
        if (user == null)
            return;

        for (int courseId : courseIds){
            if (user.getRank(String.valueOf(courseId))>0) {
                courseRanks.get(courseId).setText(String.valueOf(user.getRank(String.valueOf(courseId))));
            } else {
                courseRanks.get(courseId).setText("-");
            }
        }
    }

    private void find(View view) {
        pendingOfflineDuels = (CustomTextView) view.findViewById(R.id.pending_offline_duels);
        openExamNotTaken = (CustomTextView) view.findViewById(R.id.open_exams_not_taken);
        diamondCount = (TextView) view.findViewById(R.id.home_diamond_cnt);
        avatarImageView = (ImageView) view.findViewById(R.id.imageView_avatar);
        titleTextView = (TextView) view.findViewById(R.id.textView_title);
        refillButton = (Button) view.findViewById(R.id.button_refill);
        buyDiamondButton = (Button) view.findViewById(R.id.button_buy_diamond);
        textViewHearts = (TextView) view.findViewById(R.id.textView_heart);
        textViewCounter = (TextView) view.findViewById(R.id.textView_counter);
        heartsImageView = (ImageView) view.findViewById(R.id.imageView_hearts);
        containerHearts = (LinearLayout) view.findViewById(R.id.container_hearts);
        offlineDuelsListsButton = (Button) view.findViewById(R.id.button_offline_duels_lists);
        duel2Button = (Button) view.findViewById(R.id.button_duel2);
        stepButton = (CustomButton) view.findViewById(R.id.step_quiz);

        quizButton = (CustomButton) view.findViewById(R.id.quiz_button);
        rankingsHolder = (LinearLayout) view.findViewById(R.id.rankings_holder);
        courseRanks = new HashMap<>();
        courseRanks.put(10001, (CustomTextView) view.findViewById(R.id.c10001_rank));
        courseRanks.put(10002, (CustomTextView) view.findViewById(R.id.c10002_rank));
        courseRanks.put(10003, (CustomTextView) view.findViewById(R.id.c10003_rank));
        courseRanks.put(10004, (CustomTextView) view.findViewById(R.id.c10004_rank));
        courseRanks.put(10005, (CustomTextView) view.findViewById(R.id.c10005_rank));
        courseRanks.put(10006, (CustomTextView) view.findViewById(R.id.c10006_rank));

        courseScores = new HashMap<>();
        courseScores.put(10001, (CustomTextView) view.findViewById(R.id.c10001_score));
        courseScores.put(10002, (CustomTextView) view.findViewById(R.id.c10002_score));
        courseScores.put(10003, (CustomTextView) view.findViewById(R.id.c10003_score));
        courseScores.put(10004, (CustomTextView) view.findViewById(R.id.c10004_score));
        courseScores.put(10005, (CustomTextView) view.findViewById(R.id.c10005_score));
        courseScores.put(10006, (CustomTextView) view.findViewById(R.id.c10006_score));

        courseHolders = new HashMap<>();
        courseHolders.put(10001, (LinearLayout) view.findViewById(R.id.c10001_holder));
        courseHolders.put(10002, (LinearLayout) view.findViewById(R.id.c10002_holder));
        courseHolders.put(10003, (LinearLayout) view.findViewById(R.id.c10003_holder));
        courseHolders.put(10004, (LinearLayout) view.findViewById(R.id.c10004_holder));
        courseHolders.put(10005, (LinearLayout) view.findViewById(R.id.c10005_holder));
        courseHolders.put(10006, (LinearLayout) view.findViewById(R.id.c10006_holder));
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("TAG", "onResume HomeFragment");

        EventBus.getDefault().register(this);
        bindViewData();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_friend:
                break;

            case R.id.button_buy_diamond:
            case R.id.button_refill:
                refillHeart();
                break;

            case R.id.button_duel:
            case R.id.button_duel2:
                startGame();
                break;

            case R.id.button_offline_duels_lists:
                Intent intent = new Intent(getActivity(), OfflineDuelsListsActivity.class);
                intent.putExtra("tab", 0);
                startActivity(intent);
                break;

            case R.id.quiz_button:
                startActivity(new Intent(getActivity(), QuizActivity.class));
                break;

            case R.id.step_quiz:
                startActivity(new Intent(getActivity(), StepActivity.class));
                break;
        }
    }

    public void bindViewData() {
        User user = AuthManager.getCurrentUser();
        if(user == null) {
            // TODO, user must not be null
            Log.d("TAG", "bindViewData HomeFragment user is null");
            return;
        } else {
            Log.d("TAG", "bindViewData HomeFragment user is NOT null");
        }

        setPendingOfflineDuels();
        setOpenExamNotTaken();
        avatarImageView.setImageResource(AvatarHelper.getResourceId(getActivity(), user.getAvatar()));
        diamondCount.setText(String.valueOf(user.getDiamond()));
        titleTextView.setText(ScoreHelper.getTitle(user.getScore()));

//        if (getNumberOfVisibleCourses() == 0)
//            rankingsHolder.setVisibility(View.GONE);

//        setCoursesRank();

//        courseScores.get(10001).setText(String.valueOf(user.getScore("10001", "week")));
//        courseScores.get(10002).setText(String.valueOf(user.getScore("10002", "week")));
//        courseScores.get(10003).setText(String.valueOf(user.getScore("10003", "week")));
//        courseScores.get(10004).setText(String.valueOf(user.getScore("10004", "week")));
//        courseScores.get(10005).setText(String.valueOf(user.getScore("10005", "week")));
//        courseScores.get(10006).setText(String.valueOf(user.getScore("10006", "week")));

        if (user.isExtremeHeart()) {
            heartsImageView.setImageResource(R.drawable.extreme_heart);
            containerHearts.setPadding(0, 0, 0, 20);
            this.textViewHearts.setVisibility(View.GONE);
        } else
            this.textViewHearts.setVisibility(View.VISIBLE);
        this.textViewHearts.setText(String.valueOf((user.getHeart())));
    }

    public void startGame() {
        if (!HeartTracker.getInstance().canUseHeart()) {
            HeartLowDialog dialog = new HeartLowDialog(getActivity());
            dialog.show();
            return;
        }

        DuelDialog duelDialog = new DuelDialog(getActivity());
        duelDialog.show();
    }



    private void refillHeart() {
        EventBus.getDefault().post(new ChangePage(ParentActivity.STORE_PAGE));
    }

    public void onEvent(OnHeartChangeNotice notice) {
        // if hearts are refilling over time, counter text view should be visible, ot invisible
        if (AuthManager.getCurrentUser().getHeart() >= HeartTracker.COUNT_HEARTS_MAX || AuthManager.getCurrentUser().isExtremeHeart())
            textViewCounter.setVisibility(View.INVISIBLE);
        else
            textViewCounter.setVisibility(View.VISIBLE);

        // if notice says that heart is increase or decreased or refreshed again from server change
        // hearts count, otherwise, update countdown timer.
        if (notice.getMode() == OnHeartChangeNotice.ChangeMode.DECREASED ||
                notice.getMode() == OnHeartChangeNotice.ChangeMode.INCREASED ||
                notice.getMode() == OnHeartChangeNotice.ChangeMode.REFRESH) {

            // update hearts count textView
            this.textViewHearts.setText(String.valueOf((notice.getValue())));

        } else if (notice.getMode() == OnHeartChangeNotice.ChangeMode.TICK) {
            // update countdown timer, (getValue equals seconds remaining to the next refill)
            int minutes, seconds;
            minutes = notice.getValue() / 60;
            seconds = minutes == 0 ? notice.getValue() : notice.getValue() % (minutes * 60);

            textViewCounter.setText(String.format("%d:%d", minutes, seconds));
        }
    }

    public void onEvent(OnDiamondChangeNotice notice) {
        this.diamondCount.setText(String.valueOf(notice.getNewValue()));
    }

    public void onEvent(OnPurchaseResult settings) {
        bindViewData();
    }

    public void onEvent(OnSyncDataReceived data) {
        bindViewData();
    }

    @Override
    public void onBringToFront() {
        super.onBringToFront();
    }
}
