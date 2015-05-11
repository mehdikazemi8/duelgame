package com.mehdiii.duelgame.views.activities.home.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.HeartTracker;
import com.mehdiii.duelgame.managers.ProvinceManager;
import com.mehdiii.duelgame.models.ChangePage;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.events.OnDiamondChangeNotice;
import com.mehdiii.duelgame.models.events.OnHeartChangeNotice;
import com.mehdiii.duelgame.models.events.OnUserSettingsChanged;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.ScoreHelper;
import com.mehdiii.duelgame.views.activities.CategoryActivity;
import com.mehdiii.duelgame.views.activities.home.HomeActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.dialogs.HeartLowDialog;

import de.greenrobot.event.EventBus;

/**
 * Created by omid on 4/5/2015.
 */
public class HomeFragment extends FlippableFragment implements View.OnClickListener {

    TextView diamondCount;
    ImageView avatarImageView;
    TextView titleTextView;
    TextView levelText;
    TextView totalRankingText;
    TextView totalRanking;
    TextView friendsRankingText;
    TextView friendsRanking;
    TextView provinceRankingText;
    TextView provinceRanking;
    TextView textViewCounter;
    ImageButton addFriendButton;
    ImageView duelButton;
    ProgressBar levelProgress;
    Button refillButton;
    Button buyDiamondButton;
    TextView textViewHearts;
    ImageView heartsImageView;

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
    }

    private void configure(View view) {
        addFriendButton.setOnClickListener(this);
        refillButton.setOnClickListener(this);
        buyDiamondButton.setOnClickListener(this);
        duelButton.setOnClickListener(this);

        // set font-face
        FontHelper.setKoodakFor(view.getContext(),
                diamondCount, titleTextView, levelText, totalRankingText,
                totalRanking, friendsRankingText, friendsRanking, textViewHearts,
                provinceRanking, provinceRankingText, textViewCounter, buyDiamondButton, refillButton);
    }

    private void find(View view) {
        diamondCount = (TextView) view.findViewById(R.id.home_diamond_cnt);
        avatarImageView = (ImageView) view.findViewById(R.id.imageView_avatar);
        titleTextView = (TextView) view.findViewById(R.id.textView_title);
        levelText = (TextView) view.findViewById(R.id.home_level_text);
        totalRankingText = (TextView) view.findViewById(R.id.home_total_ranking_text);
        totalRanking = (TextView) view.findViewById(R.id.home_total_ranking);
        friendsRankingText = (TextView) view.findViewById(R.id.home_friends_ranking_text);
        friendsRanking = (TextView) view.findViewById(R.id.home_friends_ranking);
        provinceRanking = (TextView) view.findViewById(R.id.home_province_ranking);
        provinceRankingText = (TextView) view.findViewById(R.id.home_province_ranking_text);
        addFriendButton = (ImageButton) view.findViewById(R.id.button_add_friend);
        refillButton = (Button) view.findViewById(R.id.button_refill);
        buyDiamondButton = (Button) view.findViewById(R.id.button_buy_diamond);
        levelProgress = (ProgressBar) view.findViewById(R.id.home_level_progress);
        textViewHearts = (TextView) view.findViewById(R.id.textView_heart);
        textViewCounter = (TextView) view.findViewById(R.id.textView_counter);
        duelButton = (ImageView) view.findViewById(R.id.button_duel);
        heartsImageView = (ImageView) view.findViewById(R.id.imageView_hearts);
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
                addFriend();
                break;
            case R.id.button_buy_diamond:
            case R.id.button_refill:
                refillHeart();
                break;
            case R.id.button_duel:
                startGame();
                break;
        }
    }

    public void bindViewData() {
        User user = AuthManager.getCurrentUser();
        avatarImageView.setImageResource(AvatarHelper.getResourceId(getActivity(), user.getAvatar()));
        diamondCount.setText(String.valueOf(user.getDiamond()));
        levelText.setText(String.valueOf(ScoreHelper.getLevel(user.getScore())));
        levelProgress.setProgress(ScoreHelper.getThisLevelPercentage(user.getScore()));
        titleTextView.setText(ScoreHelper.getTitle(user.getScore()));
        totalRanking.setText(String.valueOf(user.getRank().getTotal()));
        provinceRanking.setText(String.valueOf(user.getRank().getProvince()));
        friendsRanking.setText(String.valueOf(user.getRank().getFriends()));
        provinceRankingText.setText(ProvinceManager.get(getActivity(), user.getProvince()));
        if (user.isExtremeHeart()) {
            heartsImageView.setImageResource(R.drawable.extreme_heart);
//            heartsImageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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

        getActivity().startActivity(new Intent(getActivity(), CategoryActivity.class));
    }

    private void addFriend() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                String.format(getResources().getString(R.string.message_share),
                        "http://cafebazaar.ir/app/" + getActivity().getPackageName()));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void refillHeart() {
        EventBus.getDefault().post(new ChangePage(1));
    }

    public void onEvent(OnHeartChangeNotice notice) {
        // if hearts are refilling over time, counter text view should be visible, ot invisible
        if (AuthManager.getCurrentUser().getHeart() >= HeartTracker.COUNT_HEARTS_MAX)
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

    public void onEvent(OnUserSettingsChanged settings) {
        bindViewData();
    }

    @Override
    public void onBringToFront() {
        super.onBringToFront();
    }
}
