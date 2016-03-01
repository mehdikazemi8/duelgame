package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by frshd on 3/1/16.
 */
public class StepResult extends BaseModel{

    @SerializedName("category")
    private Integer category;
    @SerializedName("book")
    private Integer book;
    @SerializedName("chapter")
    private Integer chapter;
    @SerializedName("correct")
    private Integer correct;
    @SerializedName("all")
    private Integer all;

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }

    public Integer getCorrect() {
        return correct;

    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }

    public Integer getChapter() {

        return chapter;
    }

    public void setChapter(Integer chapter) {
        this.chapter = chapter;
    }

    public Integer getBook() {

        return book;
    }

    public void setBook(Integer book) {
        this.book = book;
    }

    public Integer getCategory() {

        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

}
