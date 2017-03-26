package com.example.jesper.platformer.GL;

import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * Created by Jesper on 2017-03-26.
 */

public class GLGameObject {
    GLShape mMesh = null;
    PointF mPos = new PointF(0f, 0f);
    static final float depth = 0.0f;
    public  static final float[] modelMatrix = new float[4*4];
    public  static final float[] viewportModelMatrix = new float[4*4];
    public  static final float[] rotateViewportModelMatrix = new float[4*4];

    public GLGameObject(){
        float width = 30f;
        float height = 60f;
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;
        float[] vertices = new float[]{
                -halfWidth, -halfHeight, 0,
                halfWidth, -halfHeight, 0,
                0, halfHeight, 0
        };
        mMesh = new GLShape(vertices, null);
    }

    public static float sineWave(final float centreX, final float range, final float currentAngle){
        return centreX + (float) Math.sin(currentAngle) * range;
    }

    public static float cosWave(final float centreX, final float range, final float currentAngle){
        return centreX + (float) Math.cos(currentAngle) * range;
    }

    private float angle = 0f;

    public void draw(final float[] viewMatrix){
        final int OFFSET = 0;
        float scale = 1f;
        float rotation = sineWave(0, 360, angle);


        mPos.x = cosWave(0f, 80, angle);
        mPos.y = sineWave(0f, 80, angle);
        angle += 0.02f;

        Matrix.setIdentityM(modelMatrix, OFFSET);
        Matrix.translateM(modelMatrix, OFFSET, mPos.x, mPos.y, depth);
        Matrix.multiplyMM(viewportModelMatrix, OFFSET, viewMatrix, OFFSET, modelMatrix, OFFSET);

        Matrix.setRotateM(modelMatrix, OFFSET, rotation, 0, 0, 1.f);
        Matrix.scaleM(modelMatrix, OFFSET, scale, scale, 1f);
        Matrix.multiplyMM(rotateViewportModelMatrix, OFFSET, viewportModelMatrix, OFFSET, modelMatrix, OFFSET);

        //Upload mesh to GPU
        GLES20.glEnableVertexAttribArray(GLManager.aPosition);
        GLES20.glVertexAttribPointer(GLManager.aPosition, mMesh.COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                mMesh.VERTEX_STRIDE, mMesh.vertexBuffer);

        //Set shading color
        GLES20.glUniform4fv(GLManager.uColor, 1, mMesh.mColor, 0);

        GLES20.glUniformMatrix4fv(GLManager.uMVP, 1, false, rotateViewportModelMatrix, OFFSET);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, OFFSET, mMesh.mVertexCount);
        GLES20.glDisableVertexAttribArray(GLManager.aPosition);
    }
}
