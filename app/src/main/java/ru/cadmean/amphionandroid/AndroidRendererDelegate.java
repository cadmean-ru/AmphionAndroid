package ru.cadmean.amphionandroid;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import ru.cadmean.amphion.android.droidCli.DroidCli;
import ru.cadmean.amphion.android.cli.Cli;
import ru.cadmean.amphion.android.cli.RendererDelegate;

public class AndroidRendererDelegate implements RendererDelegate {

    private final GLSurfaceView glView;
    private final ShaderLoader shaderLoader;

    public AndroidRendererDelegate(Context context, GLSurfaceView glView) {
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
        glView.requestRender();
    }

    @Override
    public void onPrepare() {
        GLES20.glClearColor(0,0.6f,0.9f,1);

        DroidCli.registerPrimitiveRendererDelegate(Cli.PrimitiveTriangle, new TriangleRendererDelegate(shaderLoader));
    }

    @Override
    public void onStop() {

    }
}
