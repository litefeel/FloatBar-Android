package com.litefeel.floatbar.logic;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewManager;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

/**
 * Created by lie3 on 2015/11/18.
 */
public abstract class FloatBarBase extends LinearLayout {
    private static final String TAG = FloatBarBase.class.getSimpleName();
    private int _xInView;
    private int _yInView;
    private int _xDownInScreen;
    private int _yDownInScreen;
    private int _xInScreen;
    private int _yInScreen;
    private int _screenWidth;
    private int _screenHeight;
    private int _statusBarHeight;
    private boolean _opening;       // 是否打开功能区域
    private WindowManager.LayoutParams _layoutParams;
    private ViewManager _windowManager;

    public FloatBarBase(Context context) {
        super(context);

        initView();
        closeFunctionArea();
        initLayoutParams();
    }

    protected abstract int getViewWidth();
    protected abstract int getViewHeight();
    protected abstract void initView();

    public WindowManager.LayoutParams getWMLayoutParams() {
        return _layoutParams;
    }

    private void initLayoutParams() {
        _windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        _layoutParams = new WindowManager.LayoutParams();
        _layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;     // 需要权限 android.permission.SYSTEM_ALERT_WINDOW
        _layoutParams.format = PixelFormat.RGBA_8888;                   // 设置图片格式，效果为背景透明
        _layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        _layoutParams.gravity = Gravity.LEFT | Gravity.TOP;

        DisplayMetrics dm = getResources().getDisplayMetrics();
        _screenWidth = dm.widthPixels;
        _screenHeight = dm.heightPixels;

        _layoutParams.x = 0;
        _layoutParams.y = _screenHeight / 2;
        _layoutParams.width = getViewWidth();
        _layoutParams.height = getViewHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                _xInView = (int) event.getX();
                _yInView = (int) event.getY();
                _xDownInScreen = _xInScreen = (int) event.getRawX();
                _yDownInScreen = _yInScreen = (int) (event.getRawY() - getStatusBarHeight());
                break;
            case MotionEvent.ACTION_MOVE:
                _xInScreen = (int) event.getRawX();
                _yInScreen = (int) (event.getRawY() - getStatusBarHeight());
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
                if (_xDownInScreen == _xInScreen && _yDownInScreen == _yInScreen) {
                    if (_opening)
                        closeFunctionArea();
                    else
                        opoenFunctionArea();
                }
                break;
        }
        return true;
    }

    // 打开功能区域
    protected void opoenFunctionArea() {
        _opening = true;
    }
    // 关闭功能区域
    protected void closeFunctionArea() {
        _opening = false;
    }

    private void updateViewPosition() {
        _layoutParams.x = (int) (_xInScreen - _xInView);
        _layoutParams.y = (int) (_yInScreen - _yInView);
        _windowManager.updateViewLayout(this, _layoutParams);
    }
    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (_statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                _statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return _statusBarHeight;
    }
}
