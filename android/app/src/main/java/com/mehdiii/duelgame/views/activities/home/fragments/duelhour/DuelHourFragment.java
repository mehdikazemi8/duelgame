package com.mehdiii.duelgame.views.activities.home.fragments.duelhour;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.responses.RankList;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.activities.ranking.fragments.adapters.RankingListAdapter;

/**
 * Created by mehdiii on 12/8/15.
 */
public class DuelHourFragment extends FlippableFragment {

    ListView listView;
    ProgressBar progressBar;

    RankingListAdapter adapter;
    Activity activity = null;
    boolean viewAvailable;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_duel_hour, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewAvailable = true;
        find(view);
    }

    private void find(View view) {
        listView = (ListView) view.findViewById(R.id.ranking_list_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.activity != null)
            LocalBroadcastManager.getInstance(this.activity).registerReceiver(broadcastReceiver, DuelApp.getInstance().getIntentFilter());
        viewAvailable = true;
    }

    @Override
    public void onBringToFront() {
        super.onBringToFront();
        sendFetchRequest();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewAvailable = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void bindListViewData(RankList list) {
        if (this.activity == null)
            return;

        adapter = new RankingListAdapter(this.activity, R.layout.template_ranklist, list.getTop());
        this.listView.setAdapter(adapter);
    }

    private void sendFetchRequest() {
        if (adapter != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
        DuelApp.getInstance().sendMessage(new BaseModel().serialize(CommandType.GET_DUEL_HOUR_RANKING));
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    private BroadcastReceiver broadcastReceiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_DUEL_HOUR_RANKING) {
                if(!viewAvailable)
                    return;
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);

                RankList list = RankList.deserialize(json, RankList.class);

                Log.d("TAG", "DuelHourFragment BroadcastReceiver " + json);

                if (list != null) {
                    bindListViewData(list);
                }
            }
        }
    });
}
