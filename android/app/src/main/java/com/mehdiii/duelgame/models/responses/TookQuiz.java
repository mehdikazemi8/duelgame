package com.mehdiii.duelgame.models.responses;

/**
 * Created by mehdiii on 1/17/16.
 */
public class TookQuiz {
    String id;

    public TookQuiz(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
