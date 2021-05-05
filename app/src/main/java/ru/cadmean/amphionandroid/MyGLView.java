package ru.cadmean.amphionandroid;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        long callbackCode = 0;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                callbackCode = -108;
                break;
            case MotionEvent.ACTION_UP:
                callbackCode = -109;
                performClick();
                break;
            case MotionEvent.ACTION_MOVE:
                callbackCode = -110;
                break;
        }
        long x = (long) event.getX();
        long y = (long) event.getY();
        String data = String.format("%d;%d", x, y);
        AndroidFrontend.sendCallback(callbackCode, data);
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
