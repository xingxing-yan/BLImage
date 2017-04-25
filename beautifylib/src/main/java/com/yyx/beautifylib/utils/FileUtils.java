package com.yyx.beautifylib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 文件操作类
 */
public class FileUtils {

    public static File createTmpFile(Context context){

        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            // 已挂载
            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_"+timeStamp+"";
            File tmpFile = new File(pic, fileName+".jpg");
            return tmpFile;
        }else{
            File cacheDir = context.getCacheDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_"+timeStamp+"";
            File tmpFile = new File(cacheDir, fileName+".jpg");
            return tmpFile;
        }
    }
    
    
    
    public interface Callback<T> {
      	public void onSuccess(T obj);
    	public void onError(String error);
    }
    
    public static void doGetBitmap(final String url, final Callback<Bitmap> callBack) {
    	
    	new Thread() {  
            public void run() { 
            	try{
            		URL imageURl=new URL(url);
            	    URLConnection con=imageURl.openConnection();
            	    con.connect();
            	    InputStream in=con.getInputStream();
            	    Bitmap bitmap=BitmapFactory.decodeStream(in);
            	    //in.close();
            	    if (bitmap == null) {  
                        callBack.onError("获取图片失败");  
                    } 
   				 	else {  
                        callBack.onSuccess(bitmap); 
                    }
            	}
            	catch (Exception e) {
					e.printStackTrace();
				}
            	
            	
            }
    	}.start();
    	
    	
    }
		
    	
    //获取网络中的图片内容
   	private static String getImageData(String url) throws ClientProtocolException, IOException {
   		HttpClient client = new DefaultHttpClient();
   		HttpGet httpget = new HttpGet(url);
   		HttpResponse httpResponse = client.execute(httpget);
   		int status = httpResponse.getStatusLine().getStatusCode();
   		if (status == HttpStatus.SC_OK) {
   			Log.d("getImageData", "status:" + status);
   			String strResult = EntityUtils.toString(httpResponse.getEntity());
   			return strResult;
   		}
   		return null;
   	}


	public static long getFileSize(File file) throws Exception {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		} else {

		}
		return size;
	}






}
