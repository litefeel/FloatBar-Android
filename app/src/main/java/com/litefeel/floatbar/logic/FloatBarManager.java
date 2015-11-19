package com.litefeel.floatbar.logic;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by lite3 on 2015/11/18.
 */
public class FloatBarManager {
    private static final String TAG = FloatBarManager.class.getSimpleName();
    private static Application _app;

    private static WindowManager _windowManager;

    // value is will show
    private static HashMap<FloatBarBase, Boolean> _floatBarMap = new HashMap<>();

    // running activity count
    private static int _runnintCount = 0;

    public static void init(Context context) {
        if (_app != null) return;

        _app = (Application) context.getApplicationContext();
        _windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        _app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d(TAG, "onActivityCreated:" + activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d(TAG, "onActivityStarted:" + activity);
                _runnintCount++;
                onEnterForeground();
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
                onEnterBackground();
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



    public static void addFloatBar(FloatBarBase bar, boolean isShow) {
        if (!_floatBarMap.containsKey(bar)) {
            _floatBarMap.put(bar, false);
            if (isShow) showFloatBar(bar);
        }
    }

    public static void removeFloatBar(FloatBarBase bar) {
        if (_floatBarMap.containsKey(bar)) {
            hideFloatBar(bar);
            _floatBarMap.remove(bar);
        }
    }

    public static void hideFloatBar(FloatBarBase bar) {
        if (_floatBarMap.containsKey(bar) && _floatBarMap.get(bar).booleanValue()) {
            _windowManager.removeView(bar);
            _floatBarMap.put(bar, false);
        }
    }

    public static void showFloatBar(FloatBarBase bar) {
        if (_floatBarMap.containsKey(bar) && !_floatBarMap.get(bar).booleanValue()) {
            _windowManager.addView(bar, bar.getWMLayoutParams());
            _floatBarMap.put(bar, true);
        }
    }

    private static void onEnterForeground() {
        if (_floatBarMap.isEmpty()) return;
        for (Map.Entry<FloatBarBase, Boolean> entry : _floatBarMap.entrySet()) {
            if (entry.getValue().booleanValue() && entry.getKey().getParent() == null) {
                _windowManager.addView(entry.getKey(), entry.getKey().getWMLayoutParams());
            }
        }
    }

    private static void onEnterBackground() {
        if (_floatBarMap.isEmpty()) return;
        for (Map.Entry<FloatBarBase, Boolean> entry : _floatBarMap.entrySet()) {
            if (entry.getKey().getParent() != null) {
                _windowManager.removeView(entry.getKey());
            }
        }
    }
}
