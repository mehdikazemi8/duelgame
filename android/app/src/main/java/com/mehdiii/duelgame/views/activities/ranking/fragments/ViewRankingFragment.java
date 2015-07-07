package com.mehdiii.duelgame.views.activities.ranking.fragments;

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
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.responses.RankList;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.home.fragments.ranking.RankingListAdapter;

public class ViewRankingFragment extends Fragment {

    ListView listView;
    ProgressBar progressBar;
    RankingListAdapter adapter;
    Activity activity = null;

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

    private void bindListViewData(RankList list) {
        if (this.activity == null)
            return;

        adapter = new RankingListAdapter(this.activity, R.layout.template_ranklist, list.getTop());
        this.listView.setAdapter(adapter);
    }

    private void sendFetchRequest(CommandType commandType) {
        if (adapter != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }

        User user = AuthManager.getCurrentUser();
        Log.d("sendFetch", user.serialize(commandType));
        DuelApp.getInstance().sendMessage(new BaseModel().serialize(commandType)/*user.serialize(commandType)*/);
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    private BroadcastReceiver broadcastReceiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == receiveCommandType) {
                progressBar.setVisibility(View.GONE);

                RankList list = RankList.deserialize(json, RankList.class);
                if (null != list) {
                    bindListViewData(list);
                }
            }
        }
    });

    @Override
    public void onResume() {
        super.onResume();
        if (this.activity != null)
            LocalBroadcastManager.getInstance(this.activity).registerReceiver(broadcastReceiver, DuelApp.getInstance().getIntentFilter());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_ranking, container, false);
    }

    private void find(View view) {
        this.listView = (ListView) view.findViewById(R.id.ranking_list_view);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        find(view);
    }

    private CommandType receiveCommandType;
    public void onReload(CommandType sendType, CommandType receiveType) {
        Log.d("command", sendType.toString());
        Log.d("command", receiveType.toString());

        this.receiveCommandType = receiveType;

        sendFetchRequest(sendType);
    }
}

