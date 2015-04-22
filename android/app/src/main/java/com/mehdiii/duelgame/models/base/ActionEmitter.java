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
        map.put(CommandType.GET_INFO, "LI");
        map.put(CommandType.LOGIN, "UL");
        map.put(CommandType.SEND_GET_FRIEND_LIST, "GFL");
        map.put(CommandType.REGISTER, "RU");
        map.put(CommandType.RECEIVE_GET_FRIEND_LIST, "RFL");
    }

    public String getCommandCode(CommandType type) {
        return map.get(type);
    }

    public CommandType getCommandType(String code) {
        return map.getKey(code);
    }
}
