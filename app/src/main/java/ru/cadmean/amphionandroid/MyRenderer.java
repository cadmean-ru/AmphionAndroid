package ru.cadmean.amphionandroid;

import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer {
    private final String vertexSource = "attribute vec3 pos;\n" +
            "\n" +
            "void main(){\n" +
            "    gl_Position = vec4(pos, 1);\n" +
            "}";

    private final String fragmentSource = "void main(){\n" +
            "    gl_FragColor = vec4(0.6, 0.9, 0, 1);\n" +
            "}";

    private int program;

    private float[] vertices = {
            -0.5f, -0.5f, 0f,
             0.5f, -0.5f, 0f,
             0.5f,  0.5f, 0f};

    private FloatBuffer triangleBuffer;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0,0.6f,0.9f,1);

        int vertexId = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        int fragmentId = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        GLES20.glShaderSource(vertexId, vertexSource);
        GLES20.glShaderSource(fragmentId, fragmentSource);

        GLES20.glCompileShader(vertexId);
        GLES20.glCompileShader(fragmentId);

        program = GLES20.glCreateProgram();

        GLES20.glAttachShader(program, vertexId);
        GLES20.glAttachShader(program, fragmentId);

        GLES20.glLinkProgram(program);

        ByteBuffer tempTriangleBuffer = ByteBuffer.allocateDirect(vertices.length * 4);

        tempTriangleBuffer.order(ByteOrder.nativeOrder());

        triangleBuffer = tempTriangleBuffer.asFloatBuffer();

        triangleBuffer.put(vertices);

        triangleBuffer.position(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(program);
        int posId = GLES20.glGetAttribLocation(program, "pos");

        GLES20.glVertexAttribPointer(posId, 3, GLES20.GL_FLOAT, false, 12, triangleBuffer);
        GLES20.glEnableVertexAttribArray(posId);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        GLES20.glDisableVertexAttribArray(posId);
    }
}
