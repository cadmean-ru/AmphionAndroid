package ru.cadmean.amphionandroid;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShaderLoader {
    private final Context ctx;

    public ShaderLoader(Context ctx) {
        this.ctx = ctx;
    }

    public int loadAndCompile(int id, int type) {
        InputStream is = ctx.getResources().openRawResource(id);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();

        try {
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
        } catch (IOException ex) {

        }

        String shaderSource = sb.toString();

        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderSource);
        GLES20.glCompileShader(shader);

        int[] suc = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, suc, 0);

        if (suc[0] == GLES20.GL_FALSE) {
            Log.d("Shader", "Failed to compile shader:");
            Log.d("Shader", shaderSource + "\n");
            Log.d("Shader", GLES20.glGetShaderInfoLog(shader));
        }

        return shader;
    }

    public int createAndLinkProgram(int vertexShader, int fragmentShader) {
        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);

        int[] suc = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, suc, 0);

        if (suc[0] == GLES20.GL_FALSE) {
            Log.d("Shader", "Failed to link program");
            Log.d("Shader", GLES20.glGetProgramInfoLog(program));
        }

        return program;
    }
}
