package com.example.jesper.platformer.GL;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by Jesper on 2017-03-26.
 */

public class GLManager {

    private static final String TAG = "GL_MANAGER";
    public static int mProgram;
    public static int uColor;
    public static int aPosition;
    public static int uMVP;

    private static final String vertexShaderCode = ""
            + "uniform mat4 uMVPMatrix; \n"
            + "attribute vec4 aPosition; \n"
            + "void main() { \n"
            + "  gl_Position = uMVPMatrix * aPosition; \n"
            + "} \n";

    private static final String fragmentShaderCode = ""
            + "precision mediump float; \n"
            + "uniform vec4 uColor; \n"
            + "void main() { \n"
            + "  gl_FragColor = uColor; \n"
            + "} \n";


    public static int getProgam(){
        return mProgram;
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

    }

    public static int compileShader(final int type, final String shaderCode){
        final int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        Log.d(TAG, "Shader compile log: \n" + GLES20.glGetShaderInfoLog(shader));
        return shader;
    }

    public static int linkShaders(final int vertexShader, final int fragmentShader){
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        Log.d(TAG, "Shader link log: \n" + GLES20.glGetProgramInfoLog(mProgram));
        return mProgram;

    }
}
