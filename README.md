# BLImage
Android中美化图片的库。功能包括滤镜，贴纸，标签，裁剪，涂鸦，亮度，饱和度，对比度，马赛克等功能

# 效果图
![1](https://github.com/xingxing-yan/BLImage/blob/master/gif/bl1.gif)![2](https://github.com/xingxing-yan/BLImage/blob/master/gif/bl2.gif)![3](https://github.com/xingxing-yan/BLImage/blob/master/gif/bl3.gif)

# 添加依赖：


# 功能介绍：
1. 添加滤镜
2. 添加贴纸(水印)，支持文字添加和图片添加两种方式
3. 添加标签，类似小红书的标签，为图片打一些标记
4. 图片裁剪，支持按一定比例裁剪
5. 涂鸦，画笔，颜色，大小的调整。目前只支持两种画笔，以后会添加更多
6. 编辑：图片亮度，饱和度，对比度的设置
7. 马赛克：TODO

# 使用说明：
1. 入口：在MainActivity中启动BLPhotoPickActivity，进行一系列图片处理后，拿到处理后的图片路径，然后做自己项目中的处理，代码如下：
```
//跳转图片选择页面
@AfterPermissionGranted(REQUEST_CODE_PERMISSION)
    private void gotoPhotoPickActivity() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            BLPickerParam.startActivity(MainActivity.this);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问读写权限", REQUEST_CODE_PERMISSION, perms);
        }
    }
    
 //获取返回结果数据
 @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == BLPickerParam.REQUEST_CODE_PHOTO_PICKER) {
            BLResultParam param = data.getParcelableExtra(BLResultParam.KEY);
            List<String> imageList = param.getImageList();
            StringBuilder sb = new StringBuilder();
            for (String path:imageList){
                sb.append(path);
                sb.append("\n");
            }
            ToastUtils.toast(this, sb.toString());
        }
    }
```
  读取图片需要读写权限，所以注意动态权限的添加
  
2. 更改库中页面的颜色：支持以配置的方式修改库中页面的statusbar, toolbar以及应用的主体颜色，以更好的和项目中的颜色搭配, 在Application中配置如下：
```
BLConfigManager.register(new BLConfig())
                .statusBarColor(Color.parseColor("#D50A6E"))    //设置状态栏颜色
                .toolBarColor(Color.parseColor("#d4237a"))  //设置toolbar颜色
                .primaryColor(Color.parseColor("#d4237a")); //设置应用primary颜色
```


# Thanks
感谢以下开源库的帮助
![https://github.com/Yalantis/uCrop](https://github.com/Yalantis/uCrop)
![https://github.com/shellljx/TagViewGroup](https://github.com/shellljx/TagViewGroup)
![https://github.com/zxfnicholas/CameraSDK](https://github.com/zxfnicholas/CameraSDK)
![https://github.com/wuapnjie/StickerView](https://github.com/wuapnjie/StickerView)
![https://github.com/jarlen/PhotoEdit](https://github.com/jarlen/PhotoEdit)
