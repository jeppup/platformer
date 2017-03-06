package com.example.jesper.platformer;

import android.graphics.PointF;
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
    private static final float JUMP_IMPULSE = 20f;
    private static final float MAX_VELOCITY = 6f;

    private boolean mIsOnGround = false;


    public Player(final GameView engine, final float x, final float y, final int type) {
        super(engine, x, y, type);
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
                mIsOnGround = true;
            }
        }

        mWorldLocation.offset(overlap.x, overlap.y);
        updateBounds();
    }

    @Override
    public void update(float deltaTime){
        if(mVelocity.y != 0){
            mIsOnGround = false;
        }

        float targetSpeed = mEngine.mControl.mHorizontalFactor * (MAX_VELOCITY * deltaTime);
        mVelocity.x += ACCEL_X * targetSpeed;
        if(Math.abs(mVelocity.x) > Math.abs(targetSpeed)){
            mVelocity.x = targetSpeed;
        }

        if(mEngine.mControl.mIsJumping && mIsOnGround){
            mVelocity.y = -JUMP_IMPULSE*deltaTime;
            mIsOnGround = false;
        }

        targetSpeed = (TERMINAL_VELOCITY * deltaTime);
        mVelocity.y += ACCEL_Y * targetSpeed;
        if(mVelocity.y > targetSpeed){
            mVelocity.y = targetSpeed;
        }

        super.update(deltaTime);
    }
}
