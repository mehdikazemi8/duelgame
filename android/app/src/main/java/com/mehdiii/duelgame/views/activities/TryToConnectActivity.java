package com.mehdiii.duelgame.views.activities;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;

public class TryToConnectActivity extends ParentActivity {

    public void checkConnection() {
        if (DuelApp.getInstance().getSocket().isConnected()) {
            finish();
        } else {
            TextView status = (TextView) findViewById(R.id.try_to_connect_status);
            status.setText("تلاش برای وصل شدن به سرور ناموفق بود. می توانید دوباره امتحان کنید. این تمام چیزی هست که ما می دانیم.");
        }
    }

    public void reconnect(View v) {
        TextView status = (TextView) findViewById(R.id.try_to_connect_status);
        status.setText("در حال اتصال به سرور بازی");
        DuelApp.getInstance().connectToWs();

        Runnable task = new Runnable() {
            public void run() {
                checkConnection();
            }
        };
        ViewCompat.postOnAnimationDelayed(v, task, 2000);
    }

    public void exit(View v) {
        TextView status = (TextView) findViewById(R.id.try_to_connect_status);
        status.setText("eeeeeeeeeeeee");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_to_connect);
    }
}
