package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by omid on 4/12/2015.
 */
public class Category extends BaseModel {
    public enum CategoryType {
        WANNA_PLAY
    }

    public static Category newInstance(CategoryType type) {
        Category instance = new Category();
        switch (type) {
            case WANNA_PLAY:
                instance.setCommand("WP");
                break;
        }

        return instance;
    }

    @SerializedName("category")
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
