package com.mehdiii.duelgame.models.base;

import com.mehdiii.duelgame.utils.BidirectionalMap;

/**
 * Created by omid on 4/22/2015.
 */
public class ActionEmitter {
    static ActionEmitter instance = null;

    public static ActionEmitter getInstance() {
        if (instance == null)
            instance = new ActionEmitter();
        return instance;
    }

    public ActionEmitter() {
        init();
    }

    private BidirectionalMap<CommandType, String> map = new BidirectionalMap<>();

    private void init() {
        map.put(CommandType.RECEIVE_LOGIN_INFO, "LI");
        map.put(CommandType.SEND_GET_FRIEND_LIST, "GFL");
        map.put(CommandType.SEND_REGISTER, "RU");
        map.put(CommandType.RECEIVE_GET_FRIEND_LIST, "RFL");
        map.put(CommandType.RECEIVE_ASK_NEXT_QUESTION, "AQ");
        map.put(CommandType.RECEIVE_OPPONENT_SCORE, "OS");
        map.put(CommandType.RECEIVE_GAME_ENDED, "GE");
        map.put(CommandType.SEND_START_PURCHASE, "PS");
        map.put(CommandType.SEND_PURCHASE_DIAMOND, "PD");
        map.put(CommandType.RECEIVE_START_PURCHASE, "RPI");
        map.put(CommandType.SEND_PURCHASE_DONE, "PD");
        map.put(CommandType.RECEIVE_PURCHASE_DONE, "RPS");
        map.put(CommandType.RECEIVE_OPPONENT_INFO, "YOI");
        map.put(CommandType.RECEIVE_GAME_DATA, "RGD");
        map.put(CommandType.RECEIVE_START_PLAYING, "SP");
        map.put(CommandType.RECEIVE_FRIEND_LIST, "GFL");
        map.put(CommandType.SEND_ADD_FRIEND, "AF");
        map.put(CommandType.RECEIVE_ADD_FRIEND, "AFS");
        map.put(CommandType.SEND_FRIEND_REQUEST_RESPONSE, "AFR");
    }

    public String getCommandCode(CommandType type) {
        return map.get(type);
    }

    public CommandType getCommandType(String code) {
        return map.getKey(code);
    }
}
