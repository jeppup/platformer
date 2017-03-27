package com.example.jesper.platformer.GL;

import com.example.jesper.platformer.Utils;
import com.example.jesper.platformer.engine.GameEngine;

/**
 * Created by Jesper on 2017-03-27.
 */

public class GLSpaceship extends GLGameObject {

    public GLSpaceship(float x, float y){
        mPos.x = x;
        mPos.y = y;
        float width = 16f;
        float height = 8f;
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;
        float[] vertices = new float[]{
                -halfWidth, -halfHeight, 0,
                halfWidth, -halfHeight, 0,
                0, halfHeight, 0
        };
        mModel = new GLModel(vertices, null);
    }

    @Override
    public void update(float dt){
        mRotation = Utils.sineWave(0, 80, mVR);
        float xRadius = GameEngine.mWorldWidth / 2;
        float yRadius = GameEngine.mWorldHeight / 2;

        mPos.x = Utils.cosWave(GameEngine.mWorldWidth / 2, xRadius, mVR);
        mPos.y = Utils.sineWave(GameEngine.mWorldHeight / 2, yRadius, mVR);

        mVR += 0.02f;
    }
}
