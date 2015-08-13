package com.markjmind.eduction.propose.motion2;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.markjmind.propose.MotionListener;
import com.markjmind.propose.Propose;

public class MainActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		TextView textView1 = (TextView)findViewById(R.id.textView1);
		float moveWidth = Propose.getWindowWidth(this)-100*Propose.getDensity(this);
		
		/** create move ObjectAnimator**/
		ObjectAnimator move_anim = ObjectAnimator.ofFloat(textView1, View.TRANSLATION_X, 0f,moveWidth);
		move_anim.setDuration(2000); // PlayTime 100%
		/** create rotation ObjectAnimator**/
		ObjectAnimator rotation_anim = ObjectAnimator.ofFloat(textView1, View.ROTATION_X, 0,360);
		rotation_anim.setDuration(1000); // PlayTime 50%
		
		/** create propose, use Right motion **/
		Propose propose = new Propose(this); 
		propose.motionRight.play(move_anim).with(rotation_anim); //Left set play animator
		textView1.setOnTouchListener(propose);
		
		
		/************* Current Duration display *************/
		final TextView textView2 = (TextView)findViewById(R.id.textView2);
		propose.motionRight.setOnMotionListener(new MotionListener() {
			@Override
			public void onStart(boolean isForward) {}
			@Override
			public void onScroll(long currDuration, long totalDuration, boolean isForward) {
				textView2.setText("duration "+currDuration+"/"+totalDuration);
			}
			@Override
			public void onEnd(boolean isForward) {}
		});
		
	}
	
	
}
