package com.mehdiii.duelgame.views.activities.flashcards;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.GridView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.FlashCardList;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.MemoryCache;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;

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
                bindListData(list);
            }
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);

        gridVeiw = (GridView) findViewById(R.id.gridView_main);
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
            // fetch from memory
        } else
            // request from server
            DuelApp.getInstance().sendMessage(new BaseModel().serialize(CommandType.SEND_GET_FLASH_CARD_LIST));
    }

    private void bindListData(FlashCardList list) {
        FlashCardGridAdapter adapter = new FlashCardGridAdapter(this, 0, list);
        this.gridVeiw.setAdapter(adapter);
    }
}
