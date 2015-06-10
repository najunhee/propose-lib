package com.markjmind.sample.propose.estory.common;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.propose.Propose;
import com.markjmind.propose.Propose.ProposeListener;
import com.markjmind.sample.propose.estory.book.RatioFrameLayout;

public class FolioUnitRun extends MultiMotionAnimator{
	int[] pageWidth ={0,0};
	int[] pageHeight ={0,0};
	String tag;
	ObjectAnimator turnRight,turnLeft,turnRight2,turnLeft2;
	long personDuration = 3000;
	long turnDuration = 300;
	float startPoint = 0f;
	boolean firstStart = true;
	ArrayList<AnimatorSet> waitAnimList = new ArrayList<AnimatorSet>();
	UnitAnimation waitAnim;
	
	ArrayList<AnimatorSet> moveAnimList = new ArrayList<AnimatorSet>();
	UnitAnimation moveAnim;
	
	public float distanceRatio = 1.0f;
	
	public FolioUnitRun(ViewGroup... parents){
		super(parents);
	}
	
	public void setWaitAnimation(UnitAnimation waitAnim){
		this.waitAnim = waitAnim;
	}
	
	public void setMoveAnimation(UnitAnimation moveAnim){
		this.moveAnim = moveAnim;
	}
	
	public void setDuration(long duration){
		personDuration = duration;
	}
	
	@Override
	public void touchDown(int index, ViewGroup[] parents, Propose motion, ObjectAnimator[] anims) {
		if(pageWidth[index]!=parents[index].getWidth() || pageHeight[index]!=parents[index].getHeight()){
			pageWidth[index] = parents[index].getWidth();
			pageHeight[index] = parents[index].getHeight();
			
			View person = getViews(tag)[index];
			float fraction = ((RatioFrameLayout)parents[index]).getFractionY();
			
//			//좌우 움직임
			fraction = ((RatioFrameLayout)parents[0]).getFractionX();
			float startX,endX;
			if(index==0){
				startX = parents[0].getWidth()+person.getWidth()
						-(1024-RatioFrameLayout.getTagChildXY(getViews(tag)[0])[0])*fraction*distanceRatio-person.getWidth()*distanceRatio
						;
				endX = 0;
			}else{
				startX = parents[1].getWidth()+person.getWidth()-(1024-RatioFrameLayout.getTagChildXY(getViews(tag)[1])[0])*fraction*distanceRatio-person.getWidth()*distanceRatio;
				endX = -parents[0].getWidth();
			}
			motion.motionLeft.setMotionDistance((Math.abs(startX-endX)*2)*distanceRatio);
			
			anims[0].setFloatValues(startX,endX);
			anims[1].setFloatValues(endX,startX);
			if(firstStart){
				if(moveAnim!=null){
					moveAnimList.add(moveAnim.getAnimation(person));
				}
				if(index==parents.length-1){
					firstStart = false;
				}
			}
			
		}
	}
	
	@Override
	public void touchUp(int index, ViewGroup[] parents, Propose motion, ObjectAnimator[] anims) {
	}
	
	@Override
	public void play(Propose motion, ObjectAnimator[] anims) {
		anims[0].setDuration((long)(personDuration*distanceRatio));
		anims[0].setInterpolator(null);
		anims[1].setDuration((long)(personDuration*distanceRatio));
		anims[1].setInterpolator(null);
		motion.motionLeft.play(anims[0]).next(turnLeft2).with(turnLeft).next(anims[1]).next(turnRight2).with(turnRight);
		motion.motionLeft.enableMove(false);
		final Propose endMotion = motion;
		motion.setOnMotionListener(new ProposeListener() {
			@Override
			public void onStart() {
				startMoveAnimation();
				stopWaitAnimation();
			}
			
			@Override
			public void onScroll(int Direction, long currDuration, long totalDuration) {
			}
			@Override
			public void onEnd() {
				stopMoveAnimation();
				startWaitAnimation();
				endMotion.motionLeft.reset();
			}
		});
	}
	
	public void setAnimation(String tag,int id, Property<View, Float>... property){
		this.tag = tag;
		addView(tag,id);
		View[] views = getViews(tag);
		turnRight = ObjectAnimator.ofFloat(views[0], View.ROTATION_Y, 180,0);
		turnLeft  = ObjectAnimator.ofFloat(views[0], View.ROTATION_Y, 0,180);
		turnRight.setDuration(turnDuration);
		turnLeft.setDuration(turnDuration);
		turnRight.setInterpolator(null);
		turnLeft.setInterpolator(null);
		turnRight2 = ObjectAnimator.ofFloat(views[1], View.ROTATION_Y, 180,0);
		turnLeft2  = ObjectAnimator.ofFloat(views[1], View.ROTATION_Y, 0,180);
		turnRight2.setDuration(turnDuration);
		turnLeft2.setDuration(turnDuration);
		turnRight2.setInterpolator(null);
		turnLeft2.setInterpolator(null);
		loadOfFloat(tag, property);
		setMultyOnTouch(tag);
		
	}
	
	public void setStartHeight(float point){
		this.startPoint = point;
	}
	
	private void startMoveAnimation(){
		if(moveAnimList.size()>0){
			for(int i=0;i<moveAnimList.size();i++){
				moveAnimList.get(i).start();
			}
		}
	}
	private void stopMoveAnimation(){
		if(moveAnimList.size()>0){
			for(int i=0;i<moveAnimList.size();i++){
				for(Animator am : moveAnimList.get(i).getChildAnimations()){
					am.end();	
				}
			}
		}
	}
	
	public void startWaitAnimation(){
		if(waitAnim!=null){
			View[] persons = getViews(tag);
			for(int i=0;i<persons.length;i++){
				waitAnimList.add(waitAnim.getAnimation(persons[i]));
				waitAnimList.get(i).start();
			}
		}
	}
	
	public void stopWaitAnimation(){
		if(waitAnimList.size()>0){
			for(int i=0;i<waitAnimList.size();i++){
				for(Animator am : waitAnimList.get(i).getChildAnimations()){
					am.end();	
				}
			}
		}
	}

}