package com.markjmind.sample.propose.slidingdrawer;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.markjmind.propose.MotionInitor;
import com.markjmind.propose.Propose;
import com.markjmind.propose.Propose.ProposeListener;

@SuppressLint("NewApi")
public class MainActivity extends Activity{
	private FrameLayout lyt;
	private TextView text;
	private ImageView bg1;
	private ObjectAnimator rightAnimator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		lyt = (FrameLayout)findViewById(R.id.sling_framelayout);
		text = (TextView)findViewById(R.id.text);
		bg1 = (ImageView)findViewById(R.id.bg1);  
		
		text.setVisibility(View.GONE);
		rightAnimator = ObjectAnimator.ofFloat(lyt,View.TRANSLATION_Y,0,0);
		rightAnimator.setDuration(1000);
		
		Propose propose = new Propose(getApplicationContext());
		propose.motionDown.play(rightAnimator);
		lyt.setOnTouchListener(propose);
		
		
		propose.setMotionInitor(new MotionInitor() {
			@Override
			public void touchDown(Propose propose) {
				float height = lyt.getHeight()-50*Propose.getDensity(getApplicationContext());
				rightAnimator.setFloatValues(0,height);
				propose.motionDown.setMotionDistance(height);
			}
			@Override
			public void touchUp(Propose arg0) {}
		});
		 
		propose.setOnMotionListener(new ProposeListener() {
			@Override
			public void onStart() {
				text.setVisibility(View.VISIBLE);
				text.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in));
			}
			@Override
			public void onScroll(int Direction, long currDuration, long totalDuration) {
				text.setText(currDuration*100/totalDuration+"%");
				float alpha = (float)(totalDuration-currDuration*2)/(float)totalDuration; 
				bg1.setAlpha(alpha);
			}
			@Override
			public void onEnd() {
				text.setVisibility(View.GONE);
				text.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out));
			}
		});
	}
		
		
}
