package com.yyx.beautifylib.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.yyx.beautifylib.ui.activity.BLBeautifyImageActivity;
import com.yyx.beautifylib.utils.ActivityUtils;

import java.util.List;

import static android.R.attr.data;

/**
 * Created by Administrator on 2017/4/15.
 */

public class BLBeautifyParam implements Parcelable {
    public static final String KEY = "beautify_image";

    public static final int REQUEST_CODE_BEAUTIFY_IMAGE = 2;

    private List<String> images;

    public BLBeautifyParam(){}

    public BLBeautifyParam(List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.images);
    }

    protected BLBeautifyParam(Parcel in) {
        this.images = in.createStringArrayList();
    }

    public static final Parcelable.Creator<BLBeautifyParam> CREATOR = new Parcelable.Creator<BLBeautifyParam>() {
        @Override
        public BLBeautifyParam createFromParcel(Parcel source) {
            return new BLBeautifyParam(source);
        }

        @Override
        public BLBeautifyParam[] newArray(int size) {
            return new BLBeautifyParam[size];
        }
    };

    public static void startActivity(Activity activity, BLBeautifyParam param){
        Intent intent = new Intent(activity, BLBeautifyImageActivity.class);

        intent.putExtra(KEY, param);
        ActivityUtils.startActivityForResult(activity, intent, REQUEST_CODE_BEAUTIFY_IMAGE);
    }

}
