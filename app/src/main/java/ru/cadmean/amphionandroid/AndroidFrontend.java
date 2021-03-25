package ru.cadmean.amphionandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ru.cadmean.amphion.android.cli.CallbackHandler;
import ru.cadmean.amphion.android.cli.Context;
import ru.cadmean.amphion.android.cli.ExecDelegate;
import ru.cadmean.amphion.android.cli.FrontendDelegate;
import ru.cadmean.amphion.android.cli.Vector3;

public class AndroidFrontend implements FrontendDelegate {
    private android.content.Context ctx;

    public AndroidFrontend(android.content.Context ctx) {
        this.ctx = ctx;
    }


    @Override
    public void commencePanic(String s, String s1) {

    }

    @Override
    public void executeOnMainThread(ExecDelegate execDelegate) {

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
        context.setScreenSize(new Vector3(100,100,100));

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

    }
}
