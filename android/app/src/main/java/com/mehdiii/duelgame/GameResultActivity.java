package com.mehdiii.duelgame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameResultActivity extends MyBaseActivity {

    protected class TitleBarListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("MESSAGE")) {
                String inputMessage = intent.getExtras().getString("inputMessage");
                String messageCode;
                JSONObject parser = null;

                try {
                    parser = new JSONObject(inputMessage);
                    messageCode = parser.getString("code");

                    if (messageCode.compareTo("XXX") == 0) {
                    }
                } catch (JSONException e) {
                    Log.d("---------", "can not parse string Game Result Activity");
                }
            }
        }
    }

    TitleBarListener mListener;

    int gameStatus;
    int savedTime;
    String gameVertict;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        mListener = new TitleBarListener();
        registerReceiver(mListener, new IntentFilter("MESSAGE"));

        try{
            JSONObject parser = new JSONObject(resultInfo);

            myElo = (int) parser.getDouble("new_elo");
            gameStatus = parser.getInt("result");       // just this class
            savedTime = parser.getInt("saved_time");    // just this class

            if(gameStatus == 0){
                myTime = myTime + savedTime;
                gameVertict = "مساوی شد";
            }
            else if(gameStatus == 1){
                myTime = myTime + savedTime + 120;
                gameVertict = "تو بردیییی";
            }
            else {
                // nothing
                gameVertict = "تو باختی";
            }
        }catch (JSONException e)
        {
            Log.d("---- Game Result Activity, loginInfo", e.toString());
        }

        setTextView(R.id.game_result_status, gameVertict);

        ((ImageView) findViewById(R.id.game_result_my_avatar)).setImageResource(avatarId[myAvatarIndex]);
        ((ImageView) findViewById(R.id.game_result_opponent_avatar)).setImageResource(avatarId[oppAvatarIndex]);

        setTextView(R.id.game_result_opponent_points, "" + oppPoints);
        setTextView(R.id.game_result_my_points, "" + myPoints);

        setTextView(R.id.game_result_new_elo, ""+myElo);
        setTextView(R.id.game_result_saved_time, "+"+savedTime);
        setTextView(R.id.game_result_new_score, "+"+myPoints);

        myScore += myPoints;
    }

    public void setTextView(int viewId, String s) {
        ((TextView) findViewById(viewId)).setText(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mListener);
    }
}
