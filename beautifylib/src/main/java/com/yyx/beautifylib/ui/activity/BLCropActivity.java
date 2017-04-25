package com.yyx.beautifylib.ui.activity;

import android.widget.TextView;

import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.UCropView;
import com.yyx.beautifylib.R;
import com.yyx.beautifylib.ui.activity.base.BLToolBarActivity;

/**
 * Created by Administrator on 2017/4/18.
 * 裁剪：暂时没用
 */

public class BLCropActivity extends BLToolBarActivity {
    private UCropView mUCropView;
    private GestureCropImageView mGestureCropImageView;
    private OverlayView mOverlayView;
    private TextView mTvRatio11, mTvRatioOriginal, mTvRatio34, mTvRatio32, mtvRatio169;

    @Override
    protected int getContentLayoutId() {
        return R.layout.bl_activity_crop;
    }

    @Override
    protected void customToolBarStyle() {
        mToolbar.setTitle("裁剪");
    }

    @Override
    protected void initView() {
        mUCropView = getViewById(R.id.crop_u_crop_view);
        mGestureCropImageView = mUCropView.getCropImageView();
        mOverlayView = mUCropView.getOverlayView();
        mTvRatio11 = getViewById(R.id.crop_ratio_1_1);
        mTvRatio32 = getViewById(R.id.crop_ratio_3_2);
        mtvRatio169 = getViewById(R.id.crop_ratio_16_9);
        mTvRatio34 = getViewById(R.id.crop_ratio_3_4);
        mTvRatioOriginal = getViewById(R.id.crop_ratio_original);
    }

    @Override
    protected void otherLogic() {

    }

    @Override
    protected void setListener() {

    }
}
