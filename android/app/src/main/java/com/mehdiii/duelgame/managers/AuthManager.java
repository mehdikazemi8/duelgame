package com.mehdiii.duelgame.managers;

import android.content.Context;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.utils.MemoryCache;

/**
 * Created by omid on 4/12/2015.
 */
public class AuthManager {
    public static final String CACHE_TAG = "cache_auth_user";

    public static User getCurrentUser() {
        return MemoryCache.get(CACHE_TAG);
    }

    public static boolean isLoggedin() {
        return getCurrentUser() != null;
    }

    public static User authenticate(Context context, User user) {
        MemoryCache.set(CACHE_TAG, user);
        HeartTracker.configure(context, user.getHeart(), user.isExtremeHeart(), user.getTimeToNextHeart());
        return user;
    }
}
