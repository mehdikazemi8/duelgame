package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;

/**
 * Created by Omid on 5/18/2015.
 */
public class AlertDialog extends Dialog {
    String message;

    public AlertDialog(Context context, String message) {
        super(context);
        this.message = message;
        Log.d("TAG", "bbbb AlertDialog constructor");
    }

    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_alert);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();

        getWindow().setLayout((int) (metrics.widthPixels * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);


        button = (Button) findViewById(R.id.button_confirm);
        textView = (TextView) findViewById(R.id.textView_caption);

        FontHelper.setKoodakFor(getContext(), button, textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onCompleteListener != null)
                    onCompleteListener.onComplete(view);
                dismiss();
            }
        });

        textView.setText(this.message);

    }

    OnCompleteListener onCompleteListener = null;

    public OnCompleteListener getOnCompleteListener() {
        return onCompleteListener;
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }
}
