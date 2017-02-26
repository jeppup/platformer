package com.example.jesper.platformer;

/**
 * Created by Jesper on 2017-02-26.
 */

public abstract class InputManager {
    public float mVerticalFactor = 0.0f;
    public float mHorizontalFactor = 0.0f;
    public boolean mIsJumping = false;

    public void onStart() {};
    public void onStop() {};
    public void onPause() {};
    public void onResume() {};
}
