package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.Map;

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
    @SerializedName("weekly_ranks")
    private Map<String, Integer> weeklyRanks;
    @SerializedName("scores")
    private Map<String, CourseScore> scores;

    @SerializedName("free_exam_count")
    private int freeExamCount;
    @SerializedName("subscribed_for_exam")
    private boolean subscribedForExam;
    @SerializedName("invited_by_me")
    private int invitedByMe;

    public int getFreeExamCount() {
        return freeExamCount;
    }

    public void setFreeExamCount(int freeExamCount) {
        this.freeExamCount = freeExamCount;
    }

    public boolean isSubscribedForExam() {
        return subscribedForExam;
    }

    public void setSubscribedForExam(boolean subscribedForExam) {
        this.subscribedForExam = subscribedForExam;
    }

    public int getInvitedByMe() {
        return invitedByMe;
    }

    public void setInvitedByMe(int invitedByMe) {
        this.invitedByMe = invitedByMe;
    }

    public Map<String, CourseScore> getScores() {
        return scores;
    }

    public void setScores(Map<String, CourseScore> scores) {
        this.scores = scores;
    }

    public Map<String, Integer> getWeeklyRanks() {

        return weeklyRanks;
    }

    public void setWeeklyRanks(Map<String, Integer> weeklyRanks) {
        this.weeklyRanks = weeklyRanks;
    }

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
