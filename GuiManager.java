package com.example.jesper.platformer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by Jesper on 2017-03-13.
 */

public class GuiManager {
    private Context mContext;

    private ArrayList<GameObject> activeComponents = new ArrayList<>();
    public GuiManager(Context context){
        mContext = context;

    }

    public void initializeLevelGui(){

    }

    public void update(float deltaTime){
        activeComponents.clear();

    }

    public void render(Canvas canvas, Paint paint){
        for(GameObject guiComponent : activeComponents){
            guiComponent.render(canvas, paint);
        }
    }
}
