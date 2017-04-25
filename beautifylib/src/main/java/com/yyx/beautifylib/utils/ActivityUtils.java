package com.yyx.beautifylib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Activity工具类.
 */
public class ActivityUtils {

    /**
     * 启动Activity
     *
     * @param context
     * @param clazz
     */
    public static void startActivity(Context context, Class<?> clazz) {
        startActivity(context, clazz, null);
    }

    /**
     * 启动Activity
     *
     * @param context
     * @param intent
     */
    public static void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }

    /**
     * 启动Activity
     *
     * @param context
     * @param clazz
     * @param bundle
     */
    public static void startActivity(Context context, Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(context, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 启动有返回值的Activity
     *
     * @param activity
     * @param clazz
     * @param requestCode
     */
    public static void startActivityForResult(Activity activity, Class<?> clazz, int requestCode) {
        startActivityForResult(activity, clazz, requestCode, null);
    }

    /**
     * 启动有返回值的Activity
     *
     * @param activity
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    public static void startActivityForResult(Activity activity, Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(activity, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     *  启动有返回值的Activity
     * @param activity
     * @param intent
     * @param requestCode
     */
    public static void startActivityForResult(Activity activity, Intent intent, int requestCode){
        activity.startActivityForResult(intent, requestCode);
    }

}
