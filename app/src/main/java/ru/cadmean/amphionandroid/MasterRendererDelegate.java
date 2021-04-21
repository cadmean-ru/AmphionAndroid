package ru.cadmean.amphionandroid;

import android.opengl.GLES20;
import ru.cadmean.amphion.android.cli.PrimitiveRendererDelegate;
import ru.cadmean.amphion.android.cli.PrimitiveRenderingContext;

import java.util.HashMap;
import java.util.Map;

abstract class MasterRendererDelegate implements PrimitiveRendererDelegate {
    protected Map<Long, PrimitiveData> primitiveData = new HashMap();
    protected int programId;
    protected ShaderLoader shaderLoader;
    protected MyGLView glView;

    MasterRendererDelegate(ShaderLoader shaderLoader, MyGLView glView) {
        this.shaderLoader = shaderLoader;
        this.glView = glView;
    }

    @Override
    public void onSetPrimitive(PrimitiveRenderingContext primitiveRenderingContext) {
        primitiveData.put(primitiveRenderingContext.getPrimitiveId(), new PrimitiveData(glView));
    }

    @Override
    public void onRemovePrimitive(PrimitiveRenderingContext primitiveRenderingContext) {
        primitiveData.remove(primitiveRenderingContext.getPrimitiveId());
        PrimitiveData data = primitiveData.get(primitiveRenderingContext.getPrimitiveId());
        if (data == null) {
            return;
        }

        int[] buffersToDelete = new int[] { data.vbo };
        GLES20.glDeleteBuffers(1, buffersToDelete, 0);
    }

    @Override
    public void onStop() {
        GLES20.glDeleteProgram(programId);
    }
}
