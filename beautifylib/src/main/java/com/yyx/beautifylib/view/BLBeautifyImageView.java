package com.yyx.beautifylib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.muzhi.camerasdk.library.filter.GPUImageView;
import com.muzhi.camerasdk.library.filter.util.ImageFilterTools;
import com.yyx.beautifylib.R;
import com.yyx.beautifylib.sticker.DrawableSticker;
import com.yyx.beautifylib.sticker.Sticker;
import com.yyx.beautifylib.sticker.StickerView;
import com.yyx.beautifylib.sticker.TextSticker;
import com.yyx.beautifylib.tag.TagViewGroup;
import com.yyx.beautifylib.tag.model.TagGroupModel;
import com.yyx.beautifylib.tag.views.TagImageView;
import com.yyx.beautifylib.utils.BLBitmapUtils;
import com.yyx.beautifylib.utils.FilterUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public class BLBeautifyImageView extends FrameLayout {
    private Context mContext;
    private StickerView mStickerView;
    private GPUImageView mGpuImageView;
    private TagImageView mTagGroupLayout;

    public BLBeautifyImageView(@NonNull Context context) {
        this(context, null);
    }

    public BLBeautifyImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BLBeautifyImageView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.bl_beautify_image_view, this, true);
        mStickerView = (StickerView) rootView.findViewById(R.id.bl_sticker_view);
        mGpuImageView = (GPUImageView) rootView.findViewById(R.id.bl_gpu_image_view);
        mTagGroupLayout = (TagImageView) rootView.findViewById(R.id.bl_tag_image_view);

        initStickerView();
    }

    /**********************************GPUImageView相关*********************************/
    public void addFilter(ImageFilterTools.FilterType filterType) {
        FilterUtils.addFilter(mContext, filterType, mGpuImageView);
    }

    /**
     * 设置网络加载图片
     *
     * @param url
     */
    public void setImageUrl(String url) {
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        float width = (float) resource.getWidth();
                        float height = (float) resource.getHeight();
                        float ratio = width / height;
                        mGpuImageView.setRatio(ratio);
                        setImage(resource);
                    }
                });
    }

    /**
     * 设置本地路径图片
     *
     * @param path
     */
    public void setImage(String path) {
        mGpuImageView.setImage(path);
    }

    public void setImage(Bitmap bitmap) {
        float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();
        float ratio = width / height;
        mGpuImageView.setRatio(ratio);
        mGpuImageView.setImage(bitmap);
    }

    public Bitmap getGPUBitmap() {
        return mGpuImageView.getCurrentBitMap();
    }

    public GPUImageView getGPUImageView() {
        return mGpuImageView;
    }


    public String save() {
        return getFilterImage();
    }

    /**
     * 合并图片
     *
     * @return
     */
    public String getFilterImage() {
        mGpuImageView.setDrawingCacheEnabled(true);
        Bitmap editbmp = Bitmap.createBitmap(mGpuImageView.getDrawingCache());
        try {
            Bitmap fBitmap = mGpuImageView.capture();
            Bitmap bitmap = Bitmap.createBitmap(fBitmap.getWidth(), fBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas cv = new Canvas(bitmap);
            cv.drawBitmap(fBitmap, 0, 0, null);
            cv.drawBitmap(editbmp, 0, 0, null);

            //最终合并生成图片
            String path = BLBitmapUtils.saveAsBitmap(mContext, bitmap);
            bitmap.recycle();
            return path;

        } catch (Exception e) {
            return "";
        }
    }


    /**********************************StickerView相关*********************************/
    private void initStickerView() {
        mStickerView.configDefaultIcons();
        mStickerView.setLocked(false);
//        mStickerView.setConstrained(true);

        mStickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerClicked(Sticker sticker) {

            }

            @Override
            public void onStickerDeleted(Sticker sticker) {

            }

            @Override
            public void onStickerDragFinished(Sticker sticker) {

            }

            @Override
            public void onStickerZoomFinished(Sticker sticker) {

            }

            @Override
            public void onStickerFlipped(Sticker sticker) {

            }

            @Override
            public void onStickerDoubleTapped(Sticker sticker) {

            }
        });
    }

    /**
     * 添加图片贴图
     * @param drawableId
     */
    public void addSticker(int drawableId) {
        if (drawableId <= 0)
            return;
        Drawable drawable = ContextCompat.getDrawable(mContext, drawableId);
        mStickerView.addSticker(new DrawableSticker(drawable));
    }

    /**
     * 添加文字贴图
     * @param text
     * @param color
     */
    public void addTextSticker(String text, int color){
        TextSticker sticker = new TextSticker(mContext);
        sticker.setText(text);
        sticker.setTextColor(color);
        sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        sticker.resizeText();

        mStickerView.addSticker(sticker);
    }

    /**
     * 添加文字贴图
     * @param text
     */
    public void addTextSticker(String text){
        addTextSticker(text, Color.WHITE);
    }

    public void stickerLocked(boolean lock) {
        mStickerView.setLocked(lock);
    }

    /**********************************TagImageView相关*********************************/
    public void addTagGroup(TagGroupModel model, TagViewGroup.OnTagGroupClickListener listener, boolean editMode) {
        mTagGroupLayout.setEditMode(editMode);
        mTagGroupLayout.addTagGroup(model, listener);
    }

    public void removeTagGroup(TagViewGroup tagViewGroup) {
        mTagGroupLayout.removeTagGroup(tagViewGroup);
    }

    public List<TagGroupModel> getTagGroupModelList() {
        return mTagGroupLayout.getTagGroupModelList();
    }

    public TagGroupModel getTagGroupModel(TagViewGroup group) {
        return mTagGroupLayout.getTagGroupModel(group);
    }

    public void setTagModelList(List<TagGroupModel> tagGroupList) {
        mTagGroupLayout.setTagList(tagGroupList);
    }

    public TagImageView getTagImageView() {
        return mTagGroupLayout;
    }

}
