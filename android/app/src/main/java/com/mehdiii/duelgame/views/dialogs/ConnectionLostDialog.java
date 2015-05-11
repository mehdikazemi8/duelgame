package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.NetworkUtils;

/**
 * Created by Omid on 5/9/2015.
 */
public class ConnectionLostDialog extends Dialog {
    public ConnectionLostDialog(Context context) {
        super(context);
    }

    TextView textViewMessage;
    Button buttonRetry;
    Button buttonEnableWifi;
    Button buttonEnableData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_connection_lost);
        configureLayoutSize();

        textViewMessage = (TextView) findViewById(R.id.textView_message);
        buttonRetry = (Button) findViewById(R.id.button_retry);
        buttonEnableWifi = (Button) findViewById(R.id.button_enable_wifi);
        buttonEnableData = (Button) findViewById(R.id.button_enable_data);

        FontHelper.setKoodakFor(getContext(), textViewMessage, buttonRetry, buttonEnableWifi, buttonEnableData);

        if (!NetworkUtils.isNetworkAvailable(getContext())) {
            textViewMessage.setText(getContext().getResources().getString(R.string.message_internet_is_not_available));
            buttonEnableData.setVisibility(View.VISIBLE);
            buttonEnableWifi.setVisibility(View.VISIBLE);
        }

        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DuelApp.getInstance().connectToWs();
                ConnectionLostDialog.this.dismiss();
            }
        });
        buttonEnableWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        buttonEnableData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(Settings.ACTION_SETTINGS));
                Intent intent = new Intent(Intent.ACTION_MAIN);
                getContext().startActivity(intent);
            }
        });
    }

    private void configureLayoutSize() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int w = Math.min(1000, (int) (0.9 * displayMetrics.widthPixels));

        getWindow().setLayout(w, -2/* means WRAP_CONTENT */);
    }
}
