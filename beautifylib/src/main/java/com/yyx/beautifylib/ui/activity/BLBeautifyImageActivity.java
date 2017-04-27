package com.yyx.beautifylib.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muzhi.camerasdk.library.views.HorizontalListView;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yyx.beautifylib.R;
import com.yyx.beautifylib.adapter.Filter_Effect_Adapter;
import com.yyx.beautifylib.adapter.Filter_Sticker_Adapter;
import com.yyx.beautifylib.adapter.FragmentViewPagerAdapter;
import com.yyx.beautifylib.model.BLBeautifyParam;
import com.yyx.beautifylib.model.BLEnhanceParam;
import com.yyx.beautifylib.model.BLResultParam;
import com.yyx.beautifylib.model.BLScrawlParam;
import com.yyx.beautifylib.model.BLStickerInfo;
import com.yyx.beautifylib.model.Filter_Effect_Info;
import com.yyx.beautifylib.tag.DIRECTION;
import com.yyx.beautifylib.tag.TagViewGroup;
import com.yyx.beautifylib.tag.model.TagGroupModel;
import com.yyx.beautifylib.tag.views.ITagView;
import com.yyx.beautifylib.tag.views.TagEditDialog;
import com.yyx.beautifylib.ui.activity.base.BLToolBarActivity;
import com.yyx.beautifylib.ui.fragment.BLBeautifyFragment;
import com.yyx.beautifylib.utils.BLConfigManager;
import com.yyx.beautifylib.utils.BLStickerUtils;
import com.yyx.beautifylib.utils.FilterUtils;
import com.yyx.beautifylib.view.CustomViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yyx.beautifylib.R.id.effect_listview;
import static com.yyx.beautifylib.R.id.txt_graffiti;

/**
 * Created by Administrator on 2017/4/15.
 * 美化图片
 */

public class BLBeautifyImageActivity extends BLToolBarActivity implements View.OnClickListener {
    private static int TXT_NORMAL_COLOR = Color.BLACK;
    private static int TXT_SELECTED_COLOR;


    private enum FUN_TYPE {FILTER, STICKER, TAG, CROP, SCRAWL, EDIT}

    private FUN_TYPE mFunType = FUN_TYPE.FILTER;
    private CustomViewPager mViewPager;
    private RelativeLayout mRlContainer;
    private HorizontalListView mHlvFilter, mHlvSticker;
    private Button mBtnCreateTag;
    private TextView mTvFilter, mTvSticker, mTvTag, mTvCrop, mTvScrawl, mTvEdit, mTvMosaic;

    private Filter_Effect_Adapter mFilterAdapter;
    private Filter_Sticker_Adapter mStickerAdapter;
    private List<Filter_Effect_Info> mFilterData = new ArrayList<>(); //特效
    private List<BLStickerInfo> mStickerData = new ArrayList<>();
    private List<String> imageList;

    private AlertDialog mTextStickerDialog;
    private EditText mEtStickerText;
    private Button mBtnDialogConfirm;

    private FragmentViewPagerAdapter fAdapter;
    private List<Fragment> fragments;
    private int curPosition;

    private BLBeautifyParam mParam;

    private TagEditDialog mDialog;
    private TagViewGroup.OnTagGroupClickListener mTagGroupClickListener;

    private MergeImageTask mMergeTask;
//    private List<String> mMergeList = new ArrayList<>();

    @Override
    protected int getContentLayoutId() {
        return R.layout.bl_activity_beautify_image;
    }

    @Override
    protected void customToolBarStyle() {
        mToolbar.inflateMenu(R.menu.menu_preview);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.preview_menu) {
                    if (mMergeTask == null) {
                        //合成图片前，锁定所有的Sticker,隐藏边框和图标
                        for (Fragment f : fragments) {
                            if (f instanceof BLBeautifyFragment) {
                                BLBeautifyFragment fragment = (BLBeautifyFragment) f;
                                fragment.stickerLocked(true);
                            }
                        }

                        mMergeTask = new MergeImageTask();
                        /**
                         * 注：AsyncTask只能执行一次，如果mMergeTask不为null,
                         * 则说明已经执行过了
                         */
                        mMergeTask.execute(fragments);
                    } else {
                        toast("图片已保存");
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void initView() {
        mViewPager = getViewById(R.id.beautify_image_viewpager);
        mRlContainer = getViewById(R.id.content_container);
        mHlvFilter = getViewById(effect_listview);
        mHlvSticker = getViewById(R.id.sticker_listview);
        mBtnCreateTag = getViewById(R.id.add_tag_btn);
        mTvFilter = getViewById(R.id.txt_effect);
        mTvSticker = getViewById(R.id.txt_sticker);
        mTvTag = getViewById(R.id.txt_tag);
        mTvCrop = getViewById(R.id.txt_cropper);
        mTvScrawl = getViewById(txt_graffiti);
        mTvEdit = getViewById(R.id.txt_enhance);
        mTvMosaic = getViewById(R.id.txt_mosaic);
    }

    @Override
    protected void otherLogic() {
        TXT_SELECTED_COLOR = BLConfigManager.getPrimaryColor();
        mParam = getIntent().getParcelableExtra(BLBeautifyParam.KEY);
        imageList = mParam.getImages();
        fragments = new ArrayList<>();
        for (String path : imageList) {
            fragments.add(new BLBeautifyFragment().newInstance(path));
        }
        fAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mViewPager, fragments);
        mViewPager.setAdapter(fAdapter);
        mViewPager.setCurrentItem(curPosition);
        mViewPager.setOffscreenPageLimit(imageList.size());

        mFilterData = FilterUtils.getEffectList();
        mStickerData = BLStickerUtils.createStickerInfoList();
        setToolbarTitle(curPosition);
        //默认滤镜被选中
        onFilterClick();
    }

    /**
     * 设置标题
     *
     * @param position
     */
    private void setToolbarTitle(int position) {
        mToolbar.setTitle("图片美化(" + (position + 1) + "/" + imageList.size() + ")");
    }

    private BLBeautifyFragment getCurrentFragment() {
        Fragment f = fragments.get(curPosition);
        if (f instanceof BLBeautifyFragment) {
            return (BLBeautifyFragment) f;
        } else {
            return null;
        }
    }


    @Override
    protected void setListener() {
        mBtnCreateTag.setOnClickListener(this);
        mTvEdit.setOnClickListener(this);
        mTvSticker.setOnClickListener(this);
        mTvFilter.setOnClickListener(this);
        mTvScrawl.setOnClickListener(this);
        mTvCrop.setOnClickListener(this);
        mTvTag.setOnClickListener(this);
        mTvMosaic.setOnClickListener(this);

        mHlvFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                mFilterAdapter.setSelectItem(position);
                final int itemWidth = view.getWidth();
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHlvFilter.scrollTo(itemWidth * (position - 1) - itemWidth / 4);
                    }
                }, 100);

                getCurrentFragment().addFilter(mFilterData.get(position).getFilterType());
            }
        });

        mHlvSticker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final int itemWidth = view.getWidth();
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHlvSticker.scrollTo(itemWidth * (position - 1) - itemWidth / 4);
                    }
                }, 100);

                if (position == 0) {
                    showTextStickerDialog();
                } else {
                    getCurrentFragment().addSticker(mStickerData.get(position));
                }

            }
        });

        fAdapter.setOnExtraPageChangeListener(new FragmentViewPagerAdapter.OnExtraPageChangeListener() {
            @Override
            public void onExtraPageSelected(int i) {
                curPosition = i;
                setToolbarTitle(curPosition);
            }
        });

        mTagGroupClickListener = new TagViewGroup.OnTagGroupClickListener() {
            @Override
            public void onCircleClick(TagViewGroup group) {
//                toast("点击中心圆");
                Fragment f = fragments.get(curPosition);
                if (f instanceof BLBeautifyFragment) {
                    TagGroupModel model = ((BLBeautifyFragment) f).getTagGroupModel(group);
                    List<TagGroupModel.Tag> tags = model.getTags();
                    List<ITagView> tagViews = group.getTagList();
                    for (int i = 0; i < tagViews.size(); i++) {
                        ITagView tagView = tagViews.get(i);
                        TagGroupModel.Tag tag = tags.get(i);
                        tag.setDirection((tag.getDirection() + 1) % 11 == 0 ? 1 : tag.getDirection() + 1);
                        int direction = tag.getDirection();
                        tagView.setDirection(DIRECTION.valueOf(direction));
                    }
                }

                group.invalidate();
                group.requestLayout();
            }

            @Override
            public void onTagClick(TagViewGroup group, ITagView tag, int index) {
//                tag.setDirection(DIRECTION.valueOf((num++ % 10 + 1)));
//                Fragment f = fragments.get(curPosition);
//                if (f instanceof BLBeautifyFragment){
//                    TagGroupModel model = ((BLBeautifyFragment) f).getTagGroupModel(group);
//                    model.getTags().get(index).setDirection(tag.getDirection().getValue());
//                }
//                group.invalidate();
//                group.requestLayout();
            }

            @Override
            public void onScroll(TagViewGroup group, float percentX, float percentY) {
                Fragment f = fragments.get(curPosition);
                if (f instanceof BLBeautifyFragment) {
                    TagGroupModel model = ((BLBeautifyFragment) f).getTagGroupModel(group);
                    model.setPercentX(percentX);
                    model.setPercentY(percentY);
                }

            }

            @Override
            public void onLongPress(final TagViewGroup group) {
                new AlertDialog.Builder(BLBeautifyImageActivity.this)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getCurrentFragment().removeTagGroup(group);
                                dialog.dismiss();
                            }
                        }).setTitle("删除标签组").setMessage("你确定要删除该标签组吗？")
                        .create().show();
            }
        };
    }

    /**
     * 文字贴图对话框
     */
    private void showTextStickerDialog() {
        if (mTextStickerDialog == null) {
            mTextStickerDialog = new AlertDialog.Builder(mInstance).create();
            View view = mInflater.inflate(R.layout.bl_dialog_sticker_text, null);
            mTextStickerDialog.setView(view);
            mEtStickerText = getViewById(R.id.dialog_sticker_text, view);
            mBtnDialogConfirm = getViewById(R.id.dialog_sticker_text_confirm_btn, view);
            mTextStickerDialog.setCanceledOnTouchOutside(true);
            mBtnDialogConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mEtStickerText.getText().toString().equals("")){
                        toast("请输入文字");
                        return;
                    }
                    mTextStickerDialog.dismiss();
                    getCurrentFragment().addTextSticker(new BLStickerInfo(mEtStickerText.getText().toString(), Color.WHITE));
                }
            });
        }

        if (!mTextStickerDialog.isShowing()){
            mEtStickerText.setText("");
            mTextStickerDialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.txt_effect) {
            onFilterClick();
        } else if (resId == R.id.txt_sticker) {
            onStickerClick();
        } else if (resId == R.id.txt_tag) {
            onTagClick();
        } else if (resId == R.id.txt_cropper) {
            onCropClick();
        } else if (resId == R.id.txt_graffiti) {
            onScrawlClick();
        } else if (resId == R.id.txt_enhance) {
            onEditClick();
        } else if (resId == R.id.add_tag_btn) {
            onCreateTagBtnClick();
        }else if (resId == R.id.txt_mosaic){
            onMosaicClick();
        }
    }

    private void onFilterClick() {
        setSelectedTxt(FUN_TYPE.FILTER);
        mHlvFilter.setVisibility(View.VISIBLE);
        mHlvSticker.setVisibility(View.GONE);
        mBtnCreateTag.setVisibility(View.GONE);
        if (mFilterAdapter == null) {
            mFilterAdapter = new Filter_Effect_Adapter(mInstance, mFilterData);
            mHlvFilter.setAdapter(mFilterAdapter);
        }
    }

    private void onStickerClick() {
        setSelectedTxt(FUN_TYPE.STICKER);
        mHlvFilter.setVisibility(View.GONE);
        mHlvSticker.setVisibility(View.VISIBLE);
        mBtnCreateTag.setVisibility(View.GONE);
        if (mStickerAdapter == null) {
            mStickerAdapter = new Filter_Sticker_Adapter(mInstance, mStickerData);
            mHlvSticker.setAdapter(mStickerAdapter);
        }
    }

    private void onTagClick() {
        setSelectedTxt(FUN_TYPE.TAG);
        mHlvFilter.setVisibility(View.GONE);
        mHlvSticker.setVisibility(View.GONE);
        mBtnCreateTag.setVisibility(View.VISIBLE);
    }

    private void onCropClick() {
        gotoUCropActivity();
    }

    private void gotoUCropActivity() {
        UCrop uCrop = UCrop.of(Uri.fromFile(new File(getCurrentFragment().getPath())), Uri.fromFile(new File(getCacheDir(), getPackageName())));

        uCrop.useSourceImageAspectRatio();
//        uCrop.withAspectRatio(3,2);
        UCrop.Options options = new UCrop.Options();
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.SCALE);
////        设置裁剪框自由移动
//        options.setFreeStyleCropEnabled(true);
//        //设置裁剪比例
//        options.setAspectRatioOptions(1, new AspectRatio(null, 5, 4), new AspectRatio(null, 16,9),new AspectRatio(getString(R.string.ucrop_label_original).toUpperCase(),
//                CropImageView.SOURCE_IMAGE_ASPECT_RATIO, CropImageView.SOURCE_IMAGE_ASPECT_RATIO), new AspectRatio(null, 3, 4), new AspectRatio(null, 6, 7));

        options.setStatusBarColor(BLConfigManager.getStatusBarColor());
        options.setToolbarColor(BLConfigManager.getToolBarColor());
        options.setActiveWidgetColor(BLConfigManager.getPrimaryColor());
        uCrop.withOptions(options);
        uCrop.start(mInstance);
    }

    private void onScrawlClick() {
        BLScrawlParam.bitmap = getCurrentFragment().getBitmap();
//        Intent intent = new Intent(mInstance, BLScrawlActivity.class);
//        intent.putExtra(BLScrawlParam.KEY, new BLScrawlParam());
//        ActivityUtils.startActivityForResult(mInstance, intent, BLScrawlParam.REQUEST_CODE_SCRAWL);
        BLScrawlParam.startActivity(mInstance, new BLScrawlParam());
    }

    private void onEditClick() {
//        setSelectedTxt(FUN_TYPE.EDIT);
        BLEnhanceParam.bitmap = getCurrentFragment().getBitmap();
        BLEnhanceParam.startActivity(mInstance, new BLEnhanceParam());
    }

    private void onCreateTagBtnClick() {
        if (mDialog == null) {
            mDialog = new TagEditDialog(this, new TagEditDialog.OnTagEditDialogClickListener() {
                @Override
                public void onCancel() {
                    mDialog.dismiss();
                }

                @Override
                public void onTagGroupCreated(TagGroupModel group) {
//                    mModelList.add(group);
                    getCurrentFragment().addTagGroup(group, mTagGroupClickListener);
                    mDialog.dismiss();
                }
            });
        }
        mDialog.show();
    }

    private void onMosaicClick() {
        //TODO
        toast("敬请期待...");
    }

    private void setSelectedTxt(FUN_TYPE type) {
        mTvFilter.setTextColor(TXT_NORMAL_COLOR);
        mTvSticker.setTextColor(TXT_NORMAL_COLOR);
        mTvCrop.setTextColor(TXT_NORMAL_COLOR);
        mTvTag.setTextColor(TXT_NORMAL_COLOR);
        mTvScrawl.setTextColor(TXT_NORMAL_COLOR);
        mTvEdit.setTextColor(TXT_NORMAL_COLOR);

        BLBeautifyFragment fragment = (BLBeautifyFragment) fragments.get(curPosition);
        switch (type) {
            case FILTER:
                mTvFilter.setTextColor(TXT_SELECTED_COLOR);
                fragment.stickerLocked(true);
                break;
            case STICKER:
                mTvSticker.setTextColor(TXT_SELECTED_COLOR);
                fragment.stickerLocked(false);
                break;
            case TAG:
                mTvTag.setTextColor(TXT_SELECTED_COLOR);
                fragment.stickerLocked(true);
                break;
            case CROP:
                mTvCrop.setTextColor(TXT_SELECTED_COLOR);
                fragment.stickerLocked(true);
                break;
            case SCRAWL:
                mTvScrawl.setTextColor(TXT_SELECTED_COLOR);
                fragment.stickerLocked(true);
                break;
            case EDIT:
                mTvEdit.setTextColor(TXT_SELECTED_COLOR);
                fragment.stickerLocked(true);
                break;
        }
        mFunType = type;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == UCrop.REQUEST_CROP) {
            Uri uri = UCrop.getOutput(data);
            getCurrentFragment().setPath(uri.getPath());
        } else if (requestCode == BLEnhanceParam.REQUEST_CODE_ENHANCE) {
            getCurrentFragment().setBitmap(BLEnhanceParam.bitmap);
        } else if (requestCode == BLScrawlParam.REQUEST_CODE_SCRAWL) {
            getCurrentFragment().setBitmap(BLScrawlParam.bitmap);
        }
    }

    @Override
    protected void onDestroy() {
        BLEnhanceParam.recycleBitmap();
        BLScrawlParam.recycleBitmap();
        super.onDestroy();
    }

    class MergeImageTask extends AsyncTask<List<Fragment>, Void, BLResultParam> {

        public MergeImageTask() {

        }

        @Override
        protected BLResultParam doInBackground(List<Fragment>... params) {
            BLResultParam resultParam = new BLResultParam();
            List<String> mergeList = new ArrayList<>();
            Map<String, BLResultParam.TagGroupModelParam> tagGroupModelMap = new HashMap<>();

            List<Fragment> fragments = params[0];
            for (Fragment f : fragments) {
                if (f instanceof BLBeautifyFragment) {
                    BLBeautifyFragment fragment = (BLBeautifyFragment) f;
                    fragment.complete();
                    String path = fragment.complete();

                    if (path != null && !path.equals("")) {
                        mergeList.add(path);
                        if (fragment.getTagGroupModelList().size() != 0) {
                            tagGroupModelMap.put(path, new BLResultParam.TagGroupModelParam(fragment.getTagGroupModelList()));
                        }
                    }
                }
            }
            resultParam.setImageList(mergeList);
            resultParam.setTagGroupModelMap(tagGroupModelMap);
            return resultParam;
        }

        @Override
        protected void onPostExecute(BLResultParam param) {
            Intent intent = new Intent();
            intent.putExtra(BLResultParam.KEY, param);
            setResult(RESULT_OK, intent);
            onBackPressed();
        }
    }

}
