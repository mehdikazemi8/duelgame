package com.mehdiii.duelgame.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

/**
 * Created by MeHdi on 4/23/2015.
 */
public class DuelMusicPlayer extends AsyncTask<Void, Void, Void> {

    private MediaPlayer player = null;

    public DuelMusicPlayer(Context context, int musicId, boolean looping) {
        player = MediaPlayer.create(context, musicId);
        player.setLooping(looping);
    }

    @Override
    protected Void doInBackground(Void... params) {
        player.start();
        return null;
    }

    public void playSound() {
        if (player.isPlaying() == false)
            player.start();
    }

    public void pauseSound() {
        if (player.isPlaying() == true)
            player.pause();
    }
}
