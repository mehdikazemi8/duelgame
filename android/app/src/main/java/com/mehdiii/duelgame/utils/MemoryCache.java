package com.mehdiii.duelgame.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by omid on 4/12/2015.
 */
public class MemoryCache {

    public static Map<String, Object> map = new HashMap<String, Object>();

    public static void set(String key, Object value) {
        map.put(key, value);
    }

    public static <T> T get(String key) {
        if (!map.containsKey(key))
            return null;
        else
            return (T) map.get(key);
    }

}
