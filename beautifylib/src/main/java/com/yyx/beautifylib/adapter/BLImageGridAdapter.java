package com.yyx.beautifylib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yyx.beautifylib.R;
import com.yyx.beautifylib.model.ImageInfo;
import com.yyx.beautifylib.utils.BLConfigManager;
import com.yyx.beautifylib.utils.BLSelectedStateListDrawable;
import com.yyx.beautifylib.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */

public class BLImageGridAdapter extends BaseAdapter {
    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;

    public int MAX_SELECT_SIZE = 9;//最大选择数量

    private Context mContext;
    private LayoutInflater mInflater;
    private boolean showCamera = true;

    private List<ImageInfo> mImages = new ArrayList<ImageInfo>();
    private List<ImageInfo> mSelectedImages = new ArrayList<ImageInfo>();

    private int mItemSize;
    private GridView.LayoutParams mItemLayoutParams;

    private SelectedListener mSelectedListener;

    public void setSelectedListener(SelectedListener listener){
        mSelectedListener = listener;
    }

    public BLImageGridAdapter(Context context, boolean isShowCamera) {
        mContext = context;
        showCamera = isShowCamera;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemLayoutParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
    }

    public void setShowCamera(boolean b) {
        if (showCamera == b) return;

        showCamera = b;
        notifyDataSetChanged();
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    /**
     * 选择某个图片，改变选择状态
     *
     * @param imageInfo
     */
    public void select(ImageInfo imageInfo) {
        if (imageInfo == null)
            return;
        if (mSelectedImages.contains(imageInfo)) {
            mSelectedImages.remove(imageInfo);
        } else {
            if (isSelectedMax()){
                ToastUtils.toast(mContext, mContext.getResources().getString(R.string.camerasdk_msg_amount_limit));
            }else{
                mSelectedImages.add(imageInfo);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 是否选择图片到最大值
     * @return
     */
    public boolean isSelectedMax(){
        return MAX_SELECT_SIZE == mSelectedImages.size();
    }

    /**
     * 重置选择的图片列表
     *
     * @param resultList
     */
    public void resetSelectedList(List<String> resultList) {
        if (resultList.size() == 0)
            return;
        mSelectedImages.clear();
        for (String path : resultList) {
            ImageInfo imageInfo = getImageByPath(path);
            if (imageInfo != null) {
                mSelectedImages.add(imageInfo);
            }
        }
        if (mSelectedImages.size() > 0) {
            notifyDataSetChanged();
        }
    }

    /**
     * 合并图片选择集合
     * @param list
     */
    public void mergeSelectedList(List<String> list){
        /**
         * 去除取消选择的图片
         * 注：涉及到循环删除list时，不能用foreach循环，要用Iterator去操作，
         * 否则会抛java.util.ConcurrentModificationException异常
         */
        Iterator<ImageInfo> it = mSelectedImages.iterator();
        while (it.hasNext()){
            ImageInfo info = it.next();
            if (!list.contains(info.path)){
                it.remove();
            }
        }

        //添加新增选择的图片
        for (String path : list){
            ImageInfo image = getImageByPath(path);
            if (image != null && !mSelectedImages.contains(image)){
                mSelectedImages.add(image);
            }
        }
        notifyDataSetChanged();
    }


    public List<ImageInfo> getSelectedImageList(){
        return mSelectedImages;
    }

    /**
     * 获取选中图片的路径集合
     * @return
     */
    public ArrayList<String> getSelectedImagePathList(){
        ArrayList<String> imagePaths = new ArrayList<>();
        for (ImageInfo info : mSelectedImages){
            imagePaths.add(info.path);
        }
        return imagePaths;
    }

    /**
     * 获取所有图片的路径集合
     * @return
     */
    public ArrayList<String> getImagePathList(){
        ArrayList<String> imagePaths = new ArrayList<>();
        for (ImageInfo info : mImages){
            imagePaths.add(info.path);
        }
        return imagePaths;
    }

    public ImageInfo getImageByPath(String path) {
        if (mImages != null && mImages.size() > 0) {
            for (ImageInfo imageInfo : mImages) {
                if (imageInfo.path.equalsIgnoreCase(path)) {
                    return imageInfo;
                }
            }
        }
        return null;
    }

    /**
     * 重置每个Column的Size
     * @param columnWidth
     */
    public void setItemSize(int columnWidth) {

        if(mItemSize == columnWidth){
            return;
        }

        mItemSize = columnWidth;

        mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);

        notifyDataSetChanged();
    }

    /**
     * 设置数据集
     *
     * @param imageInfos
     */
    public void setData(List<ImageInfo> imageInfos) {
//        mSelectedImages.clear();

        if (imageInfos != null && imageInfos.size() > 0) {
            mImages.clear();
            mImages.addAll(imageInfos);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (showCamera) {
            return position == 0 ? TYPE_CAMERA : TYPE_NORMAL;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getCount() {
        return showCamera ? mImages.size() + 1 : mImages.size();
    }

    @Override
    public ImageInfo getItem(int position) {
        if (showCamera) {
            if (position == 0) {
                return null;
            }
            return mImages.get(position - 1);
        } else {
            return mImages.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (type == TYPE_CAMERA) {
            convertView = mInflater.inflate(R.layout.camerasdk_list_item_camera, parent, false);
            convertView.setTag(null);
        } else if (type == TYPE_NORMAL) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.camerasdk_list_item_image, parent, false);
                holder = new ViewHolder(convertView);
            } else {
                holder = (ViewHolder) convertView.getTag();
                if (holder == null){
                    convertView = mInflater.inflate(R.layout.camerasdk_list_item_image, parent, false);
                    holder = new ViewHolder(convertView);
                }
            }
            if (holder != null){
                holder.bindData(getItem(position));
            }

            holder.ivCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select(getItem(position));
                    if (mSelectedListener != null){
                        mSelectedListener.onSelected(mSelectedImages);
                    }
                }
            });
        }

        /** Fixed View Size */
        GridView.LayoutParams lp = (GridView.LayoutParams) convertView.getLayoutParams();
        if(lp.height != mItemSize){
            convertView.setLayoutParams(mItemLayoutParams);
        }

        return convertView;
    }

    class ViewHolder {
        View itemView;
        ImageView ivPicture, ivCheck;

        View mask;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            ivPicture = (ImageView) itemView.findViewById(R.id.image);
            ivCheck = (ImageView) itemView.findViewById(R.id.checkmark);
            ivCheck.setImageDrawable(new BLSelectedStateListDrawable(mContext.getResources().getDrawable(R.drawable.bl_check_normal), BLConfigManager.getPrimaryColor()));
            mask = itemView.findViewById(R.id.mask);
            itemView.setTag(this);
        }

        public void bindData(final ImageInfo data) {
            if (data == null) return;
            //多选状态
            ivCheck.setVisibility(View.VISIBLE);
            if (mSelectedImages.contains(data)) {
                // 设置选中状态
                ivCheck.setSelected(true);
                mask.setVisibility(View.VISIBLE);
            } else {
                // 未选择
                ivCheck.setSelected(false);
                mask.setVisibility(View.GONE);
            }

            File imageFile = new File(data.path);
            if (mItemSize > 0) {
                // 显示图片
                Glide.with(mContext)
                        .load(imageFile)
                        .placeholder(R.drawable.camerasdk_pic_loading)
                        .error(R.drawable.camerasdk_pic_loading)
                        .override(mItemSize, mItemSize)
                        .centerCrop()
                        .into(ivPicture);
            }
        }
    }

    public interface SelectedListener{
        void onSelected(List<ImageInfo> selectedList);
    }
}
