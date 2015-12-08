package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

/**
 * Created by mehdiii on 12/8/15.
 */
public class MutualCourseStat extends BaseModel {
    @SerializedName("win")
    int win;
    @SerializedName("draw")
    int draw;
    @SerializedName("lose")
    int lose;
    @SerializedName("course_name")
    String courseName;
    @SerializedName("category")
    String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public MutualCourseStat(int win, int draw, int lose, String courseName) {
        this.win = win;
        this.draw = draw;
        this.lose = lose;
        this.courseName = courseName;
    }

    public MutualCourseStat(CommandType command, int win, int draw, int lose, String courseName) {
        super(command);
        this.win = win;
        this.draw = draw;
        this.lose = lose;
        this.courseName = courseName;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
