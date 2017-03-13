package com.example.jesper.platformer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Jesper on 2017-03-12.
 */

public class Enemy extends DynamicGameObject {
    private final Config mConfig;
    private final int mDamage;

    //Attacking
    private float mAttackCooldown;
    private float mCooldownTimer;
    private boolean mCanAttack = true;

    //Stuck handling
    private float mPreviousPosX;


    //Jumping
    private float mJumpDuration = 0.4f;
    private float mJumpTimer = 0.0f;
    private  boolean mIsJumping = false;

    private static final int LEFT = -1;
    private static final int RIGHT = 1;
    private int mFacing = RIGHT;

    public Enemy(GameView engine, float x, float y, int type) {
        super(engine, x, y, 3, 3, type);
        mConfig = engine.getConfig();
        mDamage = mConfig.E_DAMAGE;
        mPassable = true;
        mAttackCooldown = mConfig.E_ATTACK_COOLDOWN;
        mPreviousPosX = mWorldLocation.x;

    }

    @Override
    public void onCollision(GameObject collidingGameObject) {
        if(mCanAttack){
            if(collidingGameObject.applyDamage(mDamage)){
                mCanAttack = false;
                mTargetSpeed.x = 0f;
            }
        }

        if(!GameObject.getOverlap(this, collidingGameObject)){
            Log.d("ENEMY", "getOverlap false negative");
        }

        if(overlap.y != 0) {
            mVelocity.y = 0f;
        }

        if(!collidingGameObject.mPassable){
            mWorldLocation.offset(overlap.x, overlap.y);
            //updateBounds();
        }
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
        canvas.drawBitmap(mEngine.getBitmap(mType), mTransform, paint);
    }


    @Override
    public void update(float deltaTime){
        updateAttackState(deltaTime);
        updateDirection();
        updateVelocity(deltaTime);
        handleObstacles(deltaTime);

        super.update(deltaTime);
        checkIfStuck();
    }

    private void checkIfStuck(){
        float xDiff = Math.abs(mPreviousPosX - mWorldLocation.x);
        if(mCanAttack)
        {
            if(xDiff < 0.01f && distanceToPlayer() > 0.2f && !mIsJumping){
                mIsJumping = true;
            }
        }

        mPreviousPosX = mWorldLocation.x;
    }

    private void handleObstacles(float deltaTime){
        if(!mIsJumping){ return; }

        if(mJumpTimer < mJumpDuration){
            mVelocity.y += (-0.7f * 8.0f * deltaTime);
            mJumpTimer += deltaTime;
        }else{
            mJumpTimer = 0f;
            mIsJumping = false;
        }
    }

    private void updateVelocity(float deltaTime){
        if(mCanAttack){
            mTargetSpeed.x =  mFacing * 3.0f * deltaTime;
            mAcceleration.x = 0.7f;
        }

        mTargetSpeed.y = 3.0f * deltaTime;
        mAcceleration.y = 0.2f;

        mVelocity.y += mAcceleration.y * mTargetSpeed.y;
        mVelocity.y = Utils.clamp(mVelocity.y, -mTargetSpeed.y, mTargetSpeed.y);
    }

    private void updateAttackState(float deltaTime){
        if(!mCanAttack){
            mCooldownTimer += deltaTime;

            if(mCooldownTimer > mAttackCooldown){
                mCanAttack = true;
                mCooldownTimer = 0.0f;
            }
        }
    }

    private void updateDirection(){
        if(!mCanAttack)
            return;

        if(mEngine.mLevelManager.mPlayer.mWorldLocation.x > this.mWorldLocation.x){
            this.mFacing = RIGHT;
        }
        else {
            this.mFacing = LEFT;
        }
    }

    private float distanceToPlayer(){
        return Math.abs(mWorldLocation.x - mEngine.mLevelManager.mPlayer.mWorldLocation.x);
    }
}
