package com.example.jesper.platformer.engine;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import com.example.jesper.platformer.Config;
import com.example.jesper.platformer.GL.GLBorder;
import com.example.jesper.platformer.GL.GLGameObject;
import com.example.jesper.platformer.GL.GLGameView;
import com.example.jesper.platformer.GL.GLSpaceship;
import com.example.jesper.platformer.GL.GLStar;
import com.example.jesper.platformer.GL.GLViewport;
import com.example.jesper.platformer.SoundManager;
import com.example.jesper.platformer.inputs.InputManager;
import com.example.jesper.platformer.inputs.NullInput;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jesper on 2017-03-20.
 */

public class GameEngine {
    private Activity mActivity = null;
    private UpdateThread mUpdateThread = null;

    public ArrayList<GLGameObject> mGameObjects = new ArrayList();
    private ArrayList<GLGameObject> mObjectsToAdd = new ArrayList<GLGameObject>();
    private ArrayList<GLGameObject> mObjectsToRemove = new ArrayList<GLGameObject>();

    private static final int METERS_TO_SHOW_X = 0;
    private static final int METERS_TO_SHOW_Y = 180;
    public static float mWorldWidth = 1280f;
    public static float mWorldHeight = 720f;
    public static final int starCount = 500;

    public final static Random r = new Random();

    public InputManager mControl = null;
    public GLSpaceship mPlayer = null;
    GLViewport mCamera = null;

    private IGameView mGameView = null;
    private Config mConfig = null;

    public GameEngine(Activity activity, IGameView gameView){
        mActivity = activity;
        mControl = new NullInput();
        mConfig = new Config(activity);
        mGameView = gameView;
        //mCamera = mGameView.createViewPort(mConfig.GW_METERS_TO_SHOW_X, mConfig.GW_METERS_TO_SHOW_Y, mConfig.GW_SCALE_FACTOR);
        initGame();
    }

    private void initGame(){
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        mCamera = new GLViewport(width, height, METERS_TO_SHOW_X, METERS_TO_SHOW_Y);

        mPlayer = new GLSpaceship(this, mWorldWidth/2, mWorldHeight/2);
        GLBorder border = new GLBorder(mWorldWidth, mWorldHeight);

        mGameObjects.add(mPlayer);
        mGameObjects.add(border);
        mGameObjects.add(mCamera);

        for(int i = 0; i < starCount; i++){
            int x = r.nextInt((int)mWorldWidth);
            int y = r.nextInt((int)mWorldHeight);
            mGameObjects.add(new GLStar(x, y));
        }
        mGameView.setGameObjects(mGameObjects);
        mCamera.setTarget(mPlayer);
    }

    public void update(float dt){
        mControl.update(dt);
        int count = mGameObjects.size();
        for(int i = 0; i < count; i++){
            mGameObjects.get(i).update(dt);
        }

        doCollisionChecks();

        synchronized (mGameObjects){
            GLGameObject temp;
            while(!mObjectsToRemove.isEmpty()){
                temp = mObjectsToRemove.remove(0);
                mGameObjects.remove(temp);
            }
            while(!mObjectsToAdd.isEmpty()){
                temp = mObjectsToAdd.remove(0);
                addGameObjectNow(temp);
            }
        }
    }

    private void doCollisionChecks(){
        return;

/*        int count = mGameObjects.size();
        GLGameObject a, b;
        for(int i = 0; i < count-1; i++){
            a = mGameObjects.get(i);
            for(int j = i+1; j < count; j++){
                b = mGameObjects.get(j);
                if(a.isColliding(b)){
                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }*/
    }

    public void addGameObject(final GLGameObject gameObject){
        if(isRunning()){
            mObjectsToAdd.add(gameObject);
        } else {
            addGameObjectNow(gameObject);
        }
    }

    public void removeGameObject(final GLGameObject gameObject){
        mObjectsToRemove.add(gameObject);
    }

    public void startGame(){
        stopGame();
        if(mControl != null){
            mControl.onStart();
        }

        mUpdateThread = new UpdateThread(this);
        mUpdateThread.start();
    }

    public void stopGame(){
        if(mUpdateThread != null){
            mUpdateThread.stopGame();
        }
        if(mControl != null){
            mControl.onStop();
        }
    }

    public void pauseGame(){
        if(mUpdateThread != null){
            mUpdateThread.pauseGame();
        }

        if(mControl != null){
            mControl.onPause();
        }
    }

    public void resumeGame(){
        if(mUpdateThread != null){
            mUpdateThread.resumeGame();
        }
        if(mControl != null){
            mControl.onResume();
        }
    }


    public void setInputManager(InputManager input){
        mControl = input;
    }
    public Config getConfig(){ return mConfig; }
    public Context getContext(){ return mActivity; }


    private void addGameObjectNow(final GLGameObject object){
        mGameObjects.add(object);
    }

    public boolean isRunning(){
        return mUpdateThread != null &&  mUpdateThread.isGameRunning();
    }

    public boolean isPaused(){
        return mUpdateThread != null && mUpdateThread.isGamePaused();
    }
}
