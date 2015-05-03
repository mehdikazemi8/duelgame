package com.mehdiii.duelgame.views.dialogs;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.mehdiii.duelgame.models.FriendRequest;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.OnCompleteListener;

/**
 * Created by omid on 4/13/2015.
 */
public class AddFriendDialog extends Dialog implements View.OnClickListener {
    public AddFriendDialog(Context context) {
        super(context);
    }

    ProgressDialog progressDialog;

    private TextView textViewLabelCode;
    private EditText editTextCode;
    private Button buttonAdd;

    OnCompleteListener onCompleteListener = null;

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

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
        if (validateForm()) {
            FriendRequest request = new FriendRequest(editTextCode.getText().toString());
            DuelApp.getInstance().sendMessage(request.serialize(CommandType.SEND_ADD_FRIEND));
            progressDialog = new ProgressDialog(getContext());
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setMessage("لطفا کمی صبر کنید.");
            progressDialog.show();
        }
    }

    private boolean validateForm() {
        if (editTextCode.getText().toString().isEmpty()) {
            DuelApp.getInstance().toast(R.string.invalid_add_friend_code_empty, Toast.LENGTH_SHORT);
            return false;
        }

        return true;
    }


    private BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {

            if (type == CommandType.RECEIVE_ADD_FRIEND) {
                if (progressDialog != null)
                    progressDialog.dismiss();

                FriendRequest request = BaseModel.deserialize(json, FriendRequest.class);
                if (request.getStatus().equals("invalid")) {
                    // friend code doesn't exists
                    DuelApp.getInstance().toast(R.string.error_add_friend_invalid, Toast.LENGTH_SHORT);
                } else if (request.getStatus().equals("duplicate")) {
                    // use is already added.
                    DuelApp.getInstance().toast(R.string.error_add_friend_duplicate, Toast.LENGTH_SHORT);
                } else if (request.getStatus().equals("success")) {
                    // successful
                    dismiss();
                    if (onCompleteListener != null)
                        onCompleteListener.onComplete(true);
                }
            }
        }
    });
}
