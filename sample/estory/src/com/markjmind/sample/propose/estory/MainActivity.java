package com.markjmind.sample.propose.estory;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.markjmind.propose.JwMotion;
import com.markjmind.propose.JwMotion.JwMotionListener;
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
				door1.setCameraDistance(3000 * JwMotion.getDensity(getContext()));
				door_anim.setDuration(700);
				motion_door1.setMotionInitor(new MotionInitor() {
					@Override
					public void init(JwMotion jwm) {
						int distance = door1.getWidth()*2;
						jwm.motionLeft.setMotionDistance(distance);
					}
				});
				motion_door1.motionLeft.play(door_anim);
				putPageMotion(this,motion_door1,"door1");
				door1.setOnTouchListener(motion_door1);
				
				//개구리 애니메이션
				Folio2Person frog = new Folio2Person(scale_layout1, scale_layout2);
				frog.setAnimation("frog",R.id.frog, View.TRANSLATION_Y, View.TRANSLATION_X);
				putPageMotion(this,frog.getMotions(),"frog");
				
				//boy 애니메이션
				Folio2Person boy = new Folio2Person(scale_layout1, scale_layout2);
				boy.setStartHeight(100);
				boy.setAnimation("boy",R.id.boy, View.TRANSLATION_Y, View.TRANSLATION_X);
				putPageMotion(this,boy.getMotions(),"boy");
				
				
				//boy 애니메이션
				Folio2Person test = new Folio2Person(scale_layout1, scale_layout2);
				test.setStartHeight(100);
				test.setAnimation("test",R.id.test, View.TRANSLATION_Y, View.TRANSLATION_X);
				putPageMotion(this,test.getMotions(),"test");
				
				
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
							float start = RatioFrameLayout.getTagChildXY(cars)[0]*((RatioFrameLayout)parents[0]).getFractionX();
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
				putPageMotion(this,carAnim.getMotions(),"car");
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
    
    private void putPageMotion(Page page, JwMotion motion, String tag){
    	 page.addInitorMotion(tag, motion);
    }
    private void putPageMotion(Page page, JwMotion motion[], String tag){
    	for(int i=0;i<motion.length;i++){
    		page.addInitorMotion(tag+i, motion[i]);
    	}
   	 
   }
    
    
    private class Folio2Person extends MultiMotionAnimator{
    	int[] pageWidth ={0,0};
		int[] pageHeight ={0,0};
		String tag;
		ObjectAnimator turnRight,turnLeft,turnRight2,turnLeft2;;
		long personDuration = 1500;
		long turnDuration = 300;
		float startPoint = 0f;
		boolean firstStart = true;
    	public Folio2Person(ViewGroup... parents){
    		super(parents);
    	}
    	
		@Override
		public void play(JwMotion motion, ObjectAnimator[] anims) {
			anims[0].setDuration(personDuration);
			anims[0].setInterpolator(null);
			anims[1].setDuration(personDuration);
			anims[1].setInterpolator(null);
			motion.motionDown.play(anims[0]);
			motion.motionDown.enableFling(false).enableTabUp(false);
			motion.motionRight.play(anims[1]);
			motion.motionRight.enableFling(false).enableTabUp(false);
			motion.setOnMotionListener(new JwMotionListener() {
				long tempDuration=0;
				long checkDuration=0;
				boolean isFirst = true;
				boolean forward = true;
				boolean tempForward = true;
				@Override
				public void onStart() {
					isFirst = true;
					checkDuration=0;
					tempDuration=0;
					
				}
				
				@Override
				public void onScroll(int Direction, long currDuration, long totalDuration) {
					if(Direction==JwMotion.DIRECTION_RIGHT){
						if(!isFirst){
							checkDuration = checkDuration+currDuration-tempDuration;
							if(!forward && checkDuration>=5){
								turnLeft.cancel();
								turnRight.start();
								turnLeft2.cancel();
								turnRight2.start();
								forward = true;
								tempForward = forward;
							}else if(forward && checkDuration<=-5){
								turnRight.cancel();
								turnLeft.start();
								turnRight2.cancel();
								turnLeft2.start();
								forward = false;
								tempForward = forward;
							}else{
								if(tempForward == forward){
									checkDuration=0;
								}
							}
						}else{
							tempForward = !forward;
						}
						isFirst = false;
						tempDuration = currDuration;
					}
				}
				@Override
				public void onEnd() {
				}
			});
		}
		@Override
		public void motionInitor(int index, ViewGroup[] parents, JwMotion motion, ObjectAnimator[] anims) {
			if(pageWidth[index]!=parents[index].getWidth() || pageHeight[index]!=parents[index].getHeight()){
				pageWidth[index] = parents[index].getWidth();
				pageHeight[index] = parents[index].getHeight();
				
				View person = getViews(tag)[index];
				float fraction = ((RatioFrameLayout)parents[index]).getFractionY();
				float heightMargin =0;
				
				heightMargin = startPoint*fraction;
				//상하 움직임
				float startY = RatioFrameLayout.getTagChildXY(person)[1]*fraction-heightMargin;
				anims[0].setFloatValues(startY,pageHeight[index]-person.getHeight());
				motion.motionDown.setMotionDistance(pageHeight[index]-startY-person.getHeight());
				
//				//좌우 움직임
				fraction = ((RatioFrameLayout)parents[0]).getFractionX();
				float startX,endX;
				if(index==0){
					startX = 0;
					endX = parents[0].getWidth()+parents[1].getWidth()-person.getWidth();
				}else{
					startX = parents[0].getWidth()*-1;
					endX = parents[1].getWidth()-person.getWidth();
				}
				anims[1].setFloatValues(startX,endX);
				motion.motionRight.setMotionDistance(Math.abs(endX-startX));
				if(firstStart){
					Log.e("check","move:");
					motion.motionRight.move(RatioFrameLayout.getTagChildXY(getViews(tag)[0])[0]*fraction);
					if(startPoint>0){
						motion.motionDown.move(heightMargin);
					}
					if(index==parents.length-1){
						firstStart = false;
					}
				}
				
				if(tag.equals("frog")){
					ObjectAnimator jump = ObjectAnimator.ofFloat(person.findViewById(R.id.anim_img), View.TRANSLATION_Y, 0,person.getHeight()/20,0,person.getHeight()/2*-1,person.getHeight()/20,0,person.getHeight()/2*-1,0,0);
					jump.setDuration(1500);
					jump.setRepeatCount(ObjectAnimator.INFINITE);
					jump.start();
				}
			}
		}
		
		public void setAnimation(String tag,int id, Property<View, Float>... property){
			this.tag = tag;
			addView(tag,id);
			View[] views = getViews(tag);
			turnRight = ObjectAnimator.ofFloat(views[0], View.ROTATION_Y, 180,0);
			turnLeft  = ObjectAnimator.ofFloat(views[0], View.ROTATION_Y, 0,180);
			turnRight.setDuration(turnDuration);
			turnLeft.setDuration(turnDuration);
			turnRight.setInterpolator(null);
			turnLeft.setInterpolator(null);
			turnRight2 = ObjectAnimator.ofFloat(views[1], View.ROTATION_Y, 180,0);
			turnLeft2  = ObjectAnimator.ofFloat(views[1], View.ROTATION_Y, 0,180);
			turnRight2.setDuration(turnDuration);
			turnLeft2.setDuration(turnDuration);
			turnRight2.setInterpolator(null);
			turnLeft2.setInterpolator(null);
			
			loadOfFloat(tag, property);
			setMultyOnTouch(tag);
		}
		
		public void setStartHeight(float point){
			this.startPoint = point;
		}
    }
    
}
