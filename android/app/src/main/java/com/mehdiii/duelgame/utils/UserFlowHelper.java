package com.mehdiii.duelgame.utils;

import com.mehdiii.duelgame.managers.AuthManager;

/**
 * Created by mehdiii on 4/20/16.
 */
public class UserFlowHelper {

    public static boolean gotDuel() {
        return AuthManager.getCurrentUser().getScores().size() != 0;
    }

    public static boolean gotQuiz() {
        return AuthManager.getCurrentUser().getQuizTaken() != 0;
    }
}
