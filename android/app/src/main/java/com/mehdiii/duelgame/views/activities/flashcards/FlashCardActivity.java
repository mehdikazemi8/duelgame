package com.mehdiii.duelgame.views.activities.flashcards;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.FlashCard;
import com.mehdiii.duelgame.models.FlashCardList;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.MemoryCache;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.flashcards.fragments.FlashcardOverviewFragment;

/**
 * Created by Omid on 7/22/2015.
 */
public class FlashCardActivity extends ParentActivity {
    private static final String FLASH_CARD_LIST_CACHE = "flash_card_list_cache";
    private GridView gridVeiw;

    private BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_FLASH_CARD_LIST) {
                FlashCardList list = FlashCardList.deserialize(json, FlashCardList.class);
                MemoryCache.set(FLASH_CARD_LIST_CACHE, list);
                bindListData(list);
            }
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);

        gridVeiw = (GridView) findViewById(R.id.gridView_main);

        gridVeiw.setOnItemClickListener(gridViewClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, DuelApp.getInstance().getIntentFilter());
        getFlashCards();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void getFlashCards() {

        if (MemoryCache.get(FLASH_CARD_LIST_CACHE) != null) {
            bindListData((FlashCardList) MemoryCache.get(FLASH_CARD_LIST_CACHE));
        } else
            // request from server
            DuelApp.getInstance().sendMessage(new BaseModel().serialize(CommandType.SEND_GET_FLASH_CARD_LIST));
    }

    private void bindListData(FlashCardList list) {
        FlashCardGridAdapter adapter = new FlashCardGridAdapter(this, 0, list);
        this.gridVeiw.setAdapter(adapter);
    }

    AdapterView.OnItemClickListener gridViewClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            FlashCard card = ((FlashCardGridAdapter.ViewHolder) view.getTag()).data;
            Bundle bundle = new Bundle();
            bundle.putString(FlashcardOverviewFragment.BUNDLE_PARAM_FLASH_CARD, card.serialize());

            Fragment fragment = Fragment.instantiate(FlashCardActivity.this, FlashcardOverviewFragment.class.getName(), bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                    .add(R.id.frame_wrapper, fragment)
                    .commit();
        }
    };
}
