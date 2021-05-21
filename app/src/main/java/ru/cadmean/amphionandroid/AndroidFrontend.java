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
import ru.cadmean.amphion.android.dispatch.Message;
import ru.cadmean.amphion.android.dispatch.MessageDispatcher;
import ru.cadmean.amphion.android.dispatch.WorkDispatcher;

public class AndroidFrontend implements FrontendDelegate {
    private final android.content.Context ctx;
    private final MainWorkDispatcher mainWorkDispatcher;
    private final MyGLView glView;
    private static MessageDispatcher callbackDispatcher;

    public AndroidFrontend(android.content.Context ctx, MyGLView glView) {
        this.ctx = ctx;
        mainWorkDispatcher = new MainWorkDispatcher(new Handler(ctx.getMainLooper()));
        this.glView = glView;
    }

    @Override
    public void commencePanic(String s, String s1) {

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
    public WorkDispatcher getMainThreadDispatcher() {
        return mainWorkDispatcher;
    }

    @Override
    public WorkDispatcher getRenderingThreadDispatcher() {
        return glView;
    }

    @Override
    public void init() {

    }

    @Override
    public void run() {

    }

    @Override
    public void setCallbackDispatcher(MessageDispatcher messageDispatcher) {
        callbackDispatcher = messageDispatcher;
    }

    static void sendCallback(long code, String data) {
        if (callbackDispatcher == null)
            return;

        callbackDispatcher.sendMessage(new Message(code, data));
    }
}
