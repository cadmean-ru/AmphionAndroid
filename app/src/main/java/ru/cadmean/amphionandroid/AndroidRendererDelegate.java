package ru.cadmean.amphionandroid;

import android.opengl.GLES20;

import ru.cadmean.amphion.android.bind.Bind;
import ru.cadmean.amphion.android.cli.Cli;
import ru.cadmean.amphion.android.cli.RendererDelegate;

public class AndroidRendererDelegate implements RendererDelegate {
    @Override
    public void onClear() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void onPerformRenderingEnd() {

    }

    @Override
    public void onPerformRenderingStart() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void onPrepare() {
        GLES20.glClearColor(0,0.6f,0.9f,1);
        Bind.registerPrimitiveRendererDelegate(Cli.PrimitiveTriangle, new TriangleRendererDelegate());
    }

    @Override
    public void onStop() {

    }
}
