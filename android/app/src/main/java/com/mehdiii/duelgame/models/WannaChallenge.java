package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by Omid on 5/23/2015.
 */
public class WannaChallenge extends BaseModel {

    @SerializedName("user_number")
    private String userNumber;
    @SerializedName("category")
    private int category;
    @SerializedName("message")
    private String message;
    @SerializedName("book")
    private String book;
    @SerializedName("chapter")
    private String chapter;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public WannaChallenge(String userNumber, int category, String message) {
        this.userNumber = userNumber;
        this.category = category;
        this.message = message;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }


    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
