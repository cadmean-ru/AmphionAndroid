package ru.cadmean.amphionandroid;

import ru.cadmean.amphion.android.cli.CallbackHandler;
import ru.cadmean.amphion.android.cli.Context;
import ru.cadmean.amphion.android.cli.ExecDelegate;
import ru.cadmean.amphion.android.cli.FrontendDelegate;
import ru.cadmean.amphion.android.cli.Vector3;

public class AndroidFrontend implements FrontendDelegate {

    @Override
    public void commencePanic(String s, String s1) {

    }

    @Override
    public void executeOnMainThread(ExecDelegate execDelegate) {

    }

    @Override
    public byte[] getAppData() {
        return new byte[0];
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
