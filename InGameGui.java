package com.example.jesper.platformer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Jesper on 2017-03-13.
 */

public class InGameGui extends GuiComponent {

    public InGameGui(Context context, Config config){
        super(context, config);
    }

    @Override
    public void update(float deltaTime){

    }

    @Override
    public void render(Canvas canvas, Paint paint){
        drawHealthBar2(canvas, paint);
        drawScoreInfo(canvas, paint);
    }

    private void drawScoreInfo(Canvas canvas, Paint paint){
        paint.setColor(Color.GRAY);
        RectF playerScoreBackground = new RectF(mConfig.GW_STAGE_WIDTH - 200f, 10f, mConfig.GW_STAGE_WIDTH - 10f, 50);
        canvas.drawRect(playerScoreBackground, paint);

        paint.setColor(Color.GREEN);
        paint.setTextSize(20f);
        canvas.drawText("Targets: " + Target.getCollectedCount() + "/" + Target.getTotalCount(), mConfig.GW_STAGE_WIDTH - 190f, 30f, paint);
    }

    private void drawHealthBar2(Canvas canvas, Paint paint){
        paint.setColor(Color.GREEN);
        float leftMargin = 10f;
        float topMargin = 10f;
        float healthBarLength = 100f;
        float healthBarHeight = 50f;

        RectF rectangle = new RectF(leftMargin,
                topMargin,
                leftMargin + healthBarLength,
                topMargin + healthBarHeight);
        canvas.drawRect(rectangle, paint);

        paint.setColor(Color.BLUE);
        float bloodLength = healthBarLength * (Player.getMaxHitPoints() - Player.getHitPoints())/Player.getMaxHitPoints();
        rectangle = new RectF(leftMargin + healthBarLength - bloodLength,
                topMargin,
                leftMargin + healthBarLength,
                topMargin + healthBarHeight);
        canvas.drawRect(rectangle, paint);

    }

    private void drawHealthBar(Canvas canvas, Paint paint){
        paint.setColor(Color.GREEN);
        RectF healthBar = new RectF(10f, 10f, 10f + Player.getMaxHitPoints(), 50f);
        canvas.drawRect(healthBar, paint);

        paint.setColor(Color.GRAY);
        healthBar.top = healthBar.top + 5f;
        healthBar.left = healthBar.left + 5f;
        healthBar.right = healthBar.right - 5f;
        healthBar.bottom = healthBar.bottom - 5f;
        canvas.drawRect(healthBar, paint);

        paint.setColor(Color.GREEN);
        healthBar.top = healthBar.top + 5f;
        healthBar.left = healthBar.left + 5f;
        healthBar.right = Player.getHitPoints() -5f;
        healthBar.bottom = healthBar.bottom - 5f;
        canvas.drawRect(healthBar, paint);
    }

}
