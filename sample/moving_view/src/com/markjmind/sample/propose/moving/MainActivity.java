package com.markjmind.sample.propose.moving;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.markjmind.propose.Propose;

public class MainActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		ViewGroup moving_lyt = (ViewGroup)findViewById(R.id.moving_lyt);
		ImageView lion_img = (ImageView )findViewById(R.id.lion_img);
		float end = (300-70)*Propose.getDensity(this);
		
		ObjectAnimator rightMove = ObjectAnimator.ofFloat(moving_lyt, View.TRANSLATION_X, 0,end);
		ObjectAnimator rightDown = ObjectAnimator.ofFloat(moving_lyt, View.TRANSLATION_Y, 0,end);
		ObjectAnimator shake = ObjectAnimator.ofFloat(lion_img,View.ROTATION, 0,-10,20,40,0,-10,20,40,0,-10,20,40,0,-10,20,40,0);
		rightMove.setInterpolator(null);
		rightDown.setInterpolator(null);
		rightMove.setDuration(1000);
		rightDown.setDuration(1000);
		shake.setDuration(1000);
		
		Propose propose = new Propose(this);
		propose.motionRight.play(rightMove,(int)(end)).with(shake);
		propose.motionDown.play(rightDown,(int)(end));
		moving_lyt.setOnTouchListener(propose);
		
		
		
		
		
	}
	
	
}
