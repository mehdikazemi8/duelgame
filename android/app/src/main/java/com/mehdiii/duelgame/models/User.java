package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by omid on 4/8/2015.
 */
public class User extends BaseModel {

    public enum CommandType {
        REGISTER,
        LOGIN,
        GET_INFO,
        GET_FRIEND_LIST
    }

    public static User newInstance(CommandType type) {
        User user = new User();
        switch (type) {
            case REGISTER:
                user.setCommand("RU");
                break;
            case LOGIN:
                user.setCommand("UL");
                break;
            case GET_INFO:
                user.setCommand("LI");
                break;
            case GET_FRIEND_LIST:
                user.setCommand("GFL");
                break;
            default:
                user.setCommand("UNKNOWN");
        }

        return user;
    }

    public static CommandType getCommandType(String type) {

    }
    public CommandType getCommandType() {
        if (this.getCommand().equals("LI"))
            return CommandType.GET_INFO;
        else if (this.getCommand().equals("UL"))
            return CommandType.LOGIN;
        else if (this.getCommand().equals("GFL"))
            return CommandType.GET_FRIEND_LIST;
        else
            return CommandType.REGISTER;
    }

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
    @SerializedName("time")
    private int diamond;
    @SerializedName("user_number")
    private String id;

    public void getFriendsRequest() {
        setCommand(getCommandType());
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
