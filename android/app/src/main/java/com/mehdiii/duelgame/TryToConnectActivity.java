package com.mehdiii.duelgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TryToConnectActivity extends MyBaseActivity {

    public void checkConnection()
    {
        if(wsc.isConnected())
        {
            Log.d("****", "vasl shod");

            finish();
        }
        else
        {
            Log.d("****", "vasl nashod");

            TextView status = (TextView) findViewById(R.id.try_to_connect_status);
            status.setText("تلاش برای وصل شدن به سرور ناموفق بود. می توانید دوباره امتحان کنید. این تمام چیزی هست که ما می دانیم.");
        }
    }

    public void reconnect(View v)
    {
        TextView status = (TextView) findViewById(R.id.try_to_connect_status);
        status.setText("در حال اتصال به سرور بازی");

        doConnect();

        Runnable task = new Runnable() {
            public void run() {
                checkConnection();
            }
        };

        ViewCompat.postOnAnimationDelayed(v, task, 2000);
    }

    public void exit(View v)
    {
        TextView status = (TextView) findViewById(R.id.try_to_connect_status);
        status.setText("eeeeeeeeeeeee");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_to_connect);
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
