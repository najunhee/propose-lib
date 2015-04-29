package com.markjmind.sample.propose.estory;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.markjmind.propose.JwMotion;
import com.markjmind.propose.MotionInitor;
import com.markjmind.sample.propose.estory.book.Book;
import com.markjmind.sample.propose.estory.book.Page;
import com.markjmind.sample.propose.estory.book.RatioFrameLayout;
import com.markjmind.sample.propose.estory.common.LeftMenu;


public class MainActivity extends Activity {

	Book book;
	LeftMenu leftMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        View book_layout = findViewById(R.id.book_layout);
        book = new Book(findViewById(R.id.book_layout));
        initPage();
        book.setFolio(0);
        book.loadBook();
        book_layout.post(new Runnable() {
			@Override
			public void run() {
				leftMenu.initLeftMenu();
			}
		});
        leftMenu = new LeftMenu(book, (ViewGroup)findViewById(R.id.banner_lyt));
        
    }
    
    
    private void initPage(){
    	book.addPage(new Page(this,R.layout.page1) {
			@Override
			public void initAnimation(ViewGroup pageView) {}
		});
    	
    	book.addPage(new Page(this,R.layout.page2) {
			@Override
			public void initAnimation(ViewGroup page) {
				final RatioFrameLayout scale_layout = (RatioFrameLayout)page.findViewById(R.id.scale_layout);
				final ImageView door1 = (ImageView)scale_layout.findViewById(R.id.door1);
				JwMotion motion_door1 = new JwMotion(scale_layout.getContext());
				final ImageView frog = (ImageView)scale_layout.findViewById(R.id.frog);
				JwMotion motion_frog = new JwMotion(scale_layout.getContext());
				
				ObjectAnimator door_anim = ObjectAnimator.ofFloat(door1, View.ROTATION_Y, 0,-180);
				door1.setPivotX(0f);
				door_anim.setDuration(700);
				motion_door1.setMotionInitor(new MotionInitor() {
					@Override
					public void init(JwMotion jwm, View[] views) {
						int distance = door1.getWidth()*2;
						jwm.motionLeft.setMotionDistance(distance);
					}
				});
				motion_door1.motionLeft.play(door_anim);
				door1.setOnTouchListener(motion_door1);
				
				final ObjectAnimator frog_anim = ObjectAnimator.ofFloat(frog, View.TRANSLATION_Y, 0,500);
				frog_anim.setDuration(700);
				frog_anim.setInterpolator(null);
				motion_frog.setMotionInitor(new MotionInitor() {
					int pageWidth=0;
					int pageHeight=0;
					@Override
					public void init(JwMotion jwm, View[] views) {
						if(pageWidth!=scale_layout.getWidth() || pageHeight!=scale_layout.getHeight()){
							pageWidth = scale_layout.getWidth();
							pageHeight = scale_layout.getHeight();
							float startY = RatioFrameLayout.getTagChildXY(frog)[1]*scale_layout.getFractionY();
							frog_anim.setFloatValues(startY,pageHeight-frog.getHeight());
							jwm.motionDown.setMotionDistance(pageHeight-startY-frog.getHeight());
						}
					}
				});
				motion_frog.motionDown.play(frog_anim);
				motion_frog.motionDown.enableFling(false).enableTabUp(false);
				frog.setOnTouchListener(motion_frog);
			}
		});
    	
    	book.addPage(new Page(this,R.layout.page3) {
			@Override
			public void initAnimation(ViewGroup pageView) {
			}
		});
    	
    	book.addPage(new Page(this,R.layout.page4) {
			@Override
			public void initAnimation(ViewGroup pageView) {}
		});
    }
}
