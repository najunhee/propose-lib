package com.markjmind.sample.propose.slidingdrawer;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.markjmind.propose.JwMotion;
import com.markjmind.propose.JwMotionListener;

@SuppressLint("NewApi")
public class MainActivity extends Activity{
	private FrameLayout lyt;
	private TextView text;
	private ImageView bg1;
	JwMotion jwm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		lyt = (FrameLayout)findViewById(R.id.sling_framelayout);
		text = (TextView)findViewById(R.id.text);
		bg1 = (ImageView)findViewById(R.id.bg1);  
		bg1.post(new Runnable() {
			@Override
			public void run() {
				text.setVisibility(View.GONE);
				jwm = new JwMotion(MainActivity.this);
				float height = lyt.getHeight()-50*jwm.density;
				ObjectAnimator rightAnimator = ObjectAnimator.ofFloat(lyt,View.TRANSLATION_Y, 0,height);
				rightAnimator.setDuration(1000);
				jwm.motionDown.play(rightAnimator,(int)height);
				lyt.setOnTouchListener(jwm);
				 
				jwm.setOnMotionListener(new JwMotionListener() {
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
		});
	}
}
