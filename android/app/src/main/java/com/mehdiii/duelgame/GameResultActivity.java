package com.mehdiii.duelgame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    int WIN, LOSE;
    MediaPlayer myPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        WIN = R.raw.win;
        LOSE = R.raw.lose;

        mListener = new TitleBarListener();
        registerReceiver(mListener, new IntentFilter("MESSAGE"));

        try {
            JSONObject parser = new JSONObject(resultInfo);

            myElo = (int) parser.getDouble("new_elo");
            gameStatus = parser.getInt("result");       // just this class
            savedTime = parser.getInt("saved_time");    // just this class

            Log.d("-- score ye nafar ", myName + " " + parser.getInt("score"));

            if (gameStatus == 0) {
                myTime = myTime + savedTime;
                gameVertict = "مساوی شد";
                myPlayer = MediaPlayer.create(this, LOSE);
            } else if (gameStatus == 1) {
                myTime = myTime + savedTime + 120;
                gameVertict = "تو بردیییی";
                myPlayer = MediaPlayer.create(this, WIN);
            } else {
                // nothing
                gameVertict = "تو باختی";
                myPlayer = MediaPlayer.create(this, LOSE);
            }
        } catch (JSONException e) {
            Log.d("---- Game Result Activity, loginInfo", e.toString());
        }

        myPlayer.start();

        setTextView(R.id.game_result_status, gameVertict);

        ((ImageView) findViewById(R.id.game_result_my_avatar)).setImageResource(avatarId[myAvatarIndex]);
        ((ImageView) findViewById(R.id.game_result_opponent_avatar)).setImageResource(avatarId[oppAvatarIndex]);

        setTextView(R.id.game_result_opponent_points, "" + oppPoints);
        setTextView(R.id.game_result_my_points, "" + myPoints);

        setTextView(R.id.game_result_new_elo, "" + myElo);
        setTextView(R.id.game_result_saved_time, "+" + savedTime);
        setTextView(R.id.game_result_new_score, "+" + myPoints);

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
        if (id == R.id.about) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addToFriends(View v){

        boolean everythingOK = true;

        JSONObject query = new JSONObject();
        try {
            query.put("code", "AF");
            query.put("user_number", oppUserNumber);

            Log.d("-- send ADD FRIEND", query.toString());

            wsc.sendTextMessage(query.toString());
        } catch (JSONException e) {
            everythingOK = false;
            Log.d("---- GameResult JSON", e.toString());
        }

        if(everythingOK == true)
        {
            String msg ="به لیست دوستان شما اضافه شد." ;
            Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 10);
            toast.show();
        }
    }

    public void duelWithOthers(View v) {
        JSONObject query = new JSONObject();
        try {
            query.put("code", "WP");
            query.put("category", category);

            Log.d("-- send WP", query.toString());

            wsc.sendTextMessage(query.toString());
        } catch (JSONException e) {
            Log.d("---- GameResult JSON", e.toString());
        }

        startActivity(new Intent(this, WaitingActivity.class));
        this.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mListener);
    }
}
