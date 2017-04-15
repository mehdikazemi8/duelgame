package com.mehdiii.duelgame.views.activities.home.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.HeartTracker;
import com.mehdiii.duelgame.models.events.OnHeartChangeNotice;
import com.mehdiii.duelgame.models.events.OnPurchaseResult;
import com.mehdiii.duelgame.models.events.OnSyncDataReceived;
import com.mehdiii.duelgame.utils.UserFlowHelper;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.addquestion.AddQuestionFragment;
import com.mehdiii.duelgame.views.activities.offlineduellists.OfflineDuelsListsActivity;
import com.mehdiii.duelgame.views.dialogs.AlertDialog;
import com.mehdiii.duelgame.views.dialogs.DuelDialog;
import com.mehdiii.duelgame.views.dialogs.HeartLowDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

public class HomeFragment extends FlippableFragment {

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
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
//        bindViewData();
    }

    public void onEvent(OnSyncDataReceived data) {
//        bindViewData();
    }

    @OnClick(R.id.button_duel)
    public void startDuel() {
        Log.d("TAG", "startDuel");

        if (HeartTracker.getInstance() != null && !HeartTracker.getInstance().canUseHeart()) {
            HeartLowDialog dialog = new HeartLowDialog(getActivity());
            dialog.show();
            return;
        }

        DuelDialog duelDialog = new DuelDialog(getActivity());
        duelDialog.show();
    }

    @OnClick(R.id.button_offline_duels_lists)
    public void showOfflineDuels() {
        if (!UserFlowHelper.gotQuiz()){
            AlertDialog dialog = new AlertDialog(getActivity(), "برای اینکه بتونی دوئل نوبتی انجام بدی اول باید در یک \n\nآزمون\n\n شرکت کنی.");
            dialog.show();
        } else {
            Intent intent = new Intent(getActivity(), OfflineDuelsListsActivity.class);
            intent.putExtra("tab", 0);
            startActivity(intent);
        }
    }

    @OnClick(R.id.add_question)
    public void addQuestion() {
        AddQuestionFragment addQuestionFragment = new AddQuestionFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_holder, addQuestionFragment, ParentActivity.ADD_QUESTION_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }
}
