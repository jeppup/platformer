package com.example.jesper.platformer;

import android.provider.Settings;

/**
 * Created by Jesper on 2017-02-18.
 */

public class FrameTimer {
    private static final long SECOND = 1000; //ms
    private static final float TO_SECONDS = 1000.0f;
    private long mStartFrameTime = 0;
    private long mElapsedTime = 0;
    private long mMillisCount = 0;
    private long mFrameCount = 0;
    private long mMeasuringStartedTime = 0;
    private float mAvgFPS = 0;

    public FrameTimer() {
        reset();
    }

    public void reset(){
        mMeasuringStartedTime = System.currentTimeMillis();
        mStartFrameTime = 0;
        mElapsedTime = 0;
        mFrameCount = 0;
    }

    public float onEnterFrame(){
        mFrameCount++;
        mElapsedTime = System.currentTimeMillis() - mStartFrameTime;
        mMillisCount += mElapsedTime;
        mStartFrameTime = System.currentTimeMillis();
        return mElapsedTime / TO_SECONDS;
    }

    public long getElapsedMillis(){ return mElapsedTime; }

    public int getAverageFPS(){
        if(mMillisCount > SECOND){
            mAvgFPS = mFrameCount * SECOND / mMillisCount;
            mFrameCount = 0;
            mMillisCount = 0;
        }

        return (int)mAvgFPS;
    }

    public long getCurrentFps(){
        if(mElapsedTime > 0){
            return SECOND / mElapsedTime;
        }

        return 0;
    }
}
