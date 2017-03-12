package com.example.jesper.platformer;

/**
 * Created by Jesper on 2017-03-12.
 */

public class Enemy extends DynamicGameObject {
    private final Config mConfig;
    private final int mDamage;

    public Enemy(GameView engine, float x, float y, int type) {
        super(engine, x, y, type);
        mConfig = engine.getConfig();
        mDamage = mConfig.E_DAMAGE;
    }

    @Override
    public void onCollision(GameObject collidingGameObject) {
        Player.applyDamage(mDamage);
    }
}
