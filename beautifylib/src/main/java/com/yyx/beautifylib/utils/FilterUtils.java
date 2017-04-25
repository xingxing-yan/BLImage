package com.yyx.beautifylib.utils;

import android.content.Context;

import com.muzhi.camerasdk.library.filter.GPUImageFilter;
import com.muzhi.camerasdk.library.filter.GPUImageView;
import com.muzhi.camerasdk.library.filter.util.ImageFilterTools;
import com.muzhi.camerasdk.library.filter.util.ImageFilterTools.FilterType;
import com.yyx.beautifylib.R;
import com.yyx.beautifylib.model.Filter_Effect_Info;

import java.util.ArrayList;


/**
 * 特效文件
 */
public class FilterUtils {
	/**
	 * 获取特效列表
	 * @return
	 */
	public static ArrayList<Filter_Effect_Info> getEffectList(){
		
		ArrayList<Filter_Effect_Info> effect_list = new ArrayList<Filter_Effect_Info>();
		
		effect_list.add(new Filter_Effect_Info("原图", R.drawable.camerasdk_filter_normal,null));
		effect_list.add(new Filter_Effect_Info("创新", R.drawable.camerasdk_filter_in1977, FilterType.I_1977));
		effect_list.add(new Filter_Effect_Info("流年", R.drawable.camerasdk_filter_amaro, FilterType.I_AMARO));
		effect_list.add(new Filter_Effect_Info("淡雅", R.drawable.camerasdk_filter_brannan, FilterType.I_BRANNAN));
		effect_list.add(new Filter_Effect_Info("怡尚", R.drawable.camerasdk_filter_early_bird, FilterType.I_EARLYBIRD));
		effect_list.add(new Filter_Effect_Info("优格", R.drawable.camerasdk_filter_hefe, FilterType.I_HEFE));
		effect_list.add(new Filter_Effect_Info("胶片", R.drawable.camerasdk_filter_hudson, FilterType.I_HUDSON));
		effect_list.add(new Filter_Effect_Info("黑白", R.drawable.camerasdk_filter_inkwell, FilterType.I_INKWELL));
		effect_list.add(new Filter_Effect_Info("个性", R.drawable.camerasdk_filter_lomo, FilterType.I_LOMO));
		effect_list.add(new Filter_Effect_Info("回忆", R.drawable.camerasdk_filter_lord_kelvin, FilterType.I_LORDKELVIN));
		effect_list.add(new Filter_Effect_Info("不羁", R.drawable.camerasdk_filter_nashville, FilterType.I_NASHVILLE));
		effect_list.add(new Filter_Effect_Info("森系", R.drawable.camerasdk_filter_rise, FilterType.I_NASHVILLE));
		effect_list.add(new Filter_Effect_Info("清新", R.drawable.camerasdk_filter_sierra, FilterType.I_SIERRA));
		effect_list.add(new Filter_Effect_Info("摩登", R.drawable.camerasdk_filter_sutro, FilterType.I_SUTRO));
		effect_list.add(new Filter_Effect_Info("绚丽", R.drawable.camerasdk_filter_toaster, FilterType.I_TOASTER));
		effect_list.add(new Filter_Effect_Info("优雅", R.drawable.camerasdk_filter_valencia, FilterType.I_VALENCIA));
		effect_list.add(new Filter_Effect_Info("日系", R.drawable.camerasdk_filter_walden, FilterType.I_WALDEN));
		effect_list.add(new Filter_Effect_Info("新潮", R.drawable.camerasdk_filter_xproii, FilterType.I_XPROII));
		
		return effect_list;
		
	}

	/** 添加滤镜
	 * @param context
	 * @param filterType
	 * @param imageView
	 */
	public static void addFilter(Context context, ImageFilterTools.FilterType filterType, GPUImageView imageView){
		GPUImageFilter filter;
		if (filterType == null) {
			filter = new GPUImageFilter();
		} else {
			filter = ImageFilterTools.createFilterForType(context, filterType);
		}
		imageView.setFilter(filter);
//		imageView.requestRender();
	}


}
