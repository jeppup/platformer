package com.example.jesper.platformer;

import android.content.res.Resources;

import java.util.HashSet;

/**
 * Created by Jesper on 2017-02-19.
 */

public abstract class LevelData {
    public final static int BACKGROUND = 0;
    public final static char END_OF_LINE = 'X';
    public int[][] mTiles;
    public int mHeight;
    public int mWidth;
    public int mTileCount;

    protected int countUniqueTiles(){
        int tileType;
        HashSet<Integer> set = new HashSet<>();
        for(int y = 0; y < mHeight; y++){
            for(int x = 0; x < mWidth; x++){
                tileType = mTiles[y][x];
                set.add(tileType);
            }
        }

        return set.size();
    }

    protected void loadLevel(Resources resources, int resourceId){
        String levelString = resources.getString(resourceId);
        initializeTilesStructure(levelString);

        int x = 0;
        int y = 0;

        for(char currentTile : levelString.toCharArray()){
            if(currentTile == END_OF_LINE){
                y++;
                x = 0;
                continue;
            }

            int tile = Integer.parseInt(currentTile + "");
            mTiles[y][x] = tile;
            x++;
        }
    }

    protected void initializeTilesStructure(String levelData){
        int width = getLevelLength(levelData);
        int height = getLevelHeight(levelData);

        mTiles = new int[height][width];
    }

    protected int getLevelLength(String levelData){
        return levelData.indexOf(END_OF_LINE);
    }

    protected int getLevelHeight(String levelData){
        int levelHeight = 0;
        for(int i = 0; i < levelData.length(); i++){
            if(levelData.charAt(i) == END_OF_LINE){
                levelHeight++;
            }
        }

        return levelHeight;
    }

    public abstract String getBitmapName(int tileType);
    public abstract boolean levelCompleted();
}
