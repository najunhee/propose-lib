package com.markjmind.sample.propose.estory.page;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.markjmind.propose.Propose;
import com.markjmind.propose.MotionInitor;
import com.markjmind.sample.propose.estory.R;
import com.markjmind.sample.propose.estory.book.Page;
import com.markjmind.sample.propose.estory.book.RatioFrameLayout;
import com.markjmind.sample.propose.estory.common.FolioUnit;
import com.markjmind.sample.propose.estory.common.MultiMotionAnimator;
import com.markjmind.sample.propose.estory.common.UnitAnimation;

public class Page2 extends Page{
	
	public Page2(Context context, int layout_id) {
		super(context, layout_id);
	}

	@Override
	public void initAnimation(int index, ViewGroup pageView, Page page1, Page page2) {
		final RatioFrameLayout scale_layout1 = (RatioFrameLayout)pageView.findViewById(R.id.scale_layout);
		final RatioFrameLayout scale_layout2 = (RatioFrameLayout)page2.getView().findViewById(R.id.scale_layout);
	
		//문 애니메이션
		final ImageView door1 = (ImageView)scale_layout1.findViewById(R.id.door1);
		Propose motion_door1 = new Propose(scale_layout1.getContext());
		ObjectAnimator door_anim = ObjectAnimator.ofFloat(door1, View.ROTATION_Y, 0,-180);
		door1.setPivotX(0f);
		door1.setCameraDistance(3000 * Propose.getDensity(getContext()));
		door_anim.setDuration(700);
		motion_door1.setMotionInitor(new MotionInitor() {
			@Override
			public void touchDown(Propose jwm) {
				int distance = door1.getWidth()*2;
				jwm.motionLeft.setMotionDistance(distance);
			}
			@Override
			public void touchUp(Propose jwm) {
			}
		});
		motion_door1.motionLeft.play(door_anim);
		putPageMotion(this,motion_door1,"door1");
		door1.setOnTouchListener(motion_door1);
		
		//개구리 애니메이션
		FolioUnit frog = new FolioUnit(scale_layout1, scale_layout2);
		frog.setAnimation("frog",R.id.frog, View.TRANSLATION_Y, View.TRANSLATION_X);
		putPageMotion(this,frog.getMotions(),"frog");
		frog.setMoveAnimation(new UnitAnimation() {
			@Override
			public AnimatorSet getAnimation(View person) {
		  		ObjectAnimator jump = ObjectAnimator.ofFloat(person.findViewById(R.id.anim_img), View.TRANSLATION_Y, 0,person.getHeight()/20,0,person.getHeight()/2*-1,person.getHeight()/20,0,person.getHeight()/2*-1,0);
		  		jump.setDuration(1000);
		  		jump.setRepeatCount(ObjectAnimator.INFINITE);
		  		AnimatorSet set = new AnimatorSet();
		  		set.play(jump);
		  		return set;
			}
		});
		frog.setWaitAnimation(new UnitAnimation() {
			@Override
			public AnimatorSet getAnimation(View person) {
				ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(person, "scaleX", 1.0f,1.07f,1.0f);
	            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(person, "scaleY", 1.0f, 1.03f,1.0f);
	            scaleDownX.setDuration(1500);
	            scaleDownY.setDuration(1500);
	            AnimatorSet scaleDown = new AnimatorSet();
	            scaleDown.play(scaleDownX).with(scaleDownY);
	            scaleDownX.setRepeatCount(ObjectAnimator.INFINITE);
	            scaleDownY.setRepeatCount(ObjectAnimator.INFINITE);
				return scaleDown;

			}
		});
		frog.startWaitAnimation();
		
		//boy 애니메이션
		FolioUnit boy = new FolioUnit(scale_layout1, scale_layout2);
		boy.setStartHeight(100);
		putPageMotion(this,boy.getMotions(),"boy");
		boy.setAnimation("boy",R.id.boy, View.TRANSLATION_Y, View.TRANSLATION_X);
		boy.setMoveAnimation(new UnitAnimation() {
			@Override
			public AnimatorSet getAnimation(View person) {
//		  		ObjectAnimator shake = ObjectAnimator.ofFloat(person.findViewById(R.id.anim_img), 
//		  				View.ROTATION, 0,20,0,-10,0);
//		  		ObjectAnimator jump = ObjectAnimator.ofFloat(person.findViewById(R.id.anim_img), View.TRANSLATION_Y, 0,10,0);
//		  		jump.setDuration(300);
//		  		shake.setDuration(300);
//		  		AnimatorSet walk = new AnimatorSet();
//		  		jump.setRepeatCount(ObjectAnimator.INFINITE);
//		  		shake.setRepeatCount(ObjectAnimator.INFINITE);
//		  		walk.play(shake).with(jump);
		  		View unit = person.findViewById(R.id.anim_img);
				unit.setPivotX(unit.getWidth()/2);
				unit.setPivotY(unit.getHeight()/2);
		  		ObjectAnimator shake = ObjectAnimator.ofFloat(person.findViewById(R.id.anim_img), 
		  				View.ROTATION, 0,20,0,-10,0);
		  		ObjectAnimator jump = ObjectAnimator.ofFloat(person.findViewById(R.id.anim_img),
		  				View.TRANSLATION_Y, 0,10,0);
		  		ObjectAnimator rotationY = ObjectAnimator.ofFloat(person.findViewById(R.id.anim_img), 
		  				View.ROTATION_Y, 0,5,0,-5,0);
		  		jump.setDuration(300);
		  		shake.setDuration(300);
		  		rotationY.setDuration(300);
		  		AnimatorSet walk = new AnimatorSet();
		  		jump.setRepeatCount(ObjectAnimator.INFINITE);
		  		shake.setRepeatCount(ObjectAnimator.INFINITE);
		  		rotationY.setRepeatCount(ObjectAnimator.INFINITE);
		  		walk.play(shake).with(jump).with(rotationY);
		  		
		  		return walk;
			}
		});
		
	
		
		
		boy.setWaitAnimation(new UnitAnimation() {
			@Override
			public AnimatorSet getAnimation(View person) {
				ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(person, "scaleX", 1.0f,1.07f,1.0f);
	            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(person, "scaleY", 1.0f, 1.03f,1.0f);
	            scaleDownX.setDuration(1500);
	            scaleDownY.setDuration(1500);
	            AnimatorSet scaleDown = new AnimatorSet();
	            scaleDown.play(scaleDownX).with(scaleDownY);
	            scaleDownX.setRepeatCount(ObjectAnimator.INFINITE);
	            scaleDownY.setRepeatCount(ObjectAnimator.INFINITE);
				return scaleDown;

			}
		});
		boy.startWaitAnimation();
		
		//자동차 애니메이션
		MultiMotionAnimator carAnim = new MultiMotionAnimator(scale_layout1,scale_layout2) {
			int[] pageWidth ={0,0};
			@Override
			public void play(Propose motion, ObjectAnimator[] anims) {
				anims[0].setDuration(2000);
				motion.motionLeft.play(anims[0]);
				motion.motionLeft.enableTabUp(false);
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
		putPageMotion(this,carAnim.getMotions(),"car");
	}

}
