package com.yyx.beautifylib.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yyx.beautifylib.R;
import com.yyx.beautifylib.model.BLPreviewParam;
import com.yyx.beautifylib.tag.utils.ImageLoader;
import com.yyx.beautifylib.ui.activity.base.BLToolBarActivity;
import com.yyx.beautifylib.utils.BLConfigManager;
import com.yyx.beautifylib.utils.BLSelectedStateListDrawable;
import com.yyx.beautifylib.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2017/4/15.
 * 图片预览
 */

public class BLPreviewActivity extends BLToolBarActivity {
//    private CheckBox mCbMark;
    private ImageView mIvCheck;
    private CustomViewPager mViewPager;
    private BLImagePagerAdapter mPagerAdapter;

    private int SELECTED_MAX;
    private int mCurrentPos;
    private List<String> mImages = new ArrayList<>();
    private List<String> mSelectedImages = new ArrayList<>();

    private BLPreviewParam mPreview;

    private MenuItem mMenuItem;

    @Override
    protected int getContentLayoutId() {
        return R.layout.bl_activity_preview;
    }

    @Override
    protected void customToolBarStyle() {
        mPreview = getIntent().getParcelableExtra(BLPreviewParam.KEY);
        if (mPreview == null){
            throw new IllegalStateException("参数对象BLPreview不能为null");
        }
        if (mPreview.getMode() == BLPreviewParam.MODE_SELECTED){
            mToolbar.inflateMenu(R.menu.menu_preview);
            mMenuItem = mToolbar.getMenu().findItem(R.id.preview_menu);
            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.preview_menu){
                        if (mPreview.getSelectedImages().size() == 0){
                            toast("请选择图片");
                        }else{
                            Intent intent = new Intent();
                            intent.putExtra("is_click_complete", true);
                            intent.putExtra(BLPreviewParam.KEY, mPreview);
                            setResult(RESULT_OK, intent);
                            onBackPressed();
                        }
                    }
                    return false;
                }
            });

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra(BLPreviewParam.KEY, mPreview);
                    setResult(RESULT_OK, intent);
                    onBackPressed();
                }
            });
        }

    }

    @Override
    protected void initView() {
        mIvCheck = getViewById(R.id.preview_check_mark);
        mIvCheck.setImageDrawable(new BLSelectedStateListDrawable(mInstance.getResources().getDrawable(R.drawable.bl_check_normal), BLConfigManager.getPrimaryColor()));
        mViewPager = getViewById(R.id.preview_viewpager);
        if (mPreview.getMode() == BLPreviewParam.MODE_SELECTED){
            mIvCheck.setVisibility(View.VISIBLE);
        }else{
            mIvCheck.setVisibility(View.GONE);
        }
    }

    @Override
    protected void otherLogic() {
        setToolbarTitle(mPreview.getPosition());
        mPagerAdapter = new BLImagePagerAdapter(mInstance, mPreview.getImages());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mPreview.getPosition());

        if (mPreview.getMode() == BLPreviewParam.MODE_SELECTED){
            if (mPreview.getSelectedImages().contains(mPreview.getImages().get(mPreview.getPosition()))){
//                mCbMark.setChecked(true);
                mIvCheck.setSelected(true);
            }else{
//                mCbMark.setChecked(false);
                mIvCheck.setSelected(false);
            }
            setSelectedNum();
        }

    }

    private void setToolbarTitle(int currentNum){
        mToolbar.setTitle("图片预览(" + (currentNum + 1) + "/" + mPreview.getImages().size() + ")");
    }

    private void setSelectedNum(){
        mMenuItem.setTitle("完成(" + mPreview.getSelectedImages().size() + "/" + mPreview.getSelectMax() + ")");
    }

    @Override
    protected void setListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPreview.setPosition(position);
                setToolbarTitle(position);

                if (mPreview.getSelectedImages().contains(mPreview.getImages().get(position))){
//                    mCbMark.setChecked(true);
                    mIvCheck.setSelected(true);
                }else{
//                    mCbMark.setChecked(false);
                    mIvCheck.setSelected(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mIvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPreview.getSelectedImages().size() == mPreview.getSelectMax()){
                    toast(getString(R.string.camerasdk_msg_amount_limit));
//                    mCbMark.setChecked(false);
                    mIvCheck.setSelected(false);
                    return;
                }
                List<String> selectedImages = mPreview.getSelectedImages();
                List<String> images = mPreview.getImages();
                int position = mPreview.getPosition();
                if (selectedImages.contains(images.get(position))){
//                    mCbMark.setChecked(false);
                    mIvCheck.setSelected(false);
                    selectedImages.remove(images.get(position));
                }else{
//                    mCbMark.setChecked(true);
                    mIvCheck.setSelected(true);
                    selectedImages.add(images.get(position));
                }

                setSelectedNum();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent.putExtra(BLPreviewParam.KEY, mPreview);
            setResult(RESULT_OK, intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    class BLImagePagerAdapter extends PagerAdapter {

        private Context mContext;
        private List<String> imageList;
        private List<View> viewList = new ArrayList<View>();// 将要分页显示的View装入数组中

        public BLImagePagerAdapter(Context context,List<String> images) {
            mContext=context;
            imageList=images;

            LayoutInflater lf = LayoutInflater.from(mContext);
            for(int i=0;i<images.size();i++){
                viewList.add(lf.inflate(R.layout.camerasdk_list_item_preview_image, null));
            }


        }

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {

            View view = viewList.get(position);
            try {
                if (view.getParent() == null) {
                    ((ViewPager)container).addView(view, 0);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            PhotoView photoView=(PhotoView)view.findViewById(R.id.image);

            String path=imageList.get(position);

            ImageLoader.loadImage(path, photoView);

            return view;
        }

        @Override
        public int getItemPosition(Object object) {

            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //container.removeView((View) object);
            //container.removeView(viewList.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
