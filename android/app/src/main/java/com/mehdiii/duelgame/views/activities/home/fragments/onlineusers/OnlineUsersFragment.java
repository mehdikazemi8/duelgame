package com.mehdiii.duelgame.views.activities.home.fragments.onlineusers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.Friend;
import com.mehdiii.duelgame.models.FriendRequest;
import com.mehdiii.duelgame.models.OnlineUsersList;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;

import java.util.Collections;
import java.util.Comparator;

public class OnlineUsersFragment extends FlippableFragment implements View.OnClickListener {
    ListView listView;
    private ProgressBar progressBar;
    OnlineUsersListAdapter adapter;
    Activity activity = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_online_users, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        find(view);
    }

    private void find(View view) {
        this.listView = (ListView) view.findViewById(R.id.listView_friends);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (this.activity != null)
            LocalBroadcastManager.getInstance(this.activity).registerReceiver(broadcastReceiver, DuelApp.getInstance().getIntentFilter());
    }

    @Override
    public void onBringToFront() {
        super.onBringToFront();
        sendFetchRequest();
    }

    @Override
    public void onPause() {
        super.onPause();
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

    private void bindListViewData(OnlineUsersList list) {
        if (activity == null)
            return;
        Collections.sort(list.getUsers(), new Comparator<Friend>() {
            @Override
            public int compare(Friend x, Friend y) {
                return x.getName().compareTo(y.getName());
            }
        });
        adapter = new OnlineUsersListAdapter(this.activity, R.layout.template_friends_list, list.getUsers(), progressBar);
        this.listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
    }

    private void sendFetchRequest() {
        if (adapter != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
        DuelApp.getInstance().sendMessage(new BaseModel().serialize(CommandType.SEND_GET_ONLINE_USERS));
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    private BroadcastReceiver broadcastReceiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_GET_ONLINE_USERS) {
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
                OnlineUsersList list = OnlineUsersList.deserialize(json, OnlineUsersList.class);
                if (null != list)
                    bindListViewData(list);
            } else if(type == CommandType.RECEIVE_ADD_FRIEND) {
                sendFetchRequest();
            }
        }
    });

}
