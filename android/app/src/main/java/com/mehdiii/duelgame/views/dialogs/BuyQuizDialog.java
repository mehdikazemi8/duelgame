package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.mehdiii.duelgame.views.custom.CustomTextView;

public class BuyQuizDialog extends Dialog implements View.OnClickListener {
    OnCompleteListener onCompleteListener;

    CustomButton confirmButton;
    CustomButton cancelButton;

    CustomTextView quizPrice;
    CustomTextView quizDiscount;
    CustomTextView quizPayablePrice;

    String quizPriceStr;
    String quizDiscountStr;
    String quizPayablePriceStr;
    boolean isThisFreeExam;

    public BuyQuizDialog(Context context, String quizPriceStr, String quizDiscountStr,
                         String quizPayablePriceStr, boolean isThisFreeExam) {
        super(context);
        this.quizPriceStr = quizPriceStr;
        this.quizDiscountStr = quizDiscountStr;
        this.quizPayablePriceStr = quizPayablePriceStr;
        this.isThisFreeExam = isThisFreeExam;
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_buy_quiz);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        getWindow().setLayout((int) (metrics.widthPixels * 0.9), -2);
        find();
        configure();
    }

    private void find() {
        cancelButton = (CustomButton) findViewById(R.id.button_cancel);
        confirmButton = (CustomButton) findViewById(R.id.button_confirm);

        quizPrice = (CustomTextView) findViewById(R.id.quiz_price);
        quizDiscount = (CustomTextView) findViewById(R.id.quiz_discount);
        quizPayablePrice = (CustomTextView) findViewById(R.id.quiz_payable_price);
    }

    private void configure() {
        cancelButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);
        quizPrice.setText(quizPriceStr + " " + getContext().getResources().getString(R.string.caption_toman));
        quizDiscount.setText(quizDiscountStr + " " + getContext().getResources().getString(R.string.caption_discount));
        quizPayablePrice.setText(quizPayablePriceStr + " " + getContext().getResources().getString(R.string.caption_toman));

        if(!isThisFreeExam && AuthManager.getCurrentUser().getFreeExamCount() != 0) {
            cancelButton.setText(getContext().getString(R.string.button_use_free_exam));
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
                if (onCompleteListener != null)
                    onCompleteListener.onComplete(true);
                break;
        }
        dismiss();
    }
}
