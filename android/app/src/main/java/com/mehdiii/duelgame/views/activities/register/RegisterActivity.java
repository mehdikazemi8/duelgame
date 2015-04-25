package com.mehdiii.duelgame.views.activities.register;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.MyBaseActivity;
import com.mehdiii.duelgame.views.activities.TryToConnectActivity;
import com.mehdiii.duelgame.views.activities.home.HomeActivity;
import com.mehdiii.duelgame.views.dialogs.AvatarSelectionDialog;

public class RegisterActivity extends MyBaseActivity {

    String userId;
    TextView hintTextView;
    TextView girlTextView;
    TextView boyTextView;
    EditText usernameEditText;
    EditText emailEditText;
    Button registerButton;
    ImageView selectedAvatarImageView;
    Spinner provinceSpinner;
    SwitchButton genderSwitch;
    User user = new User();

    BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_LOGIN_INFO) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        }
    });


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /**
         * find child views and set fonts
         */
        find();
        configure();

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, DuelApp.getInstance().getIntentFilter());

        /**
         * generate device unique identifier
         **/
        TelephonyManager teleManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        final String deviceId, simSerialNumber;
        deviceId = "" + teleManager.getDeviceId();
        simSerialNumber = "" + teleManager.getSimSerialNumber();
        userId = deviceId + simSerialNumber;

        if (DuelApp.getInstance().getSocket() == null || !DuelApp.getInstance().getSocket().isConnected()) {
            Log.d("^^^^^", "not connected");
            startActivity(new Intent(this, TryToConnectActivity.class));
        }
    }

    private void find() {
        selectedAvatarImageView = (ImageView) findViewById(R.id.start_his_avatar);
        usernameEditText = (EditText) findViewById(R.id.editText_username);
        emailEditText = (EditText) findViewById(R.id.editText_email);
        registerButton = (Button) findViewById(R.id.button_register);
//        chooseAvatarButton = (Button) findViewById(R.id.button);
        provinceSpinner = (Spinner) findViewById(R.id.spinner_province);
        hintTextView = (TextView) findViewById(R.id.textView_hint_avatat);
        girlTextView = (TextView) findViewById(R.id.textView_girl);
        boyTextView = (TextView) findViewById(R.id.textView_boy);
        genderSwitch = (SwitchButton) findViewById(R.id.switch_gender);
    }

    private void configure() {
        /**
         * set font to controls
         */
        FontHelper.setKoodakFor(this, usernameEditText, registerButton, emailEditText, girlTextView, boyTextView, hintTextView);
        genderSwitch.setChecked(true);
        this.girlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (genderSwitch.isChecked())
                    genderSwitch.performClick();
            }
        });

        this.boyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!genderSwitch.isChecked())
                    genderSwitch.performClick();
            }
        });
    }

    private boolean validateForm() {
        if (usernameEditText.getText().length() == 0) {
            Toast toast = Toast.makeText(this, "لطفا اسم خود را وارد نمایید.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return false;

        }

        if (provinceSpinner.getSelectedItem().toString().equals("استان")) {
            Toast toast = Toast.makeText(this, "لطفا استان خود را انتخاب نمایید.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return false;
        }
        return true;
    }

    public void registerMe(View v) {
        user.setName(usernameEditText.getText().toString());
        user.setEmail(emailEditText.getText().toString());
        user.setProvince(provinceSpinner.getSelectedItemPosition());
        user.setGender(genderSwitch.isChecked() ? 1 : 0);

        if (validateForm()) {
            user.setDeviceId(userId);
            DuelApp.getInstance().sendMessage(user.serialize(CommandType.SEND_REGISTER));
        }
    }

    public void chooseAvatar(View v) {
        AvatarSelectionDialog dialog = new AvatarSelectionDialog();
        dialog.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                setAvatar((int) data);
            }
        });
        dialog.show(getSupportFragmentManager(), "DIALOG_AVATAR_CHOOSER");
    }

    @Override
    public void onResume() {
        super.onResume();
        setAvatar(user.getAvatar());
    }

    private void setAvatar(int position) {
        user.setAvatar(position);
        selectedAvatarImageView.setImageResource(AvatarHelper.getResourceId(this, position));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}
