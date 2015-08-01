package com.mehdiii.duelgame.views.activities.ranking.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.GetCourseRanking;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.responses.RankList;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.ranking.fragments.adapters.RankingListAdapter;

public class ViewRankingFragment extends Fragment {

    ListView listView;
    ProgressBar progressBar;
    RankingListAdapter adapter;
    TextView userScoreValue;
    TextView scoreCaptionTextView;
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
        this.userScoreValue.setText(String.valueOf(list.getScore()));
    }

    private void sendFetchRequest(String periodType) {
        if (adapter != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }

        DuelApp.getInstance().sendMessage((new GetCourseRanking(ParentActivity.category, periodType)).serialize(CommandType.GET_COURSE_RANKING));
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    private BroadcastReceiver broadcastReceiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_COURSE_RANKING) {
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
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_view_ranking, container, false);
    }

    private void find(View view) {
        this.listView = (ListView) view.findViewById(R.id.ranking_list_view);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        this.userScoreValue = (TextView) view.findViewById(R.id.user_score_value);
        this.scoreCaptionTextView = (TextView) view.findViewById(R.id.textView_score_caption);
    }

    private void configure() {
        FontHelper.setKoodakFor(this.activity, scoreCaptionTextView, userScoreValue);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        find(view);
        configure();
        onReload("week");
    }

    public void onReload(String sendPeriodType) {
        sendFetchRequest(sendPeriodType);
    }
}

