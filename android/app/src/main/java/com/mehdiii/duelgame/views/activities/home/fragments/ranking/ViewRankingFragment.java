package com.mehdiii.duelgame.views.activities.home.fragments.ranking;

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

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.responses.RankList;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;

public class ViewRankingFragment extends Fragment {

    ListView listView;
    RankingListAdapter adapter;

    private void bindListViewData(RankList list) {
        adapter = new RankingListAdapter(getActivity(), R.layout.template_ranklist, list.getTop());
        this.listView.setAdapter(adapter);
    }

    private void sendFetchRequest(CommandType commandType) {
        if (adapter != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }

        User user = AuthManager.getCurrentUser();
        Log.d("sendFetch", user.serialize(commandType));
        DuelApp.getInstance().sendMessage(user.serialize(commandType));
    }

    private BroadcastReceiver broadcastReceiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_GET_FRIENDS_RANK ||
                    type == CommandType.RECEIVE_GET_PROVINCE_RANK ||
                    type == CommandType.RECEIVE_GET_TOTAL_RANK) {
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
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, DuelApp.getInstance().getIntentFilter());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_ranking, container, false);
    }

    private void find(View view) {
        this.listView = (ListView) view.findViewById(R.id.ranking_list_view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        find(view);
    }

    public void onReload(CommandType sendType, CommandType receiveType) {
        Log.d("command", sendType.toString());
        Log.d("command", receiveType.toString());

        sendFetchRequest(sendType);
    }
}

