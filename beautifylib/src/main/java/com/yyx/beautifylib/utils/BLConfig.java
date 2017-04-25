package com.yyx.beautifylib.utils;

import android.graphics.Color;

/**
 * Created by Administrator on 2017/4/25.
 */

public class BLConfig {
    public int STATUS_BAR_COLOR = Color.parseColor("#D50A6E");
    public int TOOL_BAR_COLOR = Color.parseColor("#d4237a");
    public int PRIMARY_COLOR = TOOL_BAR_COLOR;

    public BLConfig statusBarColor(int color){
        this.STATUS_BAR_COLOR = color;
        return this;
    }

    public BLConfig toolBarColor(int color){
        this.TOOL_BAR_COLOR = color;
        return this;
    }

    public BLConfig primaryColor(int color){
        this.PRIMARY_COLOR = color;
        return this;
    }

}
