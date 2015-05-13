package com.markjmind.sample.propose.estory.page;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.markjmind.propose.JwMotion;
import com.markjmind.propose.MotionInitor;
import com.markjmind.sample.propose.estory.R;
import com.markjmind.sample.propose.estory.book.Page;
import com.markjmind.sample.propose.estory.book.RatioFrameLayout;
import com.markjmind.sample.propose.estory.common.FolioUnit;
import com.markjmind.sample.propose.estory.common.UnitAnimation;

public class Page3 extends Page{

	public Page3(Context context, int layout_id) {
		super(context, layout_id);
	}

	@Override
	public void initAnimation(int index, ViewGroup pageView, Page page1, Page page2) {
		final RatioFrameLayout scale_layout1 = (RatioFrameLayout)pageView.findViewById(R.id.scale_layout);
		final RatioFrameLayout scale_layout2 = (RatioFrameLayout)page1.getView().findViewById(R.id.scale_layout);
	
		//문 애니메이션
		final ImageView door = (ImageView)scale_layout1.findViewById(R.id.door2);
		JwMotion motion_door = new JwMotion(scale_layout1.getContext());
		ObjectAnimator door_anim = ObjectAnimator.ofFloat(door, View.ROTATION_Y, 0,-180);
		door.setPivotX(0f);
		door.setCameraDistance(3000 * JwMotion.getDensity(getContext()));
		door_anim.setDuration(700);
		motion_door.setMotionInitor(new MotionInitor() {
			@Override
			public void touchDown(JwMotion jwm) {
				int distance = door.getWidth()*2;
				jwm.motionLeft.setMotionDistance(distance);
			}
			@Override
			public void touchUp(JwMotion jwm) {
			}
		});
		motion_door.motionLeft.play(door_anim);
		putPageMotion(this,motion_door,"door2");
		door.setOnTouchListener(motion_door);
		
		//mouse 애니메이션
		FolioUnit mouse = new FolioUnit(scale_layout2, scale_layout1);
		mouse.isFaceForwad = false;
		mouse.distanceRatio = 0.5f;
		putPageMotion(this,mouse.getMotions(),"mouse");
		mouse.setAnimation("mouse",R.id.mouse, View.TRANSLATION_Y, View.TRANSLATION_X);
		mouse.setMoveAnimation(new UnitAnimation() {
			@Override
			public AnimatorSet getAnimation(View person) {
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
		
		//girl 애니메이션
		FolioUnit girl = new FolioUnit(scale_layout2, scale_layout1);
		girl.isFaceForwad = false;
		girl.setStartHeight(100);
		putPageMotion(this,girl.getMotions(),"girl");
		girl.setAnimation("girl",R.id.girl, View.TRANSLATION_Y, View.TRANSLATION_X);
		girl.setMoveAnimation(new UnitAnimation() {
			@Override
			public AnimatorSet getAnimation(View person) {
		  		ObjectAnimator shake = ObjectAnimator.ofFloat(person.findViewById(R.id.anim_img), 
		  				View.ROTATION, 0,20,0,-10,0);
		  		ObjectAnimator jump = ObjectAnimator.ofFloat(person.findViewById(R.id.anim_img), View.TRANSLATION_Y, 0,10,0);
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
		girl.startWaitAnimation();
	}

}
