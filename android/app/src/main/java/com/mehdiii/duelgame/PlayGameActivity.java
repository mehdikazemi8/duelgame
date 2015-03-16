package com.mehdiii.duelgame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayGameActivity extends MyBaseActivity {

    final int DURATION = 20000;
    long remainingTimeOfThisQuestion;
    CountDownTimer timeToAnswer = null;

    String correctAnswer;
    int iAnsweredThisTime;
    boolean iAnsweredThisCorrect;
    int opponentAnsweredThisTime;
    int problemIndex;

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
                    if (messageCode.compareTo("AQ") == 0) {
                        askQuestion();
                    } else if (messageCode.compareTo("OS") == 0) {
                        if (parser.getInt("ok") == 1) {
                            opponentAnsweredThisTime = parser.getInt("time");

                            if (opponentAnsweredThisTime >= 10) {
                                if (problemIndex == 6)
                                    oppPoints += 5;
                                else
                                    oppPoints += 3;
                            } else {
                                oppPoints += 1;
                            }
                        } else {
                            if (parser.getInt("time") != -1) {  // wrong answer
                                //oppPoints += -1;
                            } else if (parser.getInt("time") == -1) {   // not answered

                            }
                        }
                        setTextView(R.id.op_score, "" + oppPoints);
                    } else if (messageCode.compareTo("GE") == 0) {

                        resultInfo = inputMessage;
                        startActivity(new Intent(getApplicationContext(), GameResultActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    Log.d("---------", "can not parse string");
                }
            }
        }
    }

    public void askQuestion() {
        if (timeToAnswer != null)
            timeToAnswer.cancel();

        ((Button) findViewById(R.id.option_0)).setClickable(true);
        ((Button) findViewById(R.id.option_1)).setClickable(true);
        ((Button) findViewById(R.id.option_2)).setClickable(true);
        ((Button) findViewById(R.id.option_3)).setClickable(true);

        ((Button) findViewById(R.id.option_0)).setBackgroundResource(android.R.drawable.btn_default);
        ((Button) findViewById(R.id.option_1)).setBackgroundResource(android.R.drawable.btn_default);
        ((Button) findViewById(R.id.option_2)).setBackgroundResource(android.R.drawable.btn_default);
        ((Button) findViewById(R.id.option_3)).setBackgroundResource(android.R.drawable.btn_default);

        iAnsweredThisCorrect = false;
        iAnsweredThisTime = -1;
        opponentAnsweredThisTime = -1;
        remainingTimeOfThisQuestion = 20;

        String questionText = questionsToAsk[problemIndex].questionText;
        setTextView(R.id.question, questionText);

        String[] opts = questionsToAsk[problemIndex].options;
        problemIndex++;
        correctAnswer = opts[0];

        shuffleArray(opts);
        shuffleArray(opts);

        setButton(R.id.option_0, opts[0]);
        setButton(R.id.option_1, opts[1]);
        setButton(R.id.option_2, opts[2]);
        setButton(R.id.option_3, opts[3]);

        timeToAnswer = new CountDownTimer(DURATION, 1000) {
            @Override
            public void onTick(long arg0) {
                remainingTimeOfThisQuestion = arg0 / 1000;
                setTextView(R.id.remaining_time, "" + (arg0 / 1000));

                Log.d("--- ", myName + " - " + remainingTimeOfThisQuestion);
            }

            @Override
            public void onFinish() {
                setTextView(R.id.remaining_time, "0");

                if (iAnsweredThisCorrect == true)
                    return;

                JSONObject query = new JSONObject();
                try {
                    query.put("code", "GQ");
                    query.put("time", -1);
                    query.put("ok", 0);

                    wsc.sendTextMessage(query.toString());
                } catch (JSONException e) {
                    Log.d("---- GQ GQ GQ", e.toString());
                }
            }
        };

        timeToAnswer.start();
    }

    public void answered(View v) {
        if (iAnsweredThisTime != -1) {
            return;
        }

        iAnsweredThisTime = (int) remainingTimeOfThisQuestion;

        doDisableButtons();

        int ok = 0;
        if (correctAnswer.compareTo(((Button) v).getText().toString()) == 0) {

            iAnsweredThisCorrect = true;
            if (iAnsweredThisTime >= 10) {
                if (problemIndex == 6)
                    myPoints += 5;
                else
                    myPoints += 3;
            } else {
                myPoints += 1;
            }
            setTextView(R.id.my_score, "" + myPoints);
            ((Button) v).setBackgroundColor(Color.GREEN);
            ok = 1;
        } else {
            //myPoints += -1;
            //setTextView(R.id.my_score, "" + myPoints);

            ((Button) v).setBackgroundColor(Color.RED);
        }

        JSONObject query = new JSONObject();
        try {
            query.put("code", "GQ");
            query.put("time", remainingTimeOfThisQuestion);
            query.put("ok", ok);

            wsc.sendTextMessage(query.toString());
        } catch (JSONException e) {
            Log.d("---- GQ GQ GQ", e.toString());
        }
    }

    TitleBarListener mListener;

    public void setButton(int viewId, String s) {
        Button tv = (Button) findViewById(viewId);
        tv.setText(s);
    }

    public void setTextView(int viewId, String s) {
        TextView tv = (TextView) findViewById(viewId);
        tv.setText(s);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        mListener = new TitleBarListener();
        registerReceiver(mListener, new IntentFilter("MESSAGE"));

        setTextView(R.id.my_name, myName);
        setTextView(R.id.op_name, oppName);

        oppPoints = myPoints = 0;
        problemIndex = 0;

        askQuestion();
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

    public void doDisableButtons() {
        ((Button) findViewById(R.id.option_0)).setClickable(false);
        ((Button) findViewById(R.id.option_1)).setClickable(false);
        ((Button) findViewById(R.id.option_2)).setClickable(false);
        ((Button) findViewById(R.id.option_3)).setClickable(false);
    }

    @Override
    public void onBackPressed() {
        Log.d(" --- ", myName + " pressed onBack");

        JSONObject query = new JSONObject();
        try {
            query.put("code", "ULG");

            wsc.sendTextMessage(query.toString());
        } catch (JSONException e) {
            Log.d("---- GQ GQ GQ", e.toString());
        }

        timeToAnswer.cancel();
        finish();
    }
}
