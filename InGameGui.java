package com.example.jesper.platformer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Jesper on 2017-03-13.
 */

public class InGameGui extends GuiComponent {

    @Override
    public void update(float deltaTime){

    }

    @Override
    public void render(Canvas canvas, Paint paint){
        drawHealthBar(canvas, paint);
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

        //canvas.drawRect(10f, 10f, Player.getHitPoints(), 50f, paint);
    }

}
