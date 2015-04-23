package com.mehdiii.duelgame.views.dialogs;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;

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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_friend);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        getWindow().setLayout((int) (metrics.widthPixels * 0.9), ActionBar.LayoutParams.WRAP_CONTENT);


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
        User user = new User();
        user.setId(editTextCode.getText().toString());

        DuelApp.getInstance().sendMessage(user.getAddFriendRequest().serialize());
        DuelApp.getInstance().toast(R.string.toast_friend_request_sent, Toast.LENGTH_SHORT);
        dismiss();
    }


    private BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            return;
        }
    });
}
