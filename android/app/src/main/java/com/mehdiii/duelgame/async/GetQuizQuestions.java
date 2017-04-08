package com.mehdiii.duelgame.async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.utils.DeviceManager;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetQuizQuestions extends AsyncTask<Void, Void, String> {

    private Context mContext;
    private String quizId;

    public GetQuizQuestions(Context mContext, String quizId) {
        this.mContext = mContext;
        this.quizId = quizId;

        Log.d("TAG", "GetQuizQuestions " + quizId);
    }

    @Override
    protected String doInBackground(Void... voids) {

        Call<ResponseBody> call = DuelApp.createChatApi().getQuizQuestions(quizId, DeviceManager.getDeviceId(mContext));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        Intent i = new Intent(DuelBroadcastReceiver.ACTION_NAME);
                        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                        i.putExtra(DuelBroadcastReceiver.BUNDLE_JSON_KEY, json);
                        Log.d("TAG", "dispatchMessage " + json);
                        // use `local broadcast manager` instead of `global broadcast manager` to avoid unnecessary calls to other apps
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        return null;
    }

    @Override
    protected void onPostExecute(String json) {
    }
}
