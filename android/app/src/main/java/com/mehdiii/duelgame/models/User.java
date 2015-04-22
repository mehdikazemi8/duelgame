package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by omid on 4/8/2015.
 */
public class User extends BaseModel {
//    Map<CommandType, String> map = new HashMap<>();
//
//    public enum CommandType {
//        REGISTER,
//        LOGIN,
//        GET_INFO,
//        SEND_GET_FRIEND_LIST,
//        ADD_FRIEND,
//    }

//    public static User newInstance(CommandType type) {
//        User user = new User();
//        switch (type) {
//            case REGISTER:
//                user.setCommand("RU");
//                break;
//            case LOGIN:
//                user.setCommand("UL");
//                break;
//            case GET_INFO:
//                user.setCommand("LI");
//                break;
//            case SEND_GET_FRIEND_LIST:
//                user.setCommand("GFL");
//                break;
//            case ADD_FRIEND:
//                user.setCommand("AF");
//                break;
//            default:
//                user.setCommand("UNKNOWN");
//        }
//
//        return user;
//    }
//
//    public static CommandType getCommandType(String type) {
//        if (type.equals("LI"))
//            return CommandType.GET_INFO;
//        else if (type.equals("UL"))
//            return CommandType.LOGIN;
//        else if (type.equals("GFL"))
//            return CommandType.SEND_GET_FRIEND_LIST;
//        else
//            return CommandType.REGISTER;
//    }
//
//    public CommandType getCommandType() {
//        return getCommandType(getCommand());
//    }

    @SerializedName("email")
    private String email;
    @SerializedName("user_id")
    private String deviceId;
    @SerializedName("name")
    private String name;
    @SerializedName("ostan")
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
//    @SerializedName("purchase_items")
//    private List<PurchaseItem> purchaseItems;

    public User getAddFriendRequest() {
        setCommand("AF");
        return this;
    }

    public User getFriendsRequest() {
        setCommand("GFL");
        return this;
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

//    public List<PurchaseItem> getPurchaseItems() {
//        return purchaseItems;
//    }
//
//    public void setPurchaseItems(List<PurchaseItem> purchaseItems) {
//        this.purchaseItems = purchaseItems;
//    }
}
