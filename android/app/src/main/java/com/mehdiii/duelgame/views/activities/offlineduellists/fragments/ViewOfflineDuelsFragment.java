package com.mehdiii.duelgame.views.activities.offlineduellists.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.OfflineDuel;
import com.mehdiii.duelgame.models.OfflineDuelsList;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.dueloffline.DuelOfflineActivity;
import com.mehdiii.duelgame.views.activities.offlineduellists.fragments.adapters.BaseOfflineDuelAdapter;
import com.mehdiii.duelgame.views.activities.offlineduellists.fragments.adapters.DoneListAdapter;
import com.mehdiii.duelgame.views.activities.offlineduellists.fragments.adapters.MyTurnListAdapter;
import com.mehdiii.duelgame.views.activities.offlineduellists.fragments.adapters.OpponentTurnListAdapter;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.mehdiii.duelgame.views.dialogs.ConfirmDialog;

public class ViewOfflineDuelsFragment extends Fragment {

    ListView duelsListView;
    ProgressBar progressBar;
    Activity activity = null;
    String lastRequestTurnType = null;
    String duelId = null;

    CustomTextView messageTextView;

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

    String getMessageWhenEmptyList() {
        Log.d("TAG", "jjj " + lastRequestTurnType);

        if(lastRequestTurnType.equals("mine"))
            return getString(R.string.caption_no_pending_my_turn);
        if(lastRequestTurnType.equals("theirs"))
            return getString(R.string.caption_no_pending_opponent_turn);
        if(lastRequestTurnType.equals("done"))
            return getString(R.string.caption_no_pending_done);
        return "";
    }

    private void bindListViewData(OfflineDuelsList list) {
        if (this.activity == null)
            return;

        if(list.getOfflineDuels().size() == 0) {
            messageTextView.setVisibility(View.VISIBLE);
            messageTextView.setText(getMessageWhenEmptyList());
        } else {
            messageTextView.setVisibility(View.GONE);

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

        messageTextView.setVisibility(View.GONE);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        find(view);
        configure();
        onReload(getArguments().getString("turn"));
    }

    private void find(View view)     {
        this.duelsListView = (ListView) view.findViewById(R.id.duels_list);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        this.messageTextView = (CustomTextView) view.findViewById(R.id.message);
    }

    private void configure() {
        duelsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                Log.d("TAG", "aaabbbccc");

                if(!lastRequestTurnType.equals("mine"))
                    return;

                ConfirmDialog dialog = new ConfirmDialog(getActivity(), getString(R.string.button_accept), getString(R.string.button_deny), true, getString(R.string.caption_accept_duel_offline));
                dialog.setOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Object data) {
                        AuthManager.getCurrentUser().decreasePendingOfflineChallenges();
                        if((boolean)data) {
                            OfflineDuel request = new OfflineDuel();
                            duelId = ((BaseOfflineDuelAdapter) adapterView.getAdapter()).getItem(i).getDuelId();
                            request.setDuelId(duelId);
                            DuelApp.getInstance().sendMessage(request.serialize(CommandType.WANNA_ACCEPT_CHALLENGE));
                        } else {
                            OfflineDuel request = new OfflineDuel();
                            request.setDuelId(((BaseOfflineDuelAdapter)adapterView.getAdapter()).getItem(i).getDuelId());
                            DuelApp.getInstance().sendMessage(request.serialize(CommandType.WANNA_REJECT_CHALLENGE));
                            sendFetchRequest(lastRequestTurnType);
                        }
                    }
                });

                dialog.show();
                Log.d("TAG", "zzaa " + i + " " + lastRequestTurnType);
            }
        });
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
            } else if(type == CommandType.RECEIVE_ACCEPT_CHALLENGE) {
                Intent intent = new Intent(getActivity(), DuelOfflineActivity.class);
                intent.putExtra(DuelOfflineActivity.GAME_DATA_JSON, json);
                intent.putExtra(DuelOfflineActivity.IS_MASTER, false);
                intent.putExtra(DuelOfflineActivity.DUEL_ID, duelId);
                startActivity(intent);
                getActivity().finish();
            }
        }
    });
}

