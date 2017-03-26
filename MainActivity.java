package com.example.jesper.platformer;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.example.jesper.platformer.engine.GameEngine;
import com.example.jesper.platformer.engine.GameView;
import com.example.jesper.platformer.engine.IGameView;
import com.example.jesper.platformer.inputs.Accelerometor;
import com.example.jesper.platformer.inputs.CompositeControl;
import com.example.jesper.platformer.inputs.Gamepad;
import com.example.jesper.platformer.inputs.InputManager;
import com.example.jesper.platformer.inputs.VirtualGamepad;
import com.example.jesper.platformer.inputs.GamepadListener;
import com.example.jesper.platformer.inputs.VirtualJoystick;

public class MainActivity extends AppCompatActivity implements android.hardware.input.InputManager.InputDeviceListener{
    IGameView mGameView = null;
    GameEngine mGameEngine = null;
    private View mDecorView;
    GamepadListener mGamepadListener = null;
    static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        hideSystemUI();
        setContentView(R.layout.activity_main);
        mGameView = (IGameView)findViewById(R.id.gameView);
        mGameEngine = new GameEngine(this, mGameView);

        /**
        CompositeControl control = new CompositeControl(
                new VirtualGamepad(findViewById(R.id.keypad)),
                new Gamepad(this),
                new VirtualJoystick(findViewById(R.id.virtual_joystick)),
                new Accelerometor(this)
        );
        mGameEngine.setInputManager(control);
        **/

        //mGameEngine.setInputManager(new VirtualGamepad(findViewById(R.id.keypad)));
        //mGameEngine.setInputManager(new Gamepad(this));
        //mGameEngine.setInputManager(new VirtualJoystick(findViewById(R.id.virtual_joystick)));
        //mGameEngine.setInputManager(new Accelerometor(this));
    }

    public void setGamepadListener(GamepadListener listener){
        mGamepadListener = listener;
    }

    @Override
    protected void onPause(){
        super.onPause();
        mGameEngine.pauseGame();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(isGameControllerConnected()){
            Log.d(TAG, "Gamepad detected!");
        }
        mGameEngine.resumeGame();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mGameEngine.startGame();
    }

    @Override
    protected void onStop(){
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mGameEngine.stopGame();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus){
            return;
        }
        hideSystemUI();
    }

    private void hideSystemUI(){
        View decorView = getWindow().getDecorView();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }else{
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LOW_PROFILE);
        }
    }

    public boolean isGameControllerConnected() {
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();
            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) ||
                    ((sources & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchGenericMotionEvent(final MotionEvent ev) {
        if(mGamepadListener != null){
            if(mGamepadListener.dispatchGenericMotionEvent(ev)){
                return true;
            }
        }
        return super.dispatchGenericMotionEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(final KeyEvent ev) {
        if(mGamepadListener != null){
            if(mGamepadListener.dispatchKeyEvent(ev)){
                return true;
            }
        }
        return super.dispatchKeyEvent(ev);
    }

    @Override
    public void onInputDeviceAdded(int deviceId) {
        Log.d(TAG, "onInputDeviceAdded");
    }

    @Override
    public void onInputDeviceRemoved(int deviceId) {
        Log.d(TAG, "onInputDeviceRemoved");
    }

    @Override
    public void onInputDeviceChanged(int deviceId) {
        Log.d(TAG, "onInputDeviceChanged");
    }
}
