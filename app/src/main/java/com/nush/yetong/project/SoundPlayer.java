package com.nush.yetong.project;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * Created by Yetong on 2017/2/24.
 */

public class SoundPlayer {
    public static final int S1 = R.raw.mine_ex;
    private static SoundPool soundPool;
    private static HashMap<Integer, Integer> soundPoolMap;
    /** Populate the SoundPool*/
    public static void initSounds(Context context) {
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put( S1, soundPool.load(context, R.raw.mine_ex, 1) );
    }

    /** Play a given sound in the soundPool */
    public void playSound(Context context, int soundID) {
        if(soundPool == null || soundPoolMap == null){
            initSounds(context);
        }
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float leftVolume = curVolume/maxVolume;
        float rightVolume = curVolume/maxVolume;
        int priority = 1;
        int no_loop = 0;
        float normal_playback_rate = 1f;
        soundPool.play(soundID, leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
    }
}
