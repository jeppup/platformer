package com.example.jesper.platformer.GL;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Jesper on 2017-03-26.
 */

public class GLModel {
    public static final int SIZE_OF_FLOAT = 4;
    public static final int COORDS_PER_VERTEX = 3;
    public static final int VERTEX_STRIDE = COORDS_PER_VERTEX * SIZE_OF_FLOAT;

    public FloatBuffer mVertexBuffer;
    public int mVertexCount;
    public int mDrawMode = GLES20.GL_TRIANGLES;
    public float mColor[] = {1.0f, 1.0f, 1.0f, 1.0f };

    public GLModel(float[] geometry, float[] color, final int primitiveType){
        init(geometry, color, primitiveType);
    }
    public GLModel(float[] geometry, float[] color){
        init(geometry, color, mDrawMode);
    }
    public GLModel(float[] geometry, int primitiveType){
        init(geometry, mColor, primitiveType);
    }
    public GLModel(float[] geometry){
        init(geometry, mColor, mDrawMode);
    }


    private void init(final float[] geometry, final float[] colors, final int primitive){
        setColor(colors);
        setVertices(geometry);
        setDrawMode(primitive);
    }

    private void setColor(float[] color){
        if(color != null){
            mColor = color;
        }
    }

    private void setDrawMode(int drawMode){
        if(drawMode != GLES20.GL_TRIANGLES && drawMode != GLES20.GL_LINES && drawMode != GLES20.GL_POINTS){
            Log.d("GLModel", "drawMesh: unknown primitive type! " + drawMode);
            return;
        }

        mDrawMode = drawMode;
    }

    private void setVertices(float[] geometry){
        mVertexCount = geometry.length / COORDS_PER_VERTEX;
        ByteBuffer bb = ByteBuffer.allocateDirect(geometry.length * SIZE_OF_FLOAT);
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(geometry);
        mVertexBuffer.position(0);
    }
}
