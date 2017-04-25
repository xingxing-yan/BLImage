package com.yyx.beautifylib.model;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.yyx.beautifylib.ui.activity.BLScrawlActivity;
import com.yyx.beautifylib.utils.ActivityUtils;

/**
 * Created by Administrator on 2017/4/21.
 * 涂鸦参数类
 */

public class BLScrawlParam implements Parcelable {
    public static final String KEY = "scrawl";
    public static final int REQUEST_CODE_SCRAWL = 0x1234;
    public static Bitmap bitmap;
    private String path;

    public BLScrawlParam(){}

    public BLScrawlParam(String path) {
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

    public static void startActivity(Activity activity, BLScrawlParam param){
        Intent intent = new Intent(activity, BLScrawlActivity.class);
        intent.putExtra(KEY, param);
        ActivityUtils.startActivityForResult(activity, intent, REQUEST_CODE_SCRAWL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
    }

    protected BLScrawlParam(Parcel in) {
        this.path = in.readString();
    }

    public static final Parcelable.Creator<BLScrawlParam> CREATOR = new Parcelable.Creator<BLScrawlParam>() {
        @Override
        public BLScrawlParam createFromParcel(Parcel source) {
            return new BLScrawlParam(source);
        }

        @Override
        public BLScrawlParam[] newArray(int size) {
            return new BLScrawlParam[size];
        }
    };
}
