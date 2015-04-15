package com.mehdiii.duelgame;

import com.mehdiii.duelgame.models.base.DeliveryMessage;

/**
 * Created by omid on 4/15/2015.
 */
public interface OnMessageDelivered {
    void onDeliver(DeliveryMessage message);
}
