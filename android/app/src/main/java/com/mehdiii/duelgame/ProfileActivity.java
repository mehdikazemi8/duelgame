package com.mehdiii.duelgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends MyBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView pic = (ImageView) findViewById(R.id.pic);
        Bitmap logobit = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
        logobit = ImageHelper.getResizedBitmap(logobit, 400, 400);
        logobit = ImageHelper.getRoundedCornerBitmap(this, logobit,
                100, 400, 400, false, false, false, false);
        pic.setImageBitmap(logobit);

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

    public void wantToPlay(View v) {
        Log.d("&&&&", "aaaaaaaaaaaaaaaa");
        startActivity(new Intent(this, CategoryActivity.class));
    }
}
