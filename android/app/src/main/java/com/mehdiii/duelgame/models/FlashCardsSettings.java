package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.List;

/**
 * Created by frshd on 5/8/16.
 */
public class FlashCardsSettings extends BaseModel {

    @SerializedName("flashcard_settings")
    private List<FlashCardSetting> flashCardSettings;

    public List<FlashCardSetting> getFlashCardSettings() {
        return flashCardSettings;
    }

    public void setFlashCardSettings(List<FlashCardSetting> flashCardSettings) {
        this.flashCardSettings = flashCardSettings;
    }

}
