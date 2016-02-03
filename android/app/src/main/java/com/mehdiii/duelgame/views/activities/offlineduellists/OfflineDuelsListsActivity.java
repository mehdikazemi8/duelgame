package com.mehdiii.duelgame.views.activities.offlineduellists;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.offlineduellists.fragments.ViewOfflineDuelsFragment;
import com.mehdiii.duelgame.views.activities.ranking.fragments.ViewRankingFragment;
import com.mehdiii.duelgame.views.custom.CustomTextView;

/**
 * Created by mehdiii on 7/7/15.
 */
public class OfflineDuelsListsActivity extends ParentActivity {

    private final int NUMBER_OF_TABS = 3;

    boolean[] isFocused = new boolean[NUMBER_OF_TABS];
    int focusedColor;
    int notFocusedColor;
    CustomTextView[] duelOfflineMenu = new CustomTextView[NUMBER_OF_TABS];
    String[] sendWhat = new String[]{"mine", "theirs", "done"};
//    String[] sendWhat = new String[]{"week", "overall"};
    private Fragment viewDuelsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_duels_lists);

        findControls();
        configureControls();

        viewDuelsFragment = Fragment.instantiate(this, ViewOfflineDuelsFragment.class.getName(), null);
        getSupportFragmentManager().beginTransaction().add(R.id.view_course_ranking_fragment_holder, viewDuelsFragment).commit();
    }

    private void setBackColor() {
        for (int i = 0; i < NUMBER_OF_TABS; i++)
            if (isFocused[i])
                duelOfflineMenu[i].setBackgroundColor(focusedColor);
            else
                duelOfflineMenu[i].setBackgroundColor(notFocusedColor);
    }

    protected void findControls() {
        duelOfflineMenu[0] = (CustomTextView) findViewById(R.id.menu_my_turn);
        duelOfflineMenu[1] = (CustomTextView) findViewById(R.id.menu_their_turn);
        duelOfflineMenu[2] = (CustomTextView) findViewById(R.id.menu_done);

        for (int i = 0; i < NUMBER_OF_TABS; i++) {
            duelOfflineMenu[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int titleIndex = Integer.parseInt(v.getContentDescription().toString());
                    if (isFocused[titleIndex])
                        return;

                    for (int j = 0; j < NUMBER_OF_TABS; j++)
                        isFocused[j] = false;
                    isFocused[titleIndex] = true;
                    setBackColor();

                    ((ViewOfflineDuelsFragment) viewDuelsFragment).onReload(sendWhat[titleIndex]);
                }
            });
        }
    }

    private void setFocusInitialState() {
        isFocused[0] = true;
        isFocused[1] = false;
        isFocused[2] = false;
    }

    protected void configureControls() {
        focusedColor = getResources().getColor(R.color.purple);
        notFocusedColor = getResources().getColor(R.color.purple_dark);

        setFocusInitialState();
        setBackColor();
    }

    @Override
    public boolean canHandleChallengeRequest() {
        return true;
    }
}
