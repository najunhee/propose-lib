package com.markjmind.sample.propose.estory.book;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import android.animation.AnimatorSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.markjmind.propose.Propose;
import com.markjmind.propose.MotionInitor;
import com.markjmind.sample.propose.estory.common.UnitAnimation;

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
	private ViewGroup page_view;
	protected int index;
	private Hashtable<String,Propose> motionPool = new Hashtable<String,Propose>();
	
	public abstract void initAnimation(int index, ViewGroup pageView, Page page1, Page page2);
	
	public Page(Context context, int layout_id){
		this.context = context;
		this.layout_id = layout_id;
	}
	
	public Context getContext(){
		return context;
	}
	
	public ViewGroup makeView(){
		page_view = (ViewGroup)((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout_id, null);
		motionPool.clear();
		return page_view;
	}
	
	public ViewGroup getView(){
		return page_view;
	}
	
	public void remove(){
		dispose();
		motionPool.clear();
		page_view = null;
	}
	
	
	public int getIndex() {
		return index;
	}
	
	public void addInitorMotion(String key,Propose motion){
		motionPool.put(key, motion);
	}
	
	public Propose getInitorMotion(String key){
		return motionPool.get(key);
	}
	
	public void resetInitor(){
		Enumeration<String> em =  motionPool.keys();
		while(em.hasMoreElements()){
			String key = em.nextElement();
			MotionInitor mi = motionPool.get(key).getMotionInitor();
			if(mi!=null){
				mi.touchDown(motionPool.get(key));
			}
		}
	}
	
	
   protected void putPageMotion(Page page, Propose motion, String tag){
   	 page.addInitorMotion(tag, motion);
   }
   
   protected void putPageMotion(Page page, Propose motion[], String tag){
   	for(int i=0;i<motion.length;i++){
   		page.addInitorMotion(tag+i, motion[i]);
   	}
   }
   
   public void dispose(){
	   
   }
}
