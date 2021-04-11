package ru.cadmean.amphionandroid;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import ru.cadmean.amphion.android.cli.*;

public class TriangleRendererDelegate implements PrimitiveRendererDelegate {
    
    private ShaderLoader shaderLoader;

    private int programId;

    private FloatBuffer triangleBuffer;

    private static final String TAG = "TriangleRenderer";

    private int vbo;

    public TriangleRendererDelegate(ShaderLoader shaderLoader) {
        this.shaderLoader = shaderLoader;
    }

    @Override
    public void onRemovePrimitive(PrimitiveRenderingContext primitiveRenderingContext) {

    }

    @Override
    public void onRender(PrimitiveRenderingContext primitiveRenderingContext) {
        Log.d(TAG, "triangle render");

        GeometryPrimitiveData gp = primitiveRenderingContext.getGeometryPrimitiveData();

        Vector3 tlp = gp.getTlPositionN();
        Vector3 brp = gp.getBrPositionN();

        Vector4 color = gp.getFillColorN();

        float[] vertices = {
                tlp.getX(), tlp.getY(), tlp.getZ(), color.getX(), color.getY(), color.getZ(), color.getW(),
                brp.getX(), brp.getY(), brp.getZ(), color.getX(), color.getY(), color.getZ(), color.getW(),
                tlp.getX(), brp.getY(), brp.getZ(), color.getX(), color.getY(), color.getZ(), color.getW(),
        };

        ByteBuffer tempTriangleBuffer = ByteBuffer.allocateDirect(vertices.length * 4);

        tempTriangleBuffer.order(ByteOrder.nativeOrder());

        triangleBuffer = tempTriangleBuffer.asFloatBuffer();

        triangleBuffer.put(vertices);
        triangleBuffer.position(0);

        GLES20.glUseProgram(programId);

        int posId = GLES20.glGetAttribLocation(programId, "pos");
        int colId = GLES20.glGetAttribLocation(programId, "col");

        int stride = 28;

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertices.length*4, triangleBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glVertexAttribPointer(posId, 3, GLES20.GL_FLOAT, false, stride, 0);
        GLES20.glVertexAttribPointer(colId, 4, GLES20.GL_FLOAT, false, stride, 12);

        GLES20.glEnableVertexAttribArray(posId);
        GLES20.glEnableVertexAttribArray(colId);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
//        GLES20.glDisableVertexAttribArray(posId);
    }

    @Override
    public void onSetPrimitive(PrimitiveRenderingContext primitiveRenderingContext) {

    }

    @Override
    public void onStart() {
        Log.d(TAG, "triangle start");
        Log.d(TAG, Thread.currentThread().getName());

        int vertexId = shaderLoader.loadAndCompile(R.raw.triangle_vertex, GLES20.GL_VERTEX_SHADER);
        int fragmentId = shaderLoader.loadAndCompile(R.raw.triangle_fragment, GLES20.GL_FRAGMENT_SHADER);

        programId = shaderLoader.createAndLinkProgram(vertexId, fragmentId);

        int[] newBuffers = new int[1];
        GLES20.glGenBuffers(1, newBuffers, 0);
        vbo = newBuffers[0];
    }

    @Override
    public void onStop() {

    }
}
