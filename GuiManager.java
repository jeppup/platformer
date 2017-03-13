package com.example.jesper.platformer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by Jesper on 2017-03-13.
 */

public class GuiManager {
    public enum GameState {IN_GAME, LOST, WON };

    private Context mContext;
    private GameState mState;

    private ArrayList<GuiComponent> mActiveComponents = new ArrayList<>();
    public GuiManager(Context context){
        mContext = context;
    }

    public void startGameGui(){
        mState = GameState.IN_GAME;
        mActiveComponents.clear();
        mActiveComponents.add(new InGameGui());
    }

    public void gameLost(){
        mActiveComponents.clear();
        mState = GameState.LOST;
    }

    public void gameWon(){
        mActiveComponents.clear();
        mState = GameState.WON;
    }

    public void update(float deltaTime){
        for(GuiComponent guiComponent : mActiveComponents){
            guiComponent.update(deltaTime);
        }
    }

    public void render(Canvas canvas, Paint paint){
        for(GuiComponent guiComponent : mActiveComponents){
            guiComponent.render(canvas, paint);
        }
    }
}
