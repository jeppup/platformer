package com.example.jesper.platformer.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.jesper.platformer.Config;
import com.example.jesper.platformer.gameobjects.GameObject;
import com.example.jesper.platformer.gui.GuiManager;
import com.example.jesper.platformer.Viewport;

import java.util.ArrayList;

/**
 * Created by Jesper on 2017-02-18.
 * School project
 * Player art: https://bakudas.itch.io/generic-platformer-pack
 * Enemy art: http://opengameart.org/content/more-rpg-enemies Stephen Challener (Redshrike)
 * World tiles: http://opengameart.org/content/nature-tileset
 */

public class GameView extends SurfaceView implements IGameView{
    private static final String TAG = "GameView";

    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    Context mContext = null;
    Paint mPaint = null;
    private Config mConfig;
    private ArrayList<GameObject> mGameObjects = new ArrayList();
    public GuiManager mGuiManager = null;
    Viewport mCamera = null;

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

    public void setGameObjects(ArrayList<GameObject> gameObjects){
        mGameObjects = gameObjects;
    }

    private void init(Context context){
        mContext = context;
        mConfig = new Config(context);
        mPaint = new Paint();
        mSurfaceHolder = getHolder();
        createViewPort();
        mGuiManager = new GuiManager(context, mConfig);
    }

    public Viewport createViewPort(){
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        if(mConfig.GW_SCALE_CONTENT){
            mSurfaceHolder.setFixedSize(mConfig.GW_STAGE_WIDTH, mConfig.GW_STAGE_HEIGHT);
            screenWidth = mConfig.GW_STAGE_WIDTH;
            screenHeight = mConfig.GW_STAGE_HEIGHT;
        }

        mCamera = new Viewport(mConfig, screenWidth, screenHeight, mConfig.GW_METERS_TO_SHOW_X, mConfig.GW_METERS_TO_SHOW_Y);
        return mCamera;
    }

    public void render(){
        if(!lockAndSetCanvas()){
            return;
        }

        mCanvas.drawColor(mConfig.GW_BACKGROUND_COLOR);
        mPaint.setColor(Color.WHITE);
        mGuiManager.render(mCanvas, mPaint);
        int count = mGameObjects.size();
        GameObject temp;
        for(int i = 0; i < count; i++){
            temp = mGameObjects.get(i);
            if(mCamera.inView(temp.mWorldLocation, temp.mWidth, temp.mHeight)){
                temp.render(mCanvas, mPaint);
            }
        }

        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
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
}
