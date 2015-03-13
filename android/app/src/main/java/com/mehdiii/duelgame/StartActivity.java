package com.mehdiii.duelgame;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class StartActivity extends MyBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        TelephonyManager teleManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);

        final String deviceId, simSerialNumber;
        deviceId = "" + teleManager.getDeviceId();
        simSerialNumber = "" + teleManager.getSimSerialNumber();

        String identify = deviceId + simSerialNumber;

        if(wsc.isConnected() == false)
        {
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getStringFromEditText(int id)
    {
        EditText et = (EditText) findViewById(id);
        return et.getText().toString();
    }

    public String getStringFromSpinner(int id)
    {
        Spinner sp = (Spinner) findViewById(id);
        return sp.getSelectedItem().toString();
    }

    public void registerMe(View v)
    {
        String playerName = getStringFromEditText(R.id.start_player_name);
        String playerEmail = getStringFromEditText(R.id.start_player_email);
        String playerAvatar = "" + avatarIndex;
        String playerOstan = getStringFromSpinner(R.id.start_ostan_name);

        Log.d("%%%% ", playerName);
        Log.d("%%%% ", playerEmail);
        Log.d("%%%% ", playerAvatar);
        Log.d("%%%% ", playerOstan);

        JSONObject query = new JSONObject();
        try {
            query.put("code", "RU");
            query.put("user_id", "abc");
            query.put("name", playerName);
            query.put("ostan", playerOstan);
            query.put("email", playerEmail);
            query.put("avatar", ""+avatarIndex);

            wsc.sendTextMessage(query.toString());
        } catch (JSONException e) {
            Log.d("---- START ACTIVITY", e.toString());
        }
    }

    public void chooseAvatar(View v)
    {
        startActivity(new Intent(this, ChooseAvatarActivity.class));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(avatarIndex != -1)
        {
            ImageView hisAvatar = (ImageView) findViewById(R.id.start_his_avatar);
            hisAvatar.setImageResource(avatarId[avatarIndex]);
        }
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