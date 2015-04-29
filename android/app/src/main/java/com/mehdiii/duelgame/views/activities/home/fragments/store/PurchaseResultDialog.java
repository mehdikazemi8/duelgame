package com.mehdiii.duelgame.views.activities.home.fragments.store;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.FontHelper;

/**
 * Created by omid on 4/27/2015.
 */
public class PurchaseResultDialog extends Dialog {
    TextView textView;
    Button gotItButton;
    String text;

    public PurchaseResultDialog(Context context, String text) {
        super(context);
        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_purchase_result);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        getWindow().setLayout((int) (metrics.widthPixels * 0.9), ActionBar.LayoutParams.WRAP_CONTENT);

        gotItButton = (Button) findViewById(R.id.button_got_it);
        textView = (TextView) findViewById(R.id.textView_text);
        FontHelper.setKoodakFor(getContext(), textView, gotItButton);
        textView.setText(text);
        gotItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
