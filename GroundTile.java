package com.example.jesper.platformer;

import com.example.jesper.platformer.engine.GameEngine;
import com.example.jesper.platformer.engine.GameView;
import com.example.jesper.platformer.gameobjects.GameObject;

/**
 * Created by Jesper on 2017-02-18.
 */

public class GroundTile extends GameObject {
    public GroundTile(GameEngine engine, float x, float y, int type){
        super(engine, x, y, type);
    }
}
