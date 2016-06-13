package com.mehdiii.duelgame.api;

import com.mehdiii.duelgame.models.chat.responses.GetMessagesResponse;
import com.mehdiii.duelgame.models.chat.responses.SendMessageResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mehdiii on 6/11/16.
 */
public interface ChatApi {
    @GET("/messages/public")
    Call<GetMessagesResponse> getMessages(@Query("user_id") String userId,
                                          @Query("timestamp") Long timestamp);

    @Headers("Content-Type: text/plain")
    @POST("/messages/public")
    Call<SendMessageResponse> sendMessage(@Query("user_id") String userId,
                                          @Body String messageText);
}
