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
        map.put(CommandType.SEND_UPDATE_SETTINGS, "UU");
        map.put(CommandType.RECEIVE_UPDATE_SETTINGS, "UUS");
        map.put(CommandType.RECEIVE_ASK_NEXT_QUESTION, "AQ");
        map.put(CommandType.RECEIVE_OPPONENT_SCORE, "OS");
        map.put(CommandType.RECEIVE_GAME_ENDED, "GE");
        map.put(CommandType.SEND_START_PURCHASE, "PS");
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
        map.put(CommandType.SEND_GET_FRIENDS_RANK, "GFR");
        map.put(CommandType.SEND_GET_PROVINCE_RANK, "GPR");
        map.put(CommandType.SEND_GET_TOTAL_RANK, "GTR");
        map.put(CommandType.SEND_GET_TOTAL_RANK_TODAY, "GTRT");
        map.put(CommandType.RECEIVE_GET_TOTAL_RANK_TODAY, "RTRT");
        map.put(CommandType.RECEIVE_GET_FRIENDS_RANK, "RFR");
        map.put(CommandType.RECEIVE_GET_PROVINCE_RANK, "RPR");
        map.put(CommandType.RECEIVE_GET_TOTAL_RANK, "RTR");
        map.put(CommandType.SEND_USER_LEFT_GAME, "ULG");
        map.put(CommandType.SEND_GET_QUESTION, "GQ");
        map.put(CommandType.SEND_WANNA_PLAY, "WP");
        map.put(CommandType.SEND_USER_LOGIN_REQUEST, "UL");
        map.put(CommandType.SEND_READY_TO_PLAY, "RTP");
        map.put(CommandType.RECEIVE_OPPONENT_DATA, "YOI");
        map.put(CommandType.SEND_WANNA_CHALLENGE, "WC");
        map.put(CommandType.RECEIVE_CHALLENGE_REQUEST, "CR");
        map.put(CommandType.SEND_ANSWER_OF_CHALLENGE_REQUEST, "ACR");
        map.put(CommandType.RECEIVE_CHALLENGE_REQUEST_DECISION, "CRD");
        map.put(CommandType.SEND_GCM_CODE, "SGI");
        map.put(CommandType.SEND_REMOVE_FRIEND, "RMF");
        map.put(CommandType.RECEIVE_OPPONENT_HAS_LEFT, "OHL");
        map.put(CommandType.SEND_GET_FLASH_CARD_LIST, "GFCL");
        map.put(CommandType.RECEIVE_FLASH_CARD_LIST, "RFCL");
    }

    public String getCommandCode(CommandType type) {
        return map.get(type);
    }

    public CommandType getCommandType(String code) {
        CommandType ret = map.getKey(code);

        if (ret == null)
            return CommandType.UNKNOWN;
        else return ret;
    }
}
