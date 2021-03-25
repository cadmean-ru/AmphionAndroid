package ru.cadmean.amphionandroid;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import ru.cadmean.amphion.android.cli.ResourceManagerDelegate;

public class AndroidResourceManager implements ResourceManagerDelegate {
    private Context ctx;

    public AndroidResourceManager(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public byte[] readFile(String s) throws Exception {
        BufferedReader reader;

        reader = new BufferedReader(
                new InputStreamReader(ctx.getAssets().open(s), "UTF-8"));

        StringBuilder stringBuilder = new StringBuilder();

        String mLine;
        while ((mLine = reader.readLine()) != null) {
            stringBuilder.append(mLine);
        }

        return stringBuilder.toString().getBytes();
    }
}
