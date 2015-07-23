package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by Omid on 7/22/2015.
 */
public class Card extends BaseModel /*implements Comparable<Card> */{
    @SerializedName("index")
    private int index;
    @SerializedName("weight")
    private int weight;
    @SerializedName("front")
    private String front;
    @SerializedName("back")
    private String back;

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void increaseWeight() {
        setWeight(getWeight() + 1);
    }

    public void resetWeight() {
        setWeight(0);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    //
//    @Override
//    public int compareTo(Card card) {
//        return Integer.compare(getWeight(), card.getWeight());
////        if (getWeight() != card.getWeight())
////            return Integer.compare(getWeight(), card.getWeight());
////        return front.compareTo(card.getFront());
//    }

}
