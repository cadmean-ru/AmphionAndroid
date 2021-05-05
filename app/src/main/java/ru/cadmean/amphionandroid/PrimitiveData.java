package ru.cadmean.amphionandroid;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;

public class PrimitiveData {
    int vbo;
    int ebo;
    int tex;

    PrimitiveData() {
        Log.d("Data", "New");
        Log.d("Data", Thread.currentThread().getName());

        int[] newBuffers = new int[2];
        GLES20.glGenBuffers(2, newBuffers, 0);
        vbo = newBuffers[0];
        ebo = newBuffers[1];
    }
}
