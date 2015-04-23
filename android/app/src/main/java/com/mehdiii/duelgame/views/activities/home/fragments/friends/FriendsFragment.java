package com.mehdiii.duelgame.views.activities.home.fragments.friends;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.Friend;
import com.mehdiii.duelgame.models.FriendList;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.home.fragments.FlipableFragment;
import com.mehdiii.duelgame.views.dialogs.AddFriendDialog;

import java.util.List;

/**
 * Created by omid on 4/5/2015.
 */
public class FriendsFragment extends FlipableFragment implements View.OnClickListener {
    //    TabPageIndicator indicator;
    ListView listView;
    FriendsListAdapter adapter;

    LinearLayout containerHeader;
    private TextView textViewCode;
    private Button buttonAddFriend;
    List<User> friends;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        find(view);
        configure();
        bindViewData();
    }

    private void find(View view) {
        this.listView = (ListView) view.findViewById(R.id.listView_friends);
        containerHeader = (LinearLayout) view.findViewById(R.id.container_header);
        buttonAddFriend = (Button) view.findViewById(R.id.button_add_friend);
        textViewCode = (TextView) view.findViewById(R.id.textView_code);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, DuelApp.getInstance().getIntentFilter());
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

    private void configure() {
        buttonAddFriend.setOnClickListener(this);
        FontHelper.setKoodakFor(getActivity(), textViewCode, buttonAddFriend);
    }

    private void bindViewData() {
        User currentUser = AuthManager.getCurrentUser();
        textViewCode.setText("کد شما " + currentUser.getId());
    }

    private void bindListViewData(FriendList list) {
        adapter = new FriendsListAdapter(getActivity(), R.layout.template_friends_list, list.getFriends());
        adapter.setOnUserDecisionIsMade(onUserDecisionIsMadeListener);
        this.listView.setAdapter(adapter);
    }

    FriendsListAdapter.OnUserDecisionIsMade onUserDecisionIsMadeListener = new FriendsListAdapter.OnUserDecisionIsMade() {
        @Override
        public void onDuel(Friend request) {
            // TODO
        }

        @Override
        public void onApprove(Friend request) {
            DuelApp.getInstance().sendMessage(request.getRespondFriendRequest(true).serialize());
            sendFetchRequest();
        }

        @Override
        public void onReject(Friend request) {
            DuelApp.getInstance().sendMessage(request.getRespondFriendRequest(false).serialize());
            sendFetchRequest();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_friend:
                openAddFriendDialog();
                break;
        }
    }

    private void sendFetchRequest() {
        if (adapter != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }

        User user = AuthManager.getCurrentUser();
        DuelApp.getInstance().sendMessage(user.getFriendsRequest().serialize());
    }

    private BroadcastReceiver broadcastReceiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_GET_FRIEND_LIST) {
                FriendList list = FriendList.deserialize(json, FriendList.class);
                if (null != list) {
                    bindListViewData(list);
                }
            }
        }
    });

    private void openAddFriendDialog() {
        AddFriendDialog dialog = new AddFriendDialog(getActivity());
        dialog.show();
    }
}
