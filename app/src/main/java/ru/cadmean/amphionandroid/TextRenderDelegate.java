package ru.cadmean.amphionandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import ru.cadmean.amphion.android.atext.*;
import ru.cadmean.amphion.android.cli.*;

import javax.microedition.khronos.opengles.GL;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;

public class TextRenderDelegate extends MasterRendererDelegate{

    private static final String TAG = "TextRenderer";

    private final Context ctx;

    final int stride = 20;

    private Face face;

    private static final HashMap<Integer, Integer> charTextures = new HashMap();

    TextRenderDelegate(ShaderLoader shaderLoader, Context ctx) {
        super(shaderLoader);
        this.ctx = ctx;
    }

    @Override
    public void onStart() {
        Log.d(TAG, "text start");

        int vertexId = shaderLoader.loadAndCompile(R.raw.image_vertex, GLES20.GL_VERTEX_SHADER);
        int fragmentId = shaderLoader.loadAndCompile(R.raw.text_fragment, GLES20.GL_FRAGMENT_SHADER);

        programId = shaderLoader.createAndLinkProgram(vertexId, fragmentId);

        try {
            Font f = Atext.parseFont(Atext.getDefaultFontData());
            face = f.newFace(20);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRender(PrimitiveRenderingContext primitiveRenderingContext) {
        Log.d(TAG, "text render");

        if (face == null) {
            Log.d(TAG, "text bruh");
            return;
        }

        TextPrimitiveData tp = primitiveRenderingContext.getTextPrimitiveData();
        PrimitiveData prData = primitiveData.get(primitiveRenderingContext.getPrimitiveId());
        if (prData == null) {
            Log.d(TAG, "text bruh");
            return;
        }

        GLES20.glUseProgram(programId);

        Log.d(TAG, "text is being drawn " + primitiveRenderingContext.getPrimitiveId());

        Vector3 wSize = MyRenderer.getSurfaceSize();
        Vector3 tlp2 = tp.getTlPosition();
        Vector3 size = tp.getSize();
        Vector3 brp2 = new Vector3(tlp2.getX() + size.getX(), tlp2.getY() + size.getY(), 0);
        Vector4 textColorN = tp.getTextColorN();

        Text text;
        int charsCount;
        if (primitiveRenderingContext.getRedraw()) {
            text = Atext.layoutStringCompat(face, tp.getText(), tlp2.getX(), brp2.getX(), tlp2.getY(), brp2.getY(), 0, 0, 3, 0);
            prData.text = text;

            charsCount = (int)text.getCharsCount();
            prData.vbos = new int[charsCount];
            GLES20.glGenBuffers(charsCount, prData.vbos, 0);
        } else {
            text = prData.text;
            charsCount = (int)text.getCharsCount();
        }


        int textColorHandle = GLES20.glGetUniformLocation(programId, "uTextColor");
        GLES20.glUniform4f(textColorHandle, textColorN.getX(), textColorN.getY(), textColorN.getZ(), textColorN.getW());

        int texHandle = GLES20.glGetUniformLocation(programId, "uTexture");

        for (int i = 0; i < charsCount; i++) {
            Char c = text.getCharAt(i);
            Glyph g = c.getGlyph();
            int rune = c.getRune();

            if (!charTextures.containsKey(rune)) {
                int[] tempTex = new int[1];
                GLES20.glGenTextures(1, tempTex, 0);
                int glyphTex = tempTex[0];

                ByteBuffer glyphPixels = ByteBuffer.wrap(g.getPixels());
                glyphPixels.position(0);

//                Bitmap bitmap = Bitmap.createBitmap((int)g.getWidth(), (int)g.getHeight(), Bitmap.Config.ALPHA_8);
//                bitmap.copyPixelsFromBuffer(glyphPixels);
//
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glyphTex);

//                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);



                GLES20.glTexImage2D(
                        GLES20.GL_TEXTURE_2D,
                        0,
                        GLES20.GL_ALPHA,
                        (int)g.getWidth(),
                        (int)g.getHeight(),
                        0,
                        GLES20.GL_ALPHA,
                        GLES20.GL_UNSIGNED_BYTE,
                        glyphPixels
                );


                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
//                bitmap.recycle();

                charTextures.put(rune, glyphTex);
            }

            int tex = charTextures.get(rune);

            int charVbo = prData.vbos[i];
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, charVbo);

            if (primitiveRenderingContext.getRedraw()) {
                long x = c.getX();
                long y = c.getY();
                Vector3 tlp3 = new Vector3((float)x, (float)y, 0);
                Vector3 tlp4 = Cli.vector3Ndc(tlp3, wSize);
                Vector3 brp3 = new Vector3(tlp3.getX() + g.getWidth(), tlp3.getY() + g.getHeight(), 0);
                Vector3 brp4 = Cli.vector3Ndc(brp3, wSize);

                float[] vertices = {
                        tlp4.getX(), tlp4.getY(), 0, 0, 0,  // top left
                        tlp4.getX(), brp4.getY(), 0, 0, 1,  // bottom left
                        brp4.getX(), brp4.getY(), 0, 1, 1,  // bottom right
                        tlp4.getX(), tlp4.getY(), 0, 0, 0,  // top left
                        brp4.getX(), tlp4.getY(), 0, 1, 0,  // top right
                        brp4.getX(), brp4.getY(), 0, 1, 1,  // bottom right
                };

                ByteBuffer tempImageBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
                tempImageBuffer.order(ByteOrder.nativeOrder());

                FloatBuffer buffer = tempImageBuffer.asFloatBuffer();
                buffer.put(vertices);
                buffer.position(0);

                GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertices.length * 4, buffer, GLES20.GL_STATIC_DRAW);

                int posId = GLES20.glGetAttribLocation(programId, "vPos");
                int texId = GLES20.glGetAttribLocation(programId, "vTexCoord");

                GLES20.glVertexAttribPointer(posId, 3, GLES20.GL_FLOAT, false, stride, 0);
                GLES20.glVertexAttribPointer(texId, 2, GLES20.GL_FLOAT, false, stride, 12);

                GLES20.glEnableVertexAttribArray(posId);
                GLES20.glEnableVertexAttribArray(texId);
            }

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex);

            GLES20.glUniform1i(texHandle, 0);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        }
    }
}
