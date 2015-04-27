package com.markjmind.sample.propose.estory.book;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class ScaleFrameLayout extends FrameLayout{

	private float density;
	
	public ScaleFrameLayout(Context context) {
		super(context);
		density = getContext().getResources().getDisplayMetrics().density;
	}
	public ScaleFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		density = getContext().getResources().getDisplayMetrics().density;
	}
	public ScaleFrameLayout(Context context, AttributeSet attrs,int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		density = getContext().getResources().getDisplayMetrics().density;
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		parentWidth = MeasureSpec.getSize(widthMeasureSpec);
		parentHeight = MeasureSpec.getSize(heightMeasureSpec);
		drawScale(parentWidth, parentHeight);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec); 
		
	}
	int parentWidth, parentHeight;
	float scale_width, scale_height;
	public void drawScale(int width, int height){
		String tag = (String)getTag();
		if(tag!=null){
			String[] split = tag.split(",");
			float scale_width = width/Float.valueOf(split[0].trim());
			float scale_height = height/Float.valueOf(split[1].trim());
			for(int i=0;i<this.getChildCount();i++){
				View view = this.getChildAt(i);
				tag = (String)view.getTag();
				if(tag!=null){
					split = tag.split(",");
					LayoutParams lp = (LayoutParams) view.getLayoutParams();
					lp.width = (int)(Integer.parseInt(split[0].trim())*scale_width);
					lp.height = (int)(Integer.parseInt(split[1].trim())*scale_height);
					view.setLayoutParams(view.getLayoutParams());
					if(split.length==4){
						view.setX(Float.parseFloat(split[2])*scale_width);
						view.setY(Float.parseFloat(split[3])*scale_height);
					}
				}
			}
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
}
