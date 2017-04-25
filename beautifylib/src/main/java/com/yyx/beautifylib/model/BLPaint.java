package com.yyx.beautifylib.model;

import android.graphics.Color;

import com.yyx.beautifylib.R;
import com.yyx.beautifylib.scrawl.DrawAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/23.
 */

public class BLPaint {
    private DrawAttribute.DrawStatus drawStatus;
    private int paintId;
    private int imgResId;
    private int color;
    private String name;

    public BLPaint(){}

    public BLPaint(DrawAttribute.DrawStatus drawStatus, int paintId, int color, String name, int imgResId) {
        this.drawStatus = drawStatus;
        this.paintId = paintId;
        this.color = color;
        this.name = name;
        this.imgResId = imgResId;
    }

    public DrawAttribute.DrawStatus getDrawStatus() {
        return drawStatus;
    }

    public void setDrawStatus(DrawAttribute.DrawStatus drawStatus) {
        this.drawStatus = drawStatus;
    }

    public int getPaintId() {
        return paintId;
    }

    public void setPaintId(int paintId) {
        this.paintId = paintId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public static List<BLPaint> getPaints(){
        List<BLPaint> paints = new ArrayList<>();
//        paints.add(new BLPaint(DrawAttribute.DrawStatus.PEN_NORMAL, paintBitmap, Color.BLACK, "正常"));
        paints.add(new BLPaint(DrawAttribute.DrawStatus.PEN_WATER, R.drawable.crayon, Color.BLACK, "纯色", R.drawable.bl_color_block));
        paints.add(new BLPaint(DrawAttribute.DrawStatus.PEN_CRAYON, R.drawable.crayon, Color.BLACK, "水墨", R.drawable.bl_crayon_paint));
//        paints.add(new BLPaint(DrawAttribute.DrawStatus.PEN_COLOR_BIG, R.drawable.crayon, Color.BLACK, "大颜色"));
//        paints.add(new BLPaint(DrawAttribute.DrawStatus.PEN_STAMP, paintBitmap, Color.BLACK, "印记"));

        paints.add(new BLPaint(DrawAttribute.DrawStatus.PEN_ERASER, R.drawable.eraser, Color.BLACK, "橡皮擦", R.drawable.bl_eraser_paint));
        return paints;

    }
}
