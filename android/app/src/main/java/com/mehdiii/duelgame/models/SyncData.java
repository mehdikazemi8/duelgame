package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by mehdiii on 2/5/16.
 */
public class SyncData extends BaseModel {
    @SerializedName("diamond")
    private Integer diamond;
    @SerializedName("pending_offline_challenges")
    private Integer pendingOfflineChallenges;
    @SerializedName("heart")
    private Integer heart;
    @SerializedName("remaining_time_for_next_heart")
    private Integer remainingTimeForNextHeart;

    public Integer getPendingOfflineChallenges() {
        return pendingOfflineChallenges;
    }

    public void setPendingOfflineChallenges(Integer pendingOfflineChallenges) {
        this.pendingOfflineChallenges = pendingOfflineChallenges;
    }

    public Integer getHeart() {
        return heart;
    }

    public void setHeart(Integer heart) {
        this.heart = heart;
    }

    public Integer getRemainingTimeForNextHeart() {
        return remainingTimeForNextHeart;
    }

    public void setRemainingTimeForNextHeart(Integer remainingTimeForNextHeart) {
        this.remainingTimeForNextHeart = remainingTimeForNextHeart;
    }

    public SyncData(Integer diamond) {
        this.diamond = diamond;
    }

    public Integer getDiamond() {
        return diamond;
    }

    public void setDiamond(Integer diamond) {
        this.diamond = diamond;
    }
}
