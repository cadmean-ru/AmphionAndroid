package ru.cadmean.amphionandroid;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ru.cadmean.amphion.android.cli.CallbackHandler;
import ru.cadmean.amphion.android.cli.Context;
import ru.cadmean.amphion.android.cli.ExecDelegate;
import ru.cadmean.amphion.android.cli.FrontendDelegate;
import ru.cadmean.amphion.android.cli.Vector3;

public class AndroidFrontend implements FrontendDelegate {
    private final android.content.Context ctx;
    private final Handler mainHandler;
    private final MyGLView glView;
    private static CallbackHandler callbackHandler;

    public AndroidFrontend(android.content.Context ctx, MyGLView glView) {
        this.ctx = ctx;
        mainHandler = new Handler(ctx.getMainLooper());
        this.glView = glView;
    }

    @Override
    public void commencePanic(String s, String s1) {

    }

    @Override
    public void executeOnMainThread(ExecDelegate execDelegate) {
        mainHandler.post(execDelegate::execute);
    }

    @Override
    public void executeOnRenderingThread(ExecDelegate execDelegate) {
        glView.queueEvent(execDelegate::execute);
    }

    @Override
    public byte[] getAppData() {
        BufferedReader reader;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(ctx.getAssets().open("app.yaml"), "UTF-8"));


            StringBuilder stringBuilder = new StringBuilder();

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                stringBuilder.append(mLine);
                stringBuilder.append('\n');
            }

            return stringBuilder.toString().getBytes();
        }
        catch (IOException ex){
            return new byte[0];
        }
    }

    @Override
    public Context getContext() {
        Context context = new Context();
        context.setScreenSize(MyRenderer.getSurfaceSize());

        return context;
    }

    @Override
    public void init() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void run() {

    }

    @Override
    public void setCallback(CallbackHandler callbackHandler) {
        AndroidFrontend.callbackHandler = callbackHandler;
    }

    static void sendCallback(long code, String data) {
        if (callbackHandler == null)
            return;

        callbackHandler.handleCallback(code, data);
    }
}
