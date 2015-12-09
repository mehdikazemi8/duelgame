package com.mehdiii.duelgame.models.base;

/**
 * Created by omid on 4/22/2015.
 */
public enum CommandType {
    UNKNOWN,
    GET_DUEL_HOUR_INFO,
    RECEIVE_DUEL_HOUR_INFO,
    GET_DUEL_HOUR_RANKING,
    RECEIVE_DUEL_HOUR_RANKING,
    GET_ONE_VS_ONE_RESULTS,
    RECEIVE_ONE_VS_ONE_RESULTS,
    REPORT_PROBLEM,
    SEND_GET_ONLINE_USERS,
    RECEIVE_GET_ONLINE_USERS,
    SEND_GET_PROVINCE_RANK,
    SEND_GET_FRIENDS_RANK,
    SEND_GET_TOTAL_RANK,
    SEND_GET_TOTAL_RANK_TODAY,
    RECEIVE_GET_PROVINCE_RANK,
    RECEIVE_GET_FRIENDS_RANK,
    RECEIVE_GET_TOTAL_RANK,
    RECEIVE_GET_TOTAL_RANK_TODAY,
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
    RECEIVE_UPDATE_SETTINGS,
    SEND_USER_LEFT_GAME,
    SEND_GET_QUESTION,
    SEND_WANNA_PLAY,
    SEND_USER_LOGIN_REQUEST,
    SEND_READY_TO_PLAY,
    SEND_WANNA_CHALLENGE,
    RECEIVE_CHALLENGE_REQUEST,
    SEND_ANSWER_OF_CHALLENGE_REQUEST,
    RECEIVE_CHALLENGE_REQUEST_DECISION,
    RECEIVE_OPPONENT_DATA,
    SEND_GCM_CODE,
    SEND_REMOVE_FRIEND,
    RECEIVE_OPPONENT_HAS_LEFT,
    GET_COURSE_RANKING,
    RECEIVE_COURSE_RANKING,
    RECEIVE_FLASH_CARD_LIST,
    SEND_GET_FLASH_CARD_LIST,
    SEND_GET_FLASH_CARD_REQUEST,
    RECEIVE_GET_FLASH_CARD_REQUEST,
    SEND_FLASH_CARD_PROGRESS
}
