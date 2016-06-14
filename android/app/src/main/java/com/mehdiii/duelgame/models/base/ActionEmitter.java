package com.mehdiii.duelgame.models.base;

import com.mehdiii.duelgame.utils.BidirectionalMap;

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

        map.put(CommandType.SAVE_PHONE_NUMBER, "SPN");
        map.put(CommandType.RECEIVE_SAVE_PHONE_NUMBER, "RSPN");
        map.put(CommandType.VERIFY_PHONE_NUMBER, "VPN");
        map.put(CommandType.RECEIVE_VERIFY_PHONE_NUMBER, "RVPN");

        map.put(CommandType.YOU_HAVE_NEW_MESSAGE, "YHNM");

        map.put(CommandType.GET_SUBSCRIPTION_PURCHASE_PERMISSION, "GSPP");
        map.put(CommandType.RECEIVE_SUBSCRIPTION_PURCHASE_PERMISSION, "RSPP");
        map.put(CommandType.GET_SUBSCRIPTION_PURCHASE_CONFIRMATION, "GSPC");
        map.put(CommandType.RECEIVE_SUBSCRIPTION_PURCHASE_CONFIRMATION, "RSPC");

        map.put(CommandType.USE_DIAMOND, "UD");

        map.put(CommandType.LOG_COURSE_STEP_UP, "LCSU");
        map.put(CommandType.LOG_COURSE_CHAPTER_PASS, "LCCP");

        map.put(CommandType.GET_CHALLENGE_UPDATES, "GCU");
        map.put(CommandType.RECEIVE_CHALLENGE_UPDATES, "RCU");

        map.put(CommandType.GET_QUIZ_LIST_PAGE, "GQLP");
        map.put(CommandType.RECEIVE_QUIZ_LIST_PAGE, "RQLP");

        map.put(CommandType.GET_EXAM_PURCHASE_PERMISSION, "GQPP");
        map.put(CommandType.RECEIVE_EXAM_PURCHASE_PERMISSION, "RQPP");
        map.put(CommandType.GET_EXAM_PURCHASE_CONFIRMATION, "GQPC");
        map.put(CommandType.RECEIVE_EXAM_PURCHASE_CONFIRMATION, "RQPC");

        map.put(CommandType.RECEIVE_SYNC_DATA, "RSD");

        map.put(CommandType.WANNA_CANCEL_CHALLENGE, "WCC");

        map.put(CommandType.RECEIVE_ACCEPT_CHALLENGE, "RAC");
        map.put(CommandType.WANNA_ACCEPT_CHALLENGE, "WAC");
        map.put(CommandType.WANNA_REJECT_CHALLENGE, "WRC");

        map.put(CommandType.GET_CHALLENGE_LIST, "GCL");
        map.put(CommandType.RECEIVE_CHALLENGE_LIST, "RCL");

        map.put(CommandType.SUBMIT_DUEL_ANSWERS, "SCA");

        map.put(CommandType.WANNA_START_OFFLINE_DUEL, "WSC");
        map.put(CommandType.RECEIVE_START_OFFLINE_DUEL, "RSC");

        map.put(CommandType.GET_QUIZ_RESULTS, "GQR");
        map.put(CommandType.RECEIVE_QUIZ_RESULTS, "RQR");
        map.put(CommandType.SUBMIT_QUIZ_ANSWER, "SQA");
        map.put(CommandType.RECEIVED_SUBMIT_QUIZ_ANSWER, "RSQA");
        map.put(CommandType.GET_BUY_QUIZ, "GBQ");
        map.put(CommandType.RECEIVE_BUY_QUIZ, "RBQ");
        map.put(CommandType.GET_QUIZ_QUESTIONS, "GQQ");
        map.put(CommandType.RECEIVE_QUIZ_QUESTIONS, "RQQ");
        map.put(CommandType.GET_QUIZ_LIST, "GQL");
        map.put(CommandType.RECEIVE_QUIZ_LIST, "RQL");
        map.put(CommandType.GET_DUEL_HOUR_RANKING_TOTAL, "GDHRT");
        map.put(CommandType.RECEIVE_DUEL_HOUR_RANKING_TOTAL, "RDHRT");
        map.put(CommandType.GET_DUEL_HOUR_INFO, "GDHI");
        map.put(CommandType.RECEIVE_DUEL_HOUR_INFO, "RDHI");
        map.put(CommandType.GET_DUEL_HOUR_RANKING, "GDHR");
        map.put(CommandType.RECEIVE_DUEL_HOUR_RANKING, "RDHR");
        map.put(CommandType.GET_ONE_VS_ONE_RESULTS, "GOVOR");
        map.put(CommandType.RECEIVE_ONE_VS_ONE_RESULTS, "ROVOR");
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
        map.put(CommandType.GET_COURSE_RANKING, "GCR");
        map.put(CommandType.RECEIVE_COURSE_RANKING, "RCR");
        map.put(CommandType.SEND_GET_FLASH_CARD_LIST, "GFCL");
        map.put(CommandType.RECEIVE_FLASH_CARD_LIST, "RFCL");
        map.put(CommandType.SEND_GET_FLASH_CARD_REQUEST, "GDOFC");
        map.put(CommandType.RECEIVE_GET_FLASH_CARD_REQUEST, "RDOFC");
        map.put(CommandType.SEND_FLASH_CARD_PROGRESS, "UUFCS");
        map.put(CommandType.SEND_GET_ONLINE_USERS, "GOUL");
        map.put(CommandType.RECEIVE_GET_ONLINE_USERS, "ROUL");
        map.put(CommandType.REPORT_PROBLEM, "RP");
        map.put(CommandType.GET_COURSE_MAP, "GCM");
        map.put(CommandType.RECEIVE_COURSE_MAP, "RCM");

        map.put(CommandType.GET_COURSE_CHAPTER_QUESTION, "GCCQ");
        map.put(CommandType.RECEIVE_COURSE_CHAPTER_QUESTION, "RCCQ");

        map.put(CommandType.SEND_NEW_QUESTION, "SNQ");
        map.put(CommandType.RECEIVE_NEW_QUESTION, "RNQ");
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
