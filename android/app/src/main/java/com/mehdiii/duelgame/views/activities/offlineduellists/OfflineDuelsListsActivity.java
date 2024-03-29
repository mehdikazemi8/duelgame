package com.mehdiii.duelgame.views.activities.offlineduellists;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.PurchaseManager;
import com.mehdiii.duelgame.models.events.OnFinishActivity;
import com.mehdiii.duelgame.models.events.OnPurchaseResult;
import com.mehdiii.duelgame.models.events.OnShowStoreFragment;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.store.StoreFragment;
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
    private Fragment viewDuelsFragment;

    int whichTabToShow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_duels_lists);

        whichTabToShow = getIntent().getExtras().getInt("tab", 0);

        findControls();
        configureControls();

        viewDuelsFragment = Fragment.instantiate(this, ViewOfflineDuelsFragment.class.getName(), null);
        Bundle bundle = new Bundle();
        bundle.putString("turn", sendWhat[whichTabToShow]);
        viewDuelsFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.view_offline_duel_fragment_holder, viewDuelsFragment)
                .commit();
    }

    private void setBackColor() {
        for (int i = 0; i < NUMBER_OF_TABS; i++)
            if (isFocused[i])
                duelOfflineMenu[i].setBackgroundColor(focusedColor);
            else
                duelOfflineMenu[i].setBackgroundColor(notFocusedColor);
    }

    protected void findControls() {
        setFocusInitialState(whichTabToShow);

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

    @Override
    protected void onResume() {
        super.onResume();
        PurchaseManager.changeActivity(this);
    }

    private void setFocusInitialState(int focusedTab) {
        for(int k = 0; k < NUMBER_OF_TABS; k ++) {
            if(k == focusedTab) {
                isFocused[k] = true;
            } else {
                isFocused[k] = false;
            }
        }
    }

    protected void configureControls() {
        focusedColor = getResources().getColor(R.color.purple);
        notFocusedColor = getResources().getColor(R.color.purple_dark);

        setBackColor();
    }

    @Override
    public boolean canHandleChallengeRequest() {
        return true;
    }

    public void onEvent(OnFinishActivity event) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!PurchaseManager.getInstance().handleActivityResult(resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    public void onEvent(OnShowStoreFragment event) {
        StoreFragment storeFragment = (StoreFragment) Fragment.instantiate(this, StoreFragment.class.getName(), null);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.store_fragment_holder, storeFragment, "STORE_FR")
                .addToBackStack(null)
                .commit();
    }
}
