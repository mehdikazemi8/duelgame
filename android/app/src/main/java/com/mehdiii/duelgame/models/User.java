package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.List;

/**
 * Created by omid on 4/8/2015.
 */
public class User extends BaseModel {

    @SerializedName("email")
    private String email;
    @SerializedName("user_id")
    private String deviceId;
    @SerializedName("name")
    private String name;
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
    @SerializedName("rank")
    private Rank rank;

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
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

    public void changeConfiguration(int diamond, int heart, boolean extremeHeart, int scoreFactor) {
        setDiamond(diamond);
        setHeart(heart);
        setExtremeHeart(extremeHeart);
        setScoreFactor(scoreFactor);
    }
}
