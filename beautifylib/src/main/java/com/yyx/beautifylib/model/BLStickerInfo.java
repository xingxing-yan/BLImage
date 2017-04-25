package com.yyx.beautifylib.model;

import android.graphics.Color;

/**
 * Created by Administrator on 2017/4/16.
 */

public class BLStickerInfo {
    //图片贴图的属性
    private int drawableId; //本地资源ID
    private String url; //网络图片地址

    //文字贴图的属性
    private String text;
    private int color = Color.WHITE;

    public BLStickerInfo(int drawableId) {
        this.drawableId = drawableId;
    }

    public BLStickerInfo(String text, int color){
        this.text = text;
        this.color = color;
    }

    public BLStickerInfo(int drawableId, String url) {
        this.drawableId = drawableId;
        this.url = url;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
