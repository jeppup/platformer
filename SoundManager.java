package com.example.jesper.platformer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Jesper on 2017-03-18.
 */

public class SoundManager {
    private SoundPool mSoundPool;
    private final int MAX_STREAMS = 10;
    public static int ENEMY_WALKING;
    public static int BACKGROUND_MUSIC;
    public static int TARGET_COLLECTED;
    public static int PLAYER_DEATH;
    public static int LEVEL_COMPLETED;

    public SoundManager(Context context){
        mSoundPool = createSoundPool();
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if(sampleId == BACKGROUND_MUSIC){
                    play(BACKGROUND_MUSIC, true);
                }
            }
        });
        loadSounds(context);
    }

    private SoundPool createSoundPool(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            return createNewSoundPool();
        }
        return createOldSoundPool();
    }

    private void loadSounds(Context context){
        try{
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("monster_walking.ogg");
            ENEMY_WALKING = mSoundPool.load(descriptor, 1);

            descriptor = assetManager.openFd("background_music.ogg");
            BACKGROUND_MUSIC = mSoundPool.load(descriptor, 1);

            descriptor = assetManager.openFd("player_death.ogg");
            PLAYER_DEATH = mSoundPool.load(descriptor, 1);

            descriptor = assetManager.openFd("level_completed.ogg");
            LEVEL_COMPLETED = mSoundPool.load(descriptor, 1);

            descriptor = assetManager.openFd("target_collected.ogg");
            TARGET_COLLECTED = mSoundPool.load(descriptor, 1);
        }catch (IOException ex){
            Log.e("Couldn't load sound", ex.getMessage());
        }
    }

    public void play(final int soundId, boolean loop){
        float leftVolume = 1.0f;
        float rightVolume = 1.0f;
        int loopSound = loop ? -1 : 0;
        int priority = 0;
        float rate = 1.0f;

        if(soundId > 0 ){
            mSoundPool.play(soundId, leftVolume, rightVolume, priority, loopSound, rate);
        }
    }

    @SuppressWarnings("deprecation")
    private SoundPool createOldSoundPool(){
        return new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private SoundPool createNewSoundPool(){
        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        return new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(MAX_STREAMS)
                .build();
    }
}
