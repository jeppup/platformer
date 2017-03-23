package com.example.jesper.platformer;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

import com.example.jesper.platformer.gameobjects.GameObject;

/**
 * Created by Jesper on 2017-02-18.
 */

public class Viewport {
    private PointF mCurrentViewportWorldCentre;
    private int mPixelsPerMetreX;
    private int mPixelsPerMetreY;
    private int mScreenXResolution;
    private int mScreenYResolution;
    private int mScreenCentreX;
    private int mScreenCentreY;
    private int mMetresToShowX;
    private int mMetresToShowY;
    private float mHalfDistX;
    private float mHalfDistY;
    private int mClippedCount;
    private GameObject mTarget = null;
    public volatile int mInViewCount = 0;

    public Viewport(Config config, final int screenWidth, final int screenHeight, final int metersToShowX, final int metersToShowY){
        mScreenXResolution = screenWidth;
        mScreenYResolution = screenHeight;
        mScreenCentreX = mScreenXResolution / 2;
        mScreenCentreY = mScreenYResolution / 2;
        mPixelsPerMetreX = mScreenXResolution / metersToShowX;
        mPixelsPerMetreY = mScreenYResolution / metersToShowY;
        mMetresToShowX = metersToShowX + config.VP_TILE_BUFFER;
        mMetresToShowY = metersToShowY + config.VP_TILE_BUFFER;
        mHalfDistX = (mMetresToShowX / 2);
        mHalfDistY = (mMetresToShowY / 2);

        mCurrentViewportWorldCentre = new PointF(0,0);
    }

    public int getScreenWidth() {
        return mScreenXResolution;
    }
    public int getScreenHeight(){
        return mScreenYResolution;
    }
    public int getPixelsPerMetreX(){
        return mPixelsPerMetreX;
    }
    public int getClipCount(){
        return mClippedCount;
    }
    public void resetClipCount(){
        mClippedCount = 0;
    }

    public void setWorldCentre(final PointF pos){
        mCurrentViewportWorldCentre.x = pos.x;
        mCurrentViewportWorldCentre.y = pos.y;
    }

    public synchronized void setTarget(final GameObject go){
        mTarget = go;
    }

    public synchronized void update(float dt){
        if(mTarget ==null){
            return;
        }
        mInViewCount = 0;
        mCurrentViewportWorldCentre.x += (mTarget.mWorldLocation.x - mCurrentViewportWorldCentre.x)*0.125f;
        mCurrentViewportWorldCentre.y += (mTarget.mWorldLocation.y - mCurrentViewportWorldCentre.y)*0.25f;
    }

    public void worldToScreen(final PointF worldPos, final float objectWidth, final float objectHeight, Rect out){
        int left = (int) (mScreenCentreX - ((mCurrentViewportWorldCentre.x - worldPos.x) * mPixelsPerMetreX));
        int top = (int) (mScreenCentreY - ((mCurrentViewportWorldCentre.y - worldPos.y) * mPixelsPerMetreY));
        int right = (int) (left + (objectWidth * mPixelsPerMetreX));
        int bottom = (int) (top + (objectWidth * mPixelsPerMetreY));
        out.set(left, top, right, bottom);
    }

    public void worldToScreen(final PointF worldPos, Point screenPos){
        synchronized (screenPos){
            screenPos.x = (int)(mScreenCentreX - ((mCurrentViewportWorldCentre.x - worldPos.x) * mPixelsPerMetreX));
            screenPos.y = (int)(mScreenCentreY - ((mCurrentViewportWorldCentre.y - worldPos.y) * mPixelsPerMetreY));
        }
    }

    public boolean inView(final PointF worldPos, final float objectWidth, final float objectHeight) {
        float maxX = (mCurrentViewportWorldCentre.x + mHalfDistX);
        float minX = (mCurrentViewportWorldCentre.x - mHalfDistX) - objectWidth;
        float maxY = (mCurrentViewportWorldCentre.y + mHalfDistY);
        float minY  = (mCurrentViewportWorldCentre.y - mHalfDistY) - objectHeight;
        if((worldPos.x > minX && worldPos.x < maxX)
                && (worldPos.y > minY && worldPos.y < maxY)){
            mInViewCount++;
            return true;
        }
        mClippedCount++;
        return false;
    }
}
