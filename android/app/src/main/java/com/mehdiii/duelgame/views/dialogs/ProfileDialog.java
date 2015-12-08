package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.ProvinceManager;
import com.mehdiii.duelgame.models.Friend;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.ScoreHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.home.fragments.friends.StatisticsListAdapter;

/**
 * Created by Omid on 6/2/2015.
 */
public class ProfileDialog extends Dialog {

    OnCompleteListener onRemoveListener;
    ImageView avatarImageView;
    TextView textViewName;
    TextView textViewProvince;
    ImageButton buttonRemove;
    ImageButton buttonClose;
    Friend friend;

    TextView winCaption;
    TextView loseCaption;
    TextView drawCaption;
    TextView courseCaption;

    ListView statisticsListView;

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
    }

    private void configure() {
        FontHelper.setKoodakFor(getContext(), textViewName, textViewProvince,
                winCaption, loseCaption, drawCaption, courseCaption);
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmDialog alertDialog = new ConfirmDialog(getContext(), getContext().getResources().getString(R.string.message_are_you_sure_to_remove_friend));
                alertDialog.setOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Object data) {
                        if ((Boolean) data) {
                            if (onRemoveListener != null)
                                onRemoveListener.onComplete(true);
                            dismiss();
                        }
                    }
                });
                alertDialog.show();
            }
        });

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileDialog.this.dismiss();
            }
        });

        StatisticsListAdapter adapter = new StatisticsListAdapter(getContext(), R.id.course_title, friend.getStatistics().getResults());
        statisticsListView.setAdapter(adapter);
    }

    private void findControls() {
        avatarImageView = (ImageView) findViewById(R.id.imageView_avatar);
        textViewName = (TextView) findViewById(R.id.textView_name);
        textViewProvince = (TextView) findViewById(R.id.textView_province);
        buttonRemove = (ImageButton) findViewById(R.id.button_remove_friend);
        buttonClose = (ImageButton) findViewById(R.id.button_close);

        winCaption = (TextView) findViewById(R.id.win_caption);
        loseCaption = (TextView) findViewById(R.id.lose_caption);
        drawCaption = (TextView) findViewById(R.id.draw_caption);
        courseCaption = (TextView) findViewById(R.id.course_caption);

        statisticsListView = (ListView) findViewById(R.id.statistics_list_view);
    }

    public void setOnRemoveListener(OnCompleteListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }
}
