package com.example.jesper.platformer.GL;

import android.graphics.PointF;
import android.opengl.Matrix;

import com.example.jesper.platformer.Utils;

/**
 * Created by Jesper on 2017-03-26.
 */

public class GLGameObject {


    static final float depth = 0.0f;
    public  static final float[] modelMatrix = new float[4*4];
    public  static final float[] viewportModelMatrix = new float[4*4];
    public  static final float[] rotateViewportModelMatrix = new float[4*4];

    protected GLModel mModel = null;
    protected float mScale = 1f;
    protected float mRotation = 0f;
    protected float mVR = 0f;
    public PointF mPos = new PointF(0f, 0f);


    public GLGameObject(){

    }



    public void update(float dt){

    }

    public void draw(final float[] viewMatrix){
        final int OFFSET = 0;
        Matrix.setIdentityM(modelMatrix, OFFSET);
        Matrix.translateM(modelMatrix, OFFSET, mPos.x, mPos.y, depth);
        Matrix.multiplyMM(viewportModelMatrix, OFFSET, viewMatrix, OFFSET, modelMatrix, OFFSET);

        Matrix.setRotateM(modelMatrix, OFFSET, mRotation, 0, 0, 1.f);
        Matrix.scaleM(modelMatrix, OFFSET, mScale, mScale, 1f);
        Matrix.multiplyMM(rotateViewportModelMatrix, OFFSET, viewportModelMatrix, OFFSET, modelMatrix, OFFSET);

        GLManager.draw(mModel, rotateViewportModelMatrix);

    }
}
