package com.example.jesper.platformer.gameobjects;

import android.graphics.PointF;
import android.util.Log;

import com.example.jesper.platformer.Utils;
import com.example.jesper.platformer.engine.GameEngine;
import com.example.jesper.platformer.engine.GameView;

/**
 * Created by Jesper on 2017-02-26.
 */

public class DynamicGameObject extends GameObject {
    private static final String TAG = "DynamicGameObject";
    private static final float MAX_DELTA = 1.0f;
    private static final float MIN_DELTA = 0.000001f;
    public PointF mVelocity = new PointF(0.0f, 0.0f);
    public PointF mAcceleration = new PointF(0.0f, 0.0f);
    public PointF mTargetSpeed = new PointF(0.0f, 0.0f);
    public float mFriction = 0.98f;

    public DynamicGameObject(GameEngine engine, float x, float y, float width, float height, int type) {
        super(engine, x, y, width, height, type);
    }

    public DynamicGameObject(GameEngine engine, float x, float y, int type) {
        super(engine, x, y, type);
    }

    @Override
    public void update(float deltaTime){
        mVelocity.x *= mFriction;
        mVelocity.y *= mFriction;

        mVelocity.x += mAcceleration.x * mTargetSpeed.x;
        if(Math.abs(mVelocity.x) > Math.abs(mTargetSpeed.x)){
            mVelocity.x = mTargetSpeed.x;
        }
        Log.d("DYNAMICERUU", "dx / dy: " + mVelocity.x + "/" + mVelocity.y);

        mVelocity.x = Utils.clamp(mVelocity.x, -MAX_DELTA, MAX_DELTA);
        mVelocity.y = Utils.clamp(mVelocity.y, -MAX_DELTA, MAX_DELTA);
        if(Math.abs(mVelocity.y) < MIN_DELTA){ mVelocity.y = 0; }
        if(Math.abs(mVelocity.x) < MIN_DELTA){ mVelocity.x = 0;}
        Log.d("DYNAMICERUU", "dx / dy: " + mVelocity.x + "/" + mVelocity.y);
        synchronized(mWorldLocation){
            mWorldLocation.x += mVelocity.x;
            mWorldLocation.y += mVelocity.y;
        }
        updateBounds();
    }
}
