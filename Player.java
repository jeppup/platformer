package com.example.jesper.platformer;

/**
 * Created by Jesper on 2017-02-18.
 */

public class Player extends DynamicGameObject {
    static final int HEIGHT = 2;
    static final int WIDTH = 1;
    private static final float MAX_VELOCITY = 6f;

    public Player(final GameView engine, final float x, final float y, final int type) {
        super(engine, x, y, type);
    }

    @Override
    public void update(float deltaTime){
        mVelocity.x = mEngine.mControl.mHorizontalFactor * (MAX_VELOCITY * deltaTime);
        super.update(deltaTime);

    }
}
