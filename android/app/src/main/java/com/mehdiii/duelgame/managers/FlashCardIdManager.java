package com.mehdiii.duelgame.managers;

import android.content.Context;
import android.util.Log;

import com.mehdiii.duelgame.models.FlashCard;
import com.mehdiii.duelgame.models.FlashCardIdList;
import com.mehdiii.duelgame.models.FlashCardList;
import com.mehdiii.duelgame.models.FlashCardSetting;
import com.mehdiii.duelgame.models.FlashCardsSettings;
import com.mehdiii.duelgame.utils.DeckPersister;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frshd on 5/10/16.
 */
public class FlashCardIdManager {

    public static FlashCardIdList getAllFlashCards(Context context) {

        String json = GlobalPreferenceManager.readString(context, "FlashCardIds", "");
        if (json == null || json.isEmpty()) {
            return null;
        } else {
            Log.d("TAG", "flash list: " + json);
            return FlashCardIdList.deserialize(json, FlashCardIdList.class);
        }
    }

    public static void saveFlashCardId(Context context, FlashCard flashCard) {
        FlashCardIdList flashCardIdList = getAllFlashCards(context);

        if (flashCardIdList == null) {
            FlashCardIdList flashCardIdList1 = new FlashCardIdList();
            List<String> flashCardIds = new ArrayList<>();
            flashCardIds.add(flashCard.getId());
            flashCardIdList1.setIds(flashCardIds);
            GlobalPreferenceManager.writeString(context, "FlashCardIds", flashCardIdList1.serialize());

        } else {

            List<String> oldFlashCardIds = flashCardIdList.getIds();
            for (int i=0 ; i<oldFlashCardIds.size() ; i++) {
                if(oldFlashCardIds.get(i).equals(flashCard.getId())) {
                    return;
                }
            }
            oldFlashCardIds.add(flashCard.getId());
            flashCardIdList.setIds(oldFlashCardIds);
            GlobalPreferenceManager.writeString(context, "FlashCardIds", flashCardIdList.serialize());

        }
    }

    public static int getPendingFlashCards(Context context){

        int count = 0;
        FlashCardIdList flashCardList = getAllFlashCards(context);
        DeckPersister dp = new DeckPersister();

        if(flashCardList != null){
            for(String flashCardId: flashCardList.getIds()){

                FlashCard flashCard = dp.getDeck(context, flashCardId);
                int remain = getDailyCount(context, flashCardId)-flashCard.getDailyCount();
                if(remain>0){
                    count += 1;
                }
            }
        }
        return count;
    }

    public static int getDailyCount(Context context, String cardId){

        String json = GlobalPreferenceManager.readString(context, "FlashCardSettings", "");
        if (json == null || json.isEmpty()) {

            return 20;

        } else {

            FlashCardsSettings flashCardsSettings = FlashCardsSettings.deserialize(json, FlashCardsSettings.class);
            List<FlashCardSetting> oldFlashCardSettingList= flashCardsSettings.getFlashCardSettings();

            for (int i=0 ; i<oldFlashCardSettingList.size() ; i++) {

                if(oldFlashCardSettingList.get(i).getCardId().equals(cardId)) {

                    return oldFlashCardSettingList.get(i).getDailyGoal();

                }
            }

            return 20;

        }
    }

}