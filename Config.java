package com.example.jesper.platformer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

/**
 * Created by Jesper on 2017-03-07.
 */

public class Config {
    private Resources mResources;
    private Context mContext;

    public final int GW_METERS_TO_SHOW_X;
    public final int GW_METERS_TO_SHOW_Y;
    public final int GW_STAGE_WIDTH;
    public final int GW_STAGE_HEIGHT;
    public final int GW_BACKGROUND_COLOR;
    public final boolean GW_SCALE_CONTENT;

    public final int GO_DEFAULT_HEIGHT;
    public final int GO_DEFAULT_WIDTH;
    public final float GO_PRECISION;

    public final float P_ACCELERATION_X;
    public final float P_ACCELERATION_Y;
    public final float P_TERMINAL_VELOCITY;
    public final float P_MAX_VELOCITY;
    public final float P_JUMP_HEIGHT;
    public final float P_JUMP_DURATION;

    public final int VP_TILE_BUFFER;


    public Config(Context context){
        mContext = context;
        mResources = context.getResources();

        GW_METERS_TO_SHOW_X = getInt(R.integer.GW_METERS_TO_SHOW_X);
        GW_METERS_TO_SHOW_Y = getInt(R.integer.GW_METERS_TO_SHOW_Y);
        GW_STAGE_WIDTH = getInt(R.integer.GW_STAGE_WIDTH);
        GW_STAGE_HEIGHT = getInt(R.integer.GW_STAGE_HEIGHT);
        GW_SCALE_CONTENT = getBool(R.bool.GW_SCALE_CONTENT);
        GW_BACKGROUND_COLOR = getColor(R.dimen.GW_BACKGROUND_COLOR);

        GO_DEFAULT_HEIGHT = getInt(R.integer.GO_DEFAULT_HEIGHT);
        GO_DEFAULT_WIDTH = getInt(R.integer.GO_DEFAULT_WIDTH);
        GO_PRECISION = getFloat(R.dimen.GO_PRECISION);

        P_ACCELERATION_X = getFloat(R.dimen.P_ACCELERATION_X);
        P_ACCELERATION_Y = getFloat(R.dimen.P_ACCELERATION_Y);
        P_TERMINAL_VELOCITY = getFloat(R.dimen.P_TERMINAL_VELOCITY);
        P_MAX_VELOCITY = getFloat(R.dimen.P_MAX_VELOCITY);
        P_JUMP_HEIGHT = getFloat(R.dimen.P_JUMP_HEIGHT);
        P_JUMP_DURATION = getFloat(R.dimen.P_JUMP_DURATION);

        VP_TILE_BUFFER = getInt(R.integer.VP_TILE_BUFFER);
    }

    private float getFloat(int resourceId){
        TypedValue resourceVal = new TypedValue();
        mResources.getValue(resourceId, resourceVal, true);
        return resourceVal.getFloat();
    }

    private int getInt(int resourceId){
        return mResources.getInteger(resourceId);
    }

    private String getString(int resourceId){
        return mResources.getString(resourceId);
    }

    private boolean getBool(int resourceId){ return mResources.getBoolean(resourceId); }

    @SuppressWarnings("deprecation")
    private int getColor(int resourceId){
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(mContext, resourceId);
        } else {
            return mResources.getColor(resourceId);
        }
    }
}
