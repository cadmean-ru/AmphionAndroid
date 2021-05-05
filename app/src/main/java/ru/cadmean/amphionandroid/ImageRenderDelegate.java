package ru.cadmean.amphionandroid;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;
import ru.cadmean.amphion.android.cli.ImagePrimitiveData;
import ru.cadmean.amphion.android.cli.PrimitiveRenderingContext;
import ru.cadmean.amphion.android.cli.Vector3;

import javax.microedition.khronos.opengles.GL;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class ImageRenderDelegate extends MasterRendererDelegate{

    private static final String TAG = "ImageRenderer";

    final int stride = 20;

    private final Context ctx;

    ImageRenderDelegate(ShaderLoader shaderLoader, Context ctx) {
        super(shaderLoader);
        this.ctx = ctx;
    }

    @Override
    public void onStart() {
        Log.d(TAG, "image start");

        int vertexId = shaderLoader.loadAndCompile(R.raw.image_vertex, GLES20.GL_VERTEX_SHADER);
        int fragmentId = shaderLoader.loadAndCompile(R.raw.image_fragment, GLES20.GL_FRAGMENT_SHADER);

        programId = shaderLoader.createAndLinkProgram(vertexId, fragmentId);
    }

    @Override
    public void onRender(PrimitiveRenderingContext primitiveRenderingContext) {
        Log.d(TAG, "image render");

        ImagePrimitiveData ip = primitiveRenderingContext.getImagePrimitiveData();
        PrimitiveData prData = primitiveData.get(primitiveRenderingContext.getPrimitiveId());
        if (prData == null) {
            Log.d(TAG, "image bruh");
            return;
        }

        GLES20.glUseProgram(programId);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, prData.vbo);

        if (primitiveRenderingContext.getRedraw()) {
            Log.d(TAG, "Rectangle is being drawn");

            Vector3 tlp = ip.getTlPositionN();
            Vector3 brp = ip.getBrPositionN();

            if (prData.tex == 0) {
                int[] tempTex = new int[1];
                GLES20.glGenTextures(1, tempTex, 0);
                prData.tex = tempTex[0];

                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeFileDescriptor(ctx.getAssets().openFd(ip.getImageUrl()).getFileDescriptor());
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(bitmap.getByteCount());
                pixelBuffer.position(0);
                bitmap.copyPixelsToBuffer(pixelBuffer);

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, prData.tex);
                
                GLES20.glTexImage2D(
                        GLES20.GL_TEXTURE_2D,
                        0,
                        GLES20.GL_RGBA,
                        bitmap.getWidth(),
                        bitmap.getHeight(),
                        0,
                        GLES20.GL_RGBA,
                        GLES20.GL_UNSIGNED_BYTE,
                        pixelBuffer
                );

                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_REPEAT);

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            }

            float[] vertices = {
                    tlp.getX(), tlp.getY(), tlp.getZ(), 0, 0,  // top left
                    tlp.getX(), brp.getY(), brp.getZ(), 0, 1,  // bottom left
                    brp.getX(), brp.getY(), brp.getZ(), 1, 0,  // bottom right
                    tlp.getX(), tlp.getY(), tlp.getZ(), 0, 0,  // top left
                    brp.getX(), tlp.getY(), brp.getZ(), 1, 1,  // top right
                    brp.getX(), brp.getY(), brp.getZ(), 1, 0,  // bottom right
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

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, prData.tex);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    private byte[] readImage(String url) {
        byte[] bytes;
        try {
            AssetFileDescriptor assetFileDescriptor = ctx.getAssets().openFd(url);

            bytes = new byte[(int)assetFileDescriptor.getLength()];

            FileInputStream fis = new FileInputStream(assetFileDescriptor.getFileDescriptor());

            fis.read(bytes);

            fis.close();
        } catch (IOException e) {
            bytes = new byte[0];
            e.printStackTrace();
        }

        return bytes;
    }
}