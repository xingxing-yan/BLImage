package com.yyx.beautifylib.utils;

import com.yyx.beautifylib.R;
import com.yyx.beautifylib.model.BLStickerInfo;
import com.yyx.beautifylib.model.Filter_Sticker_Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public class BLStickerUtils {

    /**
     * 获取所有贴纸
     * @return
     */
    @Deprecated
    public static ArrayList<Filter_Sticker_Info> getStickerList(){

        ArrayList<Filter_Sticker_Info> stickerList = new ArrayList<Filter_Sticker_Info>();

        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_1));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_2));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_33));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_4));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_5));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_6));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_7));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_8));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_9));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_10));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_11));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_12));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_13));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_14));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_15));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_16));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_17));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_18));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_19));
        stickerList.add(new Filter_Sticker_Info(R.drawable.sticker_20));
        //stickerList.add(new Filter_Sticker_Info(R.drawable.camerasdk_stickers,true));
        return stickerList;

    }

    public static List<BLStickerInfo> createStickerInfoList(){
        ArrayList<BLStickerInfo> stickerList = new ArrayList<>();

        stickerList.add(new BLStickerInfo(R.drawable.sticker_text));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_1));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_2));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_33));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_4));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_5));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_6));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_7));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_8));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_9));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_10));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_11));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_12));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_13));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_14));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_15));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_16));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_17));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_18));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_19));
        stickerList.add(new BLStickerInfo(R.drawable.sticker_20));
        return stickerList;
    }
}
