package com.mehdiii.duelgame.async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.utils.DeviceManager;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by mehdiii on 3/15/16.
 */
public class GetQuizQuestions extends AsyncTask<Void, Void, String> {

    Context mContext;
    String quizId;

    public GetQuizQuestions(Context mContext, String quizId) {
        this.mContext = mContext;
        this.quizId = quizId;

        Log.d("TAG", "GetQuizQuestions " + quizId);
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {

            HttpClient httpClient = new DefaultHttpClient();
            URI uri = new URI(DuelApp.BASE_URL + "/quizzes/" + quizId + "/questions?user_id=" +
                    DeviceManager.getDeviceId(mContext));

            Log.d("TAG", "uri111 " + uri.toString());

            HttpGet httpGet = new HttpGet(uri);
            HttpResponse response = httpClient.execute(httpGet);

            System.out.println("TAG json list GetQuizQuestions " + response.getStatusLine().getStatusCode());

            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
                return null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
            String json = reader.readLine();
            System.out.println("TAG json list GetQuizQuestions " + json);
            return json;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String json) {
        if(json == null){
            return;
        }

        Intent i = new Intent(DuelBroadcastReceiver.ACTION_NAME);
        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        i.putExtra(DuelBroadcastReceiver.BUNDLE_JSON_KEY, json);

        Log.d("TAG", "dispatchMessage");
        // use `local broadcast manager` instead of `global broadcast manager` to avoid unnecessary calls to other apps
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(i);
    }
}
