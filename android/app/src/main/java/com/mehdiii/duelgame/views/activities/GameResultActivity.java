package com.mehdiii.duelgame.views.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.FontHelper;

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

    private TextView gameResultStatus;
    private ImageView gameResultOpponentAvatar;
    private TextView gameResultOpponentName;
    private TextView gameResultOpponentPoints;
    private ImageView gameResultPlayerAvatar;
    private TextView gameResultPlayerName;
    private TextView gameResultPlayerPoints;
    private TextView gameResultT7;
    private TextView gameResultT8;
    private TextView gameResultT5;
    private TextView gameResultT6;
    private TextView gameResultT3;
    private TextView gameResultT4;
    private TextView gameResultT1;
    private TextView gameResultT2;
    private TextView gameResultTotalExperience;
    private TextView gameResultPointFactor;
    private TextView gameResultWinBonus;
    private TextView gameResultPositivePoints;
    private ImageView gameResultLevelStar;
    private ProgressBar gameResultLevelProgress;
    private TextView gameResultLevelText;
    private TextView gameResultDiamondCnt;
    private ImageView gameResultDiamondPicture;

    private Button gameResultDuelOthers;
    private Button gameResultAddFriend;
    private Button gameResultHome;
    private Button gameResultReport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        gameResultStatus = (TextView) findViewById(R.id.game_result_status);
        gameResultOpponentAvatar = (ImageView) findViewById(R.id.game_result_opponent_avatar);
        gameResultOpponentName = (TextView) findViewById(R.id.game_result_opponent_name);
        gameResultOpponentPoints = (TextView) findViewById(R.id.game_result_opponent_points);
        gameResultPlayerAvatar = (ImageView) findViewById(R.id.game_result_player_avatar);
        gameResultPlayerName = (TextView) findViewById(R.id.game_result_player_name);
        gameResultPlayerPoints = (TextView) findViewById(R.id.game_result_player_points);
        gameResultT7 = (TextView) findViewById(R.id.game_result_t7);
        gameResultT8 = (TextView) findViewById(R.id.game_result_t8);
        gameResultT5 = (TextView) findViewById(R.id.game_result_t5);
        gameResultT6 = (TextView) findViewById(R.id.game_result_t6);
        gameResultT3 = (TextView) findViewById(R.id.game_result_t3);
        gameResultT4 = (TextView) findViewById(R.id.game_result_t4);
        gameResultT1 = (TextView) findViewById(R.id.game_result_t1);
        gameResultT2 = (TextView) findViewById(R.id.game_result_t2);
        gameResultTotalExperience = (TextView) findViewById(R.id.game_result_total_experience);
        gameResultPointFactor = (TextView) findViewById(R.id.game_result_point_factor);
        gameResultWinBonus = (TextView) findViewById(R.id.game_result_win_bonus);
        gameResultPositivePoints = (TextView) findViewById(R.id.game_result_positive_points);
        gameResultLevelStar = (ImageView) findViewById(R.id.game_result_level_star);
        gameResultLevelProgress = (ProgressBar) findViewById(R.id.game_result_level_progress);
        gameResultLevelText = (TextView) findViewById(R.id.game_result_level_text);
        gameResultDiamondCnt = (TextView) findViewById(R.id.game_result_diamond_cnt);
        gameResultDiamondPicture = (ImageView) findViewById(R.id.game_result_diamond_picture);
        gameResultHome = (Button) findViewById(R.id.game_result_home);
        gameResultAddFriend = (Button) findViewById(R.id.game_result_add_friend);
        gameResultDuelOthers = (Button) findViewById(R.id.game_result_duel_others);
        gameResultReport = (Button) findViewById(R.id.game_result_report);

        FontHelper.setKoodakFor(getApplicationContext(),
                gameResultStatus,
                gameResultPlayerName, gameResultPlayerPoints,
                gameResultOpponentName, gameResultOpponentPoints,
                gameResultT1, gameResultT2, gameResultT3, gameResultT4,
                gameResultT5, gameResultT6, gameResultT7, gameResultT8,
                gameResultPositivePoints, gameResultWinBonus, gameResultPointFactor, gameResultTotalExperience,
                gameResultDiamondCnt, gameResultLevelText,
                gameResultHome, gameResultAddFriend, gameResultDuelOthers, gameResultReport);

        WIN = R.raw.win;
        LOSE = R.raw.lose;

        mListener = new TitleBarListener();
        LocalBroadcastManager.getInstance(this).registerReceiver(mListener, new IntentFilter("MESSAGE"));

        try {
            JSONObject parser = new JSONObject(resultInfo);

//            myElo = (int) parser.getDouble("new_elo");
            gameStatus = parser.getInt("result");       // just this class
//            savedTime = parser.getInt("saved_time");    // just this class
//            Log.d("-- score ye nafar ", myName + " " + parser.getInt("score"));

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
            e.printStackTrace();
        }

        myPlayer.start();

        setTextView(R.id.game_result_status, gameVertict);


//        ((ImageView) findViewById(R.id.game_result_my_avatar)).setImageResource(AvatarHelper.getResourceId(this, myAvatarIndex));
//        ((ImageView) findViewById(R.id.game_result_opponent_avatar)).setImageResource(AvatarHelper.getResourceId(this, oppAvatarIndex));
//
//        setTextView(R.id.game_result_opponent_points, "" + oppPoints);
//        setTextView(R.id.game_result_my_points, "" + myPoints);
//
//        setTextView(R.id.game_result_saved_time, "+" + savedTime);
//        setTextView(R.id.game_result_new_score, "+" + myPoints);

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

    public void addToFriends(View v) {
        boolean everythingOK = true;
        JSONObject query = new JSONObject();
        try {
            query.put("code", "AF");
            query.put("user_number", oppUserNumber);

            Log.d("-- send ADD FRIEND", query.toString());

            DuelApp.getInstance().sendMessage(query.toString());
        } catch (JSONException e) {
            everythingOK = false;
            Log.d("---- GameResult JSON", e.toString());
        }

        if (everythingOK == true) {
            String msg = "به لیست دوستان شما اضافه شد.";
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

            DuelApp.getInstance().sendMessage(query.toString());
        } catch (JSONException e) {
            Log.d("---- GameResult JSON", e.toString());
        }

        startActivity(new Intent(this, WaitingActivity.class));
        this.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mListener);
    }
}
