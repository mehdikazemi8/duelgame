package com.mehdiii.duelgame.models.base;

import com.mehdiii.duelgame.utils.BidirectionalMap;

/**
 * Created by omid on 4/22/2015.
 */
public class ActionEmitter {
    BidirectionalMap<CommandType, String> map = new BidirectionalMap<>();
    private boolean initiated = false;d
    private void init() {
        map.put(CommandType.GET_INFO, "LI");
        map.put(CommandType.LOGIN, "UL");
        map.put(CommandType.GET_FRIEND_LIST, "GFL");
        map.put(CommandType.REGISTER, "RU");

        //        if (type.equals("LI"))
//            return CommandType.GET_INFO;
//        else if (type.equals("UL"))
//            return CommandType.LOGIN;
//        else if (type.equals("GFL"))
//            return CommandType.GET_FRIEND_LIST;
//        else
//            return CommandType.REGISTER;
    }

}
