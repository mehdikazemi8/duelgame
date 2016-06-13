package com.mehdiii.duelgame.views.activities.home.fragments.onlineusers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.Friend;
import com.mehdiii.duelgame.models.FriendRequest;
import com.mehdiii.duelgame.models.OnlineUsersList;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.chat.Message;
import com.mehdiii.duelgame.models.chat.responses.GetMessagesResponse;
import com.mehdiii.duelgame.models.chat.responses.SendMessageResponse;
import com.mehdiii.duelgame.utils.ChatHelper;
import com.mehdiii.duelgame.utils.DeviceManager;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.custom.CustomButton;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineUsersFragment extends FlippableFragment implements View.OnClickListener {
    ListView listView;
    MessageAdapter adapter;
    private ProgressBar progressBar;
    Activity activity = null;

    EditText textMessage;
    CustomButton sendButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_public_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        find(view);
        configure();
    }

    private void find(View view) {
        this.listView = (ListView) view.findViewById(R.id.listView_chats);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        textMessage = (EditText) view.findViewById(R.id.message_text);
        sendButton = (CustomButton) view.findViewById(R.id.button_send);
    }

    private void configure() {
        sendButton.setOnClickListener(this);

        adapter = new MessageAdapter(getActivity(), R.layout.template_message, DuelApp.messageDao.loadAll());
        listView.setAdapter(adapter);
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

    private void bindListViewData(List<Message> messageList) {
        adapter.notifyDataSetChanged();
        adapter = new MessageAdapter(getActivity(), R.layout.template_message, messageList);
        listView.setAdapter(adapter);
    }

    private void saveMessage(String messageStr) {
        Message myMessage = new Message();
        myMessage.setAvatar(AuthManager.getCurrentUser().getAvatar());
        myMessage.setChatId("public");
        myMessage.setName(AuthManager.getCurrentUser().getName());
        myMessage.setSender(AuthManager.getCurrentUser().getId());
        myMessage.setText(messageStr);
        myMessage.setTimestamp(ChatHelper.getLastMessageTimestamp());
        DuelApp.messageDao.insert(myMessage);
    }

    private void sendMessage() {
        final String messageStr = textMessage.getText().toString().trim();
        if (messageStr.isEmpty()) {
            textMessage.setText("");
            return;
        }
        textMessage.setText("");

        saveMessage(messageStr);
        bindListViewData(DuelApp.messageDao.loadAll());

        Call<SendMessageResponse> call = DuelApp.createChatApi().sendMessage(
                DeviceManager.getDeviceId(getActivity()), messageStr
        );

        call.enqueue(new Callback<SendMessageResponse>() {
            @Override
            public void onResponse(Call<SendMessageResponse> call, Response<SendMessageResponse> response) {
                if(!response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<SendMessageResponse> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send:
                sendMessage();
                break;
        }
    }

    private void sendFetchRequest() {
        Call<GetMessagesResponse> call = DuelApp.createChatApi().getMessages(DeviceManager.getDeviceId(getActivity()),
                ChatHelper.getLastMessageTimestamp());

        call.enqueue(new Callback<GetMessagesResponse>() {
            @Override
            public void onResponse(Call<GetMessagesResponse> call, Response<GetMessagesResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("TAGaaazzz", response.raw().toString());
                    DuelApp.messageDao.insertInTx(response.body().getMessages());
                    bindListViewData(DuelApp.messageDao.loadAll());
                }

                for (Message message : response.body().getMessages()) {
                    Log.d("TAGaaa", "p" + message.getTimestamp() + " " + message.getText());
                }
            }
            @Override
            public void onFailure(Call<GetMessagesResponse> call, Throwable t) {

            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (false) {
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
            } else if(type == CommandType.RECEIVE_ADD_FRIEND) {
            }
        }
    });

}
