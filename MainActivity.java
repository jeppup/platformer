package com.example.jesper.platformer;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.jesper.platformer.engine.GameEngine;
import com.example.jesper.platformer.engine.GameView;
import com.example.jesper.platformer.inputs.VirtualGamepad;

public class MainActivity extends AppCompatActivity {
    GameView mGameView = null;
    GameEngine mGameEngine = null;
    private View mDecorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        hideSystemUI();
        setContentView(R.layout.activity_main);
        mGameView = (GameView)findViewById(R.id.gameView);
        mGameEngine = new GameEngine(this, mGameView);
        mGameEngine.setInputManager(new VirtualGamepad(findViewById(R.id.keypad)));
    }

    @Override
    protected void onPause(){
        super.onPause();
        mGameEngine.pauseGame();
    }

    @Override
    protected void onResume(){
        super.onResume();
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
}
