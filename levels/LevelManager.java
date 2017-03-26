package com.example.jesper.platformer.levels;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import com.example.jesper.platformer.Config;
import com.example.jesper.platformer.R;
import com.example.jesper.platformer.SoundManager;
import com.example.jesper.platformer.engine.GameEngine;
import com.example.jesper.platformer.gameobjects.DebugText;
import com.example.jesper.platformer.gameobjects.Target;
import com.example.jesper.platformer.engine.GameView;
import com.example.jesper.platformer.gameobjects.Enemy;
import com.example.jesper.platformer.gameobjects.GameObject;
import com.example.jesper.platformer.gameobjects.Player;

import java.util.ArrayList;

/**
 * Created by Jesper on 2017-02-19.
 */

public class LevelManager {
    private final static String TAG = "LevelManager";
    private Bitmap[] mBitmaps;
    private LevelData mData;
    public Player mPlayer;
    private GameEngine mEngine = null;
    private Config mConfig;
    private int[] mLevelOrder;
    private int mCurrentLevel;
    private SoundManager mSoundManager;

    public LevelManager(final GameEngine engine){
        mEngine = engine;
        mConfig = engine.getConfig();
        mSoundManager = engine.mSoundManager;
        initializeLevelOrdering();
        mCurrentLevel = -1;
    }

    public void initializeLevel(int levelResourceId){
        Resources resources = mEngine.getContext().getResources();
        mData = new CollectTargetsLevel(resources, levelResourceId);
        mBitmaps = new Bitmap[mData.mTileCount];
        for (GameObject go : mEngine.mGameObjects){
            mEngine.removeGameObject(go);
        }
        loadMapAssets();
    }

    public boolean levelCompleted(){
        if(mData.levelCompleted()){
            mSoundManager.play(SoundManager.LEVEL_COMPLETED, false);
            return true;
        }

        return false;
    }

    public boolean levelLost(){
        if(Player.getHitPoints() <= 0 || mPlayer.mWorldLocation.y > mConfig.LM_BOUND_Y){
            mSoundManager.play(SoundManager.PLAYER_DEATH, false);
            return true;
        }

        return false;
    }

    public void restartLevel(){
        initializeLevel(mLevelOrder[mCurrentLevel]);
    }

    public void progressLevel(){
        mCurrentLevel++;
        if(mCurrentLevel >= mLevelOrder.length)
            mCurrentLevel = 0;

        initializeLevel(mLevelOrder[mCurrentLevel]);
    }

    public Bitmap getBitmap(int tileType){
        return mBitmaps[tileType];
    }

    private void loadMapAssets(){
        int tileType;
        for(int y = 0; y < mData.mHeight; y++) {
            int width = mData.mTiles[y].length;
            for (int x = 0; x < width; x++) {
                tileType = mData.mTiles[y][x];
                if(tileType == LevelData.BACKGROUND){
                    continue;
                }
                GameObject temp = createGameObject(tileType, x, y);
                if(temp != null){
                    loadBitmap(mEngine.getContext(), temp, mEngine.getPixelsPerMeter());
                    mEngine.addGameObject(temp);
                }
            }
        }

        mEngine.addGameObject(new DebugText(mEngine, 0.0f,0.0f,0));
    }

    private GameObject createGameObject(final int tileType, final int x, final int y){
        GameObject o = null;
        switch (tileType){
            case 1:
                mPlayer = new Player(mEngine, x, y, tileType);
                o = mPlayer;
                break;
           case 3:
                o = new Target(mEngine, x, y, tileType);
                break;
            case 4:
                o = new Enemy(mEngine, x, y, tileType);
                break;
            default:
                o = new GameObject(mEngine, x, y, tileType);
                break;
        }

        return o;
    }

    private void loadBitmap(Context context, GameObject o, int pixelsPerMeter){
        if(mBitmaps[o.mType] != null){
            return;
        }
        try{
            String bitmapName = mData.getBitmapName(o.mType);
            mBitmaps[o.mType] = o.prepareBitmap(context, bitmapName, pixelsPerMeter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initializeLevelOrdering(){
        mLevelOrder = new int[2];
        mLevelOrder[0] = R.string.LEVEL_1;
        mLevelOrder[1] = R.string.LEVEL_2;
    }
}
