package com.yyx.beautifylib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/4/16.
 */

public class BLBitmapUtils {
    /**
     * 获取Bitmap
     * inSampleSize: 这个值取4，意思是宽高为原来的1/4,像素和内存为原来的1/16
     * @param filePath
     * @return
     */
    public static Bitmap getBitmap(String filePath) {
        try {
            long size = FileUtils.getFileSize(new File(filePath));
            Bitmap bm;
            //压缩大于1M的图片
            if (size > 1024 * 1024){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, options);
                options.inSampleSize = 4;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(filePath, options);
            }else{
                bm = BitmapFactory.decodeFile(filePath);
            }
            return bm;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Bitmap getBitmap(String path, int width, int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        final int h = options.outHeight;
        final int w = options.outWidth;
        int inSampleSize = 1;
        if (h > height || w > width) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) h / (float) height);
            final int widthRatio = Math.round((float) w / (float) width);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }


    public static int getBitmapSize(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(path, options);
        int byteCount = bm.getByteCount();

        return options.outWidth * options.outHeight * 4;
    }

    private static int readPictureDegree(String path) {
        short degree = 0;

        try {
            ExifInterface e = new ExifInterface(path);
            int orientation = e.getAttributeInt("Orientation", 1);
            switch(orientation) {
                case 3:
                    degree = 180;
                case 4:
                case 5:
                case 7:
                default:
                    break;
                case 6:
                    degree = 90;
                    break;
                case 8:
                    degree = 270;
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return degree;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if(bitmap == null) {
            return null;
        } else {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            Matrix mtx = new Matrix();
            mtx.postRotate((float)rotate);
            return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
        }
    }

    public static String saveAsBitmap(Context context, Bitmap bitmap) {
        String folderName = BLCommonUtils.getApplicationName(context);
        if(folderName == null || folderName.equals("")) {
            folderName = "CameraSDK";
        }

        File parentpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String fileName = System.currentTimeMillis() + ".jpg";
        fileName = folderName + "/" + fileName;
        File file = new File(parentpath, fileName);
        file.getParentFile().mkdirs();

        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            MediaScannerConnection.scanFile(context, new String[]{file.toString()}, (String[])null, (MediaScannerConnection.OnScanCompletedListener)null);
        } catch (FileNotFoundException var7) {
            var7.printStackTrace();
        }

        bitmap.recycle();
        return file.getAbsolutePath();
    }

    public static Bitmap rotateImage(Bitmap bit, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float)degrees);
        Bitmap tempBitmap = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit.getHeight(), matrix, true);
        return tempBitmap;
    }

    public static Bitmap reverseImage(Bitmap bit, int x, int y) {
        Matrix matrix = new Matrix();
        matrix.postScale((float)x, (float)y);
        Bitmap tempBitmap = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit.getHeight(), matrix, true);
        return tempBitmap;
    }

    public static Bitmap ResizeBitmap(Bitmap bitmap, int scale) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float)(1 / scale), (float)(1 / scale));
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();
        return resizedBitmap;
    }
}
