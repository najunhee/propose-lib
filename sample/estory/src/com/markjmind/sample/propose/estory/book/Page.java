package com.markjmind.sample.propose.estory.book;

import java.util.Enumeration;
import java.util.Hashtable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.markjmind.propose.JwMotion;
import com.markjmind.propose.MotionInitor;

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
	protected int index;
	private Hashtable<String,JwMotion> motionPool = new Hashtable<String,JwMotion>();
	
	public Page(Context context, int layout_id){
		this.context = context;
		this.layout_id = layout_id;
	}
	
	public Context getContext(){
		return context;
	}
	
	public void remove(){
		lyt.removeView(page_view);
	}
	
	public ViewGroup makeView(){
		page_view = (ViewGroup)((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout_id, null);
		
		return page_view;
	}
	
	public ViewGroup getView(){
		return page_view;
	}
	
	
	public int getIndex() {
		return index;
	}
	
	public void addInitorMotion(String key,JwMotion motion){
		motionPool.put(key, motion);
	}
	
	public JwMotion getInitorMotion(String key){
		return motionPool.get(key);
	}
	
	public void resetInitor(){
		Enumeration<String> em =  motionPool.keys();
		while(em.hasMoreElements()){
			String key = em.nextElement();
			MotionInitor mi = motionPool.get(key).getMotionInitor();
			if(mi!=null){
				mi.init(motionPool.get(key));
			}
		}
	}
	
	public abstract void initAnimation(int index, ViewGroup pageView, Page page1, Page page2);
	
}
