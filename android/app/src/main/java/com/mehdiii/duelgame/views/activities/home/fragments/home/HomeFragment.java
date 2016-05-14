package com.mehdiii.duelgame.views.activities.home.fragments.home;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.FlashCardIdManager;
import com.mehdiii.duelgame.models.CourseMap;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnHeartChangeNotice;
import com.mehdiii.duelgame.models.events.OnPurchaseResult;
import com.mehdiii.duelgame.models.events.OnSyncDataReceived;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.utils.UserFlowHelper;
import com.mehdiii.duelgame.views.activities.flashcards.FlashCardActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.activities.quiz.QuizActivity;
import com.mehdiii.duelgame.views.activities.stepbystep.StepActivity;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.mehdiii.duelgame.views.dialogs.AlertDialog;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class HomeFragment extends FlippableFragment implements View.OnClickListener {

    CustomButton quizButton;
    CustomTextView openExamNotTaken;
    CustomTextView pendingFlashCards;
    ImageButton infoButton;
    Button flashCard;
    CustomButton stepButton;
    private RadarChart mChart;
    int screenW;
    int screenH;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DuelApp.getInstance().sendMessage(new BaseModel().serialize(CommandType.GET_COURSE_MAP));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, DuelApp.getInstance().getIntentFilter());
        Log.d("TAG", "onResume onViewCreated");

        find(view);
        configure(view);
    }

    private void find(View view) {
        infoButton = (ImageButton) view.findViewById(R.id.info_button);
        openExamNotTaken = (CustomTextView) view.findViewById(R.id.open_exams_not_taken);
        pendingFlashCards = (CustomTextView) view.findViewById(R.id.pending_flashcards);
        stepButton = (CustomButton) view.findViewById(R.id.step_quiz);
        flashCard = (Button) view.findViewById(R.id.button_flash_card);
        quizButton = (CustomButton) view.findViewById(R.id.quiz_button);
        mChart = (RadarChart) view.findViewById(R.id.chart1);
    }

    private void configure(View view) {
        configureChart();
        infoButton.setOnClickListener(this);
        FontHelper.setKoodakFor(view.getContext(), flashCard);
        quizButton.setOnClickListener(this);
        stepButton.setOnClickListener(this);
        flashCard.setOnClickListener(this);
    }

    public void bindViewData() {
        User user = AuthManager.getCurrentUser();
        if(user == null) {
            Log.d("TAG", "bindViewData HomeFragment user is null");
            return;
        } else {
            Log.d("TAG", "bindViewData HomeFragment user is NOT null");
        }
        setOpenExamNotTaken();
        setPendingFlashCards();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_button:
                Log.d("TAG", "info_button");
                handleInfoButton();
                break;

            case R.id.quiz_button:
                startActivity(new Intent(getActivity(), QuizActivity.class));
                break;

            case R.id.step_quiz:
                if (!UserFlowHelper.gotQuiz()){
                    AlertDialog dialog = new AlertDialog(getActivity(), "برای اینکه بتونی به سوالات درس به درس پاسخ بدی اول باید در یک \n\nآزمون\n\n شرکت کنی.");
                    dialog.show();
                    break;
                } else {
                    startActivity(new Intent(getActivity(), StepActivity.class));
                    break;
                }

            case R.id.button_flash_card:
                Intent intent = new Intent(getActivity(), FlashCardActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void configureChart() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenW = metrics.widthPixels;
        screenH = metrics.heightPixels;
        mChart.setLayoutParams(new LinearLayout.LayoutParams((int) (screenW), (int) (screenW)));
        mChart.setDescription("");
        mChart.setWebLineWidth(1.5f);
        mChart.setWebLineWidthInner(0.75f);
        mChart.setWebAlpha(100);
        setData();
        mChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(11f);

        YAxis yAxis = mChart.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinValue(0f);

        Legend l = mChart.getLegend();
        l.setEnabled(false);
    }

    private String[] mParties = new String[]{
            "ادبیات", "عربی", "دین و زندگی", "زبان", "شیمی", "زیست"
    };

    public void setData() {

        int cnt = 6;
        int[] scores = new int[6];
        User user = AuthManager.getCurrentUser();
        for(int i =0 ; i < 6; i++){
            int cid  = 10001 + i;
            scores[i] = user.getScore(String.valueOf(cid), "overall");
        }
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; i < cnt; i++) {
            yVals1.add(new Entry((float) scores[i], i));
        }
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < cnt; i++)
            xVals.add(mParties[i]);
        RadarDataSet set1 = new RadarDataSet(yVals1, "امتیاز کسب شده");
        set1.setColor(getActivity().getResources().getColor(R.color.red));
        set1.setFillColor(getActivity().getResources().getColor(R.color.red));
        set1.setDrawFilled(true);
        set1.setLineWidth(2f);
        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);
        RadarData data = new RadarData(xVals, sets);
        data.setValueTypeface(FontHelper.getKoodak(getActivity()));
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        mChart.setData(data);
        mChart.invalidate();
    }

    private void setOpenExamNotTaken(){
        if(AuthManager.getCurrentUser().getOpenExamNotTaken() == 0 || AuthManager.getCurrentUser().getOpenExamNotTaken() == null) {
            openExamNotTaken.setVisibility(View.GONE);
        } else {
            openExamNotTaken.setVisibility(View.VISIBLE);
            openExamNotTaken.setText( String.valueOf(AuthManager.getCurrentUser().getOpenExamNotTaken()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "onResume HomeFragment");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, DuelApp.getInstance().getIntentFilter());
        EventBus.getDefault().register(this);
        bindViewData();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    private void handleInfoButton() {
        String msg = "";
        if(AuthManager.getCurrentUser() != null && AuthManager.getCurrentUser().getPendingOfflineChallenges() > 0) {
            msg = String.format(getString(R.string.caption_number_of_pending_duels),
                    AuthManager.getCurrentUser().getPendingOfflineChallenges()) +
            "\n\n" +
            "-----" +
            "\n\n";
        }

        msg = msg + AuthManager.getCurrentUser().getMotd();

        AlertDialog dialog = new AlertDialog(getActivity(), msg);
        dialog.show();
    }

    private void setPendingFlashCards() {

        if(FlashCardIdManager.getPendingFlashCards(getActivity())==0) {
            pendingFlashCards.setVisibility(View.GONE);
        } else {
            pendingFlashCards.setVisibility(View.VISIBLE);
            pendingFlashCards.setText(String.valueOf(FlashCardIdManager.getPendingFlashCards(getActivity())));
        }

    }

    public void onEvent(OnHeartChangeNotice notice) {
        if (notice.getMode() == OnHeartChangeNotice.ChangeMode.DECREASED ||
                notice.getMode() == OnHeartChangeNotice.ChangeMode.INCREASED ||
                notice.getMode() == OnHeartChangeNotice.ChangeMode.REFRESH) {
        } else if (notice.getMode() == OnHeartChangeNotice.ChangeMode.TICK) {
            int minutes, seconds;
            minutes = notice.getValue() / 60;
            seconds = minutes == 0 ? notice.getValue() : notice.getValue() % (minutes * 60);

        }
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

    private BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_COURSE_MAP) {
                CourseMap cm = CourseMap.deserialize(json, CourseMap.class);
                Log.d("TAG", "course map received" + cm.serialize());
                User user = AuthManager.getCurrentUser();
                user.setCourseMap(cm);
                Log.d("TAG", "course map received" + user.getCourseMap().serialize());
            }
        }
    });
}
