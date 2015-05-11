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

import com.markjmind.propose.JwMotion;
import com.markjmind.propose.JwMotion.JwMotionListener;

@SuppressLint("NewApi")
public class MainActivity extends Activity{
	private FrameLayout lyt;
	private TextView text;
	private ImageView bg1;
	
	private ViewGroup lyt1,lyt2;
	private TextView test; 
	int a =0;
	JwMotion jwm;
	Pm pm;
	Paper paper;
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
		
		lyt1 = (ViewGroup)findViewById(R.id.lyt1);
		lyt2 = (ViewGroup)findViewById(R.id.lyt2);
		test = (TextView)findViewById(R.id.test);
		pm = new Pm();
		pm.page = new Page(test);
		paper = new Paper();
		test.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				a++;
				if(a%2==0){
					pm.page = paper.page;
					paper = null;
					paper = new Paper();
					lyt2.removeAllViews();
					lyt1.addView(pm.page.tx);
				}else{
					paper.page = pm.page;
					pm = null;
					pm = new Pm();
					lyt1.removeAllViews();
					lyt2.addView(paper.page.tx);
				}
			}
		});
	}
	
	class Pm{
		Page page;
	}
	
	class Paper{
		Page page;
	}
	
	class Page{
		public TextView tx=null;
		public Page(TextView tx){
			this.tx = tx;
		}
	}
}
