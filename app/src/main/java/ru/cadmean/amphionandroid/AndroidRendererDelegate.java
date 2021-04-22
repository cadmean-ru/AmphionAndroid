package ru.cadmean.amphionandroid;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import android.util.Log;
import ru.cadmean.amphion.android.droidCli.DroidCli;
import ru.cadmean.amphion.android.cli.Cli;
import ru.cadmean.amphion.android.cli.RendererDelegate;

public class AndroidRendererDelegate implements RendererDelegate {

    private final MyGLView glView;
    private final ShaderLoader shaderLoader;

    private final static String TAG = "RendererDelegate";

    public AndroidRendererDelegate(Context context, MyGLView glView) {
        this.glView = glView;
        shaderLoader = new ShaderLoader(context);
    }

    @Override
    public void onClear() {
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void onPerformRenderingEnd() {

    }

    @Override
    public void onPerformRenderingStart() {
        Log.d(TAG, "Rendering requested onPerformRenderingStart");
        glView.requestRender();
        MyRenderer.renderingRequested = true;
    }

    @Override
    public void onPrepare() {
        Log.d(TAG, "onPrepare");
        Log.d(TAG, Thread.currentThread().getName());

        Log.d(TAG, GLES20.glGetString(GLES20.GL_VENDOR));
        Log.d(TAG, GLES20.glGetString(GLES20.GL_VERSION));

        GLES20.glClearColor(1,1,1,1);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);

        DroidCli.registerPrimitiveRendererDelegate(Cli.PrimitiveTriangle, new TriangleRendererDelegate(shaderLoader, glView));
        DroidCli.registerPrimitiveRendererDelegate(Cli.PrimitiveRectangle, new RectangleRendererDelegate(shaderLoader, glView));

        Log.d(TAG, "onPrepare finished");
    }

    @Override
    public void onStop() {

    }
}
