package com.mehdiii.duelgame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends MyBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        try{
            JSONObject parser = new JSONObject(loginInfo);
            myAvatarIndex = parser.getInt("avatar");
            myTime = parser.getInt("time");
            myOstanInt = parser.getInt("ostan");
            myScore = parser.getInt("score");
            myUserNumber = parser.getString("user_number");
            myElo = (int) parser.getDouble("elo");
            myName = parser.getString("name");

        }catch (JSONException e)
        {
            Log.d("---- HOME Activity, loginInfo", e.toString());
        }

        setData();
    }

    public void setTextView(int id, String str)
    {
        ((TextView) findViewById(id)).setText(str);
    }

    public void setData()
    {
        setTextView(R.id.home_my_name, myName);
        setTextView(R.id.home_my_elo, ""+myElo);
        setTextView(R.id.home_my_score, ""+myScore);
        setTextView(R.id.home_my_time, ""+myTime);

        ((ImageView) findViewById(R.id.home_my_avatar)).setImageResource(avatarId[myAvatarIndex]);
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
    public void onResume()
    {
        super.onResume();
        setData();
    }

    public void wantToPlay(View v) {
        Log.d("&&&&", "aaaaaaaaaaaaaaaa");
        startActivity(new Intent(this, CategoryActivity.class));
    }
}
