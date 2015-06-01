package com.mehdiii.duelgame.views.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.FontHelper;

/**
 * Created by MeHdi on 4/20/2015.
 */
public class AppRater {
    private final static String APP_TITLE = "دوئل کنکور";
    private final static String APP_PNAME = "YOUR-PACKAGE-NAME";

    private final static int GAME_UNTIL_PROMPT = 10;

    public static void init(Context mContext, boolean wonThisGame) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment game counter
        long game_count = prefs.getLong("game_count", 0) + 1;
        editor.putLong("game_count", game_count);

        // Wait at least GAME_UNTIL_PROMPT games and he has to win
        if (game_count >= GAME_UNTIL_PROMPT && wonThisGame) {
            showRateDialog(mContext, editor);
        }

        editor.apply();
        showRateDialog(mContext, editor);
    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setTitle("امتیاز دادن به " + APP_TITLE);

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.setMargins(5, 2, 2, 5);

        TextView tv = new TextView(mContext);
        tv.setText("اگه با " + APP_TITLE + " حال کردی ......");
        tv.setWidth(240);
        tv.setPadding(4, 4, 4, 4);
        tv.setGravity(Gravity.CENTER);
        tv.setLayoutParams(buttonParams);

        ll.addView(tv);

        Button b1 = new Button(mContext);
        b1.setLayoutParams(buttonParams);
        b1.setText("امتیاز دادن به " + APP_TITLE);

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://cafebazaar.ir/app/ir.ugstudio.duelkonkoor/?l=fa")));
                dialog.dismiss();
            }
        });
        b1.setBackgroundResource(R.drawable.game_result_app_rater_button);
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setLayoutParams(buttonParams);
        b2.setText("بعدا یادم بنداز");
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong("game_count", 1);
                editor.apply();

                dialog.dismiss();
            }
        });
        b2.setBackgroundResource(R.drawable.game_result_app_rater_button);
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setLayoutParams(buttonParams);
        b3.setText("نه، مرسی");
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        b3.setBackgroundResource(R.drawable.game_result_app_rater_button);
        ll.addView(b3);

        dialog.setContentView(ll);

        FontHelper.setKoodakFor(mContext, tv, b1, b2, b3);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.show();

        dialog.getWindow().setAttributes(lp);
    }
}