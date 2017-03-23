package com.example.jesper.platformer.engine;

import com.example.jesper.platformer.FrameTimer;

/**
 * Created by Jesper on 2017-03-22.
 */

public class UpdateThread extends Thread {
    private final GameEngine mEngine;
    private boolean mIsRunning = true;
    private boolean mIsPaused = false;
    private Object mLock = new Object();
    private FrameTimer mTimer = new FrameTimer();

    public UpdateThread(GameEngine engine){
        mEngine = engine;
    }

    @Override
    public synchronized void start() {
        super.start();
        mIsRunning = true;
        mIsPaused = false;
    }

    @Override
    public void run() {
        super.run();
        mTimer.reset();
        while(mIsRunning){
            if(mIsPaused){
                waitUntilResumed();
            }
            mEngine.update(mTimer.onEnterFrame());

        }
    }

    public void stopGame(){
        mIsRunning = false;
        resumeGame();
    }

    public void pauseGame(){
        mIsPaused = true;
    }

    public void resumeGame(){
        mTimer.reset();
        if(mIsPaused == true){
            mIsPaused = false;
            synchronized (mLock){
                mLock.notify();
            }
        }
    }

    private void waitUntilResumed(){
        while(mIsPaused){
            synchronized (mLock){
                try{
                    mLock.wait();
                }catch (InterruptedException e){

                }
            }
        }
        mTimer.reset();
    }

    public long getAverageFPS(){
        return mTimer.getCurrentFps();
    }
    public boolean isGameRunning(){
        return mIsRunning;
    }

    public boolean isGamePaused(){
        return mIsPaused;
    }
}
