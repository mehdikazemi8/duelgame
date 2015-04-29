package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by omid on 4/28/2015.
 */
public class DeliveryReport extends BaseModel {
    public enum DeliveryReportType {
        SUCCESSFUL,
        FAILED,
        UNKNOWN
    }

    public DeliveryReportType getStatusType() {
        if (getStatus().equals("complete")) {
            return DeliveryReportType.SUCCESSFUL;
        } else if (getStatus().equals("fail"))
            return DeliveryReportType.FAILED;
        else return DeliveryReportType.UNKNOWN;
    }

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
