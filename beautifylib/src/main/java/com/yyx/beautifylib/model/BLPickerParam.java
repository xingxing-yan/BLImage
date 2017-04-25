package com.yyx.beautifylib.model;

import android.app.Activity;

import com.yyx.beautifylib.ui.activity.BLPhotoPickActivity;
import com.yyx.beautifylib.utils.ActivityUtils;

/**
 * Created by Administrator on 2017/4/23.
 */

public class BLPickerParam {

    public static final int REQUEST_CODE_PHOTO_PICKER = 0x1003;

    public static void startActivity(Activity activity){
        ActivityUtils.startActivityForResult(activity, BLPhotoPickActivity.class, REQUEST_CODE_PHOTO_PICKER);
    }
}
