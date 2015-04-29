package com.markjmind.sample.propose.estory.book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 
 * @author 오재웅
 * @phone 010-2898-7850
 * @email markjmind@gmail.com
 * @date 2015. 4. 29.
 */
public abstract class Page {
	private Context context;
	private int layout_id;
	private FrameLayout lyt;
	private ViewGroup page_view;
	
	public Page(Context context, int layout_id){
		this.context = context;
		this.layout_id = layout_id;
	}
	
	public void remove(){
		lyt.removeView(page_view);
	}
	
	public ViewGroup getPageView(){
		page_view = (ViewGroup)((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout_id, null);
		initAnimation(page_view);
		return page_view;
	}
	
	public abstract void initAnimation(ViewGroup pageView);
}
