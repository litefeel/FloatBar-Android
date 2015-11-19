package com.litefeel.floatbar;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by lite3 on 2015/11/18.
 */
public class FloatBarManager {
    private static final String TAG = FloatBarManager.class.getSimpleName();
    private static Application _app;
    private static FloatBar _floatBar;

    private static WindowManager _windowManager;

    // running activity count
    private static int _runnintCount = 0;

    public static void init(Context context) {
        if (_app != null) return;

        _app = (Application) context.getApplicationContext();
        initFloatBar();

        _app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d(TAG, "onActivityCreated:" + activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d(TAG, "onActivityStarted:" + activity);
                _runnintCount++;
                if (_floatBar.getParent() == null) {
                    _windowManager.addView(_floatBar, _floatBar.getWMLayoutParams());
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d(TAG, "onActivityResumed:" + activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.d(TAG, "onActivityPaused:" + activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d(TAG, "onActivityStopped:" + activity);
                _runnintCount--;
                if (_runnintCount == 0) {
                    _windowManager.removeView(_floatBar);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.d(TAG, "onActivitySaveInstanceState:" + activity);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(TAG, "onActivityDestroyed:" + activity);
            }
        });
    }

    private static void initFloatBar() {
        _windowManager = (WindowManager) _app.getSystemService(Context.WINDOW_SERVICE);

        _floatBar = new FloatBar(_app);
    }
}
