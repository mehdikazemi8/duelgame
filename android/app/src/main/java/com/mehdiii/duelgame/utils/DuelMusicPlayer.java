package com.mehdiii.duelgame.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.mehdiii.duelgame.managers.GlobalPreferenceManager;

/**
 * Created by MeHdi on 4/23/2015.
 */
public class DuelMusicPlayer extends AsyncTask<Void, Void, Void> {

    private MediaPlayer player = null;
    private static final String PREFERENCE_VOICE = "preference_voice";
    private Context mContext;

    public DuelMusicPlayer(Context context, int musicId, boolean looping) {
        player = MediaPlayer.create(context, musicId);
        player.setLooping(looping);
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        boolean doIPlaySound = GlobalPreferenceManager.readBoolean(mContext, PREFERENCE_VOICE, true);
        if (!doIPlaySound)
            return null;

        player.start();
        return null;
    }

    public void playSound() {
        boolean doIPlaySound = GlobalPreferenceManager.readBoolean(mContext, PREFERENCE_VOICE, true);
        if (!doIPlaySound) {
            return;
        }

        if (player.isPlaying() == false)
            player.start();
    }

    public void pauseSound() {
        if (player.isPlaying() == true)
            player.pause();
    }
}
