package com.example.jesper.platformer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Jesper on 2017-03-18.
 */

public class HealthBar extends GuiComponent {
    public HealthBar(Context context, Config config){
        super(context, config);
    }

    @Override
    public void render(Canvas canvas, Paint paint){
        paint.setColor(Color.GREEN);
        RectF rectangle = new RectF(mConfig.GUI_SCREEN_MARGIN,
                mConfig.GUI_SCREEN_MARGIN,
                mConfig.GUI_SCREEN_MARGIN + mConfig.GUI_HEALTHBAR_LENGTH,
                mConfig.GUI_SCREEN_MARGIN + mConfig.GUI_HEALTHBAR_HEIGHT);
        canvas.drawRect(rectangle, paint);

        paint.setColor(Color.BLUE);
        float lostHpLength = mConfig.GUI_HEALTHBAR_LENGTH * (Player.getMaxHitPoints() - Player.getHitPoints())/Player.getMaxHitPoints();
        rectangle = new RectF(mConfig.GUI_SCREEN_MARGIN + mConfig.GUI_HEALTHBAR_LENGTH - lostHpLength,
                mConfig.GUI_SCREEN_MARGIN,
                mConfig.GUI_SCREEN_MARGIN + mConfig.GUI_HEALTHBAR_LENGTH,
                mConfig.GUI_SCREEN_MARGIN + mConfig.GUI_HEALTHBAR_HEIGHT);
        canvas.drawRect(rectangle, paint);
    }
}
