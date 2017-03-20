package com.example.jesper.platformer.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.jesper.platformer.Config;
import com.example.jesper.platformer.gameobjects.Target;

/**
 * Created by Jesper on 2017-03-13.
 */

public class ScoreBox extends GuiComponent {

    public ScoreBox(Context context, Config config){
        super(context, config);
    }

    @Override
    public void render(Canvas canvas, Paint paint){
        paint.setColor(Color.GRAY);
        RectF playerScoreBackground = new RectF(mConfig.GW_STAGE_WIDTH - mConfig.GUI_SCOREBOARD_WIDTH,
                mConfig.GUI_SCREEN_MARGIN,
                mConfig.GW_STAGE_WIDTH - mConfig.GUI_SCREEN_MARGIN,
                mConfig.GUI_SCOREBOARD_HEIGHT);

        canvas.drawRect(playerScoreBackground, paint);

        paint.setColor(Color.GREEN);
        paint.setTextSize(mConfig.GUI_TEXT_SIZE);
        canvas.drawText(mConfig.GUI_SCOREBOARD_TEXT + Target.getCollectedCount() + "/" + Target.getTotalCount(), mConfig.GW_STAGE_WIDTH - 190f, 30f, paint);
    }
}
