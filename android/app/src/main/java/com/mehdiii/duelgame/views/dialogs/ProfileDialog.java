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
import com.mehdiii.duelgame.managers.ProvinceManager;
import com.mehdiii.duelgame.models.Friend;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.ScoreHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;

/**
 * Created by Omid on 6/2/2015.
 */
public class ProfileDialog extends Dialog {

    OnCompleteListener onRemoveListener;
    ImageView avatarImageView;
    TextView textViewName;
    TextView textViewProvince;
    TextView textViewScore;
    Button buttonRemove;
    Friend friend;

    public ProfileDialog(Context context, Friend friend) {
        super(context);
        this.friend = friend;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title from top
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // set content view
        setContentView(R.layout.dialog_profile);

        // set width and height
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        getWindow().setLayout((int) ((double) metrics.widthPixels * 0.9), -2);

        findControls();
        configure();
        bindViewData();
    }

    private void bindViewData() {
        this.avatarImageView.setImageResource(AvatarHelper.getResourceId(getContext(), friend.getAvatar()));
        this.textViewName.setText(friend.getName());
        this.textViewProvince.setText(ProvinceManager.get(getContext(), friend.getProvince()));
        this.textViewScore.setText(ScoreHelper.getTitle(friend.getScore()));
    }

    private void configure() {
        FontHelper.setKoodakFor(getContext(), textViewName, textViewProvince, textViewScore, buttonRemove);
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRemoveListener != null)
                    onRemoveListener.onComplete(true);

                dismiss();
            }
        });
    }

    private void findControls() {
        avatarImageView = (ImageView) findViewById(R.id.imageView_avatar);
        textViewName = (TextView) findViewById(R.id.textView_name);
        textViewProvince = (TextView) findViewById(R.id.textView_province);
        textViewScore = (TextView) findViewById(R.id.textView_score);
        buttonRemove = (Button) findViewById(R.id.button_remove_friend);
    }

    public void setOnRemoveListener(OnCompleteListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }
}
