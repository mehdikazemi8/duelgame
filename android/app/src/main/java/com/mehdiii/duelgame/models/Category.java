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
    private int category;
    @SerializedName("book")
    private String book;
    @SerializedName("chapter")
    private String chapter;

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
