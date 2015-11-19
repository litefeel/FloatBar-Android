package com.litefeel.floatbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.litefeel.floatbar.logic.FloatBarManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatBarManager.init(this);
        FloatBarManager.addFloatBar(FloatBar.getInstance(this), true);
    }
}
