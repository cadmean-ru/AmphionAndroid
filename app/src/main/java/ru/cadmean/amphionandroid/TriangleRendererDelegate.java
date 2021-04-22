package ru.cadmean.amphionandroid;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import ru.cadmean.amphion.android.cli.*;

class TriangleRendererDelegate extends MasterRendererDelegate {
    
    private static final String TAG = "TriangleRenderer";

    final int stride = 28;

    TriangleRendererDelegate(ShaderLoader shaderLoader, MyGLView glView) {
        super(shaderLoader, glView);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "triangle start");
        Log.d(TAG, Thread.currentThread().getName());

        int vertexId = shaderLoader.loadAndCompile(R.raw.triangle_vertex, GLES20.GL_VERTEX_SHADER);
        int fragmentId = shaderLoader.loadAndCompile(R.raw.triangle_fragment, GLES20.GL_FRAGMENT_SHADER);

        programId = shaderLoader.createAndLinkProgram(vertexId, fragmentId);
    }

    @Override
    public void onRender(PrimitiveRenderingContext primitiveRenderingContext) {
        Log.d(TAG, "triangle render");
        Log.d(TAG, Thread.currentThread().getName());

        GeometryPrimitiveData gp = primitiveRenderingContext.getGeometryPrimitiveData();
        PrimitiveData prData = primitiveData.get(primitiveRenderingContext.getPrimitiveId());
        if (prData == null) {
            Log.d(TAG, "bruh");
            return;
        }

        GLES20.glUseProgram(programId);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, prData.vbo);

        Log.d(TAG, String.format("Redraw: %b", primitiveRenderingContext.getRedraw()));

        if (primitiveRenderingContext.getRedraw()) {
            Log.d(TAG, "Triangle was drawn");

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

            FloatBuffer buffer = tempTriangleBuffer.asFloatBuffer();

            buffer.put(vertices);
            buffer.position(0);

            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertices.length * 4, buffer, GLES20.GL_STATIC_DRAW);

            int posId = GLES20.glGetAttribLocation(programId, "pos");
            int colId = GLES20.glGetAttribLocation(programId, "col");

            GLES20.glVertexAttribPointer(posId, 3, GLES20.GL_FLOAT, false, stride, 0);
            GLES20.glVertexAttribPointer(colId, 4, GLES20.GL_FLOAT, false, stride, 12);

            GLES20.glEnableVertexAttribArray(posId);
            GLES20.glEnableVertexAttribArray(colId);
            Log.d(TAG, "Here2");
        }

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        Log.d(TAG, "Here");

//        GLES20.glDisableVertexAttribArray(posId);
//        GLES20.glDisableVertexAttribArray(colId);
    }
}
