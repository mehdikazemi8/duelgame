package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

/**
 * Created by mehdiii on 12/9/15.
 */
public class DuelHourInfo extends BaseModel {
    @SerializedName("ok")
    boolean ok;
    @SerializedName("is_duel_hour")
    boolean isDuelHour;
    @SerializedName("message")
    String message;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public boolean isDuelHour() {
        return isDuelHour;
    }

    public void setIsDuelHour(boolean isDuelHour) {
        this.isDuelHour = isDuelHour;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DuelHourInfo(CommandType command, boolean ok, boolean isDuelHour, String message) {
        super(command);
        this.ok = ok;
        this.isDuelHour = isDuelHour;
        this.message = message;
    }

    public DuelHourInfo(boolean ok, boolean isDuelHour, String message) {
        this.ok = ok;
        this.isDuelHour = isDuelHour;
        this.message = message;
    }
}
