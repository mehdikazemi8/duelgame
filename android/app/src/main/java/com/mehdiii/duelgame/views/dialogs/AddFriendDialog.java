package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceived;

/**
 * Created by omid on 4/13/2015.
 */
public class AddFriendDialog extends Dialog implements View.OnClickListener {
    public AddFriendDialog(Context context) {
        super(context);
    }

    private TextView textViewLabelCode;
    private EditText editTextCode;
    private Button buttonAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_friend);
        find();
        configure();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, DuelApp.getInstance().getIntentFilter());
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    private void find() {
        textViewLabelCode = (TextView) findViewById(R.id.textView_label_code);
        editTextCode = (EditText) findViewById(R.id.editText_code);
        buttonAdd = (Button) findViewById(R.id.button_add_friend);
    }

    private void configure() {
        FontHelper.setKoodakFor(getContext(), buttonAdd, editTextCode, textViewLabelCode);
        buttonAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_friend:
                sendAddFriendRequest();
                break;
        }
    }

    private void sendAddFriendRequest() {
        DuelApp.getInstance().sendMessage(AuthManager.getCurrentUser().getAddFriendRequest().serialize());
    }


    private BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceived() {
        @Override
        public void onReceive(String json, String type) {
            return;
        }
    });
}
