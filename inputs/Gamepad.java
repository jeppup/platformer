package com.example.jesper.platformer.inputs;

import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.jesper.platformer.MainActivity;
import com.example.jesper.platformer.Utils;

/**
 * Created by Jesper on 2017-03-25.
 */

public class Gamepad extends InputManager implements GamepadListener {
    MainActivity mMainActivity = null;
    public Gamepad(MainActivity activity) {
        mMainActivity = activity;
        mMainActivity.setGamepadListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mMainActivity != null) {
            mMainActivity.setGamepadListener(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mMainActivity != null) {
            mMainActivity.setGamepadListener(null);
        }
    }
    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if((event.getSource() & InputDevice.SOURCE_JOYSTICK) != InputDevice.SOURCE_JOYSTICK){
            return false; //we don't consume this event
        }
        mHorizontalFactor = getInputFactor(event, MotionEvent.AXIS_X, MotionEvent.AXIS_HAT_X);
        mVerticalFactor = getInputFactor(event, MotionEvent.AXIS_Y, MotionEvent.AXIS_HAT_Y);
        return true; //we did consume this event
    }

    private float getInputFactor(final MotionEvent event, final int axis, final int fallbackAxis){
        InputDevice device = event.getDevice();
        int source = event.getSource();
        float result = event.getAxisValue(axis);
        InputDevice.MotionRange range = device.getMotionRange(axis, source);
        if(Math.abs(result) <= range.getFlat()){
            result = event.getAxisValue(fallbackAxis);
            range = device.getMotionRange(fallbackAxis, source);
            if(Math.abs(result) <= range.getFlat()){
                result = 0.0f;
            }
        }
        return Utils.clamp(result, -1f, 1f);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        boolean wasConsumed = false;
        if(action == MotionEvent.ACTION_DOWN){
            if(keyCode == KeyEvent.KEYCODE_DPAD_UP){
                mVerticalFactor -= 1;
                wasConsumed = true;
            }else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                mVerticalFactor += 1;
                wasConsumed = true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                mHorizontalFactor -= 1;
                wasConsumed = true;
            } else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                mHorizontalFactor += 1;
                wasConsumed = true;
            }
            if(isJumpKey(keyCode)){
                mIsJumping = true;
                wasConsumed = true;
            }
        } else if(action == MotionEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                mVerticalFactor += 1;
                wasConsumed = true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                mVerticalFactor -= 1;
                wasConsumed = true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                mHorizontalFactor += 1;
                wasConsumed = true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                mHorizontalFactor -= 1;
                wasConsumed = true;
            }
            if(isJumpKey(keyCode)){
                mIsJumping = false;
                wasConsumed = true;
            }
            if (keyCode == KeyEvent.KEYCODE_BUTTON_B) {
                mMainActivity.onBackPressed(); //backwards comp
            }
        }
        return wasConsumed;
    }

    public boolean isJumpKey(final int keyCode){
        return keyCode == KeyEvent.KEYCODE_DPAD_UP
                || keyCode == KeyEvent.KEYCODE_BUTTON_A
                || keyCode == KeyEvent.KEYCODE_BUTTON_X
                || keyCode == KeyEvent.KEYCODE_BUTTON_Y;
    }
}
