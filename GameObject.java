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

    protected static PointF overlap = new PointF(0,0);
    public static boolean getOverlap(GameObject a, GameObject b) {
        overlap.set(0f, 0f);
        float centerDeltaX = a.mBounds.centerX() - b.mBounds.centerX();
        float halfWidths = (a.mWidth + b.mWidth) * 0.5f;

        if (Math.abs(centerDeltaX) > halfWidths) return false; //no overlap on x == no collision

        float centerDeltaY = a.mBounds.centerY() - b.mBounds.centerY();
        float halfHeights = (a.mHeight + b.mHeight) * 0.5f;

        if (Math.abs(centerDeltaY) > halfHeights) return false; //no overlap on y == no collision

        float dx = halfWidths - Math.abs(centerDeltaX); //overlap on x
        float dy = halfHeights - Math.abs(centerDeltaY); //overlap on y
        if (dy < dx) {
            overlap.y = (centerDeltaY < 0) ? -dy : dy;
        } else if (dy > dx) {
            overlap.x = (centerDeltaX < 0) ? -dx : dx;
        } else {
            overlap.x = (centerDeltaX < 0) ? -dx : dx;
            overlap.y = (centerDeltaY < 0) ? -dy : dy;
        }
        return true;
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
