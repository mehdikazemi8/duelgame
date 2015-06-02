package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.FontHelper;

/**
 * Created by Omid on 6/1/2015.
 */
public class RateDialog extends Dialog implements View.OnClickListener {
    public RateDialog(Context context) {
        super(context);
    }

    Button notNowButton;
    Button rateButton;
    TextView captionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_rate_app);

        notNowButton = (Button) findViewById(R.id.button_not_now);
        rateButton = (Button) findViewById(R.id.button_rate);
        captionTextView = (TextView) findViewById(R.id.textView_caption);

        FontHelper.setKoodakFor(getContext(), captionTextView, rateButton, notNowButton);

        notNowButton.setOnClickListener(this);
        rateButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        
    }
}
