package com.yyx.blimage;

import android.app.Application;
import android.graphics.Color;

import com.yyx.beautifylib.utils.BLConfig;
import com.yyx.beautifylib.utils.BLConfigManager;

/**
 * Created by Administrator on 2017/4/25.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BLConfigManager.register(new BLConfig())
                .statusBarColor(Color.parseColor("#D50A6E"))    //设置状态栏颜色
                .toolBarColor(Color.parseColor("#d4237a"))  //设置toolbar颜色
                .primaryColor(Color.parseColor("#d4237a")); //设置应用primary颜色
    }
}
