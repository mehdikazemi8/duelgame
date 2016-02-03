package com.mehdiii.duelgame.views.activities.home.fragments.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.HeartTracker;
import com.mehdiii.duelgame.models.ChangePage;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnDiamondChangeNotice;
import com.mehdiii.duelgame.models.events.OnHeartChangeNotice;
import com.mehdiii.duelgame.models.events.OnPurchaseResult;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.ScoreHelper;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.category.CategoryActivity;
import com.mehdiii.duelgame.views.activities.flashcards.FlashCardActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.activities.quiz.QuizActivity;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.mehdiii.duelgame.views.dialogs.DuelDialog;
import com.mehdiii.duelgame.views.dialogs.HeartLowDialog;

import de.greenrobot.event.EventBus;

/**
 * Created by omid on 4/5/2015.
 */
public class HomeFragment extends FlippableFragment implements View.OnClickListener {

    CustomButton quizButton;

    TextView diamondCount;
    ImageView avatarImageView;
    TextView titleTextView;
    TextView textViewCounter;
    Button refillButton;
    Button buyDiamondButton;
    Button flashCardButton;
    Button duel2Button;
    TextView textViewHearts;
    ImageView heartsImageView;
    LinearLayout containerHearts;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * find controls and bind view data
         */
        find(view);

        /**
         * configure click listeners and setup typeface
         */
        configure(view);

//        AppRater ar = new AppRater();
//        ar.show(getActivity(), true);
    }

    private void configure(View view) {
        refillButton.setOnClickListener(this);
        buyDiamondButton.setOnClickListener(this);
        flashCardButton.setOnClickListener(this);
        duel2Button.setOnClickListener(this);

        // set font-face
        FontHelper.setKoodakFor(view.getContext(), flashCardButton, duel2Button, textViewHearts, /*textViewSendReport,*/
                diamondCount, titleTextView/*, levelText, totalRankingText,
                totalRanking, friendsRankingText, friendsRanking,
                provinceRanking, provinceRankingText*/, textViewCounter
        );

        buyDiamondButton.setTypeface(FontHelper.getIcons(getActivity()));
        refillButton.setTypeface(FontHelper.getIcons(getActivity()));

        quizButton.setOnClickListener(this);
    }

    private void find(View view) {
        diamondCount = (TextView) view.findViewById(R.id.home_diamond_cnt);
        avatarImageView = (ImageView) view.findViewById(R.id.imageView_avatar);
        titleTextView = (TextView) view.findViewById(R.id.textView_title);
        refillButton = (Button) view.findViewById(R.id.button_refill);
        buyDiamondButton = (Button) view.findViewById(R.id.button_buy_diamond);
        textViewHearts = (TextView) view.findViewById(R.id.textView_heart);
        textViewCounter = (TextView) view.findViewById(R.id.textView_counter);
        heartsImageView = (ImageView) view.findViewById(R.id.imageView_hearts);
        containerHearts = (LinearLayout) view.findViewById(R.id.container_hearts);
        flashCardButton = (Button) view.findViewById(R.id.button_flash_card);
        duel2Button = (Button) view.findViewById(R.id.button_duel2);

        quizButton = (CustomButton) view.findViewById(R.id.quiz_button);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        bindViewData();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_friend:
                break;

            case R.id.button_buy_diamond:
            case R.id.button_refill:
                refillHeart();
                break;

            case R.id.button_duel:
            case R.id.button_duel2:
                startGame();
                break;

            case R.id.button_flash_card:
                Intent intent = new Intent(getActivity(), FlashCardActivity.class);
                startActivity(intent);
                break;

            case R.id.quiz_button:
                startActivity(new Intent(getActivity(), QuizActivity.class));
                break;
        }
    }

    public void bindViewData() {
        User user = AuthManager.getCurrentUser();
        if(user == null) {
            // TODO, user must not be null
            Log.d("TAG", "bindViewData HomeFragment user is null");
            return;
        } else {
            Log.d("TAG", "bindViewData HomeFragment user is NOT null");
        }

        avatarImageView.setImageResource(AvatarHelper.getResourceId(getActivity(), user.getAvatar()));
        diamondCount.setText(String.valueOf(user.getDiamond()));
        titleTextView.setText(ScoreHelper.getTitle(user.getScore()));

        if (user.isExtremeHeart()) {
            heartsImageView.setImageResource(R.drawable.extreme_heart);
            containerHearts.setPadding(0, 0, 0, 20);
            this.textViewHearts.setVisibility(View.GONE);
        } else
            this.textViewHearts.setVisibility(View.VISIBLE);
        this.textViewHearts.setText(String.valueOf((user.getHeart())));
    }

    public void startGame() {
        if (!HeartTracker.getInstance().canUseHeart()) {
            HeartLowDialog dialog = new HeartLowDialog(getActivity());
            dialog.show();
            return;
        }

        DuelDialog duelDialog = new DuelDialog(getActivity());
        duelDialog.show();
    }



    private void refillHeart() {
        EventBus.getDefault().post(new ChangePage(ParentActivity.STORE_PAGE));
    }

    public void onEvent(OnHeartChangeNotice notice) {
        // if hearts are refilling over time, counter text view should be visible, ot invisible
        if (AuthManager.getCurrentUser().getHeart() >= HeartTracker.COUNT_HEARTS_MAX || AuthManager.getCurrentUser().isExtremeHeart())
            textViewCounter.setVisibility(View.INVISIBLE);
        else
            textViewCounter.setVisibility(View.VISIBLE);

        // if notice says that heart is increase or decreased or refreshed again from server change
        // hearts count, otherwise, update countdown timer.
        if (notice.getMode() == OnHeartChangeNotice.ChangeMode.DECREASED ||
                notice.getMode() == OnHeartChangeNotice.ChangeMode.INCREASED ||
                notice.getMode() == OnHeartChangeNotice.ChangeMode.REFRESH) {

            // update hearts count textView
            this.textViewHearts.setText(String.valueOf((notice.getValue())));

        } else if (notice.getMode() == OnHeartChangeNotice.ChangeMode.TICK) {
            // update countdown timer, (getValue equals seconds remaining to the next refill)
            int minutes, seconds;
            minutes = notice.getValue() / 60;
            seconds = minutes == 0 ? notice.getValue() : notice.getValue() % (minutes * 60);

            textViewCounter.setText(String.format("%d:%d", minutes, seconds));
        }
    }

    public void onEvent(OnDiamondChangeNotice notice) {
        this.diamondCount.setText(String.valueOf(notice.getNewValue()));
    }

    public void onEvent(OnPurchaseResult settings) {
        bindViewData();
    }

    @Override
    public void onBringToFront() {
        super.onBringToFront();
    }
}
