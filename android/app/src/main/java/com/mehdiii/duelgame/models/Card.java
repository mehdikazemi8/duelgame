package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

public class Card extends BaseModel /*implements Comparable<Card> */ {


    public Card(boolean disabled, int index, int weight, String front, String back) {
        this.disabled = disabled;
        this.index = index;
        this.weight = weight;
        this.front = front;
        this.back = back;
    }


    private boolean disabled;
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

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean enabled) {
        this.disabled = enabled;
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
