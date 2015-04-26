package com.markjmind.sample.propose.estory.book;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class ScaleFrameLayout extends FrameLayout{

	public ScaleFrameLayout(Context context) {
		super(context);
	}
	public ScaleFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public ScaleFrameLayout(Context context, AttributeSet attrs,int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec); 
		float density = getContext().getResources().getDisplayMetrics().density;
		int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
		int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
		drawScale(parentWidth, parentHeight);
		 
	}
	
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
					view.getLayoutParams().width = (int)(Integer.parseInt(split[0].trim())*scale_width);
					view.getLayoutParams().height = (int)(Integer.parseInt(split[1].trim())*scale_height);
					view.setBackgroundColor(Color.parseColor("#ff0000"));
//					Log.e("tag","tag:"+Integer.parseInt(split[0].trim()));
//					Log.e("sdsd","scale_width:"+scale_width);
					Log.e("sdsd","width:"+width+" height:"+height);
					view.setLayoutParams(view.getLayoutParams());
				}
			}
		}
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		drawScale(canvas.getWidth(), canvas.getHeight());
		super.dispatchDraw(canvas);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		drawScale(canvas.getWidth(), canvas.getHeight());
		super.onDraw(canvas);
	}
}
