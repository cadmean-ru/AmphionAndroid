package ru.cadmean.amphionandroid;

import android.opengl.GLES20;
import android.util.Log;
import ru.cadmean.amphion.android.cli.PrimitiveRendererDelegate;
import ru.cadmean.amphion.android.cli.PrimitiveRenderingContext;

import java.util.HashMap;
import java.util.Map;

abstract class MasterRendererDelegate implements PrimitiveRendererDelegate {
    protected Map<Long, PrimitiveData> primitiveData = new HashMap();
    protected int programId;
    protected ShaderLoader shaderLoader;

    private final static String TAG = "MasterDelegate";

    MasterRendererDelegate(ShaderLoader shaderLoader) {
        this.shaderLoader = shaderLoader;
    }

    @Override
    public void onSetPrimitive(PrimitiveRenderingContext primitiveRenderingContext) {
        Log.d(TAG, "onSet");
        Log.d(TAG, Thread.currentThread().getName());
        if (!primitiveData.containsKey(primitiveRenderingContext.getPrimitiveId()))
            primitiveData.put(primitiveRenderingContext.getPrimitiveId(), new PrimitiveData());
        Log.d(TAG, "onSet finished");
    }

    @Override
    public void onRemovePrimitive(PrimitiveRenderingContext primitiveRenderingContext) {
        Log.d(TAG, "onRemove");
        Log.d(TAG, Thread.currentThread().getName());

        primitiveData.remove(primitiveRenderingContext.getPrimitiveId());
        PrimitiveData data = primitiveData.get(primitiveRenderingContext.getPrimitiveId());
        if (data == null) {
            return;
        }

        int[] buffersToDelete = new int[] { data.vbo, data.ebo };
        GLES20.glDeleteBuffers(2, buffersToDelete, 0);
    }

    @Override
    public void onStop() {
        GLES20.glDeleteProgram(programId);
    }
}
