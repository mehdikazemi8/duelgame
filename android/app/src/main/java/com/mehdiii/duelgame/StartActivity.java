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

        /*
        if(wsc.isConnected() == false)
        {
            PrintError in Dialogue until it is connected.
        }
        * */

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


        startActivity(new Intent(this, ProfileActivity.class));
        this.finish();
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
}


/*
        EditText inputName = (EditText) findViewById(R.id.input_name);

        Log.d("--- input", "^^^" + inputName.getText().toString() + "$$$");

        if (inputName.toString() == null || inputName.toString().isEmpty()) {
            Log.d("----- ", "inputName empty!");
            return;
        }


* */