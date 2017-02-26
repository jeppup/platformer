package com.example.jesper.platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by Jesper on 2017-02-18.
 */

public abstract class GameObject {
    public static final int HEIGHT = 1;
    public static final int WIDTH = 1;
    public RectF mBounds = new RectF(0.0f, 0.0f, WIDTH, HEIGHT);

    public PointF mWorldLocation = new PointF(0.f, 0.f);
    public float mWidth = WIDTH;
    public float mHeight = HEIGHT;
    public boolean mVisible = true;
    public int mType = 0;
    public GameView mEngine = null;

    public GameObject(final GameView engine, final float x, final float y,
                      final float width, final float height, final int type){
        init(engine, x, y, width, height, type);
    }

    public GameObject(final GameView engine, final float x, final float y, final int type){
        init(engine, x, y, WIDTH, HEIGHT, type);
    }

    private void init(final GameView engine, final float x, final float y,
                      final float width, final float height, final int type){
        mEngine = engine;
        mType = type;
        mHeight = height;
        mWidth = width;
        mWorldLocation.x = x;
        mWorldLocation.y = y;
        updateBounds();
    }

    public boolean isColliding(final GameObject objectToCheck){
        return RectF.intersects(this.mBounds, objectToCheck.mBounds);
    }

    public void onCollision(final GameObject collidingGameObject){

    }

    protected void updateBounds(){
        mBounds.left = mWorldLocation.x;
        mBounds.top = mWorldLocation.y;
        mBounds.right = mWorldLocation.x + mWidth;
        mBounds.bottom = mWorldLocation.y + mHeight;
    }


    public void update(float deltaTime){};

    public Bitmap prepareBitmap(Context context, String bitMapName, int pixelsPerMeter) throws Exception{
        int resId = context.getResources().getIdentifier(bitMapName, "drawable", context.getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        if(bitmap == null){
            throw new Exception("No bitmap named " + bitMapName);
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(mWidth*pixelsPerMeter), (int)(mHeight*pixelsPerMeter), false);
        return bitmap;
    }
}
