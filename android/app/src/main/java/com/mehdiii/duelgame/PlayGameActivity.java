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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class PlayGameActivity extends MyBaseActivity {

    final int DURATION = 20000;
    long remainingTimeOfThisQuestion;
    CountDownTimer timeToAnswer = null;

    String rightAnswer;
    boolean answeredThis;
    int whenDoIDisable;

    int problemIndex;

    Color ccc;

    public void callCancel() {
        timeToAnswer.onFinish();
    }

    protected class TitleBarListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("%%%%%%%%%%%", "salam, onReceive Play Game");

            if (intent.getAction().equals("MESSAGE")) {
                Log.d("-------", "tooye if MESSAGE");

                String inputMessage = intent.getExtras().getString("inputMessage");
                String messageCode;
                JSONObject parser = null;

                try {
                    parser = new JSONObject(inputMessage);
                    messageCode = parser.getString("code");
                    if (messageCode.compareTo("AQ") == 0) {

                        if (timeToAnswer != null)
                            timeToAnswer.cancel();

                        answeredThis = false;
                        remainingTimeOfThisQuestion = 20;

                        ((Button) findViewById(R.id.option_0)).setClickable(true);
                        ((Button) findViewById(R.id.option_1)).setClickable(true);
                        ((Button) findViewById(R.id.option_2)).setClickable(true);
                        ((Button) findViewById(R.id.option_3)).setClickable(true);

                        ((Button) findViewById(R.id.option_0)).setBackgroundResource(android.R.drawable.btn_default);
                        ((Button) findViewById(R.id.option_1)).setBackgroundResource(android.R.drawable.btn_default);
                        ((Button) findViewById(R.id.option_2)).setBackgroundResource(android.R.drawable.btn_default);
                        ((Button) findViewById(R.id.option_3)).setBackgroundResource(android.R.drawable.btn_default);


                        String questionText = questionsToAsk[problemIndex].questionText;

                        setTextView(R.id.question, questionText);

                        String[] opts = questionsToAsk[problemIndex].options;
                        problemIndex++;

                        rightAnswer = opts[0];

                        shuffleArray(opts);
                        shuffleArray(opts);

                        setButton(R.id.option_0, opts[0]);
                        setButton(R.id.option_1, opts[1]);
                        setButton(R.id.option_2, opts[2]);
                        setButton(R.id.option_3, opts[3]);

                        timeToAnswer = new CountDownTimer(DURATION, 1000) {
                            @Override
                            public void onTick(long arg0) {
                                TextView remainingTime = (TextView) findViewById(R.id.remaining_time);
                                remainingTimeOfThisQuestion = arg0 / 1000;

                                Log.d("------ on tick ", "" + remainingTimeOfThisQuestion + " " + whenDoIDisable);
                                if (remainingTimeOfThisQuestion <= whenDoIDisable) {
                                    Log.d("--- finished", "aaa");
                                    doDisableButtons();
//                                    this.onFinish();
//                                    timeToAnswer = null;
//                                    callCancel();

//                                    this.cancel();


//                                    JSONObject query = new JSONObject();
//                                    try {
//                                        query.put("code", "GQ");
//                                        query.put("time", -1);
//                                        query.put("ok", 0);
//
//                                        wsc.send(query.toString());
//                                    } catch (JSONException e) {
//                                        Log.d("---- GQ GQ GQ", e.toString());
//                                    }
                                    return;
                                }
                                remainingTime.setText("" + (arg0 / 1000));
                            }

                            @Override
                            public void onFinish() {
                                TextView remainingTime = (TextView) findViewById(R.id.remaining_time);
                                remainingTime.setText("0");

                                if (answeredThis == true) {
                                    return;
                                }

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
                        whenDoIDisable = 0;

                    } else if (messageCode.compareTo("OS") == 0) {
                        TextView opScore = (TextView) findViewById(R.id.op_score);

                        Log.d("-----", "here");

                        if (parser.getInt("ok") == 1) {
                            whenDoIDisable = parser.getInt("time") - 1;
                            Log.d("#### remaining ", "" + remainingTimeOfThisQuestion);
                            Log.d("#### time to disable ", "" + whenDoIDisable);

                            // ****************************** delete this
                            timeToAnswer.cancel();
                            doDisableButtons();
                            if (answeredThis == false) {
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

                            if (problemIndex == 6)
                                oppPoints += 5;
                            else
                                oppPoints += 3;
                        } else {
                            if (parser.getInt("time") != -1) {
                                oppPoints += -1;
                            }
                        }

                        opScore.setText("" + oppPoints);

                        Log.d("sssssssssssss", inputMessage.toString());
                    } else if (messageCode.compareTo("GE") == 0) {
                        startActivity(new Intent(getApplicationContext(), GameResultActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    Log.d("---------", "can not parse string");
                }
            }
        }
    }

    public void answered(View v) {
        //timeToAnswer.cancel();

        doDisableButtons();

        answeredThis = true;

        int ok = 0;
        if (rightAnswer.compareTo(((Button) v).getText().toString()) == 0) {
            if (problemIndex == 6)
                myPoints += 5;
            else
                myPoints += 3;
            setTextView(R.id.my_score, "" + myPoints);
            ((Button) v).setBackgroundColor(Color.GREEN);
            ok = 1;
        } else {
            myPoints += -1;
            setTextView(R.id.my_score, "" + myPoints);

            ((Button) v).setBackgroundColor(Color.RED);
        }

        Log.d("$$$$", rightAnswer);
        Log.d("$$$$", ((Button) v).getText().toString());

        JSONObject query = new JSONObject();
        try {
            query.put("code", "GQ");
            query.put("time", remainingTimeOfThisQuestion);
            query.put("ok", ok);

            Log.d("aaaaaaa", query.toString());

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

        setTextView(R.id.my_name, myNameIs);
        setTextView(R.id.op_name, oppNameIs);

        oppPoints = myPoints = 0;
        problemIndex = 0;

        mListener = new TitleBarListener();
        registerReceiver(mListener, new IntentFilter("MESSAGE"));

        // ************* Start of the game
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
}
