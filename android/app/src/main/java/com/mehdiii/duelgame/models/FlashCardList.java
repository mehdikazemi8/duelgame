package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.List;

/**
 * Created by Omid on 7/22/2015.
 */
public class FlashCardList extends BaseModel {

    @SerializedName("flash_cards_list")
    private List<FlashCard> list;

    public List<FlashCard> getList() {
        return list;
    }

    public void setList(List<FlashCard> list) {
        this.list = list;
    }
}
