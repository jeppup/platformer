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

    private GLModel mModel = null;
    public PointF mPos = new PointF(0f, 0f);
    private float mScale = 1f;
    private float mRotation = 0f;
    private float mVR = 0f;

    public GLGameObject(){
        float width = 16f;
        float height = 8f;
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;
        float[] vertices = new float[]{
                -halfWidth, -halfHeight, 0,
                halfWidth, -halfHeight, 0,
                0, halfHeight, 0
        };
        mModel = new GLModel(vertices, null);
    }



    public void update(float dt){

    }

    public void draw(final float[] viewMatrix){
        final int OFFSET = 0;
        mScale = 1f;
        mRotation = Utils.sineWave(0, 80, mVR);
        mPos.x = Utils.cosWave(0f, 80, mVR);
        mPos.y = Utils.sineWave(0f, 80, mVR);
        mVR += 0.02f;

        Matrix.setIdentityM(modelMatrix, OFFSET);
        Matrix.translateM(modelMatrix, OFFSET, mPos.x, mPos.y, depth);
        Matrix.multiplyMM(viewportModelMatrix, OFFSET, viewMatrix, OFFSET, modelMatrix, OFFSET);

        Matrix.setRotateM(modelMatrix, OFFSET, mRotation, 0, 0, 1.f);
        Matrix.scaleM(modelMatrix, OFFSET, mScale, mScale, 1f);
        Matrix.multiplyMM(rotateViewportModelMatrix, OFFSET, viewportModelMatrix, OFFSET, modelMatrix, OFFSET);

        GLManager.draw(mModel, rotateViewportModelMatrix);

    }
}
