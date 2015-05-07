package com.mehdiii.duelgame;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by MeHdi on 3/26/2015.
 */
public class MusicPlayer extends Service {

    MediaPlayer musicPlayer;

    public MusicPlayer() {
        Log.d("-- MusicPlayer", "Constructor");
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("-- MusicPlayer", "onCreate");
//        musicPlayer = MediaPlayer.create(this, R.raw.music);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("-- MusicPlayer", "onStartCommand");
        musicPlayer.setLooping(true);
        musicPlayer.start();
        return 1;
    }

    @Override
    public void onDestroy() {
        musicPlayer.stop();
        musicPlayer.release();
    }
}
