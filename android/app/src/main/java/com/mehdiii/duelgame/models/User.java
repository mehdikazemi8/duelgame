package com.mehdiii.duelgame.models;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.managers.HeartTracker;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.List;
import java.util.Map;

public class User extends BaseModel {
    public User() {
        this.setAvatar(1);
    }

    @SerializedName("email")
    private String email;
    @SerializedName("user_id")
    private String deviceId;
    @SerializedName("name")
    private String name;
    @SerializedName("school")
    private String school;
    @SerializedName("major")
    private String major;
    @SerializedName("province")
    private int province;
    @SerializedName("avatar")
    private int avatar;
    @SerializedName("level")
    private int level;
    @SerializedName("diamond")
    private int diamond;
    @SerializedName("score")
    private int score;
    @SerializedName("game_score")
    private int duelScore;
    @SerializedName("heart")
    private int heart;
    @SerializedName("user_number")
    private String id;
    @SerializedName("purchase_items")
    List<PurchaseItem> purchaseItems;
    @SerializedName("extreme_heart")
    private boolean extremeHeart;
    @SerializedName("score_factor")
    private int scoreFactor;
    @SerializedName("gender")
    private int gender;
    @SerializedName("update_version")
    private UpdateVersion updateVersion;
    @SerializedName("time_to_next_heart")
    private int timeToNextHeart;
    @SerializedName("pending_offline_challenges")
    private Integer pendingOfflineChallenges;
    @SerializedName("weekly_ranks")
    private Map<String, Integer> weeklyRanks;
    @SerializedName("scores")
    private Map<String, CourseScore> scores;
    @SerializedName("progress")
    private List<StepProgress> stepProgress;
    @SerializedName("open_exams_not_taken")
    private Integer openExamNotTaken;
    @SerializedName("subscribed_for_exam")
    private boolean subscribedForExam;
    @SerializedName("free_exam_count")
    private int freeExamCount;
    @SerializedName("invited_by_me")
    private int invitedByMe;
    @SerializedName("subscription_price")
    private int subscriptionPrice;
    @SerializedName("motd")
    private String motd;

    public String getMotd() {
        return motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public int getSubscriptionPrice() {
        return subscriptionPrice;
    }

    public void setSubscriptionPrice(int subscriptionPrice) {
        this.subscriptionPrice = subscriptionPrice;
    }

    public int getInvitedByMe() {
        return invitedByMe;
    }

    public void setInvitedByMe(int invitedByMe) {
        this.invitedByMe = invitedByMe;
    }

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

    public Integer getOpenExamNotTaken() {
        return openExamNotTaken;
    }

    public void setOpenExamNotTaken(Integer openExamNotTaken) {
        this.openExamNotTaken = openExamNotTaken;
    }

    @SerializedName("referral_code")
    private String referralCode;

    public List<StepProgress> getStepProgress() {
        return stepProgress;
    }

    public void setStepProgress(List<StepProgress> stepProgress) {
        this.stepProgress = stepProgress;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public Map<String, Integer> getWeeklyRanks() {
        return weeklyRanks;
    }

    public void setWeeklyRanks(Map<String, Integer> weeklyRanks) {
        this.weeklyRanks = weeklyRanks;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major= major;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    //    public void addRank(String category, int addingRank){
//        category = "c" + category;
//        if(weeklyRanks.get(category) != null){  // update the existing object in 'scores'
//            weeklyRanks.get(category).setRank(addingRank);
//        }
//        else {    // create a new object in 'weekly rank'
//            weeklyRanks.put(category, new CourseRank(category, addingRank));
//        }
//    }

    public int getRank(String category)
    {
        category = "c" + category;
        int courseRank = weeklyRanks.get(category);
        if(courseRank == -1)
            return 0;
        return courseRank;
    }

//

    public Integer getPendingOfflineChallenges() {
        return pendingOfflineChallenges;
    }

    public void setPendingOfflineChallenges(Integer pendingOfflineChallenges) {
        this.pendingOfflineChallenges = pendingOfflineChallenges;
    }

    public void decreasePendingOfflineChallenges() {
        this.pendingOfflineChallenges --;
    }

    public int getDuelScore() {
        return duelScore;
    }

    public void setDuelScore(int duelScore) {
        this.duelScore = duelScore;
    }

    public void addScore(String category, int addingScore){
        category = "c" + category;
        if(scores.get(category) != null){  // update the existing object in 'scores'
            scores.get(category).addScore(addingScore);
        }
        else {    // create a new object in 'scores'
            scores.put(category, new CourseScore(addingScore, addingScore));
        }
    }

    public int getScore(String category, String period)
    {
        category = "c" + category;
        CourseScore courseScore = scores.get(category);
        if(courseScore == null)
            return 0;
        return courseScore.getScore(period);
    }

    public Map<String, CourseScore> getScores() {
        return scores;
    }

    public void setScores(Map<String, CourseScore> scores) {
        this.scores = scores;
    }

    public int getTimeToNextHeart() {
        return timeToNextHeart;
    }

    public void setTimeToNextHeart(int timeToNextHeart) {
        this.timeToNextHeart = timeToNextHeart;
    }

    public UpdateVersion getUpdateVersion() {
        return updateVersion;
    }

    public void setUpdateVersion(UpdateVersion updateVersion) {
        this.updateVersion = updateVersion;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public void addDiamond(int diamond) {
        setDiamond(getDiamond() + diamond);
    }

    public void decreaseDiamond(int diamond) {
        setDiamond(getDiamond() - diamond);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public Map<CommandType, String> getMap() {
//        return map;
//    }
//
//    public void setMap(Map<CommandType, String> map) {
//        this.map = map;
//    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public List<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(List<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }

    public PurchaseItem findPurchaseById(int id) {
        List<PurchaseItem> items = getPurchaseItems();

        for (PurchaseItem item : items)
            if (item.getId() == id)
                return item;
        return null;
    }

    public boolean isExtremeHeart() {
        return extremeHeart;
    }

    public void setExtremeHeart(boolean extremeHeart) {
        this.extremeHeart = extremeHeart;
    }

    public int getScoreFactor() {
        return scoreFactor;
    }

    public void setScoreFactor(int scoreFactor) {
        this.scoreFactor = scoreFactor;
    }

    public void changeConfiguration(Context context, int diamond, int heart, boolean extremeHeart, int scoreFactor) {
        setDiamond(diamond);
        setHeart(heart);
        setExtremeHeart(extremeHeart);
        setScoreFactor(scoreFactor);

        HeartTracker.configure(context, heart, isExtremeHeart());
    }

    public PurchaseItem getFlashcardPurchaseItem() {
        if (this.getPurchaseItems() == null || this.getPurchaseItems().size() == 0)
            return null;

        for (PurchaseItem item : this.getPurchaseItems()) {
            if (item.getId() == 9) {
                return item;
            }
        }
        return null;
    }
}
