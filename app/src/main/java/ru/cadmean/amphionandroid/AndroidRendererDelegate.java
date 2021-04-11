package ru.cadmean.amphionandroid;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import ru.cadmean.amphion.android.droidCli.DroidCli;
import ru.cadmean.amphion.android.cli.Cli;
import ru.cadmean.amphion.android.cli.RendererDelegate;

public class AndroidRendererDelegate implements RendererDelegate {

    private final GLSurfaceView glView;

    public AndroidRendererDelegate(GLSurfaceView glView) {
        this.glView = glView;
    }

    @Override
    public void onClear() {
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void onPerformRenderingEnd() {

    }

    @Override
    public void onPerformRenderingStart() {
        glView.requestRender();
    }

    @Override
    public void onPrepare() {
//        EGL10 mEgl = (EGL10) EGLContext.getEGL();
//
//        EGLDisplay mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
//
//        if (mEglDisplay == EGL10.EGL_NO_DISPLAY) {
//            throw new RuntimeException("eglGetDisplay failed");
//        }
//
//        int[] version = new int[2];
//        if(!mEgl.eglInitialize(mEglDisplay, version)) {
//            throw new RuntimeException("eglInitialize failed");
//        }
//
//        EGLConfig[] maEGLconfigs = new EGLConfig[1];
//
//        int[] configsCount = new int[1];
//        int[] configSpec = new int[]
//                {
//                        EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
//                        EGL10.EGL_RED_SIZE, 8,
//                        EGL10.EGL_GREEN_SIZE, 8,
//                        EGL10.EGL_BLUE_SIZE, 8,
//                        EGL10.EGL_ALPHA_SIZE, 8,
//                        EGL10.EGL_DEPTH_SIZE, 0,
//                        EGL10.EGL_STENCIL_SIZE, 0,
//                        EGL10.EGL_NONE,
//                };
//
//        mEgl.eglChooseConfig(mEglDisplay, configSpec, maEGLconfigs, 1, configsCount);
//
//        int[] attrib_list = { EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE };
//
//        EGLContext mEglContext = mEgl.eglCreateContext(mEglDisplay, maEGLconfigs[0], EGL10.EGL_NO_CONTEXT, attrib_list);
//
//        EGL14.eglCreateWindowSurface(mEglDisplay, maEGLconfigs[0], null, attrib_list, 0);
//        mEglSurface = EGL10.eglCreateWindowSurface(mEgl, mEglDisplay, maEGLconfigs[0], view.getHolder());
//
//        if (mEglSurface == null || mEglSurface == EGL10.EGL_NO_SURFACE)
//        {
//            int error = mEgl.eglGetError();
//
//            if (error == EGL10.EGL_BAD_NATIVE_WINDOW)
//            {
//                Log.e(LOG_TAG, "Error: createWindowSurface() Returned EGL_BAD_NATIVE_WINDOW.");
//                return;
//            }
//            throw new RuntimeException("Error: createWindowSurface() Failed " + GLUtils.getEGLErrorString(error));
//        }
//        if (!mEgl.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext))
//            throw new RuntimeException("Error: eglMakeCurrent() Failed " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
//
//        int[] widthResult = new int[1];
//        int[] heightResult = new int[1];
//
//        mEgl.eglQuerySurface(mEglDisplay, mEglSurface, EGL10.EGL_WIDTH, widthResult);
//        mEgl.eglQuerySurface(mEglDisplay, mEglSurface, EGL10.EGL_HEIGHT, heightResult);
//        Log.i(LOG_TAG, "EGL Surface Dimensions:" + widthResult[0] + " " + heightResult[0]);

        GLES20.glClearColor(0,0.6f,0.9f,1);

        DroidCli.registerPrimitiveRendererDelegate(Cli.PrimitiveTriangle, new TriangleRendererDelegate());
    }

    @Override
    public void onStop() {

    }
}
