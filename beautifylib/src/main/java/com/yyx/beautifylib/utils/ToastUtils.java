package com.yyx.beautifylib.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    private static Toast mToast;

    /**
     * Toast提示信息
     *
     * @param context
     * @param msg
     */
    public static void toast(Context context, String msg) {
        toast(context, msg, Toast.LENGTH_SHORT);
    }

    /**
     * Toast提示信息
     *
     * @param context
     * @param msg
     */
    public static void toastLong(Context context, String msg) {
        toast(context, msg, Toast.LENGTH_LONG);
    }

    /**
     * 非阻塞试显示Toast,防止出现连续点击Toast时的显示问题
     */

    public static void toast(Context context, String msg, int time) {
        if (null == msg){
            msg = "";
        }
        if (null == mToast) {
            mToast = Toast.makeText(context, msg, time);
        } else {
            mToast.setText(msg);
            mToast.setDuration(time);
        }
        mToast.show();
    }

}
