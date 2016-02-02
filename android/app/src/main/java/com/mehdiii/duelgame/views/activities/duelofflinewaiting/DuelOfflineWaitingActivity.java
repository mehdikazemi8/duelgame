package com.mehdiii.duelgame.views.activities.duelofflinewaiting;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.ProvinceManager;
import com.mehdiii.duelgame.models.StartOfflineDuelRequest;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.utils.ScoreHelper;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.dueloffline.DuelOfflineActivity;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.squareup.picasso.Picasso;

/**
 * Created by mehdiii on 2/1/16.
 */
public class DuelOfflineWaitingActivity extends ParentActivity {

    CustomTextView name;
    CustomTextView title;
    CustomTextView province;
    ProgressBar progressBar;
    ImageView avatar;
    LinearLayout holder;
    CustomButton letsGoButton;
    String gameDataJson = null;

    String opponentUserNumber;
    String category;
    boolean isMaster;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_duel_waiting);

        if( !readExtras(getIntent().getExtras()) ) {
            Log.e("TAG", "getIntent().getExtras() is null");
            return;
        }

        find();
        configure();

        DuelApp.getInstance().sendMessage(new StartOfflineDuelRequest(CommandType.WANNA_START_OFFLINE_DUEL, opponentUserNumber, category).serialize());
    }

    private boolean readExtras(Bundle extras) {
        if(extras != null) {
            opponentUserNumber = extras.getString("opponent_user_number");
            category = extras.getString("category");
            isMaster = extras.getBoolean("master");
            return true;
        }
        return false;
    }

    private void find() {
        name = (CustomTextView) findViewById(R.id.name);
        province = (CustomTextView) findViewById(R.id.province);
        title = (CustomTextView) findViewById(R.id.title);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        letsGoButton = (CustomButton) findViewById(R.id.lets_go_button);
        holder = (LinearLayout) findViewById(R.id.text_and_button_holder);
        avatar = (ImageView) findViewById(R.id.avatar);
    }

    private void configure() {
        Picasso.with(this).load(AvatarHelper.getResourceId(this, AuthManager.getCurrentUser().getAvatar())).into(avatar);
        name.setText(AuthManager.getCurrentUser().getName());
        province.setText(ProvinceManager.get(this, AuthManager.getCurrentUser().getProvince()));
        title.setText(ScoreHelper.getTitle(AuthManager.getCurrentUser().getScore()));

        letsGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "jsonn " + gameDataJson);
                Intent intent = new Intent(DuelOfflineWaitingActivity.this, DuelOfflineActivity.class);
                intent.putExtra(DuelOfflineActivity.GAME_DATA_JSON, gameDataJson);
                intent.putExtra(DuelOfflineActivity.IS_MASTER, isMaster);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, DuelApp.getInstance().getIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            switch (type) {
                case RECEIVE_START_OFFLINE_DUEL:
                    gameDataJson = json;
                    progressBar.setVisibility(View.GONE);
                    holder.setVisibility(View.VISIBLE);
                    break;
            }
        }
    });
}
