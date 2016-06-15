package com.mehdiii.duelgame.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.models.chat.Message;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mehdiii on 6/15/16.
 */
public class CleanDB extends AsyncTask<Void, Void, Void> {
    private final int MAX_MESSAGES_SIZE = 400;

    @Override
    protected Void doInBackground(Void... params) {
        List<Message> messages = DuelApp.getInstance().messageDao.loadAll();
        Log.d("TAG", "CleanDB aa " + messages.size());

        if(messages.size() > MAX_MESSAGES_SIZE) {
            int needToDelete = messages.size() - MAX_MESSAGES_SIZE;

            Collections.sort(messages, new Comparator<Message>() {
                @Override
                public int compare(Message x, Message y) {
                    if(!x.getTimestamp().equals( y.getTimestamp() )) {
                        return x.getTimestamp() < y.getTimestamp() ? -1 : +1;
                    }
                    return x.getSender().compareTo(y.getSender());
                }
            });

            messages = messages.subList(0, needToDelete);
            DuelApp.getInstance().messageDao.deleteInTx(messages);
            Log.d("TAG", "CleanDB bb " + messages.size());

            List<Message> after = DuelApp.getInstance().messageDao.loadAll();
            Log.d("TAG", "CleanDB cc " + after.size());
        }
        return null;
    }
}
