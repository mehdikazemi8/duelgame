package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.Calendar;

/**
 * Created by frshd on 5/8/16.
 */
public class FlashCardSetting extends BaseModel{

    @SerializedName("card_id")
    private String cardId;
    @SerializedName("alarm")
    Calendar alarm;
    @SerializedName("daily_goal")
    int dailyGoal;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Calendar getAlarm() {
        return alarm;
    }

    public void setAlarm(Calendar alarm) {
        this.alarm = alarm;
    }

    public int getDailyGoal() {
        return dailyGoal;
    }

    public void setDailyGoal(int dailyGoal) {
        this.dailyGoal = dailyGoal;
    }

}
