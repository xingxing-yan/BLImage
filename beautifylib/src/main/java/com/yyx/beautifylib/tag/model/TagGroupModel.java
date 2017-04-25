package com.yyx.beautifylib.tag.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * author: shell
 * date 2016/12/30 下午3:37
 **/
public class TagGroupModel implements Parcelable {

    private List<Tag> tags = new ArrayList<>();
    private float percentX;
    private float percentY;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public float getPercentX() {
        return percentX;
    }

    public void setPercentX(float percentX) {
        this.percentX = percentX;
    }

    public float getPercentY() {
        return percentY;
    }

    public void setPercentY(float percentY) {
        this.percentY = percentY;
    }

    public static class Tag implements Parcelable {
        public String name;
        public String link;
        public int direction;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.link);
            dest.writeInt(this.direction);
        }

        public Tag() {
        }

        protected Tag(Parcel in) {
            this.name = in.readString();
            this.link = in.readString();
            this.direction = in.readInt();
        }

        public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {
            @Override
            public Tag createFromParcel(Parcel source) {
                return new Tag(source);
            }

            @Override
            public Tag[] newArray(int size) {
                return new Tag[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.tags);
        dest.writeFloat(this.percentX);
        dest.writeFloat(this.percentY);
    }

    public TagGroupModel() {
    }

    protected TagGroupModel(Parcel in) {
        this.tags = in.createTypedArrayList(Tag.CREATOR);
        this.percentX = in.readFloat();
        this.percentY = in.readFloat();
    }

    public static final Parcelable.Creator<TagGroupModel> CREATOR = new Parcelable.Creator<TagGroupModel>() {
        @Override
        public TagGroupModel createFromParcel(Parcel source) {
            return new TagGroupModel(source);
        }

        @Override
        public TagGroupModel[] newArray(int size) {
            return new TagGroupModel[size];
        }
    };
}
