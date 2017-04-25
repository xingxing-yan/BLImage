package com.yyx.beautifylib.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muzhi.camerasdk.library.filter.util.ImageFilterTools;
import com.yyx.beautifylib.R;
import com.yyx.beautifylib.model.BLStickerInfo;
import com.yyx.beautifylib.tag.TagViewGroup;
import com.yyx.beautifylib.tag.model.TagGroupModel;
import com.yyx.beautifylib.utils.BLBitmapUtils;
import com.yyx.beautifylib.view.BLBeautifyImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public class BLBeautifyFragment extends Fragment {
    private BLBeautifyImageView mBeautifyImage;
    private String mPath;

    public static BLBeautifyFragment newInstance(String path) {
        BLBeautifyFragment f = new BLBeautifyFragment();
        Bundle b = new Bundle();
        b.putString("path", path);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPath = getArguments().getString("path");
    }

    public String getPath(){
        return mPath;
    }

    public void setPath(String path){
        mPath = path;
        if (mBeautifyImage != null){
            mBeautifyImage.setImage(path);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.bl_fragment_beautify, container, false);
        mBeautifyImage = (BLBeautifyImageView) view.findViewById(R.id.beautify_image);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //由于拍照的图片很大，所以要先做一下处理，否则在改变亮度和饱和度时会很卡

        mBeautifyImage.setImage(BLBitmapUtils.getBitmap(mPath));
    }

    /**
     * 添加滤镜
     * @param filterType
     */
    public void addFilter(ImageFilterTools.FilterType filterType){
        mBeautifyImage.addFilter(filterType);
    }

    public Bitmap getBitmap(){
        return mBeautifyImage.getGPUBitmap();
    }

    public void setBitmap(Bitmap bitmap){
        mBeautifyImage.setImage(bitmap);
    }

    /**
     * 添加贴图
     * @param stickerInfo
     */
    public void addSticker(BLStickerInfo stickerInfo){
        mBeautifyImage.addSticker(stickerInfo.getDrawableId());
    }


    public void addTextSticker(BLStickerInfo stickerInfo){
        mBeautifyImage.addTextSticker(stickerInfo.getText(), stickerInfo.getColor());
    }

    /**
     * 添加标签组
     * @param model
     * @param listener
     */
    public void addTagGroup(TagGroupModel model, TagViewGroup.OnTagGroupClickListener listener){
        mBeautifyImage.addTagGroup(model, listener, true);
    }

    /**
     * 移除标签组
     * @param tagViewGroup
     */
    public void removeTagGroup(TagViewGroup tagViewGroup) {
        mBeautifyImage.removeTagGroup(tagViewGroup);
    }

    /**
     * 根据TagViewGroup获取TagGroupModel
     * @param group
     * @return
     */
    public TagGroupModel getTagGroupModel(TagViewGroup group){
        return mBeautifyImage.getTagGroupModel(group);
    }

    public List<TagGroupModel> getTagGroupModelList(){
        return mBeautifyImage.getTagGroupModelList();
    }

    public void stickerLocked(boolean lock){
        if (mBeautifyImage == null)
            return;
        mBeautifyImage.stickerLocked(lock);
    }

    public String complete(){
        if(mBeautifyImage != null){
            return mBeautifyImage.save();
        }else{
            return mPath;
        }
    }

}
