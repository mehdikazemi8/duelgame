package com.mehdiii.duelgame.views.activities.home.fragments.friends;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.Friend;
import com.mehdiii.duelgame.models.FriendList;
import com.mehdiii.duelgame.models.RemoveFriend;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.WannaChallenge;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.activities.waiting.WaitingActivity;
import com.mehdiii.duelgame.views.dialogs.AddFriendDialog;
import com.mehdiii.duelgame.views.dialogs.DuelFriendDialog;
import com.mehdiii.duelgame.views.dialogs.ProfileDialog;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by omid on 4/5/2015.
 */
public class FriendsFragment extends FlippableFragment implements View.OnClickListener {
    //    TabPageIndicator indicator;
    ListView listView;
    FriendsListAdapter adapter;

    LinearLayout containerHeader;
    private TextView textViewCode;
    private Button buttonAddFriend;
    private Button buttonTellFriend;
    private ProgressBar progressBar;

    Activity activity = null;

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
        buttonTellFriend = (Button) view.findViewById(R.id.button_tell_friends);
        textViewCode = (TextView) view.findViewById(R.id.textView_code);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
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

    private void configure() {
        buttonAddFriend.setOnClickListener(this);
        buttonTellFriend.setOnClickListener(this);
        if (this.activity != null)
            FontHelper.setKoodakFor(this.activity, textViewCode, buttonAddFriend, buttonTellFriend);
    }

    private void bindViewData() {
        User currentUser = AuthManager.getCurrentUser();
        if (currentUser != null)
            textViewCode.setText("کد شما " + currentUser.getId());
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

    private void bindListViewData(FriendList list) {
        if (activity == null)
            return;
        Collections.sort(list.getFriends(), new Comparator<Friend>() {
            @Override
            public int compare(Friend x, Friend y) {
                if(x.getStatus().equals("request") || y.getStatus().equals("request")) {

                    if(x.getStatus().equals("request") && !y.getStatus().equals("request"))
                        return -1;
                    if(!x.getStatus().equals("request") && y.getStatus().equals("request"))
                        return 1;
                    return x.getId().compareTo(y.getId());
                }
                else if(x.isOnline() || y.isOnline()) {
                    if(x.isOnline() && !y.isOnline())
                        return -1;
                    if(!x.isOnline() && y.isOnline())
                        return 1;
                    return x.getId().compareTo(y.getId());
                }
                else if(x.getStatus().equals("friend") || y.getStatus().equals("friend")) {

                    if(x.getStatus().equals("friend") && !y.getStatus().equals("friend"))
                        return -1;
                    if(!x.getStatus().equals("friend") && y.getStatus().equals("friend"))
                        return 1;
                    return x.getId().compareTo(y.getId());
                }
                return x.getId().compareTo(y.getId());
            }
        });
        adapter = new FriendsListAdapter(this.activity, R.layout.template_friends_list, list.getFriends());
        adapter.setOnUserDecisionIsMade(onUserDecisionIsMadeListener);
        this.listView.setAdapter(adapter);
    }

    private void tellFriends() {
        Tracker tracker = DuelApp.getInstance().getTracker(DuelApp.TrackerName.GLOBAL_TRACKER);
        // Build and send an Event.
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("button_click")
                .setAction("report_button")
                .setLabel("tell_friend")
                .build());

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                 String.format(getResources().getString(R.string.message_share),
                        "http://cafebazaar.ir/app/" + this.activity.getPackageName()));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    FriendsListAdapter.OnUserDecisionIsMade onUserDecisionIsMadeListener = new FriendsListAdapter.OnUserDecisionIsMade() {
        @Override
        public void onDuel(final Friend request) {
            final DuelFriendDialog dialog = new DuelFriendDialog(FriendsFragment.this.activity);
            dialog.setOnResult(new DuelFriendDialog.OnResult() {
                @Override
                public void getChallenge(WannaChallenge challenge) {
                    Tracker tracker = DuelApp.getInstance().getTracker(DuelApp.TrackerName.GLOBAL_TRACKER);
                    // Build and send an Event.
                    tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("button_click")
                            .setAction("duel_button")
                            .setLabel("send_duel_request")
                            .build());

//                    AlertDialog dialog = new AlertDialog(FriendsFragment.this.activity, "به زودی این قابلیت اضافه خواهد شد.");
//                    dialog.show();


//                    challenge.setUserNumber(request.getId());
//                    DuelApp.getInstance().sendMessage(challenge.serialize(CommandType.SEND_WANNA_CHALLENGE));
//
                    ((ParentActivity) getActivity()).category = String.valueOf(challenge.getCategory());

                    Intent i = new Intent(getActivity(), WaitingActivity.class);
                    i.putExtra("user_number", request.getId());
                    i.putExtra("category", challenge.getCategory());
                    i.putExtra("message", challenge.getMessage());
                    i.putExtra("master", true);

                    if (FriendsFragment.this.activity == null || !(FriendsFragment.this.activity instanceof ParentActivity)) {
                        Log.d("FRIEND_FRAGMENT", "activity is null");
                        return;
                    }

                    startActivity(i);
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        @Override
        public void onApprove(Friend request) {
            request.setAccepted(true);
            DuelApp.getInstance().sendMessage(request.serialize(CommandType.SEND_FRIEND_REQUEST_RESPONSE));
            sendFetchRequest();
        }

        @Override
        public void onReject(Friend request) {
            request.setAccepted(false);
            DuelApp.getInstance().sendMessage(request.serialize(CommandType.SEND_FRIEND_REQUEST_RESPONSE));
            sendFetchRequest();
        }

        @Override
        public void onSelect(final Friend friend) {
            ProfileDialog dialog = new ProfileDialog(getActivity(), friend);
            dialog.setOnRemoveListener(new OnCompleteListener() {
                @Override
                public void onComplete(Object data) {
                    DuelApp.getInstance().sendMessage(new RemoveFriend(friend.getId()).serialize(CommandType.SEND_REMOVE_FRIEND));
                    sendFetchRequest();
                }
            });
            dialog.show();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_friend:
                openAddFriendDialog();
                break;
            case R.id.button_tell_friends:
                tellFriends();
                break;
        }
    }

    private void sendFetchRequest() {
        if (adapter != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
        User user = AuthManager.getCurrentUser();
        DuelApp.getInstance().sendMessage(user.serialize(CommandType.SEND_GET_FRIEND_LIST));
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    private BroadcastReceiver broadcastReceiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_GET_FRIEND_LIST) {
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
                FriendList list = FriendList.deserialize(json, FriendList.class);
                if (null != list) {
                    bindListViewData(list);
                }
            }
        }
    });

    private void openAddFriendDialog() {
        AddFriendDialog dialog = new AddFriendDialog(this.activity);
        dialog.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                sendFetchRequest();
            }
        });
        dialog.show();
    }
}
