package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.List;

/**
 * Created by mehdiii on 2/14/16.
 */
public class ChallengeUpdates extends BaseModel {
    @SerializedName("new_challenges")
    List<OfflineDuel> newChallenges;
    @SerializedName("challenge_results")
    List<OfflineDuel> challengeResults;

    public boolean isNewChallenge() {
        return newChallenges.size() != 0;
    }

    public boolean isChallengeResult() {
        return challengeResults.size() != 0;
    }

    public  OfflineDuel getFirstNewChallenge() {
        if(newChallenges.size() == 0)
            return null;
        return newChallenges.get(0);
    }

    public OfflineDuel getFirstChallengeResult() {
        if(challengeResults.size() == 0)
            return null;
        return challengeResults.get(0);
    }

    public List<OfflineDuel> getNewChallenges() {
        return newChallenges;
    }

    public void setNewChallenges(List<OfflineDuel> newChallenges) {
        this.newChallenges = newChallenges;
    }

    public List<OfflineDuel> getChallengeResults() {
        return challengeResults;
    }

    public void setChallengeResults(List<OfflineDuel> challengeResults) {
        this.challengeResults = challengeResults;
    }

    public ChallengeUpdates() {
    }

    public ChallengeUpdates(List<OfflineDuel> newChallenges, List<OfflineDuel> challengeResults) {
        this.newChallenges = newChallenges;
        this.challengeResults = challengeResults;
    }
}
