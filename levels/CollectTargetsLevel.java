package com.example.jesper.platformer.levels;

import android.content.res.Resources;
import android.util.Log;

import com.example.jesper.platformer.gameobjects.Target;

/**
 * Created by Jesper on 2017-02-19.
 */

public class CollectTargetsLevel extends LevelData {
    private static final String TAG = "CollectTargetsLevel";
    public CollectTargetsLevel(Resources resources, int resourceId){
        resetLevelResources();
        loadLevel(resources, resourceId);

        mWidth = mTiles[0].length;
        mHeight = mTiles.length;
        mTileCount = countUniqueTiles();
    }

    private void resetLevelResources(){
        Target.resetTargetCount();
    }

    @Override
    public String getBitmapName(int tileType){
        String fileName = "nullsprite";
        switch (tileType){
            case 1:
                fileName = "player_idle1";
                break;
            case 2:
                fileName = "ground";
                break;
            case 3:
                fileName = "target";
                break;
            case 4:
                fileName = "monster";
                break;
            default:
                Log.d(TAG, "getBitmapName unknown tiletype: " + tileType);
                break;
        }


        return fileName;
    }

    public boolean levelCompleted(){
        return Target.getRemainingCount() == 0;
    }
}
