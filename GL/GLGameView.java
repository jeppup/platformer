package com.example.jesper.platformer.GL;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import com.example.jesper.platformer.FrameTimer;
import com.example.jesper.platformer.Viewport;
import com.example.jesper.platformer.engine.IGameView;
import com.example.jesper.platformer.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jesper on 2017-03-26.
 */

public class GLGameView extends GLSurfaceView implements IGameView, GLSurfaceView.Renderer {
    private static final int METERS_TO_SHOW_X = 0;
    private static final int METERS_TO_SHOW_Y = 180;
    private static final int BG_COLOR = Color.rgb(135, 206,235);
    private GLModel triangle = null;
    GLGameObject go = null;
    private ArrayList<GLGameObject> mGameObjects = new ArrayList<>();

    private GLViewport mViewPort = null;
    int mScreenWidth = 0;
    int mScreenHeight = 0;
    float mWorldWidth = 1280f;
    float mWorldHeight = 720f;
    private FrameTimer mTimer = new FrameTimer();
    private Handler mHandler = new Handler();

    public GLGameView(Context context) {
        super(context);
        init();
    }

    public GLGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setEGLContextClientVersion(2);
        setRenderer(this);

        go = new GLSpaceship(mWorldWidth/2, mWorldHeight/2);
        mGameObjects.add(go);

        Random r = new Random();
        int starCount = 500;
        for(int i = 0; i < starCount; i++){
            int x = r.nextInt((int)mWorldWidth);
            int y = r.nextInt((int)mWorldHeight);
            mGameObjects.add(new GLStar(x, y));
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        float red = Color.red(BG_COLOR) / 255f;
        float blue = Color.blue(BG_COLOR) / 255f;
        float green = Color.green(BG_COLOR) / 255f;
        float alpha = 1.0f;
        GLES20.glClearColor(red, green, blue, alpha);
        GLManager.buildProgram();
        GLES20.glUseProgram(GLManager.mProgram);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mScreenHeight = height;
        mScreenWidth = width;
        mViewPort = new GLViewport(mScreenWidth, mScreenHeight, METERS_TO_SHOW_X, METERS_TO_SHOW_Y);
        mViewPort.setTarget(go);
        GLES20.glViewport(0, 0, mScreenWidth, mScreenHeight);
        mTimer.reset();

        final int delay = 500;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Avg. fps", "" + mTimer.getAverageFPS());
                mHandler.postDelayed(this, delay);
            }
        }, delay);
    }

    public void update(float dt){
        mViewPort.update(dt);
        go.update(dt);

        int count = mGameObjects.size();
        for(int i = 0; i < count; i++){
            mGameObjects.get(i).update(dt);
        }
    }

    @Override
    public void render() {
        go.draw(mViewPort.getViewMatrix());

        float[] viewMatrix = mViewPort.getViewMatrix();
        int count = mGameObjects.size();
        for(int i = 0; i < count; i++){
            mGameObjects.get(i).draw(viewMatrix);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        float dt = mTimer.onEnterFrame();
        update(dt);
        render();
    }

    @Override
    public void setGameObjects(ArrayList<GameObject> gameObjects) {

    }



    @Override
    public Viewport createViewPort(float metersToShowX, float metersToShowY, float scaleFactor) {
        return null;
    }
}
