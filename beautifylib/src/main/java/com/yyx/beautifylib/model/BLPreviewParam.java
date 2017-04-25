package com.yyx.beautifylib.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.yyx.beautifylib.ui.activity.BLPreviewActivity;
import com.yyx.beautifylib.utils.ActivityUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public class BLPreviewParam implements Parcelable {
    public static final String KEY = "preview";
    public static final int REQUEST_CODE_PREVIEW = 0;

    public static final int MODE_NORMAL = 1;    //正常预览模式
    public static final int MODE_SELECTED = 2;  //图片选择预览模式

    private int mode = MODE_NORMAL;
    private int selectMax;
    private List<String> images;
    private List<String> selectedImages;
    private int position;

    public BLPreviewParam() {
    }

    /**
     * 构造正常预览的参数对象
     * @param position
     * @param images
     */
    public BLPreviewParam(int position, List<String> images){
        this.position = position;
        this.images = images;
    }

    /**
     * 构造选择预览的参数对象
     * @param mode
     * @param selectMax
     * @param position
     * @param images
     * @param selectedImages
     */
    public BLPreviewParam(int mode, int selectMax, int position, List<String> images, List<String> selectedImages) {
        this.mode = mode;
        this.selectMax = selectMax;
        this.position = position;
        this.images = images;
        this.selectedImages = selectedImages;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getSelectMax() {
        return selectMax;
    }

    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getSelectedImages() {
        return selectedImages;
    }

    public void setSelectedImages(List<String> selectedImages) {
        this.selectedImages = selectedImages;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static void startActivity(Activity activity, BLPreviewParam param){
        Intent intent = new Intent(activity, BLPreviewActivity.class);
        intent.putExtra(BLPreviewParam.KEY, param);
        ActivityUtils.startActivityForResult(activity, intent, REQUEST_CODE_PREVIEW);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mode);
        dest.writeInt(this.selectMax);
        dest.writeStringList(this.images);
        dest.writeStringList(this.selectedImages);
        dest.writeInt(this.position);
    }

    protected BLPreviewParam(Parcel in) {
        this.mode = in.readInt();
        this.selectMax = in.readInt();
        this.images = in.createStringArrayList();
        this.selectedImages = in.createStringArrayList();
        this.position = in.readInt();
    }

    public static final Parcelable.Creator<BLPreviewParam> CREATOR = new Parcelable.Creator<BLPreviewParam>() {
        @Override
        public BLPreviewParam createFromParcel(Parcel source) {
            return new BLPreviewParam(source);
        }

        @Override
        public BLPreviewParam[] newArray(int size) {
            return new BLPreviewParam[size];
        }
    };
}
