package com.example.jesper.platformer.GL;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Jesper on 2017-03-26.
 */

public class GLShape {
    public static final int SIZE_OF_FLOAT = 4;
    public static final int COORDS_PER_VERTEX = 3;
    public static final int VERTEX_STRIDE = COORDS_PER_VERTEX * SIZE_OF_FLOAT;

    public final int mVertexCount;

    public FloatBuffer vertexBuffer;
    public float mColor[] = {1.0f, 1.0f, 1.0f, 1.0f };

    public GLShape(float[] geometry, float[] color){
        if(color != null){
            mColor = color;
        }

        mVertexCount = geometry.length / COORDS_PER_VERTEX;

        ByteBuffer bb = ByteBuffer.allocateDirect(geometry.length * SIZE_OF_FLOAT);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(geometry);
        vertexBuffer.position(0);
    }

    public void draw(){
        GLES20.glUseProgram(GLManager.mProgram);
        GLES20.glEnableVertexAttribArray(GLManager.aPosition);
        GLES20.glVertexAttribPointer(GLManager.aPosition, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, vertexBuffer);
        GLES20.glUniform4fv(GLManager.uColor, 1, mColor, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mVertexCount);
        GLES20.glDisableVertexAttribArray(GLManager.aPosition);
    }
}
