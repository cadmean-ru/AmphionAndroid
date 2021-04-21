package ru.cadmean.amphionandroid;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

public class PrimitiveData {
    int vbo;

    PrimitiveData() {
        int[] newBuffers = new int[1];
        GLES20.glGenBuffers(1, newBuffers, 0);
        vbo = newBuffers[0];
    }
}
