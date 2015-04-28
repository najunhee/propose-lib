package com.markjmind.sample.propose.estory.book;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
	float scale_width=1f,temp_scale_width=1f, scale_height=1f, temp_scale_height=1f;
	boolean isFirst = true;
	public void drawScale(int width, int height){
		String tag = (String)getTag();
		if(tag!=null){
			String[] split = tag.split(",");
			temp_scale_width = width/Float.valueOf(split[0].trim());
			temp_scale_height = height/Float.valueOf(split[1].trim());
				for(int i=0;i<this.getChildCount();i++){
					View view = this.getChildAt(i);
					tag = (String)view.getTag();
					if(tag!=null){
						split = tag.split(",");
						LayoutParams lp = (LayoutParams) view.getLayoutParams();
						lp.width = (int)(Integer.parseInt(split[0].trim())*temp_scale_width);
						lp.height = (int)(Integer.parseInt(split[1].trim())*temp_scale_width);
						view.setLayoutParams(view.getLayoutParams());
					}
				}
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		if(scale_width!=temp_scale_width || scale_height!=temp_scale_height){
			for(int i=0;i<this.getChildCount();i++){
				View view = this.getChildAt(i);
				String tag = (String)view.getTag();
				if(tag!=null){
					String[] split = tag.split(",");
					if(split.length==4){
						if(isFirst){
							view.setX(Float.parseFloat(split[2])*temp_scale_width);
							view.setY(Float.parseFloat(split[3])*temp_scale_height);
						}else{
							view.setX(view.getX()/scale_width*temp_scale_width);
							view.setY(view.getY()/scale_height*temp_scale_height);
						}
						Log.e("test","drawScale "+"X:"+view.getX()+" Y:"+view.getY());
					}
				}
			}
			scale_width = temp_scale_width;
			scale_height = temp_scale_height;
			isFirst = false;
		}
		super.onLayout(changed, left, top, right, bottom);
	}
	
	public static float[] getChildPoint(View view){
		String tag = (String)view.getTag();
		float[] point = null;
		if(tag!=null){
			String[] split = tag.split(",");
			if(split.length==4){
				point = new float[2];
				point[0] = Float.parseFloat(split[2]);
				point[1] = Float.parseFloat(split[3]);
			}
		}
		return point;
	}
	
	public float getFractionX(){
		return scale_width;
	}
	
	public float getFractionY(){
		return scale_height;
	}
}
