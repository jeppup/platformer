package com.example.jesper.platformer.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.jesper.platformer.engine.GameEngine;

/**
 * Created by Jesper on 2017-03-23.
 */

public class DebugText extends GameObject {
    private String[] mDebugStrings = new String[0];
    public DebugText(GameEngine engine, float x, float y, int type) {
        super(engine, x, y, 0f, 0f, type);
    }

    @Override
    public void update(float deltaTime) {
        mWorldLocation = mEngine.mPlayer.mWorldLocation;
        updateBounds();
    }

    @Override
    public void render(Canvas canvas, Paint paint) {
        int textSize = (int)(mEngine.getPixelsPerMeter()*0.5f);
        int y = textSize;
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.WHITE);
        mDebugStrings = mEngine.getDebugStrings();
        for(String s : mDebugStrings){
            canvas.drawText(s, 10, y, paint);
            y += textSize;
        }
    }
}
