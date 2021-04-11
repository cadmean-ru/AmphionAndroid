package ru.cadmean.amphionandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ru.cadmean.amphion.android.droidCli.DroidCli;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyGLView myGLView = new MyGLView(this);

        DroidCli.amphionInitAndroid(
                new AndroidFrontend(this, myGLView),
                new AndroidResourceManager(this),
                new AndroidRendererDelegate(this, myGLView));

        setContentView(myGLView);
    }
}