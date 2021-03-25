package ru.cadmean.amphionandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import ru.cadmean.amphion.android.bind.Bind;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyGLView(this));

        Bind.amphionInitAndroid(new AndroidFrontend(this), new AndroidResourceManager(this), new AndroidRendererDelegate());
    }

    
}