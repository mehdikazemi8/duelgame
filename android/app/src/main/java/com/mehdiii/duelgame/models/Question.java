package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MeHdi on 3/2/2015.
 */
public class Question {
    @SerializedName("question_text")
    public String questionText;
    public String[] options = new String[4];
}
