package com.example.jesper.platformer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by Jesper on 2017-02-18.
 */

public class Player extends DynamicGameObject {
    static final String TAG = "Player";
    private final float PLAYER_JUMP_IMPULSE;
    private static final int LEFT = -1;
    private static final int RIGHT = 1;
    private int mFacing = RIGHT;
    private float mJumpTime = 0f;
    private AnimationManager mAnim = null;

    private static int mMaxHitPoints;
    private static int mHitPoints;



    public Player(final GameView engine, final float x, final float y, final int type) {
        super(engine, x, y, type);
        mAcceleration.x = mConfig.P_ACCELERATION_X;
        mAcceleration.y = mConfig.P_ACCELERATION_Y;
        PLAYER_JUMP_IMPULSE = -(mConfig.P_JUMP_HEIGHT / mConfig.P_JUMP_DURATION);
        mAnim = new AnimationManager(engine, R.drawable.player_anim, mWidth, mHeight);
        reset();
    }

    private void reset(){
        mMaxHitPoints = mConfig.P_MAX_HEALTH;
        mHitPoints = mConfig.P_MAX_HEALTH;
    }

    public static int getHitPoints(){ return mHitPoints; }
    public static int getMaxHitPoints(){ return mMaxHitPoints; }

    @Override
    public boolean applyDamage(int dmgAmount){
        mHitPoints -= dmgAmount;
        return true;
    }

    @Override
    public void onCollision(GameObject collidingGameObject) {
        if(!GameObject.getOverlap(this, collidingGameObject)){
            Log.d(TAG, "getOverlap false negative");
        }

        if(overlap.y != 0){
            mVelocity.y = 0;
            if(overlap.y < 0){
                mJumpTime = 0.0f;
            }
        }

        mWorldLocation.offset(overlap.x, overlap.y);
        updateBounds();
    }

    @Override
    public void render(Canvas canvas, Paint paint){
        mTransform.reset();
        mTransform.setScale(mFacing, 1.0f);
        mEngine.setScreenCoordinate(mWorldLocation, GameObject.screenCord);

        int offset = 0;
        if(mFacing == LEFT){
            offset = (int)mWidth * mEngine.getPixelsPerMeter();
        }

        mTransform.postTranslate(GameObject.screenCord.x + offset, GameObject.screenCord.y);
        canvas.drawBitmap(mAnim.getCurrentBitmap(), mTransform, paint);
    }

    @Override
    public void update(float deltaTime){
        mAnim.update(deltaTime);

        if(mEngine.mControl.mHorizontalFactor < 0){
            mFacing = LEFT;
        }else if(mEngine.mControl.mHorizontalFactor > 0){
            mFacing = RIGHT;
        }

        mTargetSpeed.x = mEngine.mControl.mHorizontalFactor * (mConfig.P_MAX_VELOCITY * deltaTime);

        if(mEngine.mControl.mIsJumping && mJumpTime < mConfig.P_JUMP_DURATION){
            mVelocity.y = (PLAYER_JUMP_IMPULSE * deltaTime);
            mJumpTime += deltaTime;
        }else{
            mVelocity.y += mAcceleration.y * (mConfig.P_TERMINAL_VELOCITY * deltaTime);
        }

        super.update(deltaTime);
    }
}
