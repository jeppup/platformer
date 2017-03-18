package com.example.jesper.platformer;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;

import java.util.Random;

/**
 * Created by Jesper on 2017-03-07.
 */

public class AnimationManager {
    private GameView mEngine = null;
    private AnimationDrawable mAnim = null;
    private Bitmap[] mFrames = null;
    private int mFrameCount = 0;
    private int mDuration = 0;
    private long mCurrentAnimationTime = 0;
    private int mCurrentFrame = 0;
    private int mSoundResourceId = -1;

    public AnimationManager(GameView engine, int resourceId, float width, float height){
        mEngine = engine;
        loadAnimationResource(resourceId, width, height);
    }

    public AnimationManager(GameView engine, int resourceId, int soundResourceId, float width, float height){
        mEngine = engine;
        mSoundResourceId = soundResourceId;
        loadAnimationResource(resourceId, width, height);
    }

    private void loadAnimationResource(int resourceId, float width, float height){
        recycle();
        mAnim = (AnimationDrawable) ContextCompat.getDrawable(mEngine.getContext(), resourceId);
        mFrameCount = mAnim.getNumberOfFrames();
        mDuration = getAnimationDuration(mAnim);
        mFrames = prepareAnimation(mAnim, mEngine.getPixelsPerMeter(), width, height);
        mCurrentAnimationTime = 0;
        if(!mAnim.isOneShot()){
            Random r = new Random();
            mCurrentAnimationTime = r.nextInt(mDuration);
        }else{
            mCurrentAnimationTime = 0;
        }


        mCurrentFrame = 0;
    }

    public Bitmap[] prepareAnimation(AnimationDrawable anim, int pixelsPerMeter, float width, float height){
        int count = anim.getNumberOfFrames();
        Bitmap[] frames = new Bitmap[count];
        for(int i = 0; i < count; i++){
            frames[i] = Bitmap.createScaledBitmap(((BitmapDrawable)anim.getFrame(i)).getBitmap(),
                    (int)(width*pixelsPerMeter), (int)(height*pixelsPerMeter), false);

        }

        return frames;
    }

    public void recycle(){
        if(mFrames == null){ return; }
        for(int i = 0; i < mFrames.length; i++){
            mFrames[i].recycle();
        }

        mFrames = null;
    }

    public Bitmap getCurrentBitmap(){
       return mFrames[mCurrentFrame];
    }

    public void update(float deltaTime){
        long elapsedMillis = (long)(1000.0f * deltaTime);
        mCurrentAnimationTime += elapsedMillis;

        if(mCurrentAnimationTime > mDuration){
            if(mSoundResourceId != -1){
                mEngine.mSoundManager.play(mSoundResourceId);
            }

            if(mAnim.isOneShot()){
                return;
            }
            mCurrentAnimationTime = mCurrentAnimationTime % mDuration;
        }

        int frameDuration = 0;
        for(int i = 0; i < mFrameCount; i++){
            frameDuration += mAnim.getDuration(i);
            if(frameDuration > mCurrentAnimationTime){
                mCurrentFrame = i;
                break;
            }
        }
    }

    public int getAnimationDuration(AnimationDrawable anim){
        int count = anim.getNumberOfFrames();
        int duration = 0;
        for(int i = 0; i < count; i++){
            duration += mAnim.getDuration(i);
        }

        return duration;
    }
}
