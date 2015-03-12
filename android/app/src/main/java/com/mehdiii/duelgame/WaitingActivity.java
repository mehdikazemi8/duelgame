package com.mehdiii.duelgame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WaitingActivity extends MyBaseActivity {

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
                        String oppName = parser.getString("data");

                        TextView oppNameTV = (TextView) findViewById(R.id.opponent_name);
                        oppNameTV.setText(oppName);

                        oppNameIs = oppName;

                        for(int problemIndex = 0; problemIndex < NUMBER_OF_QUESTIONS; problemIndex ++)
                        {
                            questionsToAsk[problemIndex] = new Question();
                            JSONObject thisQuestion = new JSONObject( parser.getString("problem"+problemIndex) );
                            questionsToAsk[problemIndex].questionText = thisQuestion.getString("question_text");
                            JSONArray options = thisQuestion.getJSONArray("options");
                            for(int op = 0; op < 4; op ++)
                            {
                                questionsToAsk[problemIndex].options[op] = "" + options.get(op);
                            }
                        }

                        JSONObject query = new JSONObject();
                        try {
                            query.put("code", "RTP");
                            wsc.sendTextMessage(query.toString());
                        } catch (JSONException e) {
                            Log.d("---- StartActivity JSON", e.toString());
                        }
                    } else if (messageCode.compareTo("SP") == 0) {
                        startActivity(new Intent(getApplicationContext(), PlayGameActivity.class));
                        finish();
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

        ImageView pic = (ImageView) findViewById(R.id.pic);
        Bitmap logobit = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
        logobit = ImageHelper.getResizedBitmap(logobit, 400, 400);
        logobit = ImageHelper.getRoundedCornerBitmap(this, logobit,
                100, 400, 400, false, false, false, false);
        pic.setImageBitmap(logobit);

        mListener = new TitleBarListener();
        registerReceiver(mListener, new IntentFilter("MESSAGE"));
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
