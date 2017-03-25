package com.example.jesper.platformer;

import android.content.res.Resources;

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

    public static int pxToDp(int px){
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp){
        return (int)(dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
