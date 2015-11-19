package com.litefeel.floatbar;

import android.content.Context;
import android.view.View;

import com.litefeel.floatbar.logic.FloatBarBase;

/**
 * Created by lie3 on 2015/11/18.
 */
public class FloatBar extends FloatBarBase {
    private static final String TAG = FloatBar.class.getSimpleName();

    private static FloatBar _instance;

    private View _floatBarLayout;
    private View _contentView;


    public static FloatBar getInstance(Context context) {
        if (_instance == null) {
            _instance = new FloatBar(context);
        }
        return _instance;
    }


    public FloatBar(Context context) {
        super(context);
    }

    @Override
    protected int getViewWidth() {
        return _floatBarLayout.getLayoutParams().width;
    }

    @Override
    protected int getViewHeight() {
        return _floatBarLayout.getLayoutParams().height;
    }

    @Override
    protected void initView() {
        inflate(getContext(), R.layout.float_bar, this);
        _floatBarLayout = findViewById(R.id.float_bar_layout);
        _contentView = findViewById(R.id.float_bar_content_layout);
    }

    // 打开功能区域
    @Override
    protected void opoenFunctionArea() {
        super.opoenFunctionArea();
        _contentView.setVisibility(VISIBLE);
    }
    // 关闭功能区域
    @Override
    protected void closeFunctionArea() {
        super.closeFunctionArea();
        _contentView.setVisibility(GONE);
    }
}
