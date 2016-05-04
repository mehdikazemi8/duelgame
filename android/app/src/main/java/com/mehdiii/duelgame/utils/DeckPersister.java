package com.mehdiii.duelgame.utils;

import android.content.Context;
import android.util.Log;

import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.FlashCard;

/**
 * Created by Omid on 7/22/2015.
 */
public class DeckPersister {

    private static final String PREFERENCE_DECK = "preference_deck";

    public static boolean hasDeck(Context context, String id) {
        return GlobalPreferenceManager.hasKey(context, getKey(id));
    }

    public static FlashCard getDeck(Context context, String id) {
        String json = GlobalPreferenceManager.readString(context, getKey(id), "");

        if (json == null || json.isEmpty())
            return null;

        return FlashCard.deserialize(json, FlashCard.class);
    }

    public static void saveDeck(Context context, FlashCard deck) {
        String key = getKey(deck.getId());
        GlobalPreferenceManager.writeString(context, key, deck.serialize());
    }

    private static String getKey(String id) {
        return PREFERENCE_DECK + "_" + id;
    }
}
