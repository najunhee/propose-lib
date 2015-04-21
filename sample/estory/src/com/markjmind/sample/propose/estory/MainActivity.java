package com.markjmind.sample.propose.estory;

import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.markjmind.propose.JwMotion;
import com.markjmind.propose.JwMotionListener;
import com.markjmind.propose.JwMotionSet;
import com.markjmind.sample.propose.estory.book.Page;


public class MainActivity extends Activity {

	public static int NONE=0,LEFT=-1,RIGHT = 1;
	public int DIRECTION = 0;
	private FrameLayout left_lyt,right_lyt,left_paper,right_paper;
	private ArrayList<Page> pageList = new ArrayList<Page>();
	private int currPage = 0;
	
	JwMotion leftMotion,rightMotion;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DIRECTION = NONE;
        left_lyt = (FrameLayout)findViewById(R.id.left_lyt);
        right_lyt = (FrameLayout)findViewById(R.id.right_lyt);
        left_paper = (FrameLayout)findViewById(R.id.left_paper);
        right_paper = (FrameLayout)findViewById(R.id.right_paper);
        
        initPage();
        currPage = 2;
        setPage(left_lyt,3);
        left_lyt.post(new Runnable() {
			@Override
			public void run() {
				initAnimation();
			}
		});
        
        
    }
    
    public void setPage(FrameLayout lyt, int index){
    	FrameLayout page = getPage(lyt);
    	page.removeAllViews();
    	page.addView(pageList.get(index).getPageView());
    }
    public void setPage(FrameLayout lyt, View view){
    	FrameLayout page = getPage(lyt);
    	page.addView(view);
    }
    
    public void initAnimation(){
    	leftMotion = new JwMotion(this);
        rightMotion = new JwMotion(this);
    	initAnimation(LEFT);
    	initAnimation(RIGHT);
    }
    
    private void initAnimation(int direction){
        final int dir = direction;
        final JwMotion motion;
        final JwMotionSet motionSet;
        final FrameLayout firstLayout;
        final FrameLayout lastLayout;
        final FrameLayout paperLayout;
        
        if(direction==RIGHT){
        	motion = rightMotion;
        	motionSet = motion.motionLeft;
        	firstLayout = right_lyt;
        	lastLayout = left_lyt;
        	paperLayout = right_paper;
        	
        }else if(direction==LEFT){
        	motion = leftMotion;
        	motionSet = motion.motionRight;
        	firstLayout = left_lyt;
        	lastLayout = right_lyt;
        	paperLayout = left_paper;
        }else{
        	return;
        }
        
        final FrameLayout paper1 = getPaper1(paperLayout);
		final FrameLayout paper2 = getPaper2(paperLayout);
		final FrameLayout paper_anim = (FrameLayout)paperLayout.findViewById(R.id.paper_anim);
		paper1.removeAllViews();
		paper2.removeAllViews();
		paperLayout.setRotationY(0);
		paperLayout.setCameraDistance(10000f);
		paper_anim.setOnTouchListener(null);
		
////		paperLayout.post(new Runnable() {
//				@Override
//				public void run() {
					if((dir==RIGHT && pageList.size()>currPage*2+dir) || (dir==LEFT && 0<=currPage*2+dir)){
						final View movePage = getPage(firstLayout).getChildAt(0);
						paper_anim.getLayoutParams().width = movePage.getWidth();
						paper_anim.getLayoutParams().height = movePage.getHeight();
						ObjectAnimator anim1;
						ObjectAnimator anim2;
						if(dir==RIGHT){
							anim1 = ObjectAnimator.ofFloat(paperLayout, View.ROTATION_Y, 0,-90);
							anim2 = ObjectAnimator.ofFloat(paperLayout, View.ROTATION_Y, -90,-180);
							anim1.setFloatValues(0,-90);
							anim2.setFloatValues(-90,-180);
							paperLayout.setPivotX(0f);
						}else{
							anim1 = ObjectAnimator.ofFloat(paperLayout, View.ROTATION_Y, 0,90);
							anim2 = ObjectAnimator.ofFloat(paperLayout, View.ROTATION_Y, 90,180);
							anim1.setFloatValues(0,90);
							anim2.setFloatValues(90,180);
							paperLayout.setPivotX(paperLayout.getWidth());
						}
						anim1.setDuration(500);
						anim2.setDuration(500);
						motionSet.play(anim1,movePage.getWidth()*2).next(anim2);
						
						
						motion.setOnMotionListener(new JwMotionListener() {
							@Override
							public void onStart() {
								getPage(firstLayout).removeAllViews();
								paper1.addView(movePage);
								if(dir==LEFT){
									paper2.addView(pageList.get(currPage*2-2).getPageView());
									if(0<=currPage*2-3){
										setPage(firstLayout,currPage*2-3);
									}
								}else{
									paper2.addView(pageList.get(currPage*2+dir).getPageView());
									if(pageList.size()>currPage*2+2*dir){
										setPage(firstLayout,currPage*2+2*dir);
									}
									
								}
								
							}
							@Override
							public void onScroll(int Direction, long currDuration, long totalDuration) {
							}
							@Override
							public void onEnd() {
								if(JwMotionSet.STATUS.end.equals(motionSet.getStatus())){
									currPage+=dir;
									View view = paper2.getChildAt(0);
									paper2.removeAllViews();
									setPage(lastLayout,view);
								}else{
									if(dir==LEFT){
										setPage(firstLayout,currPage*2-1);										
									}else{
										setPage(firstLayout,currPage*2);	
									}
								}
								leftMotion = null;
						        rightMotion = null;
								initAnimation();
							}
						});
						paper_anim.setOnTouchListener(motion.getOnTouchListener());
					}
				}
//			});
//    }
    
    
    private FrameLayout getPage(FrameLayout lyt){
    	return (FrameLayout)lyt.findViewById(R.id.page);
    }
    
    private FrameLayout getPaper1(FrameLayout lyt){
    	return (FrameLayout)lyt.findViewById(R.id.paper1);
    }
    
    private FrameLayout getPaper2(FrameLayout lyt){
    	return (FrameLayout)lyt.findViewById(R.id.paper2);
    }
    
    private void initPage(){
    	pageList.add(new Page(this,R.layout.page1) {
			@Override
			public void initAnimation() {
			}
		});
		
    	pageList.add(new Page(this,R.layout.page2) {
			@Override
			public void initAnimation() {
			}
		});
		
		pageList.add(new Page(this,R.layout.page3) {
			@Override
			public void initAnimation() {
			}
		});
		
		pageList.add(new Page(this,R.layout.page4) {
			@Override
			public void initAnimation() {
			}
		});
    }
}
