package com.mehdiii.duelgame.views.activities.home.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.activities.home.fragments.FlipableFragment;

/**
 * Created by omid on 4/5/2015.
 */
public class HomeFragment extends FlipableFragment {

    private TextView homeDiamondCnt;
    private ImageView homeMyAvatar;
    private TextView homeMyDegree;
    private TextView homeLevelText;
    private TextView homeTotalRankingText;
    private TextView homeTotalRanking;
    private TextView homeFriendsRankingText;
    private TextView homeFriendsRanking;

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

        FontHelper.setKoodakFor(view.getContext(),
                homeDiamondCnt, homeMyDegree, homeLevelText, homeTotalRankingText,
                homeTotalRanking, homeFriendsRankingText, homeFriendsRanking);

        return view;
    }
}
