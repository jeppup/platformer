package com.example.jesper.platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

/**
 * Created by Jesper on 2017-02-18.
 */

public abstract class GameObject {
    public PointF mWorldLocation = new PointF(0.f, 0.f);
    public float mWidth;
    public float mHeight;
    public boolean mVisible = true;
    public int mType;

    public abstract void update(long deltaTime);

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
