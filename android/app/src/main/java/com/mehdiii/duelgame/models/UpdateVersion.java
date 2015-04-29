package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by omid on 4/28/2015.
 */
public class UpdateVersion {
    @SerializedName("version")
    private int version;
    @SerializedName("min_supported_version")
    private int minSupportedVersion;
    @SerializedName("change_set")
    private String changeset;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getMinSupportedVersion() {
        return minSupportedVersion;
    }

    public void setMinSupportedVersion(int minSupportedVersion) {
        this.minSupportedVersion = minSupportedVersion;
    }

    public String getChangeset() {
        return changeset;
    }

    public void setChangeset(String changeset) {
        this.changeset = changeset;
    }
}
