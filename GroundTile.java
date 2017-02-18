package com.example.jesper.platformer;

/**
 * Created by Jesper on 2017-02-18.
 */

public class GroundTile extends GameObject {
    public GroundTile(float x, float y, int type){
        final int HEIGHT = 1;
        final int WIDTH = 1;
        mType = type;
        mHeight = HEIGHT;
        mWidth = WIDTH;
        mWorldLocation.x = x;
        mWorldLocation.y = y;
    }

    public void update(long deltaTime){

    }
}
