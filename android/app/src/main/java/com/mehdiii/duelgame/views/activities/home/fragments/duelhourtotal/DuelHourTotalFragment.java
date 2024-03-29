package com.mehdiii.duelgame.views.activities.home.fragments.duelhourtotal;

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
import com.mehdiii.duelgame.models.UserForRanklist;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.responses.RankList;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;

import java.util.List;

public class DuelHourTotalFragment extends Fragment {

    private boolean viewAvailable;
    ProgressBar progressBar;
    ListView listView;

    public static DuelHourTotalFragment getInstance() {
        return new DuelHourTotalFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_duel_hour_total, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewAvailable = true;
        find(view);
        configure();
    }

    private void configure() {
        sendFetchData();
    }

    private void sendFetchData() {
        progressBar.setVisibility(View.VISIBLE);
        DuelApp.getInstance().sendMessage(new BaseModel(CommandType.GET_DUEL_HOUR_RANKING_TOTAL).serialize());
    }

    private void find(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        listView = (ListView) view.findViewById(R.id.list_view_total_duel_hour);
    }

    private boolean areEqual(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size() || a.size() != 3)
            return false;

        return a.get(0).equals(b.get(0)) &&
                a.get(1).equals(b.get(1)) &&
                a.get(2).equals(b.get(2));
    }

    private void bindListViewData(RankList list) {

        List<UserForRanklist> all = list.getTop();
        if (list.getTop().size() != 0) {
            UserForRanklist separator = new UserForRanklist(0, ParentActivity.SEPARATOR_CUP);
            all.add(separator);
            for (UserForRanklist user : list.getNear())
                all.add(user);
        }

        DuelHourTotalAdapter adapter = new DuelHourTotalAdapter(getActivity(), R.layout.template_duel_hour_total, all);
        listView.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        viewAvailable = true;
        if (getActivity() != null)
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewAvailable = true;
        if (getActivity() != null)
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, DuelApp.getInstance().getIntentFilter());
    }

    private BroadcastReceiver broadcastReceiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {

            if (type == CommandType.RECEIVE_DUEL_HOUR_RANKING_TOTAL) {
                if (!viewAvailable)
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
