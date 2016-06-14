package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.DeliveryReport;
import com.mehdiii.duelgame.models.SendPhoneNumber;
import com.mehdiii.duelgame.models.VerifyPhoneNumber;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnPurchaseResult;
import com.mehdiii.duelgame.models.responses.Status;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.mehdiii.duelgame.views.custom.CustomTextView;

import de.greenrobot.event.EventBus;

/**
 * Created by mehdiii on 6/14/16.
 */
public class GetPhoneNumberDialog extends Dialog implements View.OnClickListener {

    private final int SEND_PHONE_STATE = 0;
    private final int SEND_VERIFICATION_STATE = 1;

    ProgressDialog progressDialog;
    EditText phoneNumber;
    CustomButton sendButton;
    CustomTextView caption;
    ImageButton closeButton;

    int currentState = SEND_PHONE_STATE;

    public GetPhoneNumberDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_get_phone_number);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        getWindow().setLayout((int) (metrics.widthPixels * 0.9), -2);
        findControls();
        configure();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, DuelApp.getInstance().getIntentFilter());
    }

    private void findControls() {
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        sendButton = (CustomButton) findViewById(R.id.button_send);
        caption = (CustomTextView) findViewById(R.id.caption_text);
        closeButton = (ImageButton) findViewById(R.id.button_close);
    }

    private void configure() {
        FontHelper.setKoodakFor(getContext(), phoneNumber);
        sendButton.setOnClickListener(this);
        closeButton.setOnClickListener(this);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_close:
                dismiss();
                break;

            case R.id.button_send:
                if(currentState == SEND_PHONE_STATE) {
                    sendPhoneNumber();
                } else {
                    sendVerificationCode();
                }
                break;
        }
    }

    private void sendPhoneNumber() {
        String phoneStr = phoneNumber.getText().toString().trim();
        if(!checkPhoneNumber(phoneStr)) {
            DuelApp.getInstance().toast(R.string.enter_phone_correct, Toast.LENGTH_LONG);
        } else {
            DuelApp.getInstance().sendMessage(new SendPhoneNumber(CommandType.SAVE_PHONE_NUMBER, phoneStr).serialize());
            progressDialog.setMessage(getContext().getString(R.string.sending_verification_code));
            progressDialog.show();
        }
    }

    private void sendVerificationCode() {
        String verificationNumber = phoneNumber.getText().toString().trim();
        if(verificationNumber.isEmpty()) {
            DuelApp.getInstance().toast(R.string.enter_verification_code_toast, Toast.LENGTH_LONG);
        } else {
            DuelApp.getInstance().sendMessage(new VerifyPhoneNumber(CommandType.VERIFY_PHONE_NUMBER,
                    Integer.valueOf(verificationNumber)).serialize());
            progressDialog.setMessage(getContext().getString(R.string.checking_verification_code));
            progressDialog.show();
        }
    }

    private boolean checkPhoneNumber(String phone) {
        if(phone == null || phone.isEmpty())
            return false;

        return phone.matches("^(?:0098|\\+98|0)?9\\d{9}$");
    }

    BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            try {
                if (type == CommandType.RECEIVE_SAVE_PHONE_NUMBER) {
                    Status status = Status.deserialize(json, Status.class);
                    progressDialog.hide();
                    if(status.isSuccessful()) {
                        phoneNumber.setHint(getContext().getString(R.string.hint_verification_code));
                        phoneNumber.setText("");
                        caption.setText(getContext().getString(R.string.caption_enter_verification_code));
                        caption.setTextColor(getContext().getResources().getColor(R.color.blue));
                        currentState = SEND_VERIFICATION_STATE;
                    } else {
                        DuelApp.getInstance().toast(R.string.try_again_later, Toast.LENGTH_LONG);
                        dismiss();
                    }
                } else if(type == CommandType.RECEIVE_VERIFY_PHONE_NUMBER) {
                    Status status = Status.deserialize(json, Status.class);
                    progressDialog.hide();
                    if(status.isSuccessful()) {
                        DuelApp.getInstance().toast(R.string.verified_you_can_chat, Toast.LENGTH_LONG);
                        AuthManager.getCurrentUser().setPhoneNumberVerified(true);
                        dismiss();
                    } else {
                        DuelApp.getInstance().toast(R.string.send_correct_code, Toast.LENGTH_LONG);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
}
