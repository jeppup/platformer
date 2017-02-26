package com.example.jesper.platformer;

/**
 * Created by Jesper on 2017-02-26.
 */

public class Utils {
    public static float clamp(float value, float min, float max)
    {
        if(value < min)
            return min;
        else if(value > max)
            return max;

        return value;
    }
}
