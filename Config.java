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
    public final int P_MAX_HEALTH;

    public final int E_DAMAGE;
    public final float E_ATTACK_COOLDOWN;
    public final float E_WIDTH;
    public final float E_HEIGHT;
    public final float E_STUCK_TOLERANCE;
    public final float E_STUCK_PLAYER_TOLERANCE;
    public final float E_JUMP_ACCELERATION;
    public final float E_JUMP_SPEED;
    public final float E_JUMP_DURATION;
    public final float E_MOVE_SPEED;
    public final float E_MOVE_ACCELERATION;
    public final float E_TERMINAL_SPEED;
    public final float E_GRAVITY;


    public final int VP_TILE_BUFFER;

    public final float GUI_TEXT_SIZE;
    public final float GUI_SCOREBOARD_WIDTH;
    public final float GUI_SCREEN_MARGIN;
    public final float GUI_SCOREBOARD_HEIGHT;
    public final float GUI_HEALTHBAR_LENGTH;
    public final float GUI_HEALTHBAR_HEIGHT;
    public final String GUI_SCOREBOARD_TEXT;

    public final float LM_BOUND_X;
    public final float LM_BOUND_Y;


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
        P_MAX_HEALTH = getInt(R.integer.P_MAX_HEALTH);

        E_DAMAGE = getInt(R.integer.E_DAMAGE);
        E_ATTACK_COOLDOWN = getFloat(R.dimen.E_ATTACK_COOLDOWN);
        E_WIDTH = getFloat(R.dimen.E_WIDTH);
        E_HEIGHT = getFloat(R.dimen.E_HEIGHT);
        E_STUCK_TOLERANCE = getFloat(R.dimen.E_STUCK_TOLERANCE);
        E_STUCK_PLAYER_TOLERANCE = getFloat(R.dimen.E_STUCK_PLAYER_TOLERANCE);
        E_JUMP_ACCELERATION = getFloat(R.dimen.E_JUMP_ACCELERATION);
        E_JUMP_SPEED = getFloat(R.dimen.E_JUMP_SPEED);
        E_JUMP_DURATION = getFloat(R.dimen.E_JUMP_DURATION);
        E_MOVE_SPEED = getFloat(R.dimen.E_MOVE_SPEED);
        E_GRAVITY = getFloat(R.dimen.E_GRAVITY);
        E_MOVE_ACCELERATION = getFloat(R.dimen.E_MOVE_ACCELERATION);
        E_TERMINAL_SPEED = getFloat(R.dimen.E_TERMINAL_SPEED);

        VP_TILE_BUFFER = getInt(R.integer.VP_TILE_BUFFER);

        GUI_TEXT_SIZE = getFloat(R.dimen.GUI_TEXT_SIZE);
        GUI_SCOREBOARD_HEIGHT = getFloat(R.dimen.GUI_SCOREBOARD_HEIGHT);
        GUI_SCOREBOARD_WIDTH = getFloat(R.dimen.GUI_SCOREBOARD_WIDTH);
        GUI_SCREEN_MARGIN = getFloat(R.dimen.GUI_SCREEN_MARGIN);
        GUI_SCOREBOARD_TEXT = getString(R.string.GUI_SCOREBOARD_TEXT);
        GUI_HEALTHBAR_LENGTH = getFloat(R.dimen.GUI_HEALTHBAR_LENGTH);
        GUI_HEALTHBAR_HEIGHT = getFloat(R.dimen.GUI_HEALTHBAR_HEIGHT);

        LM_BOUND_X = getFloat(R.dimen.LM_BOUND_X);
        LM_BOUND_Y = getFloat(R.dimen.LM_BOUND_Y);
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
