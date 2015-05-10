package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DialerFilter;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.FontHelper;

/**
 * Created by Omid on 5/9/2015.
 */
public class ConnectionLostDialog extends Dialog {
    public ConnectionLostDialog(Context context) {
        super(context);
    }

    TextView textViewMessage;
    Button buttonRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_connection_lost);

        textViewMessage = (TextView) findViewById(R.id.textView_message);
        buttonRetry = (Button) findViewById(R.id.button_retry);

        FontHelper.setKoodakFor(getContext(), textViewMessage, buttonRetry);
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DuelApp.getInstance().connectToWs();
                ConnectionLostDialog.this.dismiss();
            }
        });
    }
}
