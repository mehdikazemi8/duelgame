package com.mehdiii.duelgame.views.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mehdiii.duelgame.R;

public class ChooseAvatarActivity extends MyBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_avatar);

        LinearLayout avatarList = (LinearLayout) findViewById(R.id.choose_avatar_list);


        int i = 1;
        while(i <= NUMBER_OF_AVATARS)
        {
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rowParams.setMargins(0, 0, 0, 0);

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(rowParams);

            if(i % 2 == 0)
                row.setBackgroundColor(Color.CYAN);
            else
                row.setBackgroundColor(Color.RED);

            for(int j = 0; j < 3 && i <= NUMBER_OF_AVATARS; i ++, j ++)
            {
                LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                avatarParams.weight = 1;
                avatarParams.setMargins(0, 0, 0, 0);

                ImageView avat = new ImageView(this);
                avat.setLayoutParams(avatarParams);
                avat.setAdjustViewBounds(true);
                avat.setContentDescription(""+i);
                avat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAvatarIndex = Integer.parseInt( v.getContentDescription().toString() );
                        finish();
                    }
                });

                if(i%2 == 0)
                    avat.setBackgroundColor(Color.GREEN);
                else
                    avat.setBackgroundColor(Color.LTGRAY);


                avat.setImageResource(avatarId[i]);
                row.addView(avat);
            }

            avatarList.addView(row);
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
        if (id == R.id.about) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}