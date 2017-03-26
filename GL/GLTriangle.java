package com.example.jesper.platformer.GL;

/**
 * Created by Jesper on 2017-03-26.
 */

public class GLTriangle extends GLShape {
    private static float triangleCoords[] = {
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };

    public GLTriangle(float[] color) {
        super(triangleCoords, color);
    }
}
