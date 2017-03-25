package com.example.jesper.platformer.inputs;

import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by Jesper on 2017-03-25.
 */

public interface GamepadListener {
    boolean dispatchGenericMotionEvent(final MotionEvent ev);
    boolean dispatchKeyEvent(final KeyEvent ev);
}
