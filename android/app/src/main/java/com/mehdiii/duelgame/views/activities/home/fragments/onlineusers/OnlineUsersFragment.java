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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.ChatUpdate;
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
import com.mehdiii.duelgame.views.dialogs.BlockAddDuelDialog;
import com.mehdiii.duelgame.views.dialogs.GetPhoneNumberDialog;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineUsersFragment extends FlippableFragment implements View.OnClickListener {
    ListView listView;
    MessageAdapter adapter;
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
        sendFetchRequest();
    }

    private void find(View view) {
        listView = (ListView) view.findViewById(R.id.listView_chats);
        textMessage = (EditText) view.findViewById(R.id.message_text);
        sendButton = (CustomButton) view.findViewById(R.id.button_send);
    }

    private void configure() {
        sendButton.setOnClickListener(this);
        bindListViewData(DuelApp.messageDao.loadAll());
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
        if(messageList == null || messageList.size() == 0)
            return;

        Collections.sort(messageList, new Comparator<Message>() {
            @Override
            public int compare(Message x, Message y) {
                if(!x.getTimestamp().equals( y.getTimestamp() )) {
                    return x.getTimestamp() < y.getTimestamp() ? -1 : +1;
                }
                return x.getSender().compareTo(y.getSender());
            }
        });

        adapter = new MessageAdapter(getActivity(), R.layout.template_message, messageList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!((Message)parent.getAdapter().getItem(position)).getSender().equals("backend")) {
                    BlockAddDuelDialog dialog = new BlockAddDuelDialog(getActivity(),
                            ((Message)parent.getAdapter().getItem(position)).getSender(),
                            ((Message)parent.getAdapter().getItem(position)).getAvatar(),
                            ((Message)parent.getAdapter().getItem(position)).getName());
                    dialog.show();
                }
            }
        });
        listView.setSelection(listView.getCount());
    }

    private void saveMessage(String messageStr) {
//        Message myMessage = new Message();
//        myMessage.setAvatar(AuthManager.getCurrentUser().getAvatar());
//        myMessage.setChatId("public");
//        myMessage.setName(AuthManager.getCurrentUser().getName());
//        myMessage.setSender(AuthManager.getCurrentUser().getId());
//        myMessage.setText(messageStr);
//        myMessage.setTimestamp(ChatHelper.getLastMessageTimestamp());
//        DuelApp.messageDao.insert(myMessage);
    }

    private void verifyPhoneNumber() {
        GetPhoneNumberDialog dialog = new GetPhoneNumberDialog(getActivity());
        dialog.setCancelable(false);
        dialog.show();
    }

    private void sendMessage() {
        try {
            if(!AuthManager.getCurrentUser().isPhoneNumberVerified()) {
                verifyPhoneNumber();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String messageStr = textMessage.getText().toString().trim();
        if (messageStr.isEmpty()) {
            textMessage.setText("");
            return;
        }
        textMessage.setText("");

        //saveMessage(messageStr);
        //bindListViewData(DuelApp.messageDao.loadAll());

        Call<SendMessageResponse> call = DuelApp.createChatApi().sendMessage(
                DeviceManager.getDeviceId(getActivity()),
                RequestBody.create(MediaType.parse("text/plain"), messageStr)
        );

        call.enqueue(new Callback<SendMessageResponse>() {
            @Override
            public void onResponse(Call<SendMessageResponse> call, Response<SendMessageResponse> response) {
                if(response.code() == 403) {
                    DuelApp.getInstance().toast(R.string.cant_send_message_now, Toast.LENGTH_LONG);
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
                    DuelApp.messageDao.insertInTx(response.body().getMessages());
                    bindListViewData(DuelApp.messageDao.loadAll());
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
            if (type == CommandType.YOU_HAVE_NEW_MESSAGE) {
                ChatUpdate update = ChatUpdate.deserialize(json, ChatUpdate.class);
                if("public".equals(update.getRoom())) {
                    sendFetchRequest();
                }
            }
        }
    });
}
