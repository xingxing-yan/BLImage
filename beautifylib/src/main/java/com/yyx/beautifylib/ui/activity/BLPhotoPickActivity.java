package com.yyx.beautifylib.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yyx.beautifylib.R;
import com.yyx.beautifylib.adapter.BLImageGridAdapter;
import com.yyx.beautifylib.adapter.FolderAdapter;
import com.yyx.beautifylib.model.BLBeautifyParam;
import com.yyx.beautifylib.model.BLPreviewParam;
import com.yyx.beautifylib.model.FolderInfo;
import com.yyx.beautifylib.model.ImageInfo;
import com.yyx.beautifylib.ui.activity.base.BLToolBarActivity;
import com.yyx.beautifylib.utils.BLConfigManager;
import com.yyx.beautifylib.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.yyx.beautifylib.model.BLPreviewParam.REQUEST_CODE_PREVIEW;

/**
 * Created by Administrator on 2017/4/13.
 * 图片选择
 */

public class BLPhotoPickActivity extends BLToolBarActivity implements EasyPermissions.PermissionCallbacks {

    public static final  int TAKE_PICTURE_FROM_CAMERA = 0x1002;

    private TextView mTvFolderName, mTvComplete;
    private ImageView mIvArrow;

    private GridView mGridView;
    private ListView mFolderListView;
    private PopupWindow mPopupWindow;
    private BLImageGridAdapter mImageAdapter;
    private FolderAdapter mFolderAdapter;
    private boolean hasFolderGened = false;
    private File mTmpFile;  //照相后的临时文件

    private ArrayList<FolderInfo> mResultFolder = new ArrayList<FolderInfo>();// 文件夹数据
    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;


    public static final int REQUEST_CODE_CAMERA = 1;


    private View.OnClickListener mShowImageFolderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showPopupFolder(v);
        }
    };
    private View.OnClickListener mCompleterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectComplete();
        }
    };
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.SIZE};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(mInstance,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                CursorLoader cursorLoader = new CursorLoader(mInstance,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                List<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        int size = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                        boolean show_flag = size > 1024 * 10; //是否大于10K
                        ImageInfo imageInfo = new ImageInfo(path, name, dateTime);
                        if (show_flag) {
                            imageInfos.add(imageInfo);
                        }

                        if (!hasFolderGened && show_flag) {
                            // 获取文件夹名称
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            FolderInfo folderInfo = new FolderInfo();
                            folderInfo.name = folderFile.getName();
                            folderInfo.path = folderFile.getAbsolutePath();
                            folderInfo.cover = imageInfo;
                            if (!mResultFolder.contains(folderInfo)) {
                                List<ImageInfo> imageList = new ArrayList<ImageInfo>();
                                imageList.add(imageInfo);
                                folderInfo.imageInfos = imageList;
                                mResultFolder.add(folderInfo);
                            } else {
                                // 更新
                                FolderInfo f = mResultFolder.get(mResultFolder.indexOf(folderInfo));
                                if (!f.imageInfos.contains(imageInfo)){
                                    f.imageInfos.add(imageInfo);
                                }
                            }
                        }

                    } while (data.moveToNext());

                    mImageAdapter.setData(imageInfos);
                    //将照相返回的图片默认选中
                    if (mTmpFile != null){
                        mImageAdapter.select(mImageAdapter.getImageByPath(mTmpFile.getPath()));
                        setSelectedNum(mImageAdapter.getSelectedImageList().size());
                        mTmpFile = null;
                    }
                    if (mFolderAdapter != null){
                        mFolderAdapter.setData(mResultFolder);
                    }
                    hasFolderGened = true;

                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Override
    protected int getContentLayoutId() {
        return R.layout.bl_activity_photo_pick;
    }

    @Override
    protected void customToolBarStyle() {
        mToolbar.inflateMenu(R.menu.menu_photo_pick);
        MenuItem item = mToolbar.getMenu().findItem(R.id.photo_pick_menu);
        View actionView = item.getActionView();
        actionView.setBackgroundColor(BLConfigManager.getToolBarColor());
        mTvFolderName = getViewById(R.id.photo_picker_menu_item_title, actionView);
        mTvComplete = getViewById(R.id.photo_picker_menu_item_complete, actionView);
        mIvArrow = getViewById(R.id.photo_picker_menu_item_arrow, actionView);
        if (mShowImageFolderListener != null) {
            mTvFolderName.setOnClickListener(mShowImageFolderListener);
            mIvArrow.setOnClickListener(mShowImageFolderListener);
        }
        if (mCompleterListener != null) {
            mTvComplete.setOnClickListener(mCompleterListener);
        }

    }

    @Override
    protected void initView() {
        mGridView = getViewById(R.id.photo_pick_gv);
    }

    @Override
    protected void otherLogic() {
        mImageAdapter = new BLImageGridAdapter(mInstance, true);
        mGridView.setAdapter(mImageAdapter);
        mFolderAdapter = new FolderAdapter(mInstance);
        setSelectedNum(mImageAdapter.getSelectedImageList().size());

        getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
    }

    @Override
    protected void setListener() {

        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {
                if (state == SCROLL_STATE_IDLE || state == SCROLL_STATE_TOUCH_SCROLL) {
                    //停止滚动时加载图片
                    Glide.with(mInstance).resumeRequests();
                } else {
                    //滚动时暂停加载图片
                    Glide.with(mInstance).pauseRequests();
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout() {

                final int width = mGridView.getWidth();
                final int height = mGridView.getHeight();
                // mGridWidth = width;
                // mGridHeight = height;
                final int desireSize = getResources().getDimensionPixelOffset(R.dimen.image_size);
                final int numCount = width / desireSize;
                final int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
                int columnWidth = (width - columnSpace * (numCount - 1)) / numCount;
                mImageAdapter.setItemSize(columnWidth);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mImageAdapter.isShowCamera() && position == 0) {
                    if (mImageAdapter.isSelectedMax()) {
                        toast(getString(R.string.camerasdk_msg_amount_limit));
                    } else {
                        showCameraAction();
                    }
                } else {
                    BLPreviewParam preview = new BLPreviewParam(BLPreviewParam.MODE_SELECTED, mImageAdapter.MAX_SELECT_SIZE, mImageAdapter.isShowCamera() ? position - 1 : position, mImageAdapter.getImagePathList(), mImageAdapter.getSelectedImagePathList());
//                    Intent intent = new Intent(mInstance, BLPreviewActivity.class);
//                    intent.putExtra(BLPreviewParam.KEY, preview);
//                    ActivityUtils.startActivityForResult(mInstance, intent, REQUEST_CODE_PREVIEW);
                    BLPreviewParam.startActivity(mInstance, preview);
                }
            }
        });

        mImageAdapter.setSelectedListener(new BLImageGridAdapter.SelectedListener() {
            @Override
            public void onSelected(List<ImageInfo> selectedList) {
                setSelectedNum(selectedList.size());
            }
        });
    }

    /**
     * 设置选择的数量
     * @param selectedNum
     */
    private void setSelectedNum(int selectedNum){
        mTvComplete.setText("完成(" + selectedNum + "/"+ mImageAdapter.MAX_SELECT_SIZE +")");
    }

    /**
     * 选择相机
     */
    @AfterPermissionGranted(REQUEST_CODE_CAMERA)
    private void showCameraAction() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 跳转到系统照相机
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(mInstance.getPackageManager()) != null) {
                // 设置系统相机拍照后的输出路径
                // 创建临时文件
                mTmpFile = FileUtils.createTmpFile(mInstance);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
                startActivityForResult(cameraIntent, TAKE_PICTURE_FROM_CAMERA);
            } else {
                Toast.makeText(mInstance, R.string.camerasdk_msg_no_camera, Toast.LENGTH_SHORT).show();
            }
        } else {
            EasyPermissions.requestPermissions(this, "照相机需要以下权限:\n\n1.照相", REQUEST_CODE_CAMERA, perms);
        }

    }

    /**
     * 选择图片文件夹对话框
     * @param v
     */
    private void showPopupFolder(View v) {
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(mInstance);
            mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));

            View view = getLayoutInflater().inflate(R.layout.camerasdk_popup_folder, null);
            LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.camerasdk_push_up_in));
            mPopupWindow.setContentView(view);
            mFolderListView = (ListView) view.findViewById(R.id.lsv_folder);
            mFolderListView.setAdapter(mFolderAdapter);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                }
            });

            mFolderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    mFolderAdapter.setSelectIndex(arg2);
                    final int index = arg2;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPopupWindow.dismiss();
                            if (index == 0) {
                                getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                                mTvFolderName.setText(getString(R.string.camerasdk_album_all));
                                mImageAdapter.setShowCamera(true);
                            } else {
                                FolderInfo folderInfo = mFolderAdapter.getItem(index);
                                if (null != folderInfo) {
                                    mImageAdapter.setData(folderInfo.imageInfos);
                                    mTvFolderName.setText(folderInfo.name);
                                }
                            }
                            // 滑动到最初始位置
                            mGridView.smoothScrollToPosition(0);

                        }
                    }, 100);
                }
            });
        }

        mPopupWindow.showAsDropDown(mToolbar);
    }

    //选择完成实现跳转
    private void selectComplete() {
        if (mImageAdapter.getSelectedImageList().size() == 0){
            toast("请选择图片");
            return;
        }
//        Intent intent = new Intent(mInstance, BLBeautifyImageActivity.class);
//
//        intent.putExtra(BLBeautifyParam.KEY, param);
//        ActivityUtils.startActivityForResult(mInstance, intent, BLBeautifyParam.REQUEST_CODE_BEAUTIFY_IMAGE);

        BLBeautifyParam param = new BLBeautifyParam(mImageAdapter.getSelectedImagePathList());
        BLBeautifyParam.startActivity(mInstance, param);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 相机拍照完成后，返回图片路径
        if (requestCode == TAKE_PICTURE_FROM_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {

                    //加入content provider
                    ContentValues values = new ContentValues(7);
                    values.put(MediaStore.Images.Media.TITLE, System.currentTimeMillis());
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, "");
                    values.put(MediaStore.Images.Media.DATE_TAKEN, "");
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.Images.Media.ORIENTATION, 0);
                    values.put(MediaStore.Images.Media.DATA, mTmpFile.getPath());
                    values.put(MediaStore.Images.Media.SIZE, mTmpFile.length());
                    getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    //拍照完成后，默认切回所有图片文件夹
                    mFolderAdapter.setSelectIndex(0);
                    mTvFolderName.setText(getString(R.string.camerasdk_album_all));

                }
            } else {
                if (mTmpFile != null && mTmpFile.exists()) {
                    mTmpFile.delete();
                }
            }
        }else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PREVIEW){
            boolean isClickComplete = data.getBooleanExtra("is_click_complete", false);
            BLPreviewParam preview = data.getParcelableExtra(BLPreviewParam.KEY);
            mImageAdapter.mergeSelectedList(preview.getSelectedImages());
            setSelectedNum(mImageAdapter.getSelectedImageList().size());
            if (isClickComplete){
                selectComplete();
            }
        }else if (resultCode == RESULT_OK && requestCode == BLBeautifyParam.REQUEST_CODE_BEAUTIFY_IMAGE){

            setResult(RESULT_OK, data);
            onBackPressed();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            toast("您拒绝了照相的权限");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
