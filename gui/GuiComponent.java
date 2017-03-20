package com.example.jesper.platformer.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.jesper.platformer.Config;

/**
 * Created by Jesper on 2017-03-13.
 */

public class GuiComponent {

    protected Context mContext;
    protected Config mConfig;
    public GuiComponent(Context context, Config config){
        mContext = context;
        mConfig = config;
    }

    public void update(float deltaTime){

    }

    public void render(Canvas canvas, Paint paint){

    }

}
