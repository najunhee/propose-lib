package com.markjmind.sample.propose.paper;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.propose.Propose;

public class MainActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		ViewGroup move_lyt = (ViewGroup)findViewById(R.id.move_lyt);
		ViewGroup book_lyt = (ViewGroup)findViewById(R.id.book_lyt);
		
		ObjectAnimator paperAnim = ObjectAnimator.ofFloat(move_lyt, View.ROTATION_Y, 0,180);
		ObjectAnimator paperUpDown = ObjectAnimator.ofFloat(move_lyt, View.ROTATION_X, -50,50);
		ObjectAnimator bookUpDown = ObjectAnimator.ofFloat(book_lyt, View.ROTATION_X, -50,50);
		move_lyt.setCameraDistance(Propose.getCameraDistanceY(this)*5);
		book_lyt.setCameraDistance(Propose.getCameraDistanceY(this)*5);
		
		Propose propose = new Propose(this);
		propose.motionRight.play(paperAnim);
		propose.motionUp.play(paperUpDown,500).with(bookUpDown);
		propose.motionUp.enableFling(false).enableTabUp(false).enableSingleTabUp(false).move(250);
		move_lyt.setOnTouchListener(propose);
	}
	
	
}
