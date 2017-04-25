package com.yyx.beautifylib.model;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.yyx.beautifylib.ui.activity.BLEnhanceImageActivity;
import com.yyx.beautifylib.utils.ActivityUtils;

/**
 * Created by Administrator on 2017/4/19.
 */

public class BLEnhanceParam implements Parcelable {
    public static final String KEY = "enhance";
    public static final int REQUEST_CODE_ENHANCE = 0x010;
    private String path;
    public static Bitmap bitmap;

    public BLEnhanceParam(){}

    public BLEnhanceParam(String path) {
        this.path = path;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static void recycleBitmap(){
        if (bitmap != null){
            bitmap.recycle();
            bitmap = null;
        }
    }

    public static void startActivity(Activity activity, BLEnhanceParam param){
        Intent intent = new Intent(activity, BLEnhanceImageActivity.class);
        intent.putExtra(BLEnhanceParam.KEY, param);
        ActivityUtils.startActivityForResult(activity, intent, BLEnhanceParam.REQUEST_CODE_ENHANCE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
    }

    protected BLEnhanceParam(Parcel in) {
        this.path = in.readString();
    }

    public static final Creator<BLEnhanceParam> CREATOR = new Creator<BLEnhanceParam>() {
        @Override
        public BLEnhanceParam createFromParcel(Parcel source) {
            return new BLEnhanceParam(source);
        }

        @Override
        public BLEnhanceParam[] newArray(int size) {
            return new BLEnhanceParam[size];
        }
    };
}
