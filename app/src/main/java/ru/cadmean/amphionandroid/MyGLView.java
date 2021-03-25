package ru.cadmean.amphionandroid;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MyGLView extends GLSurfaceView {
    public MyGLView(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        setRenderer(new MyRenderer());
    }
}
