package com.markjmind.sample.propose.estory.page;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.markjmind.propose.MotionInitor;
import com.markjmind.propose.Propose;
import com.markjmind.propose.ProposeTouchListener;
import com.markjmind.propose.Propose.ProposeListener;
import com.markjmind.sample.propose.estory.R;
import com.markjmind.sample.propose.estory.book.Page;
import com.markjmind.sample.propose.estory.book.RatioFrameLayout;
import com.markjmind.sample.propose.estory.common.FolioListener;
import com.markjmind.sample.propose.estory.common.FolioUnit;
import com.markjmind.sample.propose.estory.common.MultiMotionAnimator;
import com.markjmind.sample.propose.estory.common.UnitAnimation;

public class Page3 extends Page{
	FolioUnit girl;
	
	public Page3(Context context, int layout_id) {
		super(context, layout_id);
	}

	@Override
	public void initAnimation(int index, ViewGroup pageView, Page page1, Page page2) {
		final RatioFrameLayout scale_layout1 = (RatioFrameLayout)pageView.findViewById(R.id.scale_layout);
		final RatioFrameLayout scale_layout2 = (RatioFrameLayout)page1.getView().findViewById(R.id.scale_layout);
	
		//문 애니메이션
		final ImageView door = (ImageView)scale_layout1.findViewById(R.id.door2);
		Propose motion_door = new Propose(scale_layout1.getContext());
		ObjectAnimator door_anim = ObjectAnimator.ofFloat(door, View.ROTATION_Y, 0,-180);
		door.setPivotX(0f);
		door.setCameraDistance(3000 * Propose.getDensity(getContext()));
		door_anim.setDuration(700);
		motion_door.setMotionInitor(new MotionInitor() {
			@Override
			public void touchDown(Propose jwm) {
				int distance = door.getWidth()*2;
				jwm.motionLeft.setMotionDistance(distance);
			}
			@Override
			public void touchUp(Propose jwm) {
			}
		});
		motion_door.motionLeft.play(door_anim);
		putPageMotion(motion_door,"door2");
		door.setOnTouchListener(motion_door);
		
		//mouse 애니메이션
		FolioUnit mouse = new FolioUnit(scale_layout2, scale_layout1);
		mouse.isFaceForwad = false;
		mouse.distanceRatio = 0.5f;
		putPageMotion(mouse.getMotions(),"mouse");
		mouse.setAnimation("mouse",R.id.mouse, View.TRANSLATION_Y, View.TRANSLATION_X);
		mouse.setMoveAnimation(new UnitAnimation() {
			@Override
			public AnimatorSet getAnimation(int index, View person) {
		  		ObjectAnimator shake = ObjectAnimator.ofFloat(person.findViewById(R.id.anim_img), 
		  				View.ROTATION, 0,20,0,-10,0);
		  		ObjectAnimator jump = ObjectAnimator.ofFloat(person.findViewById(R.id.anim_img), View.TRANSLATION_Y, 0,10,0);
		  		jump.setDuration(100);
		  		shake.setDuration(100);
		  		AnimatorSet walk = new AnimatorSet();
		  		jump.setRepeatCount(ObjectAnimator.INFINITE);
		  		shake.setRepeatCount(ObjectAnimator.INFINITE);
		  		walk.play(shake).with(jump);
		  		return walk;
			}
		});
		mouse.setFolioListener(new FolioListener() {
			@Override
			public void onTouch(boolean isMotionStart) {
				
			}
			@Override
			public void onTouchUp(boolean isMotionStart) {
			}
			@Override
			public void onStart() {
				playSound(R.raw.mouse, false);
			}
			@Override
			public void onEnd() {
			}
		});
		
		
		//자동차 애니메이션
		MultiMotionAnimator carAnim = new MultiMotionAnimator(scale_layout2,scale_layout1) {
			int[] pageWidth ={0,0};
			@Override
			public void play(int index, Propose motion, ObjectAnimator[] anims) {
				anims[0].setDuration(2000);
				motion.motionLeft.play(anims[0]);
				motion.motionLeft.enableTabUp(false);
				if(index==0){
					motion.setProposeTouchListener(new ProposeTouchListener() {
						@Override
						public void actionDown(boolean isMotionStart) {
							Log.e("test","soundPlay!!");
							playSound(R.raw.car, false);
						}
						@Override
						public void actionMove(boolean isMotionStart) {
						}
						@Override
						public void actionUp(boolean isMotionStart) {
						}
						
					});
					motion.setOnMotionListener(new ProposeListener() {
						@Override
						public void onStart() {
						}
						@Override
						public void onScroll(int Direction, long currDuration, long totalDuration) {
						}
						@Override
						public void onEnd() {
						}
					});
				}
			}
			@Override
			public void touchDown(int index, ViewGroup[] parents, Propose motion, ObjectAnimator[] anims) {
				if(pageWidth[index]!=parents[0].getWidth()+parents[1].getWidth()){
					pageWidth[index] = parents[0].getWidth()+parents[1].getWidth();
					View cars = getViews("car")[index];
					float start = RatioFrameLayout.getTagChildXY(cars)[0]*((RatioFrameLayout)parents[0]).getFractionX();
					float 	end = 0f;
					if(index==1){
						end = parents[0].getWidth()*-1;
					}
					anims[0].setFloatValues(start,end);
					motion.motionLeft.setMotionDistance(Math.abs(start-end));
				}
			}
			@Override
			public void touchUp(int index, ViewGroup[] parents, Propose motion, ObjectAnimator[] anims) {
			}
		};
		carAnim.addView("car",R.id.car);
		carAnim.loadOfFloat("car", View.TRANSLATION_X);
		carAnim.setMultyOnTouch("car");
		putPageMotion(carAnim.getMotions(),"car");
		
		
		//girl 애니메이션
		girl = new FolioUnit(scale_layout2, scale_layout1);
		girl.isFaceForwad = false;
		girl.setDuration(5000);
		girl.setStartHeight(100);
		putPageMotion(girl.getMotions(),"girl");
		girl.setAnimation("girl",R.id.girl, View.TRANSLATION_Y, View.TRANSLATION_X);
		girl.setMoveAnimation(new UnitAnimation() {
			@Override
			public AnimatorSet getAnimation(int index, View person) {
				View unit = person.findViewById(R.id.anim_img);
				Log.e("test","무부");
		  		ObjectAnimator shake = ObjectAnimator.ofFloat(unit, 
		  				View.ROTATION, 0,-10,0,10,0);
		  		ObjectAnimator jump = ObjectAnimator.ofFloat(unit,
		  				View.TRANSLATION_Y, 0,10,0);
		  		jump.setDuration(500);
		  		shake.setDuration(500);
		  		AnimatorSet walk = new AnimatorSet();
		  		jump.setRepeatCount(ObjectAnimator.INFINITE);
		  		shake.setRepeatCount(ObjectAnimator.INFINITE);
		  		walk.play(shake).with(jump);
		  		return walk;
			}
		});
		girl.setWaitAnimation(new UnitAnimation() {
			@Override
			public AnimatorSet getAnimation(int index, View person) {
				View unit = person.findViewById(R.id.anim_img);
				float startRotation = unit.getRotationY();
				ObjectAnimator rotation = ObjectAnimator.ofFloat(unit, View.ROTATION_Y, 0,30,0,-30,0);
	            rotation.setDuration(2000);
	            AnimatorSet set = new AnimatorSet();
	            set.play(rotation);
	            rotation.setRepeatCount(ObjectAnimator.INFINITE);
				return set;
			}
		});
		girl.setFolioListener(new FolioListener() {
			@Override
			public void onTouch(boolean isMotionStart) {
				playSound(R.raw.bells, false);
			}
			@Override
			public void onTouchUp(boolean isMotionStart) {
			}
			@Override
			public void onStart() {
			}
			@Override
			public void onEnd() {
			}
		});
		
		girl.startWaitAnimation();
		
		
	}

	@Override
	public void dispose() {
		if(girl!=null){
			girl.stopWaitAnimation();
		}
	}
}
