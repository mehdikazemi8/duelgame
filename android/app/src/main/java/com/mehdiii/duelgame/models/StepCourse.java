package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.ArrayList;

/**
 * Created by frshd on 4/5/16.
 */
public class StepCourse extends BaseModel {
    @SerializedName("course_id")
    private String course_id;
    @SerializedName("name")
    private String name;
    @SerializedName("category")
    private Integer category;
    @SerializedName("book")
    private Integer book;
    @SerializedName("num_chapters")
    private Integer num_chapters;
    @SerializedName("progress")
    private ArrayList<Integer> progress;

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getBook() {
        return book;
    }

    public void setBook(Integer book) {
        this.book = book;
    }

    public Integer getNum_chapters() {
        return num_chapters;
    }

    public void setNum_chapters(Integer num_chapters) {
        this.num_chapters = num_chapters;
    }

    public ArrayList<Integer> getProgress() {
        return progress;
    }

    public void setProgress(ArrayList<Integer> progress) {
        this.progress = progress;
    }
}
