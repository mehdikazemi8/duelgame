package com.mehdiii.duelgame.views.activities.flashcards;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.PurchaseManager;
import com.mehdiii.duelgame.models.FlashCard;
import com.mehdiii.duelgame.models.FlashCardList;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnFlashCardReceived;
import com.mehdiii.duelgame.utils.DeckManager;
import com.mehdiii.duelgame.utils.DeckPersister;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.MemoryCache;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.flashcards.fragments.OverviewFragment;
import com.mehdiii.duelgame.views.dialogs.AlertDialog;

import org.w3c.dom.ls.LSInput;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Omid on 7/22/2015.
 */
public class FlashCardActivity extends ParentActivity implements View.OnClickListener{
    private static final String FLASH_CARD_LIST_CACHE = "flash_card_list_cache";
//    private GridView gridView;
    private ListView listView;
    private ImageButton infoButton;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!PurchaseManager.getInstance().handleActivityResult(resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flash_card_new);
//        gridView = (GridView) findViewById(R.id.gridView_main);
//        gridView.setOnItemClickListener(gridViewClickListener);
        listView = (ListView) findViewById(R.id.deck_list);
        infoButton = (ImageButton) findViewById(R.id.info_button);
        listView.setOnItemClickListener(listViewClickListener);
        infoButton.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, DuelApp.getInstance().getIntentFilter());
        PurchaseManager.changeActivity(this);
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
        FlashCardListAdapter adapter = new FlashCardListAdapter(this, 0, list);
        this.listView.setAdapter(adapter);
//        FlashCardGridAdapter adapter = new FlashCardGridAdapter(this, 0, list);
//        this.gridView.setAdapter(adapter);

    }

    AdapterView.OnItemClickListener gridViewClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            FlashCard card = ((FlashCardGridAdapter.ViewHolder) view.getTag()).data;

            Bundle bundle = new Bundle();
            bundle.putString(OverviewFragment.BUNDLE_PARAM_FLASH_CARD, card.serialize());
            Fragment fragment = Fragment.instantiate(FlashCardActivity.this, OverviewFragment.class.getName(), bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.abc_fade_out)
                    .add(R.id.frame_wrapper, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    };

    AdapterView.OnItemClickListener listViewClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            FlashCard card = ((FlashCardListAdapter.ViewHolder) view.getTag()).data;

            Bundle bundle = new Bundle();
            bundle.putString(OverviewFragment.BUNDLE_PARAM_FLASH_CARD, card.serialize());
            Fragment fragment = Fragment.instantiate(FlashCardActivity.this, OverviewFragment.class.getName(), bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.abc_fade_out)
                    .add(R.id.frame_wrapper, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    };


    @Override
    public void onBackPressed() {
        // if this activity is going to be seen again.
        if (getSupportFragmentManager().getBackStackEntryCount() == 1)
            getFlashCards();

        super.onBackPressed();
    }

    public void reloadAsync() {
        FlashCardListAdapter adapter = (FlashCardListAdapter) this.listView.getAdapter();
//        FlashCardGridAdapter adapter = (FlashCardGridAdapter) this.gridView.getAdapter();
        adapter.clear();
        adapter.notifyDataSetChanged();

        MemoryCache.set(FLASH_CARD_LIST_CACHE, null);
        getFlashCards();
    }

    @Override
    public boolean canHandleChallengeRequest() {
        return true;
    }


    private BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_FLASH_CARD_LIST) {
                Log.d("TAG", "RECEIVE_FLASH_CARD_LIST " + json);

                FlashCardList list = FlashCardList.deserialize(json, FlashCardList.class);

                for(FlashCard fc : list.getList()) {
                    Log.d("TAG", "RECEIVE_FLASH_CARD_LIST " + fc.getProgress());
                }

                MemoryCache.set(FLASH_CARD_LIST_CACHE, list);
                bindListData(list);
            } else if (type == CommandType.RECEIVE_GET_FLASH_CARD_REQUEST) {
                FlashCard card = FlashCard.deserialize(json, FlashCard.class);
                if (card != null) {
                    card.saveIndexes();
                    DeckPersister.saveDeck(FlashCardActivity.this, card);
                    EventBus.getDefault().post(new OnFlashCardReceived());
                }
            }
        }
    });

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_button:
                handleInfoButton();
                break;
        }
    }

    private void handleInfoButton() {
        String msg = getString(R.string.flashcart_info);
        AlertDialog dialog = new AlertDialog( this, msg);
        dialog.show();
    }
}
