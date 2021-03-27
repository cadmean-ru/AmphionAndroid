package ru.cadmean.amphionandroid;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ru.cadmean.amphion.android.cli.ExecDelegate;
import ru.cadmean.amphion.android.droidCli.DroidCli;

public class MyRenderer implements GLSurfaceView.Renderer {

    private ExecDelegate renderingPerformer;
    private final GLSurfaceView glView;

    private static final String TAG = "MyRenderer";

    public MyRenderer(GLSurfaceView glView) {
        this.glView = glView;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        renderingPerformer = DroidCli.getRenderingPerformer();
        Log.d(TAG, "surface created");
        DroidCli.getRendererPrepareDelegate().execute();
        AndroidFrontend.sendCallback(-111, "");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d(TAG, "onDrawFrame");
        Log.d(TAG, Thread.currentThread().getName());

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        renderingPerformer.execute();
    }
}
