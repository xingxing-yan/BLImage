package com.yyx.beautifylib.ui.activity.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.yyx.beautifylib.R;
import com.yyx.beautifylib.utils.BLConfigManager;

/**
 * Created by Administrator on 2017/4/13.
 */

public abstract class BLToolBarActivity extends BLBaseActivity{
    protected Toolbar mToolbar;
    protected FrameLayout mFlContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = getViewById(R.id.toolbar_base);
        mFlContent = getViewById(R.id.toolbar_base_fl);
        mFlContent.addView(mInflater.inflate(getContentLayoutId(), null));
        setToolBar(mToolbar);

        initView();
        otherLogic();
        setListener();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bl_activity_toolbar;
    }

    protected void setToolBar(Toolbar toolbar){
        toolbar.setBackgroundColor(BLConfigManager.getToolBarColor());
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        customToolBarStyle();
    }

    /**
     * 内容布局
     * @return
     */
    protected abstract int getContentLayoutId();

    /**
     * 自定义toolbar的样式
     */
    protected abstract void customToolBarStyle();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 其他业务逻辑
     */
    protected abstract void otherLogic();

    /**
     * 初始化监听器
     */
    protected abstract void setListener();
}
