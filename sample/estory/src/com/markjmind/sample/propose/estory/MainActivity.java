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
import com.markjmind.sample.propose.estory.common.MultiMotionAnimator;


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
    	/** page1 */
    	book.addPage(new Page(this,R.layout.page1) {
			@Override
			public void initAnimation(int index, ViewGroup pageView, Page page1, Page page2) {}
		});
    	
    	/** page2 */
    	book.addPage(new Page(this,R.layout.page2) {
			@SuppressWarnings("unchecked")
			@Override
			public void initAnimation(int index, ViewGroup pageView, Page page1, Page page2) {
				final RatioFrameLayout scale_layout1 = (RatioFrameLayout)pageView.findViewById(R.id.scale_layout);
				final RatioFrameLayout scale_layout2 = (RatioFrameLayout)page2.getView().findViewById(R.id.scale_layout);
			
				//문 애니메이션
				final ImageView door1 = (ImageView)scale_layout1.findViewById(R.id.door1);
				JwMotion motion_door1 = new JwMotion(scale_layout1.getContext());
				ObjectAnimator door_anim = ObjectAnimator.ofFloat(door1, View.ROTATION_Y, 0,-180);
				door1.setPivotX(0f);
				door_anim.setDuration(700);
				motion_door1.setMotionInitor(new MotionInitor() {
					@Override
					public void init(JwMotion jwm) {
						int distance = door1.getWidth()*2;
						jwm.motionLeft.setMotionDistance(distance);
					}
				});
				motion_door1.motionLeft.play(door_anim);
				door1.setOnTouchListener(motion_door1);
				
				//개구리 애니메이션
				MultiMotionAnimator frogAnim = new MultiMotionAnimator(scale_layout1,scale_layout2) {
					int[] pageWidth ={0,0};
					int[] pageHeight ={0,0};
					@Override
					public void play(JwMotion motion, ObjectAnimator[] anims) {
						anims[0].setDuration(700);
						anims[0].setInterpolator(null);
						anims[1].setDuration(700);
						anims[1].setInterpolator(null);
						motion.motionDown.play(anims[0]);
						motion.motionDown.enableFling(false).enableTabUp(false);
						motion.motionRight.play(anims[1]);
						motion.motionRight.enableFling(false).enableTabUp(false);
					}
					@Override
					public void motionInitor(int index, ViewGroup[] parents, JwMotion motion, ObjectAnimator[] anims) {
						if(pageWidth[index]!=parents[index].getWidth() || pageHeight[index]!=parents[index].getHeight()){
							pageWidth[index] = parents[index].getWidth();
							pageHeight[index] = parents[index].getHeight();
							
							//상하 움직임
							View frog = getViews("frog")[index];
							float startY = RatioFrameLayout.getTagChildXY(frog)[1]*((RatioFrameLayout)parents[index]).getFractionY();
							anims[0].setFloatValues(startY,pageHeight[index]-frog.getHeight());
							motion.motionDown.setMotionDistance(pageHeight[index]-startY-frog.getHeight());
							
//							//좌우 움직임
							float startX,endX;
							if(index==0){
								startX = 0;
								endX = parents[0].getWidth()+parents[1].getWidth()-frog.getWidth();
							}else{
								startX = parents[0].getWidth()*-1;
								endX = parents[1].getWidth()-frog.getWidth();
							}
							anims[1].setFloatValues(startX,endX);
							motion.motionRight.move();
							motion.motionRight.setMotionDistance(Math.abs(endX-startX));
						}
					}
					
				};
				frogAnim.addView("frog",R.id.frog);
				frogAnim.loadOfFloat("frog", View.TRANSLATION_Y, View.TRANSLATION_X);
				frogAnim.setMultyOnTouch("frog");
				
				
				//자동차 애니메이션
				MultiMotionAnimator carAnim = new MultiMotionAnimator(scale_layout1,scale_layout2) {
					int[] pageWidth ={0,0};
					@Override
					public void play(JwMotion motion, ObjectAnimator[] anims) {
						motion.motionLeft.play(anims[0]);
						motion.motionLeft.enableSingleTabUp(false).enableTabUp(false);
					}
					@Override
					public void motionInitor(int index, ViewGroup[] parents, JwMotion motion, ObjectAnimator[] anims) {
						if(pageWidth[index]!=parents[0].getWidth()+parents[1].getWidth()){
							pageWidth[index] = parents[0].getWidth()+parents[1].getWidth();
							View cars = getViews("car")[index];
							float start = RatioFrameLayout.getTagChildXY(cars)[0]*((RatioFrameLayout)parents[index]).getFractionX();
							float 	end = 0f;
							if(index==1){
								end = parents[0].getWidth()*-1;
							}
							anims[0].setFloatValues(start,end);
							motion.motionLeft.setMotionDistance(Math.abs(start-end));
						}
					}
				};
				carAnim.addView("car",R.id.car);
				carAnim.loadOfFloat("car", View.TRANSLATION_X);
				carAnim.setMultyOnTouch("car");
				
				
			}
		});
    	
    	/** page3 */
    	book.addPage(new Page(this,R.layout.page3) {
			@Override
			public void initAnimation(int index, ViewGroup pageView, Page page1, Page page2) {
			}
		});
    	
    	/** page4 */
    	book.addPage(new Page(this,R.layout.page4) {
			@Override
			public void initAnimation(int index, ViewGroup pageView, Page page1, Page page2) {}
		});
    }
    
}
