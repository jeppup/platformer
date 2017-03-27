package com.example.jesper.platformer.GL;

import android.graphics.PointF;

/**
 * Created by Jesper on 2017-03-27.
 */

public class GLViewport extends GLGameObject{
    private int mScreenWidth;
    private int mScreenHeight;
    private float mMetersToShowX;
    private float mMetersToShowY;
    private float mHalfDistX;
    private float mHalfDistY;
    private GLGameObject mTarget = null;
    float[] mViewMatrix = new float[4*4];


    public GLViewport(final int screenWidth, final int screenHeight, final int metersToShowX, final int metersToShowY){
        setViewport(screenWidth, screenHeight, metersToShowX, metersToShowY);
    }

    public void setViewport(final int screenWidth, final int screenHeight, final int metersToShowX, final int metersToShowY){
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        mPos = new PointF(0,0);
        setMetersToShow(metersToShowX, metersToShowY);
    }



    private void setMetersToShow(final float metersToShowX, final float metersToShowY){
        if(metersToShowX < 1 && metersToShowY < 1) throw new IllegalArgumentException("One of the dimensions must be provided");

        mMetersToShowX = metersToShowX;
        mMetersToShowY = metersToShowY;

        if(metersToShowX > 0 && mMetersToShowY > 0){
            //Both well defined
        }else if(metersToShowY > 0){
            mMetersToShowX = ((float) mScreenWidth / mScreenHeight) * metersToShowY;
        }else{
            mMetersToShowY = ((float) mScreenHeight / mScreenWidth) * metersToShowX;
        }

        mHalfDistX = (mMetersToShowX  / 2);
        mHalfDistY = (mMetersToShowY / 2);
    }

    public int getScreenWidth() {
        return mScreenWidth;
    }
    public int getScreenHeight(){
        return mScreenHeight;
    }
    public float getAspectRatio(){
        return mScreenWidth / mScreenHeight;
    }


    public void setWorldCentre(final PointF pos){
        mPos.x = pos.x;
        mPos.y = pos.y;
    }

    public synchronized void setTarget(final GLGameObject go){
        mTarget = go;
    }

    public final float[] getViewMatrix(){
        return mViewMatrix;
    }

    @Override
    public void draw(final float[] viewMatrix){

    }

    @Override
    public void update(float dt){
        if(mTarget !=null){
            mPos.x += (mTarget.mPos.x - mPos.x) * 1f;
            mPos.y += (mTarget.mPos.y - mPos.y) * 1f;
        }

        final float NEAR = 0f;
        final float FAR = 1f;
        final float LEFT = mPos.x - mHalfDistX;
        final float RIGHT = mPos.x + mHalfDistX;
        final float TOP = mPos.y + mHalfDistY;
        final float BOTTOM = mPos.y - mHalfDistY;
        synchronized (mViewMatrix){
            android.opengl.Matrix.orthoM(mViewMatrix, 0, LEFT, RIGHT, BOTTOM, TOP, NEAR, FAR);
        }
    }

    public boolean inView(final PointF worldPos, final float objectWidth, final float objectHeight) {
        float maxX = (mPos.x + mHalfDistX);
        float minX = (mPos.x - mHalfDistX) - objectWidth;
        float maxY = (mPos.y + mHalfDistY);
        float minY  = (mPos.y - mHalfDistY) - objectHeight;
        if((worldPos.x > minX && worldPos.x < maxX)
                && (worldPos.y > minY && worldPos.y < maxY)){
            return true;
        }
        return false;
    }
}
