package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

import java.util.List;

/**
 * Created by mehdiii on 1/14/16.
 */
public class Quiz extends BaseModel {
    @SerializedName("title")
    private String title;
    @SerializedName("id")
    private String id;
    @SerializedName("start")
    private String start;
    @SerializedName("end")
    private String end;
    @SerializedName("status")
    private String status;
    @SerializedName("owned")
    private Boolean owned;
    @SerializedName("price")
    private Integer price;
    @SerializedName("taken")
    private Boolean taken;
    @SerializedName("duration")
    private Integer duration;
    @SerializedName("questions_per_category")
    private List<QuizCourse> courses;
    @SerializedName("seconds_to_start")
    private Integer timeToQuiz;

    public Quiz(String title, String id, String start, String end, String status, Boolean owned, Integer price, Boolean taken, Integer duration, List<QuizCourse> courses) {
        this.title = title;
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
        this.owned = owned;
        this.price = price;
        this.taken = taken;
        this.duration = duration;
        this.courses = courses;
    }

    public Quiz(CommandType command, String title, String id, String start, String end, String status, Boolean owned, Integer price, Boolean taken, Integer duration, List<QuizCourse> courses) {
        super(command);
        this.title = title;
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
        this.owned = owned;
        this.price = price;
        this.taken = taken;
        this.duration = duration;
        this.courses = courses;
    }

    public Integer getTimeToQuiz() {
        return timeToQuiz;
    }

    public void setTimeToQuiz(Integer timeToQuiz) {
        this.timeToQuiz = timeToQuiz;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getOwned() {
        return owned;
    }

    public void setOwned(Boolean owned) {
        this.owned = owned;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean getTaken() {
        return taken;
    }

    public void setTaken(Boolean taken) {
        this.taken = taken;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<QuizCourse> getCourses() {
        return courses;
    }

    public void setCourses(List<QuizCourse> courses) {
        this.courses = courses;
    }
}
