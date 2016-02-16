package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.CategoryManager;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.ScoreHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;

import de.greenrobot.event.EventBus;

public class AnswerDuelWithFriendRequestDialog extends Dialog implements View.OnClickListener {

    ImageView imageViewAvatar;
    TextView nameTextView;
    TextView titleTextView;
    TextView provinceTextView;
    TextView messageTextView;
    TextView categoryTextView;
    Button okayButton;
    Button cancelButton;
    DuelOpponentRequest data;
    OnCompleteListener onPostDecisionMade = null;

    public AnswerDuelWithFriendRequestDialog(Context context, DuelOpponentRequest data) {
        super(context);
        this.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_answer_of_challenge_request);
        findControls();
        configure();
        bindUiData();
    }

    private void configure() {
        FontHelper.setKoodakFor(getContext(), okayButton, cancelButton, messageTextView, nameTextView, titleTextView, provinceTextView, categoryTextView);

        okayButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    private void findControls() {
        okayButton = (Button) findViewById(R.id.button_okay);
        cancelButton = (Button) findViewById(R.id.button_cancel);
        messageTextView = (TextView) findViewById(R.id.textView_message);
        provinceTextView = (TextView) findViewById(R.id.textView_province);
        titleTextView = (TextView) findViewById(R.id.textView_title);
        categoryTextView = (TextView) findViewById(R.id.textView_category);
        nameTextView = (TextView) findViewById(R.id.textView_name);
        imageViewAvatar = (ImageView) findViewById(R.id.imageView_avatar);
    }

    private void bindUiData() {
        imageViewAvatar.setImageResource(AvatarHelper.getResourceId(getContext(), data.getAvatar()));
        nameTextView.setText(data.getName());
        // TODO: I don't have user score in this category, I just have his 'score'
//        Log.d("TAG", "bindUiData " + data.getCategory());
//        titleTextView.setText(ScoreHelper.getTitle(data.getScore(String.valueOf(data.getCategory()), "overall")));
        provinceTextView.setText(ProvinceManager.get(getContext(), data.getProvince()));
        messageTextView.setText(data.getMessage());
        categoryTextView.setText(CategoryManager.getCategory(getContext(), data.getCategory()));
    }

    @Override
    public void onClick(View view) {

        ChallengeRequestDecision requestDecision = new ChallengeRequestDecision(CommandType.SEND_ANSWER_OF_CHALLENGE_REQUEST);
        requestDecision.setUserNumber(this.data.getId());

        switch (view.getId()) {
            case R.id.button_cancel:
                requestDecision.setDecision(0);
                break;
            case R.id.button_okay:
                okayButton.setEnabled(false);
                requestDecision.setDecision(1);
                requestDecision.setCategory(data.getCategory());
                EventBus.getDefault().post(requestDecision);
                break;
        }

        dismiss();

        DuelApp.getInstance().sendMessage(requestDecision.serialize());
        if (onPostDecisionMade != null)
            onPostDecisionMade.onComplete(requestDecision.getDecision() != 0);
    }

    public void onDecisionMade(OnCompleteListener onPostDecisionMade) {
        this.onPostDecisionMade = onPostDecisionMade;
    }
}
