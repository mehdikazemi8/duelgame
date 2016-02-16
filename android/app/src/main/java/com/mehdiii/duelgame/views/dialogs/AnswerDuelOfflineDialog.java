package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.ProvinceManager;
import com.mehdiii.duelgame.models.ChallengeRequestDecision;
import com.mehdiii.duelgame.models.DuelOpponentRequest;
import com.mehdiii.duelgame.models.OfflineDuel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.CategoryManager;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;

public class AnswerDuelOfflineDialog extends Dialog implements View.OnClickListener {

    ImageView imageViewAvatar;
    CustomTextView name;
    CustomTextView province;
    CustomTextView category;
    CustomTextView result;
    CustomTextView opponentScore;
    CustomTextView userScore;
    CustomTextView collectedDiamond;

    Button okayButton;
    Button cancelButton;
    OfflineDuel data;
    OnCompleteListener onPostDecisionMade = null;

    Context mContext;

    public AnswerDuelOfflineDialog(Context context, OfflineDuel data) {
        super(context);
        mContext = context;
        this.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_answer_duel_offline);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        getWindow().setLayout((int) (metrics.widthPixels * 0.9), -2);

        findControls();
        configure();
        bindUiData();
    }

    private void configure() {
        FontHelper.setKoodakFor(getContext(), okayButton, cancelButton);

        okayButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    private void findControls() {
        okayButton = (Button) findViewById(R.id.button_okay);
        cancelButton = (Button) findViewById(R.id.button_cancel);
        imageViewAvatar = (ImageView) findViewById(R.id.imageView_avatar);

        name = (CustomTextView) findViewById(R.id.textView_name);
        province = (CustomTextView) findViewById(R.id.textView_province);
        category = (CustomTextView) findViewById(R.id.textView_category);
        result = (CustomTextView) findViewById(R.id.textView_duel_offline_result);
        opponentScore = (CustomTextView) findViewById(R.id.textView_opponent_score);
        userScore = (CustomTextView) findViewById(R.id.textView_user_score);
        collectedDiamond = (CustomTextView) findViewById(R.id.textView_collected_diamond);
    }

    private String getResultVerdict(int user, int opp) {
        if(user > opp)
            return "بردی";
        if(user < opp)
            return "این دفعه نبردی";
        return "مساوی شد";
    }

    private void bindUiData() {
        Log.d("TAG", "aabbcc " + (getContext() == null));
        Log.d("TAG", "aabbcc " + (imageViewAvatar == null));

        name.setText(data.getOpponent().getName());
        Picasso.with(getContext()).load(AvatarHelper.getResourceId(getContext(), data.getOpponent().getAvatar())).into(imageViewAvatar);
        province.setText(ProvinceManager.get(getContext(), data.getOpponent().getProvince()));
        category.setText(CategoryManager.getCategory(getContext(), Integer.valueOf(data.getCategory())));
        result.setText(getResultVerdict(data.getUserDuelScore(), data.getOpponent().getDuelScore()));
        opponentScore.setText(String.valueOf(data.getOpponent().getDuelScore()));
        userScore.setText(String.valueOf(data.getUserDuelScore()));
        collectedDiamond.setText("+" + String.valueOf(data.getDiamondsCollected()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_cancel:
                break;

            case R.id.button_okay:
                okayButton.setEnabled(false);
                DuelDialog dialog = new DuelDialog(getContext(), true, data.getOpponent().getId());
                dialog.show();
                break;
        }
        dismiss();
    }

    public void onDecisionMade(OnCompleteListener onPostDecisionMade) {
        this.onPostDecisionMade = onPostDecisionMade;
    }
}
