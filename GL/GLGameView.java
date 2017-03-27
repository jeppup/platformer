package com.example.jesper.platformer.GL;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import com.example.jesper.platformer.FrameTimer;
import com.example.jesper.platformer.engine.IGameView;

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
    private ArrayList<GLGameObject> mGameObjects = new ArrayList<>();
    private GLViewport mViewPort = null;

    private FrameTimer mTimer = new FrameTimer();

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
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        float red = Color.red(BG_COLOR) / 255f;
        float blue = Color.blue(BG_COLOR) / 255f;
        float green = Color.green(BG_COLOR) / 255f;
        float alpha = 1.0f;
        GLManager.buildProgram();
        GLES20.glClearColor(red, green, blue, alpha);
        GLES20.glLineWidth(10);
        GLES20.glUseProgram(GLManager.mProgram);
        mTimer.reset();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mTimer.reset();
    }

    private void render() {
        float[] viewMatrix = mViewPort.getViewMatrix();
        int count = mGameObjects.size();
        for(int i = 0; i < count; i++){
            mGameObjects.get(i).draw(viewMatrix);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        render();
    }

    @Override
    public void setGameObjects(ArrayList<GLGameObject> gameObjects) {
        mGameObjects = gameObjects;

        int count = mGameObjects.size();
        for(int i = 0; i < count; i++){
            if(GLViewport.class.isInstance(mGameObjects.get(i)))
            {
                mViewPort = (GLViewport)mGameObjects.get(i);
            }
        }
    }
}
