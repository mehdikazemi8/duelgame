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
import android.widget.TextView;
import android.widget.Toast;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.HeartTracker;
import com.mehdiii.duelgame.models.HeartChangeCommand;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.activities.home.fragments.FlipableFragment;

import de.greenrobot.event.EventBus;

/**
 * Created by omid on 4/5/2015.
 */
public class HomeFragment extends FlipableFragment implements View.OnClickListener {

    private TextView homeDiamondCnt;
    private ImageView homeMyAvatar;
    private TextView homeMyDegree;
    private TextView homeLevelText;
    private TextView homeTotalRankingText;
    private TextView homeTotalRanking;
    private TextView homeFriendsRankingText;
    private TextView homeFriendsRanking;
    ImageButton addFriendButton;
    Button refillButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        homeDiamondCnt = (TextView) view.findViewById(R.id.home_diamond_cnt);
        homeMyAvatar = (ImageView) view.findViewById(R.id.home_my_avatar);
        homeMyDegree = (TextView) view.findViewById(R.id.home_my_degree);
        homeLevelText = (TextView) view.findViewById(R.id.home_level_text);
        homeTotalRankingText = (TextView) view.findViewById(R.id.home_total_ranking_text);
        homeTotalRanking = (TextView) view.findViewById(R.id.home_total_ranking);
        homeFriendsRankingText = (TextView) view.findViewById(R.id.home_friends_ranking_text);
        homeFriendsRanking = (TextView) view.findViewById(R.id.home_friends_ranking);
        addFriendButton = (ImageButton) view.findViewById(R.id.button_add_friend);
        refillButton = (Button) view.findViewById(R.id.button_refill);

        FontHelper.setKoodakFor(view.getContext(),
                homeDiamondCnt, homeMyDegree, homeLevelText, homeTotalRankingText,
                homeTotalRanking, homeFriendsRankingText, homeFriendsRanking);

        addFriendButton.setOnClickListener(this);
        refillButton.setOnClickListener(this);

        EventBus.getDefault().register(this);

        return view;
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
        HeartTracker.getInstance(getActivity()).useHeart();
    }

    public void onEvent(HeartChangeCommand command) {
        String msg = "";
        if (command.getMode() == HeartChangeCommand.ChangeMode.INCREASED) {
            msg += "HEART INCREASED";
        } else
            msg += "HEART DECREASED";

        msg += ": " + String.valueOf(command.getState().getCurrent());

        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

    }
}
