package com.mehdiii.duelgame.views.activities.home.fragments.friends;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.activities.home.fragments.FlipableFragment;
import com.mehdiii.duelgame.views.dialogs.AddFriendDialog;

import java.util.List;

/**
 * Created by omid on 4/5/2015.
 */
public class FriendsFragment extends FlipableFragment implements View.OnClickListener {
    //    TabPageIndicator indicator;
    ListView listView;
    private LinearLayout containerHeader;
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

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("MESSAGE"));
        super.onResume();
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
        textViewCode.setText("کد شما" + currentUser.getId());
        sendFetchRequest();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_friend:
                openAddFriendDialog();
                break;
        }
    }

    private void sendFetchRequest() {
        User user = AuthManager.getCurrentUser();
        DuelApp.getInstance().sendMessage(user.getFriendsRequest().serialize());
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String json = intent.getExtras().getString("inputMessage");
//            User.deserialize(json, User.class);
        }
    };

    private void openAddFriendDialog() {
        AddFriendDialog dialog = new AddFriendDialog(getActivity());
        dialog.show();
    }
}
