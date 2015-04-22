package com.mehdiii.duelgame.utils;

/**
 * Created by omid on 4/22/2015.
 */
/**
 ** Java Program to implement Bi Directional Map
 **/

import java.util.HashMap;

/**
 * class BiDrirectionalMap
 */
public class BidirectionalMap<T, K> {
    private HashMap<T, K> keyVal;
    private HashMap<K, T> valKey;

    /**
     * constructor *
     */
    public BidirectionalMap() {
        keyVal = new HashMap<>();
        valKey = new HashMap<>();
    }

    /**
     * function to clear maps *
     */
    public void clear() {
        keyVal.clear();
        valKey.clear();
    }

    /**
     * function to get size of maps *
     */
    public int size() {
        return keyVal.size();
    }

    /**
     * function to insert element *
     */
    public void put(T key, K val) {
        keyVal.put(key, val);
        valKey.put(val, key);
    }

    /**
     * function to get element *
     */
    public K get(T ele) {
        return keyVal.get(ele);
    }

    public T getKey(K value) {
        return valKey.get(value);
    }
}
