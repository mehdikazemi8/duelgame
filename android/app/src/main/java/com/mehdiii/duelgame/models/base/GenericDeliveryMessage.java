package com.mehdiii.duelgame.models.base;

/**
 * Created by omid on 4/15/2015.
 */
public class GenericDeliveryMessage<T> {
    public String code;
    public T payload;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
