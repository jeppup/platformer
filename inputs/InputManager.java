package com.example.jesper.platformer.inputs;

/**
 * Created by Jesper on 2017-02-26.
 */

public abstract class InputManager {
    public float mVerticalFactor = 0.0f;
    public float mHorizontalFactor = 0.0f;
    public boolean mIsJumping = false;

    public static final float MIN = -1.0f;
    public static final float MAX = 1.0f;

    protected void clampInputs(){
        if(mVerticalFactor < MIN){
            mVerticalFactor = MIN;
        }else if(mVerticalFactor > MAX){
            mVerticalFactor = MAX;
        }

        if(mHorizontalFactor < MIN){
            mHorizontalFactor = MIN;
        }else if(mHorizontalFactor > MAX){
            mHorizontalFactor = MAX;
        }
    }

    public void update(float dt){}


    public void onStart() {};
    public void onStop() {};
    public void onPause() {};
    public void onResume() {};
}
