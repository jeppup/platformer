package com.example.jesper.platformer.GL;

import com.example.jesper.platformer.Utils;
import com.example.jesper.platformer.engine.GameEngine;

/**
 * Created by Jesper on 2017-03-27.
 */

public class GLSpaceship extends GLGameObject {
    private static final float TO_RADIANS = ((float)Math.PI / 180f);
    private GameEngine mEngine = null;
    private float mMaxRotationSpeed = 200f;
    private float mThrust = 1.0f;
    private float mVelX = 0f;
    private float mVelY = 0f;
    private float mMaxVelocity =  6f;
    private float mFriction = 0.98f;

    public GLSpaceship(GameEngine engine, float x, float y){
        mEngine = engine;
        mPos.x = x;
        mPos.y = y;
        float width = 8f;
        float height = 20f;
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;
        float[] vertices = new float[]{
                -halfWidth, -halfHeight, 0,
                halfWidth, -halfHeight, 0,
                0, halfHeight, 0
        };
        mModel = new GLModel(vertices, null);
    }

    @Override
    public void update(float dt){
        mRotation += mEngine.mControl.mHorizontalFactor * mMaxRotationSpeed * dt;
        float accel = mEngine.mControl.mVerticalFactor * mThrust;
        float angle = (mRotation - 90)* TO_RADIANS;
        float ax = (float)Math.cos(angle) * accel;
        float ay = (float)Math.sin(angle) * accel;

        mVelX += ax * dt;
        mVelY += ay * dt;

        mVelX = Utils.clamp(mVelX, -mMaxVelocity, mMaxVelocity);
        mVelY = Utils.clamp(mVelY, -mMaxVelocity, mMaxVelocity);

        mPos.x += mVelX;
        mPos.y += mVelY;

        mVelX *= mFriction;
        mVelY *= mFriction;


        /*mRotation = Utils.sineWave(0, 80, mVR);
        float xRadius = GameEngine.mWorldWidth / 2;
        float yRadius = GameEngine.mWorldHeight / 2;

        mPos.x = Utils.cosWave(GameEngine.mWorldWidth / 2, xRadius, mVR);
        mPos.y = Utils.sineWave(GameEngine.mWorldHeight / 2, yRadius, mVR);

        mVR += 0.02f;*/
    }
}
