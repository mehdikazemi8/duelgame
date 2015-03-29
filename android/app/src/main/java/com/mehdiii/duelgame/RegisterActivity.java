package com.mehdiii.duelgame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends MyBaseActivity {

    String userId;

    protected class TitleBarListener extends BroadcastReceiver {
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
    }
    TitleBarListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mListener = new TitleBarListener();
        registerReceiver(mListener, new IntentFilter("MESSAGE"));

        TelephonyManager teleManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        final String deviceId, simSerialNumber;
        deviceId = "" + teleManager.getDeviceId();
        simSerialNumber = "" + teleManager.getSimSerialNumber();
        userId = deviceId + simSerialNumber;

        if (wsc.isConnected() == false) {
            Log.d("^^^^^", "not connected");
            startActivity(new Intent(this, TryToConnectActivity.class));
        }


        /*
        else
        {
            send 'identify' to server to see if this device has registered before.
            if(this device has been registered before)
            {
                get the name and put it in some variable.
                go to Profile Page
            }
            else
                show First Login Page.
        }
        * */

        //startActivity(new Intent(this, ProfileActivity.class));
        //this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        myName = getStringFromEditText(R.id.start_player_name);
        myEmail = getStringFromEditText(R.id.start_player_email);
        myOstanStr = getStringFromSpinner(R.id.start_ostan_name);

        Log.d("%%%% ", myName);
        Log.d("%%%% ", myEmail);
        Log.d("%%%% ", "" + myAvatarIndex);
        Log.d("%%%% ", myOstanStr);

        if (((EditText) findViewById(R.id.start_player_name)).getText().length() == 0) {
            Toast toast = Toast.makeText(this, "لطفا اسم خود را وارد نمایید.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return;
        }
        if (((Spinner) findViewById(R.id.start_ostan_name)).getSelectedItem().toString().equals("استان")) {
            Toast toast = Toast.makeText(this, "لطفا استان خود را انتخاب نمایید.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return;
        }

        Log.d("!!!!!!!", ">>" + ((Spinner) findViewById(R.id.start_ostan_name)).getSelectedItemPosition());
        myOstanInt = ((Spinner) findViewById(R.id.start_ostan_name)).getSelectedItemPosition();

        myName = myName;

        JSONObject query = new JSONObject();
        try {
            query.put("code", "RU");
            query.put("user_id", userId);
            query.put("name", myName);
            query.put("ostan", myOstanInt);
            query.put("email", myEmail);
            query.put("avatar", myAvatarIndex);

            wsc.sendTextMessage(query.toString());
        } catch (JSONException e) {
            Log.d("---- Register ACTIVITY", e.toString());
        }
    }

    public void chooseAvatar(View v) {
        startActivity(new Intent(this, ChooseAvatarActivity.class));
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


/*
        EditText inputName = (EditText) findViewById(R.id.input_name);

        Log.d("--- input", "^^^" + inputName.getText().toString() + "$$$");

        if (inputName.toString() == null || inputName.toString().isEmpty()) {
            Log.d("----- ", "inputName empty!");
            return;
        }


* */