package com.example.jesper.platformer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Jesper on 2017-03-12.
 */

public class Enemy extends DynamicGameObject {
    private AnimationManager mAnim = null;
    private float mCooldownTimer;
    private boolean mCanAttack = true;
    private  boolean mIsJumping = false;
    private float mPreviousPosX;
    private float mJumpTimer = 0.0f;
    private int mFacing = RIGHT;

    private static final int LEFT = -1;
    private static final int RIGHT = 1;

    public Enemy(GameView engine, float x, float y, int type) {
        super(engine, x, y, engine.getConfig().E_WIDTH, engine.getConfig().E_HEIGHT, type);
        mPreviousPosX = mWorldLocation.x;
        mAnim = new AnimationManager(engine, R.drawable.enemy_anim, engine.mSoundManager.ENEMY_WALKING,  mWidth, mHeight);
    }

    @Override
    public void onCollision(GameObject collidingGameObject) {
        if(mCanAttack){
            if(collidingGameObject.applyDamage(mConfig.E_DAMAGE)){
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
            if(xDiff < mConfig.E_STUCK_TOLERANCE && distanceToPlayer() > mConfig.E_STUCK_PLAYER_TOLERANCE && !mIsJumping){
                mIsJumping = true;
            }
        }

        mPreviousPosX = mWorldLocation.x;
    }

    private void handleObstacles(float deltaTime){
        if(!mIsJumping){ return; }

        if(mJumpTimer < mConfig.E_JUMP_DURATION){
            mVelocity.y += (-mConfig.E_JUMP_ACCELERATION * mConfig.E_JUMP_SPEED * deltaTime);
            mJumpTimer += deltaTime;
        }else{
            mJumpTimer = 0f;
            mIsJumping = false;
        }
    }

    private void updateVelocity(float deltaTime){
        if(mCanAttack){
            mTargetSpeed.x =  mFacing * mConfig.E_MOVE_SPEED * deltaTime;
            mAcceleration.x = mConfig.E_MOVE_ACCELERATION;
        }

        mTargetSpeed.y = mConfig.E_TERMINAL_SPEED * deltaTime;
        mAcceleration.y = mConfig.E_GRAVITY;
        mVelocity.y += mAcceleration.y * mTargetSpeed.y;
        mVelocity.y = Utils.clamp(mVelocity.y, -mTargetSpeed.y, mTargetSpeed.y);
    }

    private void updateAttackState(float deltaTime){
        if(!mCanAttack){
            mCooldownTimer += deltaTime;

            if(mCooldownTimer > mConfig.E_ATTACK_COOLDOWN){
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
