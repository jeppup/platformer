package com.example.jesper.platformer.GL;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;

/**
 * Created by Jesper on 2017-03-26.
 */

public class GLManager {

    private static final String TAG = "GL_MANAGER";
    public static int mProgram;
    public static int uColor;
    public static int aPosition;
    public static int uMVP;
    private static final String GLSL_VERSION = "#version 100 \n";

    private static final String vertexShaderCode = ""
            + GLSL_VERSION
            + "uniform mat4 uMVPMatrix; \n"
            + "attribute vec4 aPosition; \n"
            + "void main() { \n"
            + "  gl_Position = uMVPMatrix * aPosition; \n"
            + "} \n";

    private static final String fragmentShaderCode = ""
            + GLSL_VERSION
            + "precision mediump float; \n"
            + "uniform vec4 uColor; \n"
            + "void main() { \n"
            + "  gl_FragColor = uColor; \n"
            + "} \n";


    public static int getProgam(){
        return mProgram;
    }

    public static void draw(final GLModel model, final float[] modelViewMatrix){
        GLManager.uploadMesh(model.mVertexBuffer);
        GLManager.setShaderColor(model.mColor);
        GLManager.setModelViewMatrix(modelViewMatrix);
        GLManager.drawMesh(model.mDrawMode, model.mVertexCount);
    }

    public static void uploadMesh(final FloatBuffer vertexBuffer){
        GLES20.glEnableVertexAttribArray(GLManager.aPosition);
        GLES20.glVertexAttribPointer(GLManager.aPosition, GLModel.COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                GLModel.VERTEX_STRIDE, vertexBuffer);
        checkGLError("uploadMesh");
    }

    public static void setShaderColor(final float[] color){
        GLES20.glUniform4fv(GLManager.uColor, 1, color, 0);
        checkGLError("setShaderColor");
    }

    public static void setModelViewMatrix(final float[] modelViewMatrix){
        GLES20.glUniformMatrix4fv(GLManager.uMVP, 1, false, modelViewMatrix, 0);
        checkGLError("setModelViewMatrix");
    }

    public static void drawMesh(final int drawMode, final int vertexCount){
        if(drawMode != GLES20.GL_TRIANGLES && drawMode != GLES20.GL_LINES && drawMode != GLES20.GL_POINTS){
            Log.d(TAG, "drawMesh: unknown primitive type! " + drawMode);
            return;
        }

        GLES20.glDrawArrays(drawMode, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(GLManager.aPosition);
        checkGLError("drawMesh");
    }

    public static void checkGLError(final String callingFunction){
        int error;
        while((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR){
            Log.e(callingFunction, "glError: " + error);
        }
    }

    public static void buildProgram(){
        mProgram = linkShaders(
                compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode),
                compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        );
        GLES20.glUseProgram(mProgram);
        aPosition = GLES20.glGetAttribLocation(mProgram, "aPosition");
        uColor = GLES20.glGetUniformLocation(mProgram, "uColor");
        uMVP = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        checkGLError("buildProgram");
    }

    public static int compileShader(final int type, final String shaderCode){
        final int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        Log.d(TAG, "Shader compile log: \n" + GLES20.glGetShaderInfoLog(shader));
        checkGLError("compileShader");
        return shader;
    }

    public static int linkShaders(final int vertexShader, final int fragmentShader){
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        Log.d(TAG, "Shader link log: \n" + GLES20.glGetProgramInfoLog(mProgram));
        checkGLError("linkShaders");
        return mProgram;

    }
}
