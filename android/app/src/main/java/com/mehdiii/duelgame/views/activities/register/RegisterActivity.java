package com.mehdiii.duelgame.views.activities.register;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.activities.MyBaseActivity;
import com.mehdiii.duelgame.views.activities.TryToConnectActivity;
import com.mehdiii.duelgame.views.activities.home.HomeActivity;
import com.mehdiii.duelgame.views.dialogs.AvatarSelectionDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends MyBaseActivity {

    String userId;
    EditText usernameEditText;
    EditText emailEditText;
    Button registerButton;
    Button chooseAvatarButton;
    Spinner provinceSpinner;

    BroadcastReceiver mListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("%%%%%%%%%%%", "onReceive register Activity");

            if (intent.getAction().equals("MESSAGE")) {
                Log.d("-------", "tooye if MESSAGE");

                String inputMessage = intent.getExtras().getString("inputMessage");
                String messageCode;
                JSONObject parser = null;

                try {
                    parser = new JSONObject(inputMessage);
                    messageCode = parser.getString("code");
                    if (messageCode.compareTo("LI") == 0) {
                        Log.d("**** Start Activity ", inputMessage);
                        loginInfo = inputMessage;
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    Log.d("---------", "can not parse string");
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


        registerReceiver(mListener, new IntentFilter("MESSAGE"));

        /**
         * generate device unique identifier
         **/
        TelephonyManager teleManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        final String deviceId, simSerialNumber;
        deviceId = "" + teleManager.getDeviceId();
        simSerialNumber = "" + teleManager.getSimSerialNumber();
        userId = deviceId + simSerialNumber;

        if (wsc == null || !wsc.isConnected()) {
            Log.d("^^^^^", "not connected");
            startActivity(new Intent(this, TryToConnectActivity.class));
        }
    }

    private void find() {
        usernameEditText = (EditText) findViewById(R.id.editText_username);
        emailEditText = (EditText) findViewById(R.id.editText_email);
        registerButton = (Button) findViewById(R.id.button_register);
        chooseAvatarButton = (Button) findViewById(R.id.button_choose_avatar);
        provinceSpinner = (Spinner) findViewById(R.id.spinner_province);
    }

    private void configure() {
        /**
         * set font to controls
         */
        FontHelper.setKoodakFor(this, usernameEditText, registerButton, emailEditText, chooseAvatarButton);
    }


    public String getStringFromEditText(int id) {
        EditText et = (EditText) findViewById(id);
        Log.d("@@@ ", "--- " + et.getText().length());
        return et.getText().toString();
    }

    public String getStringFromSpinner(int id) {
        Spinner sp = (Spinner) findViewById(id);
        return sp.getSelectedItem().toString();
    }

    public void registerMe(View v) {
//        myName = getStringFromEditText(R.id.start_player_name);
//        myEmail = getStringFromEditText(R.id.start_player_email);
//        myOstanStr = getStringFromSpinner(R.id.start_ostan_name);

//        Log.d("%%%% ", myName);
//        Log.d("%%%% ", myEmail);
//        Log.d("%%%% ", "" + myAvatarIndex);
//        Log.d("%%%% ", myOstanStr);
//
//        if (((EditText) findViewById(R.id.start_player_name)).getText().length() == 0) {
//            Toast toast = Toast.makeText(this, "لطفا اسم خود را وارد نمایید.", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.TOP, 0, 0);
//            toast.show();
//            return;
//        }
//        if (((Spinner) findViewById(R.id.start_ostan_name)).getSelectedItem().toString().equals("استان")) {
//            Toast toast = Toast.makeText(this, "لطفا استان خود را انتخاب نمایید.", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.TOP, 0, 0);
//            toast.show();
//            return;
//        }
//
//        Log.d("!!!!!!!", ">>" + ((Spinner) findViewById(R.id.start_ostan_name)).getSelectedItemPosition());
//        myOstanInt = ((Spinner) findViewById(R.id.start_ostan_name)).getSelectedItemPosition();
//
//        JSONObject query = new JSONObject();
//        try {
//            query.put("code", "RU");
//            query.put("user_id", userId);
//            query.put("name", myName);
//            query.put("ostan", myOstanInt);
//            query.put("email", myEmail);
//            query.put("avatar", myAvatarIndex);
//
//            wsc.sendTextMessage(query.toString());
//        } catch (JSONException e) {
//            Log.d("---- Register ACTIVITY", e.toString());
//        }
    }

    public void chooseAvatar(View v) {
        AvatarSelectionDialog dialog = new AvatarSelectionDialog();
        dialog.show(getSupportFragmentManager(), "DIALOG_AVATAR_CHOOSER");
//        startActivity(new Intent(this, ChooseAvatarActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (myAvatarIndex != -1) {
            ImageView hisAvatar = (ImageView) findViewById(R.id.start_his_avatar);
            hisAvatar.setImageResource(avatarId[myAvatarIndex]);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mListener);
    }
}
