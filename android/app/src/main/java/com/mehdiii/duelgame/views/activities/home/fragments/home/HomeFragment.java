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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.HeartTracker;
import com.mehdiii.duelgame.models.events.OnDiamondChangeNotice;
import com.mehdiii.duelgame.models.events.OnHeartChangeNotice;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.events.OnUserSettingsChanged;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.ScoreHelper;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;

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
    ProgressBar levelProgress;
    Button refillButton;
    Button buyDiamondButton;
    TextView textViewHearts;

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
        bindViewData();

        /**
         * configure click listeners and setup typeface
         */
        addFriendButton.setOnClickListener(this);
        refillButton.setOnClickListener(this);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
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
            case R.id.button_refill:
                refillHeart();
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

        arrangeHearts(user.getHeart());
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
        HeartTracker.getInstance().useHeart();
    }

    public void onEvent(OnHeartChangeNotice notice) {
        if (notice.getMode() == OnHeartChangeNotice.ChangeMode.DECREASED ||
                notice.getMode() == OnHeartChangeNotice.ChangeMode.INCREASED) {
            arrangeHearts(notice.getValue());
            if (notice.getValue() >= HeartTracker.COUNT_HEARTS_MAX)
                textViewCounter.setVisibility(View.INVISIBLE);
            else
                textViewCounter.setVisibility(View.VISIBLE);
        } else if (notice.getMode() == OnHeartChangeNotice.ChangeMode.TICK) {
            int minutes, seconds;
            minutes = notice.getValue() / 60;
            if (minutes == 0)
                seconds = notice.getValue();

            else
                seconds = notice.getValue() % (minutes * 60);
            textViewCounter.setText(String.format("%d:%d", minutes, seconds));
        }
    }

    public void onEvent(OnDiamondChangeNotice notice) {
        this.diamondCount.setText(String.valueOf(notice.getNewValue()));
    }

    public void onEvent(OnUserSettingsChanged settings) {
        bindViewData();
    }

    private void arrangeHearts(int count) {
        this.textViewHearts.setText(String.valueOf(count));
    }

}
