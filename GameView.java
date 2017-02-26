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

/**
 * Created by Jesper on 2017-02-18.
 * Player art: https://bakudas.itch.io/generic-platformer-pack
 * World tiles: http://opengameart.org/content/nature-tileset
 */

public class GameView extends SurfaceView implements Runnable{
    private static final String TAG = "GameView";
    private static final int BG_COLOR = Color.rgb(135, 206, 235);

    private volatile boolean mIsRunning = false;
    private Thread mGameThread = null;
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    Context mContext = null;
    Paint mPaint = null;


    Viewport mCamera = null;
    LevelManager mLevelManager = null;
    InputManager mControl = null;


    private static final int METERS_TO_SHOW_X = 16;
    private static final int METERS_TO_SHOW_Y = 9;
    private static final int STAGE_WIDTH = 1920/3;
    private static final int STAGE_HEIGHT = 1080/3;
    private static final boolean SCALE_CONTENT = true;

    private PointF mCameraPos = new PointF(0,0);
    private float mScrollSpeed = 2.0f; //meters per second

    private boolean mDebugging = true;
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
        mPaint = new Paint();
        mSurfaceHolder = getHolder();
        mFrameTimer = new FrameTimer();
        mControl = new NullInput();
        createViewPort();
        loadLevel("LevelName");
    }

    private void createViewPort(){
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        if(SCALE_CONTENT){
            mSurfaceHolder.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT);
            screenWidth = STAGE_WIDTH;
            screenHeight = STAGE_HEIGHT;
        }

        mCamera = new Viewport(screenWidth, screenHeight, METERS_TO_SHOW_X, METERS_TO_SHOW_Y);
    }

    public void setInputManager(InputManager input){
        mControl = input;
    }

    private void loadLevel(String levelName){
        mLevelManager = new LevelManager(this, levelName);
        mCameraPos.x = mLevelManager.mPlayer.mWorldLocation.x;
        mCameraPos.y = mLevelManager.mPlayer.mWorldLocation.y;

        mCamera.setWorldCentre(mLevelManager.mPlayer.mWorldLocation);
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

    private void update(float secondsPassed){
        for(GameObject go : mLevelManager.mGameObjects){
            go.update(secondsPassed);
            go.mVisible = mCamera.inView(go.mWorldLocation, go.mWidth, go.mHeight);
        }

        mCameraPos.x += (mControl.mHorizontalFactor * mScrollSpeed) * secondsPassed;
        mCameraPos.y += (mControl.mVerticalFactor * mScrollSpeed) * secondsPassed;
        mCamera.setWorldCentre(mCameraPos);
    }

    private void render(){
        if(!lockAndSetCanvas()){
            Log.d(TAG, "CANVAS FAILED");
            return;
        }

        Point screenCord = new Point();
        mCanvas.drawColor(BG_COLOR);
        mPaint.setColor(Color.WHITE);
        for (GameObject go : mLevelManager.mGameObjects){
            if(!go.mVisible){
                continue;
            }
            mCamera.worldToScreen(go.mWorldLocation, screenCord);
            Bitmap b = mLevelManager.getBitmap(go.mType);
            mCanvas.drawBitmap(b, screenCord.x, screenCord.y, mPaint);
        }


        if(mDebugging){
            doDebugDrawing();
        }

        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
    }

    private void doDebugDrawing(){
        int y = 60;
        int textSize = 10;
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(textSize);
        mPaint.setColor(Color.WHITE);
        mCanvas.drawText("FPS: " + mFrameTimer.getCurrentFps(), 10, y, mPaint);
        y += textSize;
        mCanvas.drawText("Sprites: " + mLevelManager.mGameObjects.size(), 10, y, mPaint);
        y += textSize;
        mCanvas.drawText("Clipped: " + mCamera.getClipCount(), 10, y, mPaint);

        y += textSize;
        mCanvas.drawText("[" + mControl.mHorizontalFactor + " : " + mControl.mVerticalFactor + " J: " + mControl.mIsJumping + "]", 10, y, mPaint);

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
        //mTimer.reset()
        while(mIsRunning){
            update(mFrameTimer.onEnterFrame());
            render();
            //mTimer.endFrame();
        }
    }

    public int getPixelsPerMeter(){
        return mCamera.getPixelsPerMetreX();
    }
}
