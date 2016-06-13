package com.mehdiii.duelgame.utils;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.models.chat.Message;

/**
 * Created by mehdiii on 6/13/16.
 */
public class ChatHelper {
    public static long getLastMessageTimestamp () {
        long mx = -1;
        if(DuelApp.messageDao.loadAll().size() > 0) {
            for (Message message : DuelApp.messageDao.loadAll()) {
                if(message.getTimestamp() > mx) {
                    mx = message.getTimestamp();
                }
            }
        }
        return mx;
    }
}
