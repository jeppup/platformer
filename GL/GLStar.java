package com.example.jesper.platformer.GL;

import android.opengl.GLES20;

import com.example.jesper.platformer.Utils;

/**
 * Created by Jesper on 2017-03-27.
 */

public class GLStar extends GLGameObject {

    public GLStar(float x, float y){
        mPos.x = x;
        mPos.y = y;
        float[] vertices = new float[]{ 0, 0, 0 };
        mModel = new GLModel(vertices, GLES20.GL_POINTS);
    }


    @Override
    public void update(float dt){

    }
}
