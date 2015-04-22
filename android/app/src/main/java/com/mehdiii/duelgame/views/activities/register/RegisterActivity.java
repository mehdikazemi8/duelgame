package com.mehdiii.duelgame.views.activities.register;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.MyBaseActivity;
import com.mehdiii.duelgame.views.activities.TryToConnectActivity;
import com.mehdiii.duelgame.views.activities.home.HomeActivity;
import com.mehdiii.duelgame.views.dialogs.AvatarSelectionDialog;

import org.json.JSONException;
import org.json.JSONObject;

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

    BroadcastReceiver mListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("MESSAGE")) {

                String inputMessage = intent.getExtras().getString("inputMessage");
                String messageCode;
                JSONObject parser = null;

                try {
                    parser = new JSONObject(inputMessage);
                    messageCode = parser.getString("code");
                    if (messageCode.compareTo("LI") == 0) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /**
         * find child views and set fonts
         */
        find();
        configure();


        LocalBroadcastManager.getInstance(this).registerReceiver(mListener, new IntentFilter("MESSAGE"));

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
        // TODO
//        myName = usernameEditText.getText().toString();
//        myEmail = emailEditText.getText().toString();
//        myOstanStr = provinceSpinner.getSelectedItem().toString();
//        myOstanInt = provinceSpinner.getSelectedItemPosition();

        if (validateForm()) {
            User registerUser = User.newInstance(User.CommandType.REGISTER);
            registerUser.setDeviceId(userId);
            // TODO
//            registerUser.setName(myName);
//            registerUser.setProvince(myOstanInt);
//            registerUser.setEmail(myEmail);
//            registerUser.setAvatar(AuthManager.getCurrentUser().getAvatar());

            DuelApp.getInstance().sendMessage(registerUser.serialize());
        }
    }

    public void chooseAvatar(View v) {
        AvatarSelectionDialog dialog = new AvatarSelectionDialog();
        dialog.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                setAvatar();
            }
        });
        dialog.show(getSupportFragmentManager(), "DIALOG_AVATAR_CHOOSER");
    }

    @Override
    public void onResume() {
        super.onResume();
        setAvatar();
    }

    private void setAvatar() {
        selectedAvatarImageView.setImageResource(AvatarHelper.getResourceId(this, 1));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mListener);
    }
}
