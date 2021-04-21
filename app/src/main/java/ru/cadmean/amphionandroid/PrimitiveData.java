package ru.cadmean.amphionandroid;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

public class PrimitiveData {
    int vbo;
    int ebo;

    PrimitiveData(MyGLView glView) {
        glView.queueEvent(() -> {
            int[] newBuffers = new int[2];
            GLES20.glGenBuffers(2, newBuffers, 0);
            vbo = newBuffers[0];
            ebo = newBuffers[1];
        });
    }
}
