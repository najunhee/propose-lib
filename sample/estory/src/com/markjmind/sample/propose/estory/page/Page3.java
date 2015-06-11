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
import com.markjmind.sample.propose.estory.R;
import com.markjmind.sample.propose.estory.book.Page;
import com.markjmind.sample.propose.estory.book.RatioFrameLayout;
import com.markjmind.sample.propose.estory.common.FolioUnit;
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
		girl = new FolioUnit(scale_layout2, scale_layout1);
		girl.isFaceForwad = false;
		girl.setDuration(5000);
		girl.setStartHeight(100);
		putPageMotion(girl.getMotions(),"girl");
		girl.setAnimation("girl",R.id.girl, View.TRANSLATION_Y, View.TRANSLATION_X);
		girl.setMoveAnimation(new UnitAnimation() {
			@Override
			public AnimatorSet getAnimation(View person) {
				View unit = person.findViewById(R.id.anim_img);
				unit.setPivotX(unit.getWidth()/2);
				unit.setPivotY(unit.getHeight()/2);
		  		ObjectAnimator shake = ObjectAnimator.ofFloat(person.findViewById(R.id.anim_img), 
		  				View.ROTATION, 0,-10,0,10,0);
		  		ObjectAnimator jump = ObjectAnimator.ofFloat(person.findViewById(R.id.anim_img),
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
			public AnimatorSet getAnimation(View person) {
				float startRotation = person.getRotationY();
				Log.e("teset","startRotation:"+startRotation);
				ObjectAnimator rotation = ObjectAnimator.ofFloat(person, View.ROTATION_Y, startRotation,30+startRotation,startRotation,-30+startRotation,startRotation);
	            rotation.setDuration(2000);
	            AnimatorSet set = new AnimatorSet();
	            set.play(rotation);
	            rotation.setRepeatCount(ObjectAnimator.INFINITE);
				return set;
			}
		});
		Log.e("init","init 호출");
		girl.startWaitAnimation();
	}

	@Override
	public void dispose() {
		if(girl!=null){
			girl.stopWaitAnimation();
		}
	}
}
