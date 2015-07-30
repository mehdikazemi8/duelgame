package com.mehdiii.duelgame.views.activities.ranking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.ranking.fragments.ViewRankingFragment;

/**
 * Created by mehdiii on 7/7/15.
 */
public class RankingActivity extends ParentActivity {

    private final int NUMBER_OF_TABS = 2;

    boolean[] isFocused = new boolean[NUMBER_OF_TABS];
    int focusedColor;
    int notFocusedColor;
    TextView[] rankTitle = new TextView[NUMBER_OF_TABS];
    String[] sendWhat = new String[]{"week", "overall"};
    private Fragment viewRankingFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        findControls();
        configureControls();

        viewRankingFragment = Fragment.instantiate(this, ViewRankingFragment.class.getName(), null);
        getSupportFragmentManager().beginTransaction().add(R.id.view_course_ranking_fragment_holder, viewRankingFragment).commit();
    }

    private void setBackColor() {
        for (int i = 0; i < NUMBER_OF_TABS; i++)
            if (isFocused[i])
                rankTitle[i].setBackgroundColor(focusedColor);
            else
                rankTitle[i].setBackgroundColor(notFocusedColor);
    }

    protected void findControls() {
        rankTitle[0] = (TextView) findViewById(R.id.ranking_week);
        rankTitle[1] = (TextView) findViewById(R.id.ranking_overall);

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

                    ((ViewRankingFragment) viewRankingFragment).onReload(sendWhat[titleIndex]);
                }
            });
        }
    }

    private void setFocusInitialState() {
        isFocused[0] = true;
        isFocused[1] = false;
    }

    protected void configureControls() {
        focusedColor = getResources().getColor(R.color.yellow);
        notFocusedColor = getResources().getColor(R.color.yellow_light);

        FontHelper.setKoodakFor(this, rankTitle[0], rankTitle[1]);

        setFocusInitialState();
        setBackColor();
    }
}
