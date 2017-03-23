package com.example.jesper.platformer.engine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.jesper.platformer.Config;
import com.example.jesper.platformer.SoundManager;
import com.example.jesper.platformer.Viewport;
import com.example.jesper.platformer.gameobjects.GameObject;
import com.example.jesper.platformer.inputs.InputManager;
import com.example.jesper.platformer.inputs.NullInput;
import com.example.jesper.platformer.levels.LevelManager;

import java.util.ArrayList;

/**
 * Created by Jesper on 2017-03-20.
 */

public class GameEngine {
    private Activity mActivity = null;
    private RenderThread mRenderThread = null;
    private UpdateThread mUpdateThread = null;
    public ArrayList<GameObject> mGameObjects = new ArrayList();
    public InputManager mControl = null;
    public LevelManager mLevelManager = null;
    public SoundManager mSoundManager = null;
    public GameObject mPlayer = null;
    Viewport mCamera = null;
    private IGameView mGameView = null;
    private Config mConfig = null;

    public GameEngine(Activity activity, IGameView gameView){
        mActivity = activity;
        mControl = new NullInput();
        mConfig = new Config(activity.getApplicationContext());
        mSoundManager = new SoundManager(mActivity);
        mGameView = gameView;
        mCamera = mGameView.createViewPort();
        loadLevel();
        resetFocus();
    }

    private void loadLevel(){
        mLevelManager = new LevelManager(this);
        mLevelManager.progressLevel();
        mPlayer = mLevelManager.mPlayer;
        mGameView.setGameObjects(mGameObjects);
    }

    public void update(float dt){
        if(lostOrCompleted()){
            resetFocus();
            return;
        }

        mCamera.update(dt);
        int count = mGameObjects.size();
        for(int i = 0; i < count; i++){
            mGameObjects.get(i).update(dt);
            /*if(mCamera.inView(go.mWorldLocation, go.mWidth, go.mHeight)){
                mActiveEntities.add(go);
            }*/
        }

        doCollisionChecks();
    }

    public void render(){
        mGameView.render();
    }

    private void doCollisionChecks(){
        int count = mGameObjects.size();
        GameObject a, b;
        for(int i = 0; i < count-1; i++){
            a = mGameObjects.get(i);
            for(int j = i+1; j < count; j++){
                b = mGameObjects.get(j);
                if(a.isColliding(b)){
                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }
    }

    public void startGame(){
        stopGame();
        if(mControl != null){
            mControl.onStart();
        }
        mUpdateThread = new UpdateThread(this);
        mUpdateThread.start();

        mRenderThread = new RenderThread(this);
        mRenderThread.start();
    }

    public void stopGame(){
        if(mUpdateThread != null){
            mUpdateThread.stopGame();
        }
        if(mRenderThread != null){
            mRenderThread.stopGame();
        }
        if(mControl != null){
            mControl.onStop();
        }
    }

    public void pauseGame(){
        if(mUpdateThread != null){
            mUpdateThread.pauseGame();
        }
        if(mRenderThread != null){
            mRenderThread.pauseGame();
        }
        if(mControl != null){
            mControl.onPause();
        }
    }

    public void resumeGame(){
        if(mUpdateThread != null){
            mUpdateThread.resumeGame();
        }
        if(mRenderThread != null){
            mRenderThread.resumeGame();
        }
        if(mControl != null){
            mControl.onResume();
        }
    }


    public void setScreenCoordinate(final PointF worldLocation, Point screenCord){
        mCamera.worldToScreen(worldLocation, screenCord);
    }


    private void resetFocus(){
        mCamera.setWorldCentre(mPlayer.mWorldLocation);
        mCamera.setTarget(mPlayer);
    }

    private boolean lostOrCompleted(){
        if(mLevelManager.levelCompleted()){
            mLevelManager.progressLevel();
            return true;
        }
        else if(mLevelManager.levelLost()){
            mLevelManager.restartLevel();
            return true;
        }

        return false;
    }

    public void setInputManager(InputManager input){
        mControl = input;
    }
    public Bitmap getBitmap(int tileType){
        return mLevelManager.getBitmap(tileType);
    }
    public int getPixelsPerMeter(){
        return mCamera.getPixelsPerMetreX();
    }
    public Config getConfig(){ return mConfig; }
    public Context getContext(){ return mActivity; }
    public String[] getDebugStrings(){

        return new String[]{
                "Ticks/s: " + mUpdateThread.getAverageFPS(),
                "Frames/s: " + mRenderThread.getAverageFPS(),
                "Objects rendered: " + mCamera.mInViewCount + " / " + mGameObjects.size(),
                mCamera.toString(),
                "Player : [" + mPlayer.mWorldLocation.x + ", " + mPlayer.mWorldLocation.y + "]"
        };
    }
}
