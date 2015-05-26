package com.mehdiii.duelgame.views.activities.home.fragments.ranking;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.ProvinceManager;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;

/**
 * Created by omid on 4/5/2015.
 */
public class RankingFragment extends FlippableFragment {

    private final int NUMBER_OF_TABS = 4;

    boolean[] isFocused = new boolean[NUMBER_OF_TABS];
    int focusedColor;
    int notFocusedColor;
    TextView[] rankTitle = new TextView[NUMBER_OF_TABS];
    CommandType[] sendWhat = new CommandType[]{CommandType.SEND_GET_TOTAL_RANK_TODAY, CommandType.SEND_GET_TOTAL_RANK, CommandType.SEND_GET_PROVINCE_RANK, CommandType.SEND_GET_FRIENDS_RANK};
    CommandType[] receiveWhat = new CommandType[]{CommandType.RECEIVE_GET_TOTAL_RANK_TODAY, CommandType.RECEIVE_GET_TOTAL_RANK, CommandType.RECEIVE_GET_PROVINCE_RANK, CommandType.RECEIVE_GET_FRIENDS_RANK};

    Activity activity = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private Fragment viewRankingFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }

    private void setBackColor() {
        for (int i = 0; i < NUMBER_OF_TABS; i++)
            if (isFocused[i])
                rankTitle[i].setBackgroundColor(focusedColor);
            else
                rankTitle[i].setBackgroundColor(notFocusedColor);
    }

    protected void findControls(View view) {
        rankTitle[0] = (TextView) view.findViewById(R.id.ranking_total_today);
        rankTitle[1] = (TextView) view.findViewById(R.id.ranking_total);
        rankTitle[2] = (TextView) view.findViewById(R.id.ranking_province);
        rankTitle[3] = (TextView) view.findViewById(R.id.ranking_friends);

        for (int i = 0; i < NUMBER_OF_TABS; i++) {
            rankTitle[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int titleIndex = Integer.parseInt(v.getContentDescription().toString());
                    if (isFocused[titleIndex])
                        return;

                    for (int j = 0; j < NUMBER_OF_TABS; j++)
                        isFocused[j] = false;
                    isFocused[titleIndex] = true;
                    setBackColor();

                    ((ViewRankingFragment) viewRankingFragment).onReload(sendWhat[titleIndex], receiveWhat[titleIndex]);
                }
            });
        }
    }

    private void setFocusInitialState() {
        isFocused[0] = true;
        isFocused[1] = isFocused[2] = isFocused[3] = false;
    }

    protected void configureControls() {
        if (this.activity == null)
            return;
        focusedColor = this.activity.getResources().getColor(R.color.yellow);
        notFocusedColor = this.activity.getResources().getColor(R.color.yellow_light);

        FontHelper.setKoodakFor(this.activity, rankTitle[0], rankTitle[1], rankTitle[2], rankTitle[3]);
        rankTitle[2].setText(ProvinceManager.get(this.activity, AuthManager.getCurrentUser().getProvince()));

        setFocusInitialState();
        setBackColor();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewRankingFragment = Fragment.instantiate(this.activity, ViewRankingFragment.class.getName(), null);
        getFragmentManager().beginTransaction().add(R.id.view_ranking_fragment_holder, viewRankingFragment).commit();

        findControls(view);
        configureControls();
    }

    @Override
    public void onBringToFront() {
        super.onBringToFront();
        configureControls();
        ((ViewRankingFragment) viewRankingFragment).onReload(sendWhat[0], receiveWhat[0]);
    }
}
