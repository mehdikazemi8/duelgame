package com.mehdiii.duelgame.views.dialogs;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.PurchaseManager;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.custom.CustomTextView;

public class ConfirmDialog extends Dialog implements View.OnClickListener {
    OnCompleteListener onCompleteListener;
    TextView captionTextView;
    Button confirmButton;
    Button cancelButton;
    String captionText;

    ImageView diamondImageView;
    boolean showDiamond;
    int resourceId = -1;
    boolean useResourceId = false;

    boolean changeButtonsTexts = false;
    String confirmText;
    String cancelText;
    int subscribePrice = 0;
    boolean subscribedLayout = false;

    public ConfirmDialog(Context context, String confirmText, String cancelText, boolean changeButtonsTexts, String captionText) {
        super(context);
        this.confirmText = confirmText;
        this.cancelText = cancelText;
        this.changeButtonsTexts = changeButtonsTexts;
        this.captionText = captionText;
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public ConfirmDialog(Context context, String captionText) {
        super(context);
        this.captionText = captionText;
    }

    public ConfirmDialog(Context context, String captionText, boolean showDiamond) {
        super(context);
        this.captionText = captionText;
        this.showDiamond = showDiamond;
    }

    public ConfirmDialog(Context context, String captionText, int resourceId) {
        super(context);

        this.captionText = captionText;
        this.resourceId = resourceId;
        this.useResourceId = true;
    }

    public ConfirmDialog(Context context, String captionText, int resourceId, int subscribePrice,
                         boolean subscribedLayout) {
        super(context);

        this.captionText = captionText;
        this.resourceId = resourceId;
        this.useResourceId = true;
        this.subscribePrice = subscribePrice;
        this.subscribedLayout = subscribedLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(useResourceId) {
            setContentView(resourceId);
            Log.d("TAG", "ConfirmDialog useResourceId");
        } else {
            setContentView(R.layout.dialog_confirm);
        }

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        getWindow().setLayout((int) (metrics.widthPixels * 0.9), ActionBar.LayoutParams.WRAP_CONTENT);

        find();
        configure();

        if (captionText != null)
            this.captionTextView.setText(captionText);

        if (showDiamond)
            diamondImageView.setVisibility(View.VISIBLE);

        if(subscribedLayout) {
            ((EditText) findViewById(R.id.quiz_discount)).setTypeface(
                    FontHelper.getKoodak(getContext())
            );

            ((CustomTextView) findViewById(R.id.quiz_price)).setText(
                    String.format(getContext().getString(R.string.subscription_price), subscribePrice));
        }
    }

    private void find() {
        captionTextView = (TextView) findViewById(R.id.textView_caption);
        cancelButton = (Button) findViewById(R.id.button_cancel);
        confirmButton = (Button) findViewById(R.id.button_confirm);
        diamondImageView = (ImageView) findViewById(R.id.confirm_dialog_diamond);
    }

    private void configure() {
        FontHelper.setKoodakFor(getContext(), confirmButton, cancelButton, captionTextView);
        cancelButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);

        if(changeButtonsTexts) {
            cancelButton.setText(cancelText);
            confirmButton.setText(confirmText);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_cancel:
                if (onCompleteListener != null)
                    onCompleteListener.onComplete(false);
                break;
            case R.id.button_confirm:

                if(subscribedLayout) {
                    String discountCode = ((EditText) findViewById(R.id.quiz_discount)).getText().toString();
                    PurchaseManager.getInstance().startSubscribe(discountCode);
                } else if (onCompleteListener != null)
                    onCompleteListener.onComplete(true);
                break;
        }
        dismiss();
    }
}
