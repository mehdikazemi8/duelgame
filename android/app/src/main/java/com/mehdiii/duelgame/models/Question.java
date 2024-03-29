package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by MeHdi on 3/2/2015.
 */
public class Question {
    @SerializedName("question_text")
    private String questionText;
    @SerializedName("options")
    private ArrayList<String> options;
    @SerializedName("question_number")
    private int questionNumberInServer;
    @SerializedName("opponent_answer")
    private Boolean opponentAnswer;

    public Boolean getOpponentAnswer() {
        return opponentAnswer;
    }

    public void setOpponentAnswer(Boolean opponentAnswer) {
        this.opponentAnswer = opponentAnswer;
    }

    public int getQuestionNumberInServer() {
        return questionNumberInServer;
    }

    public void setQuestionNumberInServer(int questionNumberInServer) {
        this.questionNumberInServer = questionNumberInServer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }
}
