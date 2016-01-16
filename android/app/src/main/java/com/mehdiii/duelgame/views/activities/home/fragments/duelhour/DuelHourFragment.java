package com.mehdiii.duelgame.views.activities.home.fragments.duelhour;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.DuelHourInfo;
import com.mehdiii.duelgame.models.Friend;
import com.mehdiii.duelgame.models.MutualStats;
import com.mehdiii.duelgame.models.PVsPStatRequest;
import com.mehdiii.duelgame.models.Quiz;
import com.mehdiii.duelgame.models.QuizCourse;
import com.mehdiii.duelgame.models.Quizzes;
import com.mehdiii.duelgame.models.RemoveFriend;
import com.mehdiii.duelgame.models.UserForRanklist;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.responses.RankList;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.activities.ranking.fragments.adapters.RankingListAdapter;
import com.mehdiii.duelgame.views.dialogs.AlertDialog;
import com.mehdiii.duelgame.views.dialogs.ProfileDialog;

import java.util.List;

/**
 * Created by mehdiii on 12/8/15.
 */
public class DuelHourFragment extends FlippableFragment implements View.OnClickListener {

    private ProgressDialog progressDialog;
    private ProfileDialog profileDialog = null;
    private Friend selectedFriend;

    ListView listView;
    ProgressBar progressBar;
    TextView yourScoreCaption;
    TextView yourScoreValue;
    ImageButton refreshButton;
    ImageButton infoButton;

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
        configure();
    }

    private void sendGetInfoRequest() {
        if(progressDialog != null)
            progressDialog.show();
        DuelApp.getInstance().sendMessage(new BaseModel().serialize(CommandType.GET_DUEL_HOUR_INFO));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refresh_button:
                sendFetchRequest();
                break;
            case R.id.info_button:
                sendGetInfoRequest();
                break;
        }
    }

    private void configure() {
        FontHelper.setKoodakFor(getActivity(), yourScoreCaption, yourScoreValue);
        refreshButton.setOnClickListener(this);
        infoButton.setOnClickListener(this);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.please_wait_message));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if( ((UserForRanklist)adapterView.getAdapter().getItem(i)).getCup() == ParentActivity.SEPARATOR_CUP)
                progressDialog.show();

                UserForRanklist user = (UserForRanklist)adapterView.getAdapter().getItem(i);
                selectedFriend = new Friend();
                selectedFriend.setId(user.getId());
                selectedFriend.setAvatar(user.getAvatar());
                selectedFriend.setName(user.getName());
                selectedFriend.setProvince(user.getProvince());
                DuelApp.getInstance().sendMessage(new PVsPStatRequest(CommandType.GET_ONE_VS_ONE_RESULTS, selectedFriend.getId()).serialize());
            }
        });
    }

    private void find(View view) {
        listView = (ListView) view.findViewById(R.id.ranking_list_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        yourScoreCaption = (TextView) view.findViewById(R.id.your_score_caption);
        yourScoreValue = (TextView) view.findViewById(R.id.your_score_value);
        refreshButton = (ImageButton) view.findViewById(R.id.refresh_button);
        infoButton = (ImageButton) view.findViewById(R.id.info_button);
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
        if (this.activity != null)
            LocalBroadcastManager.getInstance(this.activity).unregisterReceiver(broadcastReceiver);
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

        yourScoreValue.setText(String.valueOf(list.getScore()));

        List <UserForRanklist> all = list.getTop();
        if(list.getTop().size() != 0) {
            UserForRanklist separator = new UserForRanklist(0, ParentActivity.SEPARATOR_CUP);
            all.add(separator);
            for(UserForRanklist user : list.getNear())
                all.add(user);
        }

        adapter = new RankingListAdapter(this.activity, R.layout.template_ranklist, all);
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

            Log.d("TAG", "DuelHourFragment onReceive");

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
            } else if (type == CommandType.RECEIVE_ONE_VS_ONE_RESULTS) {
                if(!viewAvailable || selectedFriend == null)
                    return;

                Log.d("TAG", "xxxx DuelHourFragment show");

                if(progressDialog != null)
                    progressDialog.dismiss();

                MutualStats mutualStats = MutualStats.deserialize(json, MutualStats.class);
                if(!mutualStats.getOpponentId().equals(selectedFriend.getId()))
                    return;
                selectedFriend.setStatistics(mutualStats);

                if(profileDialog != null)
                    profileDialog.dismiss();

                profileDialog = new ProfileDialog(getActivity(), selectedFriend, false);
                profileDialog.setOnRemoveListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Object data) {
                        DuelApp.getInstance().sendMessage(new RemoveFriend(selectedFriend.getId()).serialize(CommandType.SEND_REMOVE_FRIEND));
                        sendFetchRequest();
                    }
                });
                selectedFriend = null;
                Log.d("TAG", "xxxx DuelHourFragment show2222");
                profileDialog.show();
            } else if(type == CommandType.RECEIVE_DUEL_HOUR_INFO) {
                if(progressDialog != null)
                    progressDialog.dismiss();

                Log.d("TAG", "bbbbbbbbb RECEIVE_DUEL_HOUR_INFO");

                DuelHourInfo duelHourInfo = DuelHourInfo.deserialize(json, DuelHourInfo.class);
                AlertDialog alertDialog = new AlertDialog(getActivity(), duelHourInfo.getMessage());
                alertDialog.show();
            }
        }
    });
}
