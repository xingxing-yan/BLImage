package com.yyx.beautifylib.tag.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yyx.beautifylib.R;
import com.yyx.beautifylib.tag.DIRECTION;
import com.yyx.beautifylib.tag.TagViewGroup;
import com.yyx.beautifylib.tag.model.TagGroupModel;
import com.yyx.beautifylib.tag.utils.AnimatorUtils;
import com.yyx.beautifylib.tag.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * author: shell
 * date 2016/12/30 下午3:13
 **/
public class TagImageView extends FrameLayout {

    private ImageView mImageView;
    private FrameLayout mContentLayout;
    private List<TagGroupModel> mTagGroupModelList = new ArrayList<>();
    private List<TagViewGroup> mTagGroupViewList = new ArrayList<>();
    private boolean mIsEditMode;

    public TagImageView(Context context) {
        this(context, null);
    }

    public TagImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.layout_tag_imageview, this, true);
        mImageView = (ImageView) rootView.findViewById(R.id.imageview);
        mContentLayout = (FrameLayout) rootView.findViewById(R.id.tagsGroup);
    }

    public void setTagList(List<TagGroupModel> tagGroupList) {
        mTagGroupModelList.clear();
        mContentLayout.removeAllViews();
        mTagGroupViewList.clear();
        mTagGroupModelList.addAll(tagGroupList);
        for (TagGroupModel model : mTagGroupModelList) {
            TagViewGroup tagViewGroup = createTagViewGroup(model);
            tagViewGroup.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mContentLayout.addView(tagViewGroup);
            mTagGroupViewList.add(tagViewGroup);
        }
    }

    public void addTagGroup(TagGroupModel model, TagViewGroup.OnTagGroupClickListener listener) {
        TagViewGroup tagViewGroup = createTagViewGroup(model);
        tagViewGroup.setOnTagGroupClickListener(listener);
        tagViewGroup.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mContentLayout.addView(tagViewGroup);
        mTagGroupViewList.add(tagViewGroup);
        mTagGroupModelList.add(model);
    }

    public void removeTagGroup(TagViewGroup tagViewGroup) {
        mContentLayout.removeView(tagViewGroup);
        mTagGroupModelList.remove(mTagGroupViewList.indexOf(tagViewGroup));
        mTagGroupViewList.remove(tagViewGroup);
    }

    public int getTagGroupIndex(TagViewGroup tagGroup) {
        return mTagGroupViewList.indexOf(tagGroup);
    }

    public TagGroupModel getTagGroupModel(TagViewGroup tagViewGroup){
        return mTagGroupModelList.get(getTagGroupIndex(tagViewGroup));
    }


    public List<TagGroupModel> getTagGroupModelList(){
        return mTagGroupModelList;
    }

    private TagViewGroup createTagViewGroup(TagGroupModel model) {
        TagViewGroup tagViewGroup = new TagViewGroup(getContext());
        if (!mIsEditMode) {
            setTagGroupAnimation(tagViewGroup);
        }
        for (TagGroupModel.Tag tag : model.getTags()) {
            tagViewGroup.addTag(makeTagTextView(tag));
        }
        tagViewGroup.setPercent(model.getPercentX(), model.getPercentY());
        return tagViewGroup;
    }

    private TagTextView makeTagTextView(TagGroupModel.Tag tag) {
        TagTextView tagTextView = new TagTextView(getContext());
        tagTextView.setDirection(DIRECTION.valueOf(tag.getDirection()));
        tagTextView.setText(tag.getName());
        return tagTextView;
    }

    public void setTagGroupAnimation(TagViewGroup group) {
        group.setShowAnimator(AnimatorUtils.getTagShowAnimator(group))
                .setHideAnimator(AnimatorUtils.getTagHideAnimator(group)).addRipple();
    }

    public void excuteTagsAnimation() {
        for (TagViewGroup tagViewGroup : mTagGroupViewList) {
            if (tagViewGroup.isHiden()) {
                tagViewGroup.showWithAnimation();
            } else {
                tagViewGroup.hideWithAnimation();
            }
        }
    }

    public void setEditMode(boolean editMode) {
        mIsEditMode = editMode;
    }

    public void setImageUrl(String url) {
        ImageLoader.loadImage(url, mImageView);
    }

}
