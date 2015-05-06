package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;

/**
 * Created by Omid on 5/4/2015.
 */
public class ConfirmDialog extends Dialog implements View.OnClickListener {
    OnCompleteListener onCompleteListener;
    TextView captionTextView;
    Button confirmButton;
    Button cancelButton;
    String captionText;

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public ConfirmDialog(Context context, String captionText) {
        super(context);
        this.captionText = captionText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm);

        find();
        configure();

        if (captionText != null)
            this.captionTextView.setText(captionText);
    }

    private void find() {
        captionTextView = (TextView) findViewById(R.id.textView_caption);
        cancelButton = (Button) findViewById(R.id.button_cancel);
        confirmButton = (Button) findViewById(R.id.button_confirm);
    }

    private void configure() {
        FontHelper.setKoodakFor(getContext(), confirmButton, cancelButton, captionTextView);
        cancelButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_cancel:
                if (onCompleteListener != null)
                    onCompleteListener.onComplete(false);
                break;
            case R.id.button_confirm:
                if (onCompleteListener != null)
                    onCompleteListener.onComplete(true);
                break;
        }
        dismiss();
    }
}