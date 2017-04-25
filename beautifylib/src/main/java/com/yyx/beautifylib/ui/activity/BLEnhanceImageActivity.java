package com.yyx.beautifylib.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.muzhi.camerasdk.library.filter.GPUImageView;
import com.muzhi.camerasdk.library.utils.PhotoEnhance;
import com.yyx.beautifylib.R;
import com.yyx.beautifylib.model.BLEnhanceParam;
import com.yyx.beautifylib.ui.activity.base.BLToolBarActivity;
import com.yyx.beautifylib.utils.BLConfigManager;

/**
 * Created by Administrator on 2017/4/19.
 * 编辑：亮度，对比度，饱和度的调节
 */

public class BLEnhanceImageActivity extends BLToolBarActivity implements View.OnClickListener {
//    private ImageView mIvSource;
    private GPUImageView mIvSource;
    private SeekBar mSeekbar;
    private TextView mTvBrightness, mTvContrast, mTvSaturation;

    private PhotoEnhance mPhotoEnhance;
    private Bitmap mSource;
    public final int DEFAULT_PROGRESS = 127;
    private int mBProgress, mCProgress, mSProgress;

    public enum BOTTOM_TAB {BRIGHTNESS, CONTRAST, SATURATION}

    private BOTTOM_TAB mSelectedTab = BOTTOM_TAB.BRIGHTNESS;

    private BLEnhanceParam mParam;

    @Override
    protected int getContentLayoutId() {
        return R.layout.bl_activity_enhance_image;
    }

    @Override
    protected void customToolBarStyle() {
        mToolbar.setTitle("编辑");
        mToolbar.inflateMenu(R.menu.menu_preview);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.preview_menu) {
                    setResult(RESULT_OK);
                    BLEnhanceParam.recycleBitmap();
                    BLEnhanceParam.bitmap = mSource;
                    onBackPressed();
                }
                return false;
            }
        });
    }

    @Override
    protected void initView() {
        mIvSource = getViewById(R.id.enhance_image);
        mTvBrightness = getViewById(R.id.enhance_tab_brightness);
        mTvContrast = getViewById(R.id.enhance_tab_contrast);
        mTvSaturation = getViewById(R.id.enhance_tab_saturation);
        mSeekbar = getViewById(R.id.enhance_seekbar);
    }

    @Override
    protected void otherLogic() {
        mParam = getIntent().getParcelableExtra(BLEnhanceParam.KEY);
        mBProgress = mCProgress = mSProgress = DEFAULT_PROGRESS;
        setSelectedTab(BOTTOM_TAB.BRIGHTNESS);
        mSeekbar.setProgress(mBProgress);

        mSource = BLEnhanceParam.bitmap;
        mPhotoEnhance = new PhotoEnhance(mSource);

        float width = (float) mSource.getWidth();
        float height = (float) mSource.getHeight();
        float ratio = width / height;
        mIvSource.setRatio(ratio);
        mIvSource.setImage(mSource);

    }

    @Override
    protected void setListener() {
        mTvBrightness.setOnClickListener(this);
        mTvSaturation.setOnClickListener(this);
        mTvContrast.setOnClickListener(this);

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (mSelectedTab) {
                    case BRIGHTNESS:
                        mPhotoEnhance.setBrightness(progress);
                        mSource = mPhotoEnhance.handleImage(mPhotoEnhance.Enhance_Brightness);
                        break;
                    case CONTRAST:
                        mPhotoEnhance.setContrast(progress);
                        mSource = mPhotoEnhance.handleImage(mPhotoEnhance.Enhance_Contrast);
                        break;
                    case SATURATION:
                        mPhotoEnhance.setSaturation(progress);
                        mSource = mPhotoEnhance.handleImage(mPhotoEnhance.Enhance_Saturation);
                        break;
                }
                if (mSource != null) {
//                    mIvSource.setImageBitmap(mSource);
                    mIvSource.setImage(mSource);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                switch (mSelectedTab) {
                    case BRIGHTNESS:
                        mBProgress = seekBar.getProgress();
//                        mSource = mPhotoEnhance.handleImage(mPhotoEnhance.Enhance_Brightness);
                        break;
                    case CONTRAST:
                        mCProgress = seekBar.getProgress();
//                        mSource = mPhotoEnhance.handleImage(mPhotoEnhance.Enhance_Contrast);
                        break;
                    case SATURATION:
                        mSProgress = seekBar.getProgress();
//                        mSource = mPhotoEnhance.handleImage(mPhotoEnhance.Enhance_Saturation);
                        break;
                }
//                if (mSource != null){
//                    mIvSource.setImageBitmap(mSource);
//                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.enhance_tab_brightness) {
            setSelectedTab(BOTTOM_TAB.BRIGHTNESS);
        } else if (resId == R.id.enhance_tab_contrast) {
            setSelectedTab(BOTTOM_TAB.CONTRAST);
        } else if (resId == R.id.enhance_tab_saturation) {
            setSelectedTab(BOTTOM_TAB.SATURATION);
        }

    }

    private void setSelectedTab(BOTTOM_TAB tab) {
        mTvBrightness.setTextColor(Color.BLACK);
        mTvContrast.setTextColor(Color.BLACK);
        mTvSaturation.setTextColor(Color.BLACK);
        mSelectedTab = tab;
        if (tab == BOTTOM_TAB.BRIGHTNESS) {
            mTvBrightness.setTextColor(BLConfigManager.getPrimaryColor());
            mSeekbar.setProgress(mBProgress);
        } else if (tab == BOTTOM_TAB.CONTRAST) {
            mTvContrast.setTextColor(BLConfigManager.getPrimaryColor());
            mSeekbar.setProgress(mCProgress);
        } else if (tab == BOTTOM_TAB.SATURATION) {
            mTvSaturation.setTextColor(BLConfigManager.getPrimaryColor());
            mSeekbar.setProgress(mSProgress);
        }

    }
}
