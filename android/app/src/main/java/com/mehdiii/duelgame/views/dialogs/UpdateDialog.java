package com.mehdiii.duelgame.views.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.UpdateVersion;
import com.mehdiii.duelgame.utils.FontHelper;

/**
 * Created by omid on 4/28/2015.
 */
public class UpdateDialog extends AlertDialog implements View.OnClickListener {
    private final static String URL = "http://cafebazaar.ir/app/%s/?l=fa";
    private UpdateVersion mUpdate;
    private boolean mForceUpdate;
    Button positiveButton;
    Button negativeButton;

    public UpdateDialog(Context context, UpdateVersion update, boolean forceUpdate) {
        super(context);
        this.mUpdate = update;
        this.mForceUpdate = forceUpdate;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);

        WebView webView = (WebView) findViewById(R.id.webView_updates);
        TextView caption = (TextView) findViewById(R.id.textView_update_caption);
        positiveButton = (Button) findViewById(R.id.button_positive);
        negativeButton = (Button) findViewById(R.id.button_negative);

        if (mForceUpdate) {
            negativeButton.setText("خروج");
        }

        caption.setTypeface(FontHelper.getKoodak(getContext()));
        positiveButton.setTypeface(FontHelper.getKoodak(getContext()));
        negativeButton.setTypeface(FontHelper.getKoodak(getContext()));

        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);

        if (mUpdate != null) {
            webView.loadDataWithBaseURL(null, mUpdate.getChangeset(), "messageTextView/html", "utf-8", null);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_positive:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(URL, getContext().getPackageName())));
                getContext().startActivity(browserIntent);
                break;
            case R.id.button_negative:
                if (mForceUpdate) {
                    exitApp();
                } else {
                    this.dismiss();
                }
                break;
        }
    }

    private void exitApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

}
