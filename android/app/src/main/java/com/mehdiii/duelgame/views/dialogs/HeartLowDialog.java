package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.ChangePage;
import com.mehdiii.duelgame.utils.FontHelper;

import de.greenrobot.event.EventBus;

/**
 * Created by omid on 4/29/2015.
 */
public class HeartLowDialog extends Dialog implements View.OnClickListener {
    public HeartLowDialog(Context context) {
        super(context);
    }

    TextView captionTextView;
    Button buyButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_low_heart);

        find();
        configure();
    }

    private void configure() {
        FontHelper.setKoodakFor(getContext(), captionTextView, buyButton, cancelButton);
        buyButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    private void find() {
        buyButton = (Button) findViewById(R.id.button_buy_hearts);
        cancelButton = (Button) findViewById(R.id.button_cancel);
        captionTextView = (TextView) findViewById(R.id.textView_caption);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_buy_hearts:
                EventBus.getDefault().post(new ChangePage(1));
                break;
        }
        
        dismiss();
    }
}
