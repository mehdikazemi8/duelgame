package com.mehdiii.duelgame.managers;

import android.content.Context;
import android.util.Log;

import com.mehdiii.duelgame.models.FlashCardSetting;
import com.mehdiii.duelgame.models.FlashCardsSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frshd on 5/10/16.
 */
public class FlashCardSettingManager {

    public static FlashCardsSettings init(Context context){
        String json = GlobalPreferenceManager.readString(context, "FlashCardSettings", "");
        if (json == null || json.isEmpty()) {
            return null;
        }
        else {
            return FlashCardsSettings.deserialize(json, FlashCardsSettings.class);
        }
    }

    public static void saveSetting(Context context, FlashCardSetting flashCardSetting){
        FlashCardsSettings flashCardsSettings = init(context);

        if(flashCardsSettings==null){

            FlashCardsSettings flashCardsSettings2 = new FlashCardsSettings();
            List<FlashCardSetting> flashCardSettings = new ArrayList<>();
            flashCardSettings.add(flashCardSetting);
            flashCardsSettings2.setFlashCardSettings(flashCardSettings);
            GlobalPreferenceManager.writeString(context, "FlashCardSettings", flashCardsSettings2.serialize());
            Log.d("TAG", "flashCardsSettings " + flashCardsSettings2.serialize());

        }else {

            List<FlashCardSetting> oldFlashCardSettingList= flashCardsSettings.getFlashCardSettings();
            boolean found=false;
            for (int i=0 ; i<oldFlashCardSettingList.size() ; i++) {
                if(oldFlashCardSettingList.get(i).getCardId().equals(flashCardSetting.getCardId())) {
                    oldFlashCardSettingList.get(i).setAlarm(flashCardSetting.getAlarm());
                    oldFlashCardSettingList.get(i).setDailyGoal(flashCardSetting.getDailyGoal());
                    found = true;
                }
            }
            if(!found){
                oldFlashCardSettingList.add(flashCardSetting);
            }

            flashCardsSettings.setFlashCardSettings(oldFlashCardSettingList);
            GlobalPreferenceManager.writeString(context, "FlashCardSettings", flashCardsSettings.serialize());
            Log.d("TAG", "flashCardsSettings " + flashCardsSettings.serialize());

        }
    }

    public static FlashCardSetting getSettingById(Context context, String cardId){
        FlashCardsSettings flashCardsSettings = init(context);
        if(flashCardsSettings==null){
            return null;
        }
        else {

            List<FlashCardSetting> oldFlashCardSettingList= flashCardsSettings.getFlashCardSettings();

            for (int i=0 ; i<oldFlashCardSettingList.size() ; i++) {
                if(oldFlashCardSettingList.get(i).getCardId().equals(cardId)) {

                    return oldFlashCardSettingList.get(i);

                }
            }

        }

        return null;
    }
}
