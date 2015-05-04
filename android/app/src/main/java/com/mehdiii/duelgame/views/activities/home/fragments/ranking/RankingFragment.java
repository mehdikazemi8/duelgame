package com.mehdiii.duelgame.views.activities.home.fragments.ranking;

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

    boolean[] isFocused = new boolean[3];
    int focusedColor;
    int notFocusedColor;
    TextView[] rankTitle = new TextView[3];
    CommandType[] sendWhat = new CommandType[]{CommandType.SEND_GET_TOTAL_RANK, CommandType.SEND_GET_PROVINCE_RANK, CommandType.SEND_GET_FRIENDS_RANK};
    CommandType[] receiveWhat = new CommandType[]{CommandType.RECEIVE_GET_TOTAL_RANK, CommandType.RECEIVE_GET_PROVINCE_RANK, CommandType.RECEIVE_GET_FRIENDS_RANK};

    private void setBackColor() {
        for (int i = 0; i < 3; i++)
            if (isFocused[i])
                rankTitle[i].setBackgroundColor(focusedColor);
            else
                rankTitle[i].setBackgroundColor(notFocusedColor);
    }

    protected void findControls(View view) {
        rankTitle[0] = (TextView) view.findViewById(R.id.ranking_total);
        rankTitle[1] = (TextView) view.findViewById(R.id.ranking_province);
        rankTitle[2] = (TextView) view.findViewById(R.id.ranking_friends);

        for (int i = 0; i < 3; i++) {
            rankTitle[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int titleIndex = Integer.parseInt(v.getContentDescription().toString());
                    if (isFocused[titleIndex])
                        return;

                    for (int j = 0; j < 3; j++)
                        isFocused[j] = false;
                    isFocused[titleIndex] = true;
                    setBackColor();

                    ((ViewRankingFragment) viewRankingFragment).onReload(sendWhat[titleIndex], receiveWhat[titleIndex]);
                }
            });
        }
    }

    protected void configureControls() {
        focusedColor = getResources().getColor(R.color.yellow);
        notFocusedColor = getResources().getColor(R.color.yellow_light);

        FontHelper.setKoodakFor(getActivity(), rankTitle[0], rankTitle[1], rankTitle[2]);
        rankTitle[1].setText(ProvinceManager.get(getActivity(), AuthManager.getCurrentUser().getProvince()));

        isFocused[0] = true;
        isFocused[1] = isFocused[1] = false;

        setBackColor();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewRankingFragment = Fragment.instantiate(getActivity(), ViewRankingFragment.class.getName(), null);
        getFragmentManager().beginTransaction().add(R.id.view_ranking_fragment_holder, viewRankingFragment).commit();

        findControls(view);
        configureControls();

        ((ViewRankingFragment) viewRankingFragment).onReload(sendWhat[0], receiveWhat[0]);
    }

    @Override
    public void onBringToFront() {
        super.onBringToFront();
        ((ViewRankingFragment) viewRankingFragment).onReload(sendWhat[0], receiveWhat[0]);
    }
}
