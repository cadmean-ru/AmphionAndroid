package ru.cadmean.amphionandroid;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import ru.cadmean.amphion.android.cli.GeometryPrimitiveData;
import ru.cadmean.amphion.android.cli.PrimitiveRendererDelegate;
import ru.cadmean.amphion.android.cli.PrimitiveRenderingContext;
import ru.cadmean.amphion.android.cli.Vector3;

public class TriangleRendererDelegate implements PrimitiveRendererDelegate {

    private final String vertexSource = "attribute vec3 pos;\n" +
            "\n" +
            "void main(){\n" +
            "    gl_Position = vec4(pos, 1);\n" +
            "}";
    private final String fragmentSource = "void main(){\n" +
            "    gl_FragColor = vec4(0.6, 0.9, 0, 1);\n" +
            "}";

    private int programId;

    private FloatBuffer triangleBuffer;

    private static final String TAG = "TriangleRenderer";

    @Override
    public void onRemovePrimitive(PrimitiveRenderingContext primitiveRenderingContext) {

    }

    @Override
    public void onRender(PrimitiveRenderingContext primitiveRenderingContext) {
        Log.d(TAG, "triangle render");

        GeometryPrimitiveData gp = primitiveRenderingContext.getGeometryPrimitiveData();

        Vector3 tlp = gp.getTlPositionN();
        Vector3 brp = gp.getBrPositionN();

        float[] vertices = {
                tlp.getX(), tlp.getY(), tlp.getZ(),
                brp.getX(), brp.getY(), brp.getZ(),
                tlp.getX(), brp.getY(), brp.getZ()};

        ByteBuffer tempTriangleBuffer = ByteBuffer.allocateDirect(vertices.length * 4);

        tempTriangleBuffer.order(ByteOrder.nativeOrder());

        triangleBuffer = tempTriangleBuffer.asFloatBuffer();

        triangleBuffer.put(vertices);

        triangleBuffer.position(0);

        GLES20.glUseProgram(programId);

        int posId = GLES20.glGetAttribLocation(programId, "pos");

        GLES20.glVertexAttribPointer(posId, 3, GLES20.GL_FLOAT, false, 12, triangleBuffer);

        GLES20.glEnableVertexAttribArray(posId);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        GLES20.glDisableVertexAttribArray(posId);
    }

    @Override
    public void onSetPrimitive(PrimitiveRenderingContext primitiveRenderingContext) {

    }

    @Override
    public void onStart() {
        Log.d(TAG, "triangle start");
        Log.d(TAG, Thread.currentThread().getName());

        GLES20.glClearColor(0,0.6f,0.9f,1);

        int vertexId = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        int fragmentId = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        GLES20.glShaderSource(vertexId, vertexSource);
        GLES20.glShaderSource(fragmentId, fragmentSource);

        GLES20.glCompileShader(vertexId);
        GLES20.glCompileShader(fragmentId);

        programId = GLES20.glCreateProgram();

        GLES20.glAttachShader(programId, vertexId);
        GLES20.glAttachShader(programId, fragmentId);

        GLES20.glLinkProgram(programId);
    }

    @Override
    public void onStop() {

    }
}
