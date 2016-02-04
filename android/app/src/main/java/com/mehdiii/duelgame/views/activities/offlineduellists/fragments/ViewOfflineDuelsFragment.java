package com.mehdiii.duelgame.views.activities.offlineduellists.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.OfflineDuelsList;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.offlineduellists.fragments.adapters.DoneListAdapter;
import com.mehdiii.duelgame.views.activities.offlineduellists.fragments.adapters.MyTurnListAdapter;
import com.mehdiii.duelgame.views.activities.offlineduellists.fragments.adapters.OpponentTurnListAdapter;

public class ViewOfflineDuelsFragment extends Fragment {

    ListView duelsListView;
    ProgressBar progressBar;
    Activity activity = null;
    String lastRequestTurnType = null;

    MyTurnListAdapter myTurnAdapter;
    OpponentTurnListAdapter opponentTurnAdapter;
    DoneListAdapter doneAdapter;

    @Override
    public void onResume() {
        super.onResume();
        if (this.activity != null)
            LocalBroadcastManager.getInstance(this.activity).registerReceiver(broadcastReceiver, DuelApp.getInstance().getIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.activity != null)
            LocalBroadcastManager.getInstance(this.activity).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    private void bindListViewData(OfflineDuelsList list) {
        if (this.activity == null)
            return;

        if(lastRequestTurnType.equals("mine")) {
            myTurnAdapter = new MyTurnListAdapter(this.activity, R.layout.template_my_turn, list.getOfflineDuels());
            duelsListView.setAdapter(myTurnAdapter);
        } else if(lastRequestTurnType.equals("theirs")) {
            opponentTurnAdapter = new OpponentTurnListAdapter(this.activity, R.layout.template_opponent_turn, list.getOfflineDuels());
            duelsListView.setAdapter(opponentTurnAdapter);
        } else if(lastRequestTurnType.equals("done")) {
            doneAdapter = new DoneListAdapter(this.activity, R.layout.template_done, list.getOfflineDuels());
            duelsListView.setAdapter(doneAdapter);
        }
    }

    private void sendFetchRequest(String turnType) {
        if (myTurnAdapter != null) {
            myTurnAdapter.clear();
            myTurnAdapter.notifyDataSetChanged();
        }
        if (opponentTurnAdapter != null) {
            opponentTurnAdapter.clear();
            opponentTurnAdapter.notifyDataSetChanged();
        }
        if(doneAdapter != null) {
            doneAdapter.clear();
            doneAdapter.notifyDataSetChanged();
        }

        DuelApp.getInstance().sendMessage((new OfflineDuelsList(turnType, 0, 10)).serialize(CommandType.GET_CHALLENGE_LIST));
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_view_offline_duels, container, false);
    }

    private void find(View view)     {
        this.duelsListView = (ListView) view.findViewById(R.id.duels_list);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
    }

    private void configure() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        find(view);
        configure();
        onReload("mine");
    }

    public void onReload(String turnType) {
        lastRequestTurnType = turnType;
        sendFetchRequest(turnType);
    }

    private BroadcastReceiver broadcastReceiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_CHALLENGE_LIST) {
                progressBar.setVisibility(View.GONE);
                Log.d("TAG", "ViewRankingFragment BroadcastReceiver " + json);

                OfflineDuelsList list = OfflineDuelsList.deserialize(json, OfflineDuelsList.class);
                if(list != null && list.getTurn() != null && !list.getTurn().equals(lastRequestTurnType))
                    return;

                if (list != null) {
                    bindListViewData(list);
                }
            }
        }
    });
}

