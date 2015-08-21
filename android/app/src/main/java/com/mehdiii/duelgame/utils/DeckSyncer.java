package com.mehdiii.duelgame.utils;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.models.DeckSync;
import com.mehdiii.duelgame.models.Pair;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnFlashCardProgressSaved;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Omid on 7/22/2015.
 */
public class DeckSyncer {
    private static final int SYNC_CACHE_LIMIT = 5;
    int counter = 0;
    DeckManager manager;
    Map<Integer, Integer> map = new HashMap<>();

    public DeckSyncer(DeckManager manager) {
        this.manager = manager;
    }

    public void set(Pair<Integer, Integer> pair) {
        map.put(pair.getKey(), pair.getValue());

        counter++;
        if (counter > SYNC_CACHE_LIMIT)
            sync();
    }

    public void sync() {
        DeckSync model = new DeckSync();

        // serialize
        Iterator<Map.Entry<Integer, Integer>> iterator = map.entrySet().iterator();
        Map.Entry<Integer, Integer> cur;
        while (iterator.hasNext()) {
            cur = iterator.next();
            model.getSeen().add(new Pair<>(cur.getKey(), cur.getValue()));
        }
        model.setToAsk(manager.getCapacities());
        model.setId(manager.getId());

        // send changed to server
        DuelApp.getInstance().sendMessage(model.serialize(CommandType.SEND_FLASH_CARD_PROGRESS));

        // save cards locally
        DeckPersister.saveDeck(DuelApp.getInstance(), manager.getDeck());

        // reset counter
        counter = 0;

        // notify progress is saved
        EventBus.getDefault().post(new OnFlashCardProgressSaved());
    }

    private void clear() {
        map.clear();
    }

}
