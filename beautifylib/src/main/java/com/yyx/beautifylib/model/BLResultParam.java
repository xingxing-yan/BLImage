package com.yyx.beautifylib.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.yyx.beautifylib.tag.model.TagGroupModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/16.
 * 返回给主页面的数据
 */

public class BLResultParam implements Parcelable {
    public static final String KEY = "result_param";

    private List<String> imageList = new ArrayList<>();
//    private List<TagGroupModelParam> tagGroupModelParams = new ArrayList<>();
    private Map<String, TagGroupModelParam> tagGroupModelMap = new HashMap<>();


    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

//    public List<TagGroupModelParam> getTagGroupModelParams() {
//        return tagGroupModelParams;
//    }
//
//    public void setTagGroupModelParams(List<TagGroupModelParam> tagGroupModelParams) {
//        this.tagGroupModelParams = tagGroupModelParams;
//    }


    public Map<String, TagGroupModelParam> getTagGroupModelMap() {
        return tagGroupModelMap;
    }

    public void setTagGroupModelMap(Map<String, TagGroupModelParam> tagGroupModelMap) {
        this.tagGroupModelMap = tagGroupModelMap;
    }

    public BLResultParam() {
    }


    public static class TagGroupModelParam implements Parcelable {
        private List<TagGroupModel> tagGroupModelList;

        public TagGroupModelParam(List<TagGroupModel> list) {
            this.tagGroupModelList = list;
        }

        public List<TagGroupModel> getTagGroupModelList() {
            return tagGroupModelList;
        }

        public void setTagGroupModelList(List<TagGroupModel> tagGroupModelList) {
            this.tagGroupModelList = tagGroupModelList;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(this.tagGroupModelList);
        }

        protected TagGroupModelParam(Parcel in) {
            this.tagGroupModelList = new ArrayList<TagGroupModel>();
            in.readList(this.tagGroupModelList, TagGroupModel.class.getClassLoader());
        }

        public static final Creator<TagGroupModelParam> CREATOR = new Creator<TagGroupModelParam>() {
            @Override
            public TagGroupModelParam createFromParcel(Parcel source) {
                return new TagGroupModelParam(source);
            }

            @Override
            public TagGroupModelParam[] newArray(int size) {
                return new TagGroupModelParam[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.imageList);
        dest.writeInt(this.tagGroupModelMap.size());
        for (Map.Entry<String, TagGroupModelParam> entry : this.tagGroupModelMap.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
    }

    protected BLResultParam(Parcel in) {
        this.imageList = in.createStringArrayList();
        int tagGroupModelMapSize = in.readInt();
        this.tagGroupModelMap = new HashMap<String, TagGroupModelParam>(tagGroupModelMapSize);
        for (int i = 0; i < tagGroupModelMapSize; i++) {
            String key = in.readString();
            TagGroupModelParam value = in.readParcelable(TagGroupModelParam.class.getClassLoader());
            this.tagGroupModelMap.put(key, value);
        }
    }

    public static final Parcelable.Creator<BLResultParam> CREATOR = new Parcelable.Creator<BLResultParam>() {
        @Override
        public BLResultParam createFromParcel(Parcel source) {
            return new BLResultParam(source);
        }

        @Override
        public BLResultParam[] newArray(int size) {
            return new BLResultParam[size];
        }
    };
}
