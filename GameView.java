package com.example.jesper.platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by Jesper on 2017-02-18.
 * School project
 * Player art: https://bakudas.itch.io/generic-platformer-pack
 * Enemy art: http://opengameart.org/content/more-rpg-enemies Stephen Challener (Redshrike)
 * World tiles: http://opengameart.org/content/nature-tileset
 */

public class GameView extends SurfaceView implements Runnable{
    private static final String TAG = "GameView";

    private volatile boolean mIsRunning = false;
    private Thread mGameThread = null;
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    Context mContext = null;
    Paint mPaint = null;
    Viewport mCamera = null;
    LevelManager mLevelManager = null;
    SoundManager mSoundManager = null;
    InputManager mControl = null;
    GuiManager mGuiManager = null;
    private static final ArrayList<GameObject> mActiveEntities = new ArrayList<GameObject>();
    private Config mConfig;

    private boolean mDebugging = false;
    private FrameTimer mFrameTimer;

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        mConfig = new Config(context);
        mGuiManager = new GuiManager(mContext, mConfig);
        mSoundManager = new SoundManager(context);
        mPaint = new Paint();
        mSurfaceHolder = getHolder();
        mFrameTimer = new FrameTimer();
        mControl = new NullInput();
        createViewPort();
        mLevelManager = new LevelManager(this);
        mLevelManager.progressLevel();
        resetFocus();
    }

    private void createViewPort(){
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        if(mConfig.GW_SCALE_CONTENT){
            mSurfaceHolder.setFixedSize(mConfig.GW_STAGE_WIDTH, mConfig.GW_STAGE_HEIGHT);
            screenWidth = mConfig.GW_STAGE_WIDTH;
            screenHeight = mConfig.GW_STAGE_HEIGHT;
        }

        mCamera = new Viewport(mConfig, screenWidth, screenHeight, mConfig.GW_METERS_TO_SHOW_X, mConfig.GW_METERS_TO_SHOW_Y);
    }

    public void setInputManager(InputManager input){
        mControl = input;
    }

    public Bitmap getBitmap(int tileType){
        return mLevelManager.getBitmap(tileType);
    }

    public void setScreenCoordinate(final PointF worldLocation, Point screenCord){
        mCamera.worldToScreen(worldLocation, screenCord);
    }


    private void resetFocus(){
        mCamera.setWorldCentre(mLevelManager.mPlayer.mWorldLocation);
        mCamera.setTarget(mLevelManager.mPlayer);
        mGuiManager.startGameGui();
    }

    public void pause(){
        mIsRunning = false;
        try{
            mGameThread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void resume(){
        mIsRunning = true;
        mGameThread = new Thread(this);
        try{
            mGameThread.start();
        }catch(IllegalThreadStateException e){
            e.printStackTrace();
        }
    }

    public void destroy(){

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

    private void update(float secondsPassed){
        if(lostOrCompleted()){
            resetFocus();
            return;
        }


        mActiveEntities.clear();
        mCamera.update(secondsPassed);
        for(GameObject go : mLevelManager.mGameObjects){
            go.update(secondsPassed);
            if(mCamera.inView(go.mWorldLocation, go.mWidth, go.mHeight)){
                mActiveEntities.add(go);
            }
        }

        doCollisionChecks();
        mGuiManager.update(secondsPassed);
    }

    private void doCollisionChecks(){
        int count = mLevelManager.mGameObjects.size();
        GameObject a, b;
        for(int i = 0; i < count-1; i++){
            a = mLevelManager.mGameObjects.get(i);
            for(int j = i+1; j < count; j++){
                b = mLevelManager.mGameObjects.get(j);
                if(a.isColliding(b)){
                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }
    }

    private void render(){
        if(!lockAndSetCanvas()){
            Log.d(TAG, "CANVAS FAILED");
            return;
        }

        mCanvas.drawColor(mConfig.GW_BACKGROUND_COLOR);
        mPaint.setColor(Color.WHITE);
        for (GameObject go : mActiveEntities){
            go.render(mCanvas, mPaint);
        }

        mGuiManager.render(mCanvas, mPaint);


        if(mDebugging){
            doDebugDrawing();
        }

        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
    }

    private void doDebugDrawing(){
        int y = 60;
        int textSize = 20;
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(textSize);
        mPaint.setColor(Color.WHITE);
        mCanvas.drawText("FPS: " + mFrameTimer.getCurrentFps(), 10, y, mPaint);
        y += textSize;
        mCanvas.drawText("Sprites: " + mLevelManager.mGameObjects.size(), 10, y, mPaint);
        y += textSize;
        mCanvas.drawText("Clipped: " + (mLevelManager.mGameObjects.size() -  mActiveEntities.size()), 10, y, mPaint);

        y += textSize;
        mCanvas.drawText("[" + mControl.mHorizontalFactor + " : " + mControl.mVerticalFactor + " J: " + mControl.mIsJumping + "]", 10, y, mPaint);

        y += textSize;
        mCanvas.drawText("Collected " + Target.getCollectedCount() + " / " + Target.getTotalCount(), 10, y, mPaint);

        y += textSize;
        mCanvas.drawText("HP: " + Player.getHitPoints() + " / " + Player.getMaxHitPoints(), 10, y, mPaint);

        y+= textSize;
        mCanvas.drawText("Player location: " + mLevelManager.mPlayer.mWorldLocation.x + ", " + mLevelManager.mPlayer.mWorldLocation.y, 10, y, mPaint);
        mCamera.resetClipCount();
    }

    private boolean lockAndSetCanvas(){
        if(!mSurfaceHolder.getSurface().isValid()){
            return false;
        }
        mCanvas = mSurfaceHolder.lockCanvas();
        if(mCanvas == null){
            return false;
        }
        return true;
    }
    @Override
    public void run(){
        while(mIsRunning){
            update(mFrameTimer.onEnterFrame());
            render();
        }
    }

    public int getPixelsPerMeter(){
        return mCamera.getPixelsPerMetreX();
    }

    public Config getConfig(){
        if(mConfig == null){
            throw new NullPointerException("Config not initialized!");
        }

        return mConfig;
    }
}
