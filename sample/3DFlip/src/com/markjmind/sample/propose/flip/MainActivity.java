package com.markjmind.sample.propose.flip;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.markjmind.propose.Propose;
import com.markjmind.propose.Propose.ProposeListener;

public class MainActivity extends Activity{
	
	private LinearLayout sliding_lyt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
	}
	
	
}
