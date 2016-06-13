package com.mehdiii.duelgame.models.chat.responses;

import com.mehdiii.duelgame.models.chat.Message;

import java.util.List;

/**
 * Created by mehdiii on 6/11/16.
 */
public class GetMessagesResponse {
    int offset;
    int limit;
    List<Message> messages;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
