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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.HeartTracker;
import com.mehdiii.duelgame.managers.MajorManager;
import com.mehdiii.duelgame.managers.ProvinceManager;
import com.mehdiii.duelgame.models.Friend;
import com.mehdiii.duelgame.models.StepProgress;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.ScoreHelper;
import com.mehdiii.duelgame.utils.StepManager;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.home.fragments.friends.StatisticsListAdapter;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.mehdiii.duelgame.views.custom.CustomTextView;

import java.util.List;

public class ProfileDialog extends Dialog {

    OnCompleteListener onRemoveListener;
    ImageView avatarImageView;
    TextView textViewName;
    CustomTextView textViewProvince;
    CustomTextView textViewSchool;
    CustomTextView textViewField;
    CustomTextView textViewLastStep;
    CustomTextView textViewLastStepStar;
    CustomTextView textViewLastStepStarCount;
    ImageButton buttonRemove;
    ImageButton buttonClose;
    CustomButton turnBaseDuelButton;
    Friend friend;

    LinearLayout captions;
    TextView winCaption;
    TextView loseCaption;
    TextView drawCaption;
    TextView courseCaption;
    TextView noMutualStatistics;

    ListView statisticsListView;
    boolean showDeleteButton;

    public ProfileDialog(Context context, Friend friend, boolean showDeleteButton) {
        super(context);
        Log.d("TAG", "ProfileDialog Constructor");
        this.friend = friend;
        this.showDeleteButton = showDeleteButton;
    }

    public ProfileDialog(Context context, Friend friend) {
        super(context);
        Log.d("TAG", "ProfileDialog Constructor");

//        // TODO DELETE THESE
//        Toast toast = Toast.makeText(getContext(), "a: "+friend.getId(), Toast.LENGTH_LONG);
//        toast.show();

        this.friend = friend;
        this.showDeleteButton = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("TAG", "ProfileDialog onCreate" + friend.serialize());

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
        Log.d("TAG", "ProfileDialog bindViewData"+ friend.getSchool());
        StepProgress zaban = new StepProgress();
        List<StepProgress> progresses= friend.getStepProgress();
        if(progresses!=null){
            for ( StepProgress p : progresses){
                if(p.getCategory()==10004){
                    zaban.setBook(p.getBook());
                    zaban.setChapter(p.getChapter());
                    zaban.setStars(p.getStars());
                }
            }
        }
        if (zaban.getBook() == null){
            zaban.setChapter(1);
            zaban.setBook(41);
            zaban.setStars(0);
        }
        String lastStep = StepManager.getStep(getContext(),"1000"+String.valueOf(zaban.getBook())+String.valueOf(zaban.getChapter()));
        int stepPosition = StepManager.getStepPosition(getContext(), "1000" + String.valueOf(zaban.getBook()) + String.valueOf(zaban.getChapter()));
        int totalPossibleStars = stepPosition*3;
        this.textViewLastStepStarCount.setText(zaban.getStars() + " از " + String.valueOf(totalPossibleStars));
        this.textViewLastStep.setText( lastStep);
        this.textViewLastStepStar.setText("c");
        this.textViewLastStepStar.setTypeface(FontHelper.getIcons(getContext()));

        if (friend.getSchool()!=null) {
            this.textViewSchool.setText(getContext().getResources().getString(R.string.school)+": "+friend.getSchool());
        }
        else {
            this.textViewSchool.setText(getContext().getResources().getString(R.string.school)+": -");
        }

        if (friend.getMajor()!=null) {
            this.textViewField.setText(getContext().getResources().getString(R.string.field)+": " +
                    MajorManager.get(getContext(),Integer.valueOf(friend.getMajor())));
        }
        else {
            this.textViewField.setText(getContext().getResources().getString(R.string.field)+": -");
        }

        this.textViewName.setText(friend.getName());
        this.avatarImageView.setImageResource(AvatarHelper.getResourceId(getContext(), friend.getAvatar()));
        this.textViewProvince.setText(ProvinceManager.get(getContext(), friend.getProvince()));
    }

    private void configure() {
        Log.d("TAG", "ProfileDialog configure");
        FontHelper.setKoodakFor(getContext(), textViewName, textViewProvince, textViewField, textViewSchool,
                winCaption, loseCaption, drawCaption, courseCaption, noMutualStatistics);
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

        if(friend.getStatistics().getResults().size() != 0) {
            StatisticsListAdapter adapter = new StatisticsListAdapter(getContext(), R.id.course_title, friend.getStatistics().getResults());
            statisticsListView.setAdapter(adapter);
        } else {
            captions.setVisibility(View.GONE);
            noMutualStatistics.setVisibility(View.VISIBLE);
        }

        if(!showDeleteButton) {
            buttonRemove.setVisibility(View.INVISIBLE);
        }

        turnBaseDuelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!HeartTracker.getInstance().canUseHeart()) {
                    HeartLowDialog heartLowDialog = new HeartLowDialog(getContext());
                    heartLowDialog.show();
                    dismiss();
                    return;
                }

                DuelDialog dialog = new DuelDialog(getContext(), true, friend.getId());
                dialog.show();
                dismiss();
            }
        });
    }

    private void findControls() {
        Log.d("TAG", "ProfileDialog findControls");
        avatarImageView = (ImageView) findViewById(R.id.imageView_avatar);
        textViewName = (TextView) findViewById(R.id.textView_name);
        textViewProvince = (CustomTextView) findViewById(R.id.textView_province);
        textViewSchool = (CustomTextView) findViewById(R.id.textView_school);
        textViewField = (CustomTextView) findViewById(R.id.textView_field);
        textViewLastStep = (CustomTextView) findViewById(R.id.textView_last_step);
        textViewLastStepStar = (CustomTextView) findViewById(R.id.textView_last_step_star);
        textViewLastStepStarCount = (CustomTextView) findViewById(R.id.textView_last_step_star_count);
        buttonRemove = (ImageButton) findViewById(R.id.button_remove_friend);
        buttonClose = (ImageButton) findViewById(R.id.button_close);

        captions = (LinearLayout) findViewById(R.id.captions);
        winCaption = (TextView) findViewById(R.id.win_caption);
        loseCaption = (TextView) findViewById(R.id.lose_caption);
        drawCaption = (TextView) findViewById(R.id.draw_caption);
        courseCaption = (TextView) findViewById(R.id.course_caption);

        noMutualStatistics = (TextView) findViewById(R.id.no_mutual_statistics);

        statisticsListView = (ListView) findViewById(R.id.statistics_list_view);

        turnBaseDuelButton = (CustomButton) findViewById(R.id.turn_base_duel_button);
    }

    public void setOnRemoveListener(OnCompleteListener onRemoveListener) {
        Log.d("TAG", "ProfileDialog setOnRemoveListener");
        this.onRemoveListener = onRemoveListener;
    }
}
