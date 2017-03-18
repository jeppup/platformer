package com.example.jesper.platformer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Jesper on 2017-03-12.
 */

public class Target extends GameObject {
    private static int mTotalTargetAmount = 0;
    private static int mCollectedTargets = 0;

    private boolean mIsCollected = false;
    private final RectF mStoredBounds;


    public Target(GameView engine, float x, float y, int tileType){
        super(engine, x, y, tileType);
        mStoredBounds = mBounds;
        Target.mTotalTargetAmount++;
    }

    public void reset(){
        if(mIsCollected){
            Target.mCollectedTargets--;
        }

        mIsCollected = false;
        mBounds = mStoredBounds;
    }

    @Override
    public void render(Canvas canvas, Paint paint) {
        if(mIsCollected) {
            return;
        }

        super.render(canvas, paint);
    }

    @Override
    public void onCollision(GameObject collidingGameObject) {
        mCollectedTargets++;
        mIsCollected = true;
        mBounds = new RectF(0,0,0,0);
        mEngine.mSoundManager.play(SoundManager.TARGET_COLLECTED, false);
    }

    public static int getTotalCount(){
        return mTotalTargetAmount;
    }

    public static int getCollectedCount(){
        return mCollectedTargets;
    }

    public static int getRemainingCount(){
        return mTotalTargetAmount - mCollectedTargets;
    }

    public static void resetTargetCount(){
        mTotalTargetAmount = 0;
        mCollectedTargets = 0;
    }
}
