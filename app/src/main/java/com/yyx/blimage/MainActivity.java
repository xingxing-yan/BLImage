package com.yyx.blimage;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yyx.beautifylib.model.BLPickerParam;
import com.yyx.beautifylib.model.BLResultParam;
import com.yyx.beautifylib.tag.views.TagImageView;
import com.yyx.beautifylib.view.BLBeautifyImageView;

import java.util.List;
import java.util.Map;

import cn.bingoogolapple.bgabanner.BGABanner;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private Button mBtnSelectImg;
    private BGABanner mBanner;

    private BLResultParam mResultParam;

    public static final int REQUEST_CODE_PERMISSION = 0;
//    public static final int REQUEST_CODE_PHOTO_PICKER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnSelectImg = (Button) findViewById(R.id.main_btn);
        mBanner = (BGABanner) findViewById(R.id.main_view_pager);

        mBtnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPhotoPickActivity();
            }
        });

        mBanner.setAdapter(new BGABanner.Adapter<View, String>() {

            @Override
            public void fillBannerItem(BGABanner banner, View itemView, String model, int position) {
                BLBeautifyImageView imageView = (BLBeautifyImageView) itemView.findViewById(R.id.result_vp_image);
                Map<String, BLResultParam.TagGroupModelParam> tagGroupModelMap = mResultParam.getTagGroupModelMap();
                imageView.setImage(model);
                if (tagGroupModelMap.containsKey(model)){
                    imageView.setTagModelList(tagGroupModelMap.get(model).getTagGroupModelList());
                }
                final TagImageView tagImageView = imageView.getTagImageView();
                tagImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tagImageView.excuteTagsAnimation();
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == BLPickerParam.REQUEST_CODE_PHOTO_PICKER) {
            mResultParam = data.getParcelableExtra(BLResultParam.KEY);
            mBanner.setData(R.layout.item_result_view_pager, mResultParam.getImageList(), null);
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION)
    private void gotoPhotoPickActivity() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
//            Intent intent = new Intent(MainActivity.this, BLPhotoPickActivity.class);
//            startActivityForResult(intent, REQUEST_CODE_PHOTO_PICKER);

            BLPickerParam.startActivity(MainActivity.this);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问读写权限", REQUEST_CODE_PERMISSION, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            Toast.makeText(this, "您拒绝了读取图片的权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


}
