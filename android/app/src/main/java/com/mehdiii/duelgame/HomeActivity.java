package com.mehdiii.duelgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends MyBaseActivity {

    private void setupActionBar() {
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        ab.setIcon(R.drawable.icon_friends);
        ab.setBackgroundDrawable( getResources().getDrawable( R.drawable.my_action_bar_pic ) );
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar_title, null);

        TextView titleTV = (TextView) v.findViewById(R.id.title);
//        Typeface font = Typeface.createFromAsset(getAssets(),
//                "fonts/your_custom_font.ttf");
//        titleTV.setTypeface(font);

        ab.setCustomView(v);

        ab.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupActionBar();

        try {
            JSONObject parser = new JSONObject(loginInfo);
            myAvatarIndex = parser.getInt("avatar");
            myTime = parser.getInt("time");
            myOstanInt = parser.getInt("ostan");
            myScore = parser.getInt("score");
            myUserNumber = parser.getString("user_number");
            myElo = (int) parser.getDouble("elo");
            myName = parser.getString("name");

        } catch (JSONException e) {
//            Log.d("---- HOME Activity, loginInfo", e.toString());
        }

        setData();
    }

    public void setTextView(int id, String str) {
        ((TextView) findViewById(id)).setText(str);
    }

    public void setData() {

//        setTextView(R.id.home_my_name, myName);
        setTextView(R.id.home_my_degree, "دیپلم");
        //setTextView(R.id.home_my_elo, "" + myElo);
        //setTextView(R.id.home_my_score, "" + myScore);
        //setTextView(R.id.home_my_time, "" + myTime);

//        ((ImageView) findViewById(R.id.home_my_avatar)).setImageResource(avatarId[myAvatarIndex]);

        ImageView pic = (ImageView) findViewById(R.id.home_my_avatar);

        Bitmap logobit = BitmapFactory.decodeResource(getResources(), avatarId[myAvatarIndex]);
        logobit = ImageHelper.getResizedBitmap(logobit, 100, 100);
        logobit = ImageHelper.getRoundedCornerBitmap(this, logobit,
                100, 100, 100, false, false, false, false);

        pic.setImageBitmap(logobit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_start, menu);

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
    public void onResume() {
        super.onResume();
        setData();

        Intent svc = new Intent(this, MusicPlayer.class);
        startService(svc);
    }

    @Override
    public void onBackPressed() {
        Intent svc = new Intent(this, MusicPlayer.class);
        stopService(svc);
        finish();
    }

    public void wantToPlay(View v) {
        Log.d("&&&&", "aaaaaaaaaaaaaaaa");
        startActivity(new Intent(this, CategoryActivity.class));
    }

    // ******************************** HOME BUTTONE PRESSED

    @Override
    public void onPause() {
        super.onPause();

//        Intent svc = new Intent(this, MusicPlayer.class);
//        stopService(svc);
    }

    @Override
    public void onStop() {
        super.onStop();

//        Intent svc = new Intent(this, MusicPlayer.class);
//        stopService(svc);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent svc = new Intent(this, MusicPlayer.class);
        stopService(svc);
    }
}
