package com.markjmind.sample.propose.slidingmenu;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.markjmind.propose.Propose;
import com.markjmind.propose.Propose.ProposeListener;

public class MainActivity extends Activity{
	
	private LinearLayout sliding_lyt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		/** Layout init **/
		sliding_lyt = (LinearLayout)findViewById(R.id.sliding_lyt);
		sliding_lyt.setClickable(true);
		
		/** Animator create **/
		float start = -200*Propose.getDensity(this);
		float end = 0;
		sliding_lyt.setX(start);
		ObjectAnimator rightAnimator = ObjectAnimator.ofFloat(sliding_lyt,View.TRANSLATION_X,start,end);
		rightAnimator.setDuration(500); /** "duration" use to onClick **/
		
		/** Propose create **/
		Propose propose = new Propose(this); 
		propose.motionRight.play(rightAnimator);		 /** set right move Animator **/
		sliding_lyt.setOnTouchListener(propose); 		 /** set touch listener **/
		propose.motionRight.setMotionDistance(-start); 	 /** set Drag Distance **/		
		
		/**set MotionListener for black Alpha**/
		propose.setOnMotionListener(new ProposeListener() {
			@Override
			public void onStart() {}
			@Override
			public void onScroll(int Direction, long currDuration, long totalDuration) {
				float alpha = (float)currDuration/totalDuration/2; // max 0.5
				findViewById(R.id.alpha_lyt).setAlpha(alpha); 
			}
			@Override
			public void onEnd() {}
		});
	}
	
	
}
