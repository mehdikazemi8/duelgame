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
import android.widget.ImageView;
import android.widget.Toast;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.HeartTracker;
import com.mehdiii.duelgame.models.BlockRequest;
import com.mehdiii.duelgame.models.FriendRequest;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.responses.Status;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.squareup.picasso.Picasso;

/**
 * Created by mehdiii on 6/21/16.
 */
public class BlockAddDuelDialog extends Dialog implements View.OnClickListener {

    ProgressDialog progressDialog;
    ImageView avatar;
    CustomTextView name;
    CustomButton addFriend;
    CustomButton block;
    CustomButton duelOffline;
    String userNumber;
    int avatarInt;
    String nameStr;

    public BlockAddDuelDialog(Context context, String userNumber, int avatar, String name) {
        super(context);
        this.userNumber = userNumber;
        this.avatarInt = avatar;
        this.nameStr = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_block_add_duel);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        getWindow().setLayout((int) (metrics.widthPixels * 0.9), ActionBar.LayoutParams.WRAP_CONTENT);

        find();
        configure();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, DuelApp.getInstance().getIntentFilter());
    }

    private void find() {
        avatar = (ImageView) findViewById(R.id.avatar);
        name = (CustomTextView) findViewById(R.id.name);
        addFriend = (CustomButton) findViewById(R.id.button_add_friend);
        block = (CustomButton) findViewById(R.id.button_block);
        duelOffline = (CustomButton) findViewById(R.id.button_duel_offline);
    }

    private void configure() {
        addFriend.setOnClickListener(this);
        block.setOnClickListener(this);
        duelOffline.setOnClickListener(this);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getContext().getString(R.string.please_wait_message));

        Picasso.with(getContext()).load(AvatarHelper.getResourceId(getContext(), avatarInt)).into(avatar);
        name.setText(nameStr);
    }

    private void addFriendFunc() {
        FriendRequest request = new FriendRequest(userNumber);
        DuelApp.getInstance().sendMessage(request.serialize(CommandType.SEND_ADD_FRIEND));
        progressDialog.show();
        dismiss();
    }

    private void blockFunc() {
        DuelApp.getInstance().sendMessage(new BlockRequest(CommandType.BLOCK_REQUEST, userNumber).serialize());
        DuelApp.getInstance().toast(R.string.message_block_request_sent, Toast.LENGTH_LONG);
        dismiss();
    }

    private void offlineDuelFunc() {
        if (!HeartTracker.getInstance().canUseHeart()) {
            HeartLowDialog heartLowDialog = new HeartLowDialog(getContext());
            heartLowDialog.show();
            dismiss();
            return;
        }

        DuelDialog dialog = new DuelDialog(getContext(), true, userNumber);
        dialog.show();
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_friend:
                addFriendFunc();
                break;

            case R.id.button_block:
                blockFunc();
                break;

            case R.id.button_duel_offline:
                offlineDuelFunc();
                break;
        }
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    private void handleReceiveAddFriend(String json) {
        if (progressDialog != null)
            progressDialog.dismiss();

        FriendRequest request = BaseModel.deserialize(json, FriendRequest.class);
        if (request.getStatus().equals("duplicate")) {
            // use is already added.
            DuelApp.getInstance().toast(R.string.error_add_friend_duplicate, Toast.LENGTH_LONG);
        } else if (request.getStatus().equals("success")) {
            // successful
            DuelApp.getInstance().toast(R.string.success_add_friend, Toast.LENGTH_LONG);
            dismiss();
        }
    }

    BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            try {
                if (type == CommandType.RECEIVE_ADD_FRIEND) {
                    handleReceiveAddFriend(json);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
}
