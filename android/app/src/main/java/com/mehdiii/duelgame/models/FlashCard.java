package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.managers.DateManager;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlashCard extends BaseModel {
    @SerializedName("title")
    private String title;
    @SerializedName("price")
    private double price;
    @SerializedName("percent_free")
    private double percentFree;
    @SerializedName("owned")
    private int owned;
    @SerializedName("version")
    private int version;
    @SerializedName("progress")
    private int progress;
    @SerializedName("_id")
    private String id;

    /**
     * details
     */
    @SerializedName("cards")
    List<Card> cards;
    @SerializedName("seen")
    int[] seen;
    @SerializedName("to_ask")
    int[] toAsk;
    @SerializedName("daily_count")
    int dailyCount;
    @SerializedName("last_day")
    Calendar lastDay;
    @SerializedName("daily_statistics")
    List<DailyFlashCardStatistics> dailyFlashCardStatistics;
    @SerializedName("alarm")
    Calendar alarm;

    public Calendar getAlarm() {
        return alarm;
    }

    public void setAlarm(Calendar alarm) {
        this.alarm = alarm;
    }

    public void addStat(DailyFlashCardStatistics dfc){
        for(DailyFlashCardStatistics day : dailyFlashCardStatistics){
            if(DateManager.isSameDay(dfc.getDate(), day.getDate())) {

                day.plusNumber();
                return;
            }
        }
        this.dailyFlashCardStatistics.add(dfc);
        return;
    }

    public List<DailyFlashCardStatistics> getDailyFlashCardStatistics() {
        return dailyFlashCardStatistics;
    }

    public void setDailyFlashCardStatistics(List<DailyFlashCardStatistics> dailyFlashCardStatistics) {
        this.dailyFlashCardStatistics = dailyFlashCardStatistics;
    }

    public int getDailyCount() {
        return dailyCount;
    }

    public void setDailyCount(int dailyCount) {
        this.dailyCount = dailyCount;
    }

    public Calendar getLastDay() {
        return lastDay;
    }

    public void setLastDay(Calendar lastDay) {
        this.lastDay = lastDay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPercentFree() {
        return percentFree;
    }

    public void setPercentFree(double percentFree) {
        this.percentFree = percentFree;
    }

    public int getOwned() {
        return owned;
    }

    public void setOwned(int owned) {
        this.owned = owned;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public int[] getSeen() {
        return seen;
    }

    public void setSeen(int[] seen) {
        this.seen = seen;
    }

    public int[] getToAsk() {
        return toAsk;
    }

    public void setToAsk(int[] toAsk) {
        this.toAsk = toAsk;
    }

    public void saveIndexes() {
        int i = 0;
        if (getCards() != null)
            for (Card card : getCards())
                card.setIndex(i++);
    }

    public void organize() {
        if (seen == null)
            return;
        int counter = 0;
        for (Card card : getCards())
            card.setWeight(seen[counter++]);
    }
}
