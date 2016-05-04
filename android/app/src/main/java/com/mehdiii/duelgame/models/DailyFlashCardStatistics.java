package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.Calendar;

/**
 * Created by frshd on 5/1/16.
 */
public class DailyFlashCardStatistics extends BaseModel {

    @SerializedName("Date")
    Calendar Date;
    @SerializedName("Number")
    Integer Number;

    public void plusNumber(){this.Number++;}

    public Calendar getDate() {
        return Date;
    }

    public void setDate(Calendar date) {
        Date = date;
    }

    public Integer getNumber() {
        return Number;
    }

    public void setNumber(Integer number) {
        Number = number;
    }
}
