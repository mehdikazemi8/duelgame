package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.mehdiii.duelgame.views.custom.CustomTextView;

/**
 * Created by frshd on 3/2/16.
 */
public class RetakeStepDialog extends Dialog implements View.OnClickListener {
    OnCompleteListener onCompleteListener;

    CustomButton confirmButton;
    CustomButton cancelButton;
    CustomTextView captionText;

    String captionTextStr;

    public RetakeStepDialog(Context context, String captionTextStr) {
        super(context);
        this.captionTextStr = captionTextStr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_retake_step);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        getWindow().setLayout((int) (metrics.widthPixels * 0.9), -2);
        find();
        configure();
    }

    private void find() {
        cancelButton = (CustomButton) findViewById(R.id.button_cancel);
        confirmButton = (CustomButton) findViewById(R.id.button_confirm);
        captionText = (CustomTextView) findViewById(R.id.caption_text);
    }

    private void configure() {
        cancelButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);

        captionText.setText(captionTextStr);
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
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
