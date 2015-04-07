package com.mehdiii.duelgame.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mehdiii.duelgame.R;

import org.json.JSONException;
import org.json.JSONObject;

public class CategoryActivity extends MyBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
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

    public void letsPlay(View v) {
        /*
        int duelCategory = Integer.parseInt( v.getContentDescription().toString() );
        Log.d("------ letsPlay", ">>" + duelCategory);
        */

        category = v.getContentDescription().toString();

        JSONObject query = new JSONObject();
        try {
            query.put("code", "WP");
            query.put("category", category);

            Log.d("-- send WP", query.toString());

            wsc.sendTextMessage(query.toString());
        } catch (JSONException e) {
            Log.d("---- StartActivity JSON", e.toString());
        }

        startActivity(new Intent(this, WaitingActivity.class));
        this.finish();
    }
}