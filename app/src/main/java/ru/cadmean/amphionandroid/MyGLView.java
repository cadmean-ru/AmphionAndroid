package ru.cadmean.amphionandroid;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

public class MyGLView extends GLSurfaceView {

    public MyGLView(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        setRenderer(new MyRenderer(this));
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
