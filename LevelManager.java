package com.example.jesper.platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Jesper on 2017-02-19.
 */

public class LevelManager {
    private final static String TAG = "LevelManager";
    private Bitmap[] mBitmaps;
    public ArrayList<GameObject> mGameObjects;
    private LevelData mData;
    public Player mPlayer;
    private GameView mEngine = null;
    public LevelManager(final GameView engine, String levelName){
        mEngine = engine;
        switch (levelName){
            default:
                mData = new TestLevel();
                break;
        }

        mGameObjects = new ArrayList<>();
        mBitmaps = new Bitmap[mData.mTileCount];
        loadMapAssets();
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
                    mGameObjects.add(temp);
                }
            }
        }
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
}
