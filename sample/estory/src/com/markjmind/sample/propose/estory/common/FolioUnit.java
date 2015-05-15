package com.markjmind.sample.propose.estory.common;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.propose.Propose;
import com.markjmind.propose.Propose.ProposeListener;
import com.markjmind.sample.propose.estory.book.RatioFrameLayout;

public class FolioUnit extends MultiMotionAnimator{
	int[] pageWidth ={0,0};
	int[] pageHeight ={0,0};
	String tag;
	ObjectAnimator turnRight,turnLeft,turnRight2,turnLeft2;
	long personDuration = 1500;
	long turnDuration = 300;
	float startPoint = 0f;
	boolean firstStart = true;
	ArrayList<AnimatorSet> waitAnimList = new ArrayList<AnimatorSet>();
	UnitAnimation waitAnim;
	
	ArrayList<AnimatorSet> moveAnimList = new ArrayList<AnimatorSet>();
	UnitAnimation moveAnim;
	
	public float distanceRatio = 1.0f;
	
	public FolioUnit(ViewGroup... parents){
		super(parents);
	}
	
	public void setWaitAnimation(UnitAnimation waitAnim){
		this.waitAnim = waitAnim;
	}
	
	public void setMoveAnimation(UnitAnimation moveAnim){
		this.moveAnim = moveAnim;
	}
	
	@Override
	public void touchDown(int index, ViewGroup[] parents, Propose motion, ObjectAnimator[] anims) {
		if(pageWidth[index]!=parents[index].getWidth() || pageHeight[index]!=parents[index].getHeight()){
			pageWidth[index] = parents[index].getWidth();
			pageHeight[index] = parents[index].getHeight();
			
			View person = getViews(tag)[index];
			float fraction = ((RatioFrameLayout)parents[index]).getFractionY();
			float heightMargin =0;
			
			heightMargin = startPoint*fraction;
			//상하 움직임
			float startY = RatioFrameLayout.getTagChildXY(person)[1]*fraction-heightMargin;
			anims[0].setFloatValues(startY,pageHeight[index]-person.getHeight());
			motion.motionDown.setMotionDistance((pageHeight[index]-startY-person.getHeight())*distanceRatio);
			
//			//좌우 움직임
			fraction = ((RatioFrameLayout)parents[0]).getFractionX();
			float startX,endX;
			if(index==0){
				startX = 0;
				endX = parents[0].getWidth()+parents[1].getWidth()-person.getWidth();
			}else{
				startX = parents[0].getWidth()*-1;
				endX = parents[1].getWidth()-person.getWidth();
			}
			anims[1].setFloatValues(startX,endX);
			motion.motionRight.setMotionDistance((Math.abs(endX-startX))*distanceRatio);
			if(firstStart){
				if(moveAnim!=null){
					moveAnimList.add(moveAnim.getAnimation(person));
				}
				motion.motionRight.move(RatioFrameLayout.getTagChildXY(getViews(tag)[0])[0]*fraction);
				if(startPoint>0){
					motion.motionDown.move(heightMargin);
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
		anims[0].setDuration(personDuration);
		anims[0].setInterpolator(null);
		anims[1].setDuration(personDuration);
		anims[1].setInterpolator(null);
		motion.motionDown.play(anims[0]);
		motion.motionDown.enableFling(false).enableTabUp(false).enableSingleTabUp(false);
		motion.motionRight.play(anims[1]);
		motion.motionRight.enableFling(false).enableTabUp(false);
		
		motion.setOnMotionListener(new ProposeListener() {
			long tempDuration=0;
			long checkDuration=0;
			boolean isFirst = true;
			boolean forward = true;
			boolean tempForward = true;
			@Override
			public void onStart() {
				isFirst = true;
				checkDuration=0;
				tempDuration=0;
				startMoveAnimation();
				stopWaitAnimation();
			}
			
			@Override
			public void onScroll(int Direction, long currDuration, long totalDuration) {
				if(Direction==Propose.DIRECTION_RIGHT){
					if(!isFirst){
						checkDuration = checkDuration+currDuration-tempDuration;
						if(!forward && checkDuration>=5){
							if(isFaceForwad){
								backTurn();
							}else{
								frontTurn();
							}
							forward = true;
							tempForward = forward;
						}else if(forward && checkDuration<=-5){
							if(isFaceForwad){
								frontTurn();
							}else{
								backTurn();
							}
							forward = false;
							tempForward = forward;
						}else{
							if(tempForward == forward){
								checkDuration=0;
							}
						}
					}else{
						tempForward = !forward;
					}
					isFirst = false;
					tempDuration = currDuration;
				}
			}
			@Override
			public void onEnd() {
				stopMoveAnimation();
				startWaitAnimation();
			}
		});
	}
	
	public boolean isFaceForwad = true;
	
	public void frontTurn(){
		turnRight.cancel();
		turnLeft.start();
		turnRight2.cancel();
		turnLeft2.start();
	}
	public void backTurn(){
		turnLeft.cancel();
		turnRight.start();
		turnLeft2.cancel();
		turnRight2.start();
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