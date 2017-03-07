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
    static final int HEIGHT = 2;
    static final int WIDTH = 1;
    private static final float ACCEL_X = 0.15f;
    private static final float ACCEL_Y = 0.1f;
    private static final float TERMINAL_VELOCITY = 8f;
    private static final float MAX_VELOCITY = 6f;

    private static final float PLAYER_JUMP_HEIGHT = 3f;
    private static final float PLAYER_JUMP_DURATION = 0.150f;
    private static final float PLAYER_JUMP_IMPULSE = -(PLAYER_JUMP_HEIGHT/PLAYER_JUMP_DURATION);

    private static final int LEFT = -1;
    private static final int RIGHT = 1;
    private boolean mIsOnGround = false;
    private float mJumpTime = 0f;
    private int mFacing = RIGHT;

    private AnimationDrawable mAnim = null;
    private int mFrameCount = 0;
    private int mDuration = 0;
    private long mCurrentAnimationTime = 0;
    private int mCurrentFrame = 0;

    public Player(final GameView engine, final float x, final float y, final int type) {
        super(engine, x, y, type);
        mAcceleration.x = ACCEL_X;
        mAcceleration.y = ACCEL_Y;

        mAnim = (AnimationDrawable) ContextCompat.getDrawable(engine.getContext(), R.drawable.player_anim);
        mFrameCount = mAnim.getNumberOfFrames();
        for(int i = 0; i < mFrameCount; i++){
            mDuration += mAnim.getDuration(i);
        }
    }

    @Override
    public void onCollision(GameObject collidingGameObject) {
        if(!GameObject.getOverlap(this, collidingGameObject)){
            Log.d(TAG, "getOverlap false negative");
        }

        if(overlap.y != 0){
            mVelocity.y = 0;
            if(overlap.y < 0){
                Log.d(TAG, "Feet collision");
                mJumpTime = 0.0f;
                mIsOnGround = true;
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


        Bitmap b = ((BitmapDrawable)mAnim.getFrame(mCurrentFrame)).getBitmap();

        canvas.drawBitmap(b, mTransform, paint);

    }

    @Override
    public void update(float deltaTime){
        long elapsedMillis = (long)(1000.0f * deltaTime);
        mCurrentAnimationTime += elapsedMillis;
        if(mCurrentAnimationTime > mDuration){
            mCurrentAnimationTime = mCurrentAnimationTime % mDuration;
        }

        int frameDuration = 0;
        for(int i = 0; i < mFrameCount; i++){
            frameDuration += mAnim.getDuration(i);
            if(frameDuration > mCurrentAnimationTime){
                mCurrentFrame = i;
                break;
            }
        }

        if(mVelocity.y != 0){
            mIsOnGround = false;
        }

        if(mEngine.mControl.mHorizontalFactor < 0){
            mFacing = LEFT;
        }else if(mEngine.mControl.mHorizontalFactor > 0){
            mFacing = RIGHT;
        }

        mTargetSpeed.x = mEngine.mControl.mHorizontalFactor * (MAX_VELOCITY * deltaTime);
//        /*mVelocity.x += ACCEL_X * targetSpeed;
//        if(Math.abs(mVelocity.x) > Math.abs(targetSpeed)){
//            mVelocity.x = targetSpeed;
//        }*/

        if(mEngine.mControl.mIsJumping && mJumpTime < PLAYER_JUMP_DURATION){
            mVelocity.y = (PLAYER_JUMP_IMPULSE * deltaTime);
            mJumpTime += deltaTime;
            mIsOnGround = false;
        }else{
            mVelocity.y += ACCEL_Y * (TERMINAL_VELOCITY*deltaTime);
        }

        super.update(deltaTime);

//        if(mEngine.mControl.mIsJumping && mIsOnGround){
//            mVelocity.y = -PLAYER_JUMP_IMPULSE *deltaTime;
//            mIsOnGround = false;
//        }
//
//        targetSpeed = (TERMINAL_VELOCITY * deltaTime);
//        mVelocity.y += ACCEL_Y * targetSpeed;
//        if(mVelocity.y > targetSpeed){
//            mVelocity.y = targetSpeed;
//        }
//
//        super.update(deltaTime);
    }
}
