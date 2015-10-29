package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

/**
 * Created by mehdiii on 10/29/15.
 */
public class ReportProblem extends BaseModel {
    @SerializedName("description")
    String description;
    @SerializedName("category")
    int category;

    public ReportProblem(CommandType commandType) {
        super(commandType);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
