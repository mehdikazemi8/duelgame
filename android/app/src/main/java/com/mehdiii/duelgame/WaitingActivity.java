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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WaitingActivity extends MyBaseActivity {

    boolean hasLeft;

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
                    if (messageCode.compareTo("YOI") == 0) {

                        String allOpponentsStr = parser.getString("opponents");
                        Log.d("---- allOpponentsStr", allOpponentsStr);
                        JSONArray allOpponents = new JSONArray(allOpponentsStr);
                        JSONObject firstOpponent = new JSONObject(allOpponents.get(0).toString());

                        Log.d("------ firstOpponent", firstOpponent.toString());

                        oppName = firstOpponent.getString("name");
                        oppAvatarIndex = firstOpponent.getInt("avatar");

                        int oppOstanInt = firstOpponent.getInt("ostan");
                        int oppElo = (int) firstOpponent.getDouble("elo");
                        oppUserNumber = firstOpponent.getString("user_number");

                        ((ImageView) findViewById(R.id.waiting_opponent_avatar)).setImageResource(avatarId[oppAvatarIndex]);
                        setTextView(R.id.waiting_opponent_name, oppName);
                        setTextView(R.id.waiting_opponent_ostan, getOstanStr(oppOstanInt));
                        setTextView(R.id.waiting_opponent_elo, "" + oppElo);

                    } else if (messageCode.compareTo("SP") == 0) {
                        myTime -= 120;

//                        android.os.Handler waitingHandler = new android.os.Handler();
//                        waitingHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.d("-- runnable ", "start play Game");
//                                startActivity(new Intent(getApplicationContext(), PlayGameActivity.class));
//                                finish();
//                            }
//                        }, 2000);
                        startActivity(new Intent(getApplicationContext(), PlayGameActivity.class));
                        finish();
                    } else if (messageCode.compareTo("RGD") == 0) {

                        for (int problemIndex = 0; problemIndex < NUMBER_OF_QUESTIONS; problemIndex++) {
                            questionsToAsk[problemIndex] = new Question();

                            Log.d("parser.getString()", parser.toString());

                            JSONObject thisQuestion = new JSONObject(parser.getString("problem" + problemIndex));
                            questionsToAsk[problemIndex].questionText = thisQuestion.getString("question_text");
                            JSONArray options = thisQuestion.getJSONArray("options");
                            for (int op = 0; op < 4; op++) {
                                questionsToAsk[problemIndex].options[op] = "" + options.get(op);
                            }
                        }

                        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
                        ScheduledFuture scheduledFuture = scheduledExecutorService.schedule(new Runnable() {
                            @Override
                            public void run() {
                                if (hasLeft == true)
                                    return;

                                JSONObject query = new JSONObject();
                                try {
                                    query.put("code", "RTP");
                                    wsc.sendTextMessage(query.toString());
                                } catch (JSONException e) {
                                    Log.d("---- StartActivity JSON", e.toString());
                                }

                                Log.d("-- runnable ", "RTP SEND runnable");
                            }
                        }, 4, TimeUnit.SECONDS);

                        Log.d("khoonde shod", "ddd");
                    }
                } catch (JSONException e) {
                    Log.d("---------", "can not parse string Waiting Activity");
                }
            }
        }
    }

    TitleBarListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        hasLeft = false;

        mListener = new TitleBarListener();
        registerReceiver(mListener, new IntentFilter("MESSAGE"));

        ((ImageView) findViewById(R.id.waiting_my_avatar)).setImageResource(avatarId[myAvatarIndex]);
        setTextView(R.id.waiting_my_name, myName);
        setTextView(R.id.waiting_my_ostan, getOstanStr(myOstanInt));
        setTextView(R.id.waiting_my_elo, "" + myElo);
    }

    public void setTextView(int id, String str) {
        ((TextView) findViewById(id)).setText(str);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mListener);
    }

    @Override
    public void onBackPressed() {
        Log.d(" --- ", myName + " pressed onBack Waiting");

        hasLeft = true;

        JSONObject query = new JSONObject();
        try {
            query.put("code", "ULG");

            wsc.sendTextMessage(query.toString());
        } catch (JSONException e) {
            Log.d("---- GQ GQ GQ", e.toString());
        }

        finish();
    }
}
