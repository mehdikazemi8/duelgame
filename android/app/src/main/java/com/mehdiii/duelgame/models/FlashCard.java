package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by Omid on 7/22/2015.
 */
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
    private String identifier;

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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
