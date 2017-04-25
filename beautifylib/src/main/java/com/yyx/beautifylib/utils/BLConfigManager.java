package com.yyx.beautifylib.utils;

/**
 * Created by Administrator on 2017/4/25.
 */

public class BLConfigManager {
    private static BLConfig mConfig = new BLConfig();

    public static BLConfig register(BLConfig config){
        mConfig = config;
        return mConfig;
    }

    public static int getStatusBarColor(){
        return mConfig.STATUS_BAR_COLOR;
    }

    public static int getToolBarColor(){
        return mConfig.TOOL_BAR_COLOR;
    }

    public static int getPrimaryColor(){
        return mConfig.PRIMARY_COLOR;
    }


    public static BLConfig getConfig(){
        return mConfig;
    }

}
