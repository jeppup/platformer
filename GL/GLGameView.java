package com.example.jesper.platformer.GL;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.example.jesper.platformer.Viewport;
import com.example.jesper.platformer.engine.IGameView;
import com.example.jesper.platformer.gameobjects.GameObject;

import java.util.ArrayList;

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
    GLGameObject go = new GLGameObject();
    private GLViewport mViewPort = null;
    int mScreenWidth = 0;
    int mScreenHeight = 0;
    float mWorldWidth = 1280f;
    float mWorldHeight = 720f;

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
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mViewPort.update(0f);
        go.draw(mViewPort.getViewMatrix());
    }

    @Override
    public void setGameObjects(ArrayList<GameObject> gameObjects) {

    }

    @Override
    public void render() {

    }

    @Override
    public Viewport createViewPort(float metersToShowX, float metersToShowY, float scaleFactor) {
        return null;
    }
}
