package com.mehdiii.duelgame.models.base;

/**
 * Created by omid on 4/22/2015.
 */
public enum CommandType {
    UNKNOWN,
    SEND_REGISTER,
    RECEIVE_LOGIN_INFO,
    SEND_GET_FRIEND_LIST,
    RECEIVE_GET_FRIEND_LIST,
    RECEIVE_ASK_NEXT_QUESTION,
    RECEIVE_OPPONENT_SCORE,
    RECEIVE_GAME_ENDED,
    SEND_START_PURCHASE,
    SEND_PURCHASE_DIAMOND,
    RECEIVE_START_PURCHASE,
    SEND_PURCHASE_DONE,
    RECEIVE_PURCHASE_DONE,
    RECEIVE_OPPONENT_INFO,
    RECEIVE_GAME_DATA,
    RECEIVE_START_PLAYING,
    RECEIVE_FRIEND_LIST,
    SEND_ADD_FRIEND,
    RECEIVE_ADD_FRIEND,
    SEND_FRIEND_REQUEST_RESPONSE,
    SEND_UPDATE_SETTINGS,
    RECEIVE_UPDATE_SETTINGS
}
