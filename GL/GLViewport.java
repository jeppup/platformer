package com.example.jesper.platformer.GL;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

import com.example.jesper.platformer.Config;

/**
 * Created by Jesper on 2017-03-27.
 */

public class GLViewport {
    private PointF mLookAt;
    private int mScreenWidth;
    private int mScreenHeight;
    private float mMetersToShowX;
    private float mMetersToShowY;
    private float mHalfDistX;
    private float mHalfDistY;
    private GLGameObject mTarget = null;
    float[] mViewMatrix = new float[4*4];


    public GLViewport(final int screenWidth, final int screenHeight, final int metersToShowX, final int metersToShowY){
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        mLookAt = new PointF(0,0);
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
        mLookAt.x = pos.x;
        mLookAt.y = pos.y;
    }

    public synchronized void setTarget(final GLGameObject go){
        mTarget = go;
    }

    public final float[] getViewMatrix(){
        return mViewMatrix;
    }

    public synchronized void update(float dt){
        if(mTarget !=null){
            mLookAt.x += (mTarget.mPos.x - mLookAt.x) * 1f;
            mLookAt.y += (mTarget.mPos.y - mLookAt.y) * 1f;
        }

        final float NEAR = 0f;
        final float FAR = 1f;
        final float LEFT = mLookAt.x - mHalfDistX;
        final float RIGHT = mLookAt.x + mHalfDistX;
        final float TOP = mLookAt.y + mHalfDistY;
        final float BOTTOM = mLookAt.y - mHalfDistY;
        android.opengl.Matrix.orthoM(mViewMatrix, 0, LEFT, RIGHT, BOTTOM, TOP, NEAR, FAR);
    }

    public boolean inView(final PointF worldPos, final float objectWidth, final float objectHeight) {
        float maxX = (mLookAt.x + mHalfDistX);
        float minX = (mLookAt.x - mHalfDistX) - objectWidth;
        float maxY = (mLookAt.y + mHalfDistY);
        float minY  = (mLookAt.y - mHalfDistY) - objectHeight;
        if((worldPos.x > minX && worldPos.x < maxX)
                && (worldPos.y > minY && worldPos.y < maxY)){
            return true;
        }
        return false;
    }
}
