package com.mehdiii.duelgame.views.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.utils.FontHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class GameResultActivity extends MyBaseActivity {
    public static final String ARGUMENT_OPPONENT = "argument_opponent";
    public static final String ARGUMENT_RESULT_INFO = "argument_result_info";

    User opponentUser;

    String resultInfo;
    int gameStatus;
    int earnedDiamond;
    String gameVerdict;

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

        readArguments();

        findControls();

        configureControls();

        WIN = R.raw.win;
        LOSE = R.raw.lose;
        try {
            JSONObject parser = new JSONObject(resultInfo);
            gameStatus = parser.getInt("result");       // just this class

            int bonous = 0, musicId;
            if (gameStatus == 0) {
                bonous = earnedDiamond;
                gameVerdict = "مساوی شد";
                musicId = LOSE;
            } else if (gameStatus == 1) {
                bonous = earnedDiamond + 120;
                gameVerdict = "تو بردیییی";
                musicId = WIN;
            } else {
                // nothing
                gameVerdict = "تو باختی";
                musicId = LOSE;
            }
            myPlayer = MediaPlayer.create(this, musicId);
            AuthManager.getCurrentUser().addDiamond(bonous);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        myPlayer.start();
        setTextView(R.id.game_result_status, gameVerdict);
        AuthManager.getCurrentUser().setScore(AuthManager.getCurrentUser().getScore() + userPoints);
    }

    private void configureControls() {
        FontHelper.setKoodakFor(getApplicationContext(),
                gameResultStatus,
                gameResultPlayerName, gameResultPlayerPoints,
                gameResultOpponentName, gameResultOpponentPoints,
                gameResultT1, gameResultT2, gameResultT3, gameResultT4,
                gameResultT5, gameResultT6, gameResultT7, gameResultT8,
                gameResultPositivePoints, gameResultWinBonus, gameResultPointFactor, gameResultTotalExperience,
                gameResultDiamondCnt, gameResultLevelText,
                gameResultHome, gameResultAddFriend, gameResultDuelOthers, gameResultReport);
    }

    private void findControls() {
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
    }

    private void readArguments() {
        Bundle params = getIntent().getExtras();
        if (params == null)
            return;
        String json = params.getString(ARGUMENT_OPPONENT, "");

        if (!json.isEmpty()) {
            opponentUser = BaseModel.deserialize(json, User.class);
            resultInfo = params.getString(ARGUMENT_RESULT_INFO);
        }
    }

    public void setTextView(int viewId, String s) {
        ((TextView) findViewById(viewId)).setText(s);
    }

    public void addToFriends(View v) {
        boolean everythingOK = true;
        JSONObject query = new JSONObject();
        try {
            query.put("code", "AF");
            query.put("user_number", opponentUser.getId());

            DuelApp.getInstance().sendMessage(query.toString());
        } catch (JSONException e) {
            everythingOK = false;
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

            DuelApp.getInstance().sendMessage(query.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, WaitingActivity.class));
        this.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
