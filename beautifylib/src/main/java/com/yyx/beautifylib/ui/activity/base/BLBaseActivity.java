package com.yyx.beautifylib.ui.activity.base;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yyx.beautifylib.utils.ActivityUtils;
import com.yyx.beautifylib.utils.BLConfigManager;
import com.yyx.beautifylib.utils.ToastUtils;

/**
 * Created by Administrator on 2017/4/13.
 */

public abstract class BLBaseActivity extends AppCompatActivity{
    protected AppCompatActivity mInstance;
    protected LayoutInflater mInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setScreenOrientation();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mInstance = this;
        mInflater = LayoutInflater.from(this);
        getSupportActionBar().hide();
        setStatusBarColor(BLConfigManager.getStatusBarColor());
    }

    private void setScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected void toast(String msg){
        ToastUtils.toast(this, msg);
    }

    protected void gotoActivity(Class<? extends AppCompatActivity> clazz){
        ActivityUtils.startActivity(this, clazz);
    }

    /**
     * 状态栏颜色
     * @param color
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Window window = getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
            }
        }
    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }

    /**
     * 查找指定View的子控件
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id, View view) {
        return (VT) view.findViewById(id);
    }

    /**
     * 获取布局ID
     * @return
     */
    @LayoutRes
    protected abstract int getLayoutId();



}
