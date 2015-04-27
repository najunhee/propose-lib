package com.markjmind.sample.propose.estory;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.markjmind.propose.JwAnimatorListener;
import com.markjmind.propose.JwMotion;
import com.markjmind.propose.JwMotion.JwMotionListener;
import com.markjmind.propose.JwMotionSet;
import com.markjmind.propose.MotionInitor;
import com.markjmind.sample.propose.estory.book.Page;


public class MainActivity extends Activity {

	public static int NONE=0,LEFT=-1,RIGHT = 1;
	public int DIRECTION = 0;
	private FrameLayout left_lyt,right_lyt,left_paper,right_paper,banner_lyt;
	private ArrayList<Page> pageList = new ArrayList<Page>();
	private int currPage = 0;
	
	JwMotion scaleMotion,leftMotion,rightMotion;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DIRECTION = NONE;
        left_lyt = (FrameLayout)findViewById(R.id.left_lyt);
        right_lyt = (FrameLayout)findViewById(R.id.right_lyt);
        left_paper = (FrameLayout)findViewById(R.id.left_paper);
        right_paper = (FrameLayout)findViewById(R.id.right_paper);
        banner_lyt = (FrameLayout)findViewById(R.id.banner_lyt);
        scaleMotion = new JwMotion(this);
        leftMotion = new JwMotion(this);
        rightMotion = new JwMotion(this);
        initPage();
        currPage = 0;
        setPage2(right_lyt,0);
        right_lyt.post(new Runnable() {
			@Override
			public void run() {
				initAnimation();
				
				final int point = (int)banner_lyt.getX();
				final int point_width = banner_lyt.getWidth();
				ValueAnimator rightAnimator = ValueAnimator.ofInt(0,scaleMotion.getWindowWidth()/2-banner_lyt.getWidth());
				rightAnimator.addUpdateListener(new AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						int value = (Integer)animation.getAnimatedValue();
						banner_lyt.getLayoutParams().width = point_width+value;
						banner_lyt.setLayoutParams(banner_lyt.getLayoutParams());
						banner_lyt.invalidate();
					}
				});
				rightAnimator.setDuration(1000);
				rightAnimator.setInterpolator(null);
				scaleMotion.motionRight.play(banner_lyt, rightAnimator,scaleMotion.getWindowWidth()/2-banner_lyt.getWidth());
				banner_lyt.setOnTouchListener(scaleMotion);
				scaleMotion.setOnMotionListener(new JwMotionListener() {
					@Override
					public void onStart() {
						leftMotion.enableMotion(false);
						rightMotion.enableMotion(false);
					}
					@Override
					public void onScroll(int Direction, long currDuration, long totalDuration) {
					}
					@Override
					public void onEnd() {
						right_lyt.post(new Runnable() {
							@Override
							public void run() {
								leftMotion.enableMotion(true);
								rightMotion.enableMotion(true);
								initAnimation();
							}
						});
					}
				});
			}
		});
        
    }
    
    public void setPage2(FrameLayout lyt, int index){
    	FrameLayout page = getPage(lyt);
    	page.removeAllViews();
    	page.addView(pageList.get(index).getPageView());
    	Log.i("as","page size:"+page.getChildCount());
    }
    
    public void initAnimation(){
    	initAnimation(LEFT);
    	initAnimation(RIGHT);
    }
    
    
    private void initAnimation(int direction){
        final int dir = direction;
        final JwMotion motion;
        final JwMotionSet motionSet;
        final FrameLayout paperLayout;
        final FrameLayout pageLayout1;
        final FrameLayout pageLayout2;
        if(direction==RIGHT){
        	motion = rightMotion;
        	motionSet = motion.motionLeft;
        	pageLayout1 = getPage(right_lyt);
        	pageLayout2 = getPage(left_lyt);
        	paperLayout = right_paper;
        	
        }else if(direction==LEFT){
        	motion = leftMotion;
        	motionSet = motion.motionRight;
        	pageLayout1 = getPage(left_lyt);
        	pageLayout2 = getPage(right_lyt);
        	paperLayout = left_paper;
        }else{
        	return;
        }
        motionSet.enableReverse(false);
        final FrameLayout paper1 = getPaper1(paperLayout);
		final FrameLayout paper2 = getPaper2(paperLayout);
		paper1.removeAllViews();
		paper2.removeAllViews();
		paperLayout.setRotationY(0);
		paperLayout.setCameraDistance(5000f*getResources().getDisplayMetrics().density);
		paper1.setOnTouchListener(null);
		
		if((dir==RIGHT && pageList.size()>currPage*2+dir) || (dir==LEFT && 0<=currPage*2+dir)){
			View movePage = pageLayout1.getChildAt(0);
			final int paper_width = paperLayout.getWidth();
			final float paper_x = paperLayout.getX();
			
			paper1.getLayoutParams().width = movePage.getWidth();
			paper1.getLayoutParams().height = movePage.getHeight();
			paper1.setLayoutParams(paper1.getLayoutParams());
			ObjectAnimator anim1 = ObjectAnimator.ofFloat(paperLayout, View.ROTATION_Y, 0,-90*dir);
			ObjectAnimator anim2 = ObjectAnimator.ofFloat(paperLayout, View.ROTATION_Y, 90*dir,0);
			if(dir==RIGHT){
				paperLayout.setPivotX(0);
			}else{
				paperLayout.setPivotX(paper_width);
			}
			paperLayout.setPivotY(paperLayout.getHeight()/2);
			anim1.setDuration(500);
			anim2.setDuration(500);
			motionSet.play(anim1,movePage.getWidth()*2).next(anim2);
			
			anim1.addListener(new JwAnimatorListener() {
				@Override
				public void onStart(Animator animation) {
				}
				@Override
				public void onReverseStart(Animator animation) {
					paper1.setVisibility(View.VISIBLE);
					paper2.setVisibility(View.GONE);
					if(dir==RIGHT){
						paperLayout.setX(paper_x);
						paperLayout.setPivotX(0);
					}else{
						paperLayout.setX(paper_x);
						paperLayout.setPivotX(paper_width);
					}
				}
				@Override
				public void onReverseEnd(Animator animation) {
					
				}
				@Override
				public void onEnd(Animator animation) {
					paper1.setVisibility(View.GONE);
					paper2.setVisibility(View.VISIBLE);
					if(dir==RIGHT){
						paperLayout.setX(paper_x-paper_width*dir);
						paperLayout.setPivotX(paper_width);
					}else{
						paperLayout.setX(paper_x-paper_width*dir);
						paperLayout.setPivotX(0);
					}
					
					
				}
			});
			
			motion.setOnMotionListener(new JwMotionListener() {
				@Override
				public void onStart() {
					Log.e("dsd","currPage:"+currPage+" dir:"+dir);
					
					paper2.setVisibility(View.GONE);
					moveView(pageLayout1,paper1);
					scaleMotion.enableMotion(false);
					
					if(dir==LEFT){
						rightMotion.enableMotion(false);
						changeLayout(paper2,pageList.get(currPage*2-2).getPageView());
						if(0<=currPage*2-3){
							changeLayout(pageLayout1,pageList.get(currPage*2-3).getPageView());
						}
					}else{
						leftMotion.enableMotion(false);
						changeLayout(paper2,pageList.get(currPage*2+dir).getPageView());
						if(pageList.size()>currPage*2+2*dir){
							changeLayout(pageLayout1,pageList.get(currPage*2+2*dir).getPageView());
							
						}
						
					}
					
				}
				@Override
				public void onScroll(int Direction, long currDuration, long totalDuration) {
				}
				@Override
				public void onEnd() {
					scaleMotion.enableMotion(true);
					rightMotion.enableMotion(true);
					leftMotion.enableMotion(true);
					if(JwMotionSet.STATUS.end.equals(motionSet.getStatus())){
						currPage+=dir;
						motionSet.reset();
						paper1.removeAllViews();
						moveView(paper2,pageLayout2);
					}else{
						paper2.removeAllViews();
						moveView(paper1,pageLayout1);
					}
					paperLayout.setX(paper_x);
					paper1.setVisibility(View.VISIBLE);
					paper2.setVisibility(View.VISIBLE);
					pageLayout1.post(new Runnable() {
						@Override
						public void run() {
							initAnimation();
						}
					});
				}
			});
			pageLayout1.setOnTouchListener(motion);
		}
	}
    
    private void moveView(ViewGroup move, ViewGroup target){
    	View view = move.getChildAt(0);
    	move.removeAllViews();
    	if(view!=null){
    		changeLayout(target, view);
    	}
    	
    	
    }
    private void changeLayout(ViewGroup parents, View view){
    	parents.removeAllViews();
    	parents.addView(view);
    }
    
    public ViewGroup getPageView(int index){
    	return pageList.get(index).getPageView();
    	
    }
    
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
			public void initAnimation(ViewGroup pageView) {
				
			}
		});
		
    	pageList.add(new Page(this,R.layout.page2) {
			@Override
			public void initAnimation(ViewGroup page) {
				final ViewGroup pageView = (ViewGroup)page.findViewById(R.id.scale_layout);
				final ImageView door1 = (ImageView)pageView.findViewById(R.id.door1);
				JwMotion motion_door1 = new JwMotion(pageView.getContext());
				final ImageView frog = (ImageView)pageView.findViewById(R.id.frog);
				JwMotion motion_frog = new JwMotion(pageView.getContext());
				
				ObjectAnimator door_anim = ObjectAnimator.ofFloat(door1, View.ROTATION_Y, 0,-180);
				door1.setPivotX(0f);
				door_anim.setDuration(700);
				motion_door1.setMotionInitor(new MotionInitor() {
					@Override
					public void init(JwMotion jwm, View[] views) {
						int distance = door1.getWidth()*2;
						jwm.motionLeft.setMotionDistance(distance);
					}
				});
				motion_door1.motionLeft.play(door_anim);
				door1.setOnTouchListener(motion_door1);
				
				final ObjectAnimator frog_anim = ObjectAnimator.ofFloat(frog, View.TRANSLATION_Y, 0,500);
				frog_anim.setDuration(700);
				frog_anim.setInterpolator(null);
				motion_frog.setMotionInitor(new MotionInitor() {
					int pageWidth=0;
					int pageHeight=0;
					@Override
					public void init(JwMotion jwm, View[] views) {
						if(pageWidth!=pageView.getWidth() && pageHeight!=pageView.getHeight()){
							pageWidth = pageView.getWidth();
							pageHeight = pageView.getHeight();
							float distance = pageHeight-frog.getY()-frog.getHeight();
							Log.e("sdsd","frog.getHeight():"+frog.getHeight()+" pageHeight:"+pageHeight+" frog.getY():"+frog.getY());
							frog_anim.setFloatValues(frog.getY(),pageHeight-frog.getHeight());
							jwm.motionDown.setMotionDistance(distance);
						}
					}
				});
				motion_frog.motionDown.play(frog_anim);
				motion_frog.motionDown.enableFling(false).enableTabUp(false);
				frog.setOnTouchListener(motion_frog);
			}
		});
		
		pageList.add(new Page(this,R.layout.page3) {
			@Override
			public void initAnimation(ViewGroup pageView) {
			}
		});
		
		pageList.add(new Page(this,R.layout.page4) {
			@Override
			public void initAnimation(ViewGroup pageView) {
			}
		});
    }
}
