package ru.cadmean.amphionandroid;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import ru.cadmean.amphion.android.dispatch.WorkDispatcher;
import ru.cadmean.amphion.android.dispatch.WorkItem;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

public class MyGLView extends GLSurfaceView implements WorkDispatcher {

    public MyGLView(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        setRenderer(new MyRenderer(this));
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void execute(WorkItem workItem) {
        Log.d("MyGLView", workItem.toString());
        queueEvent(workItem::execute);
    }
}
