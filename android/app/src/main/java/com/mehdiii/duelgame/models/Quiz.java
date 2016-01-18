package com.mehdiii.duelgame.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehdiii on 1/14/16.
 */
public class Quiz extends GetBuyQuizRequest {
    @SerializedName("title")
    private String title;
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
    @SerializedName("discount")
    private Integer discount;
    @SerializedName("taken")
    private Boolean taken;
    @SerializedName("duration")
    private Integer duration;
    @SerializedName("questions_per_category")
    private List<QuizCourse> courses;
    @SerializedName("seconds_to_start")
    private Integer timeToQuiz;
    @SerializedName("questions")
    private ArrayList<QuestionForQuiz> questions;

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public ArrayList<QuestionForQuiz> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuestionForQuiz> questions) {
        this.questions = questions;
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
        Log.d("TAG", "onEvent owned changed " + owned);
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
