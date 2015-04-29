package com.markjmind.sample.propose.estory.book;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.markjmind.propose.JwAnimatorListener;
import com.markjmind.propose.JwMotion;
import com.markjmind.propose.JwMotion.JwMotionListener;
import com.markjmind.propose.JwMotionSet;
import com.markjmind.sample.propose.estory.R;

/**
 * 
 * @author 오재웅
 * @phone 010-2898-7850
 * @email markjmind@gmail.com
 * @date 2015. 4. 29.
 */
public class Book {
	private ViewGroup bookLayout;
	private Context context;
	public static int NONE = 0, LEFT = -1, RIGHT = 1;
	public int DIRECTION = 0;
	private FrameLayout left_lyt, right_lyt, left_paper, right_paper, left_page, right_page;
	private ArrayList<Page> pageList = new ArrayList<Page>();
	private ArrayList<JwMotion> block = new ArrayList<JwMotion>();
	private int currFolio = 0;

	public JwMotion leftMotion, rightMotion, upDownMotoin;
	private float cameraDistance;

	public Book(View bookLayout) {
		this.bookLayout = (ViewGroup) bookLayout;
		this.context = bookLayout.getContext();
		init();
	}

	public View findViewById(int id) {
		return this.bookLayout.findViewById(id);
	}

	private void init() {
		cameraDistance = 10000 * JwMotion.getDensity(context);
		DIRECTION = NONE;
		left_lyt = (FrameLayout) findViewById(R.id.left_lyt);
		right_lyt = (FrameLayout) findViewById(R.id.right_lyt);
		left_page = (FrameLayout) left_lyt.findViewById(R.id.page);
		right_page = (FrameLayout) right_lyt.findViewById(R.id.page);
		left_paper = (FrameLayout) findViewById(R.id.left_paper);
		right_paper = (FrameLayout) findViewById(R.id.right_paper);

		leftMotion = new JwMotion(context);
		rightMotion = new JwMotion(context);
		initUpDownAnimation();
		
	}

	public void addPage(Page page){
		pageList.add(page);
	}
	
	private void initUpDownAnimation() {
		upDownMotoin = new JwMotion(context);
		ObjectAnimator left_paper_UpDown = ObjectAnimator.ofFloat(left_paper, View.ROTATION_X, -50, 50);
		left_paper_UpDown.setDuration(700);
		ObjectAnimator right_paperUpDown = left_paper_UpDown.clone();
		right_paperUpDown.setTarget(right_paper);
		ObjectAnimator left_page_UpDown = left_paper_UpDown.clone();
		left_page_UpDown.setTarget(left_lyt);
		ObjectAnimator right_page_UpDown = left_paper_UpDown.clone();
		right_page_UpDown.setTarget(right_lyt);
		left_paper.setCameraDistance(cameraDistance);
		right_paper.setCameraDistance(cameraDistance);
		left_lyt.setCameraDistance(cameraDistance);
		right_lyt.setCameraDistance(cameraDistance);
		
//		rightMotion.motionUp.play(left_paper_UpDown, (int) (500 * rightMotion.density)).with(right_paperUpDown)
//		.with(left_page_UpDown).with(right_page_UpDown);
//		rightMotion.motionUp.enableFling(false).enableTabUp(false).enableSingleTabUp(false)
//				.move((int) (250 * rightMotion.density));
//		leftMotion.motionUp.play(left_paper_UpDown.clone(), (int) (500 * rightMotion.density)).with(right_paperUpDown.clone())
//				.with(left_page_UpDown.clone()).with(right_page_UpDown.clone());
//		leftMotion.motionUp.enableFling(false).enableTabUp(false).enableSingleTabUp(false)
//				.move((int) (250 * rightMotion.density));
		
		upDownMotoin.motionUp.play(left_paper_UpDown, (int) (500 * rightMotion.density)).with(right_paperUpDown)
		.with(left_page_UpDown).with(right_page_UpDown);
		upDownMotoin.motionUp.enableFling(false).enableTabUp(false).enableSingleTabUp(false)
				.move((int) (250 * rightMotion.density));
	}

	public void setFolio(int folio){
		left_page.removeAllViews();
		right_page.removeAllViews();
		if(folio>0){
			left_page.addView(pageList.get(folio*2-1).getPageView());
		}
		if(folio*2<pageList.size()){
			right_page.addView(pageList.get(folio*2).getPageView());
		}
		currFolio = folio;
	}
	
	
	public void setPage2(FrameLayout lyt, int index) {
		FrameLayout page = getPage(lyt);
		page.removeAllViews();
		page.addView(pageList.get(index).getPageView());
	}

	public void reloadBook(){
		initAnimation(LEFT);
		initAnimation(RIGHT);
	}
	
	public void loadBook() {
		right_lyt.post(new Runnable() {
			@Override
			public void run() {
				reloadBook();
			}
		});
	}

	private void initAnimation(int direction) {

		final int dir = direction;
		final JwMotion motion;
		final JwMotionSet motionSet;
		final FrameLayout paperLayout;
		final FrameLayout pageLayout1;
		final FrameLayout pageLayout2;
		if (direction == RIGHT) {
			motion = rightMotion;
			motionSet = motion.motionLeft;
			pageLayout1 = getPage(right_lyt);
			pageLayout2 = getPage(left_lyt);
			paperLayout = right_paper;

		} else if (direction == LEFT) {
			motion = leftMotion;
			motionSet = motion.motionRight;
			pageLayout1 = getPage(left_lyt);
			pageLayout2 = getPage(right_lyt);
			paperLayout = left_paper;
		} else {
			return;
		}
		motionSet.enableReverse(false);
		final FrameLayout paper1 = getPaper1(paperLayout);
		final FrameLayout paper2 = getPaper2(paperLayout);
		paper1.removeAllViews();
		paper2.removeAllViews();
		paperLayout.setRotationY(0);
		paper1.setOnTouchListener(null);

		if ((dir == RIGHT && pageList.size() > currFolio * 2 + dir) || (dir == LEFT && 0 <= currFolio * 2 + dir)) {
			View movePage = pageLayout1.getChildAt(0);
			final int paper_width = paperLayout.getWidth();
			final float paper_x = paperLayout.getX();

			paper1.getLayoutParams().width = movePage.getWidth();
			paper1.getLayoutParams().height = movePage.getHeight();
			paper1.setLayoutParams(paper1.getLayoutParams());
			ObjectAnimator anim1 = ObjectAnimator.ofFloat(paperLayout, View.ROTATION_Y, 0, -90 * dir);
			ObjectAnimator anim2 = ObjectAnimator.ofFloat(paperLayout, View.ROTATION_Y, 90 * dir, 0);
			if (dir == RIGHT) {
				paperLayout.setPivotX(0);
			} else {
				paperLayout.setPivotX(paper_width);
			}
			right_lyt.setPivotX(0);
			left_lyt.setPivotX(paper_width);
			paperLayout.setPivotY(paperLayout.getHeight() / 2);
			anim1.setDuration(500);
			anim2.setDuration(500);
			motionSet.play(anim1, movePage.getWidth() * 2).next(anim2);

			anim1.addListener(new JwAnimatorListener() {
				@Override
				public void onStart(Animator animation) {
				}

				@Override
				public void onReverseStart(Animator animation) {
					paper1.setVisibility(View.VISIBLE);
					paper2.setVisibility(View.GONE);
					if (dir == RIGHT) {
						paperLayout.setX(paper_x);
						paperLayout.setPivotX(0);
					} else {
						paperLayout.setX(paper_x);
						paperLayout.setPivotX(paper_width);
					}
				}

				@Override
				public void onReverseEnd(Animator animation) {

				}

				@Override
				public void onEnd(Animator animation) {
					paper1.setVisibility(View.GONE);
					paper2.setVisibility(View.VISIBLE);
					if (dir == RIGHT) {
						paperLayout.setX(paper_x - paper_width * dir);
						paperLayout.setPivotX(paper_width);
					} else {
						paperLayout.setX(paper_x - paper_width * dir);
						paperLayout.setPivotX(0);
					}

				}
			});

			motion.setOnMotionListener(new JwMotionListener() {
				@Override
				public void onStart() {
					Log.e("dsd", "currPage:" + currFolio + " dir:" + dir);

					paper2.setVisibility(View.GONE);
					moveView(pageLayout1, paper1);
					enableBlcok(false);

					if (dir == LEFT) {
						rightMotion.enableMotion(false);
						changeLayout(paper2, pageList.get(currFolio * 2 - 2).getPageView());
						if (0 <= currFolio * 2 - 3) {
							changeLayout(pageLayout1, pageList.get(currFolio * 2 - 3).getPageView());
						}
					} else {
						enableBlcok(false);
						changeLayout(paper2, pageList.get(currFolio * 2 + dir).getPageView());
						if (pageList.size() > currFolio * 2 + 2 * dir) {
							changeLayout(pageLayout1, pageList.get(currFolio * 2 + 2 * dir).getPageView());

						}

					}

				}

				@Override
				public void onScroll(int Direction, long currDuration, long totalDuration) {
				}

				@Override
				public void onEnd() {
					enableBlcok(true);
					rightMotion.enableMotion(true);
					leftMotion.enableMotion(true);
					if (JwMotionSet.STATUS.end.equals(motionSet.getStatus())) {
						currFolio += dir;
						motionSet.reset();
						paper1.removeAllViews();
						moveView(paper2, pageLayout2);
					} else {
						paper2.removeAllViews();
						moveView(paper1, pageLayout1);
					}
					paperLayout.setX(paper_x);
					paper1.setVisibility(View.VISIBLE);
					paper2.setVisibility(View.VISIBLE);
					pageLayout1.post(new Runnable() {
						@Override
						public void run() {
							reloadBook();
						}
					});
				}
			});
			
			pageLayout1.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					motion.onTouch(v, event);
					upDownMotoin.onTouch(v, event);
					return true;
				}
			});
		}
	}

	public void addBlockMotion(JwMotion blockMotion){
		block.add(blockMotion);
	}
	
	private void enableBlcok(boolean enable){
		for(int i=0;i<block.size();i++){
			block.get(i).enableMotion(enable);
		}
	}
	
	
	private void moveView(ViewGroup move, ViewGroup target) {
		View view = move.getChildAt(0);
		move.removeAllViews();
		if (view != null) {
			changeLayout(target, view);
		}

	}

	private void changeLayout(ViewGroup parents, View view) {
		parents.removeAllViews();
		parents.addView(view);
	}

	public ViewGroup getPageView(int index) {
		return pageList.get(index).getPageView();

	}

	private FrameLayout getPage(FrameLayout lyt) {
		return (FrameLayout) lyt.findViewById(R.id.page);
	}

	private FrameLayout getPaper1(FrameLayout lyt) {
		return (FrameLayout) lyt.findViewById(R.id.paper1);
	}

	private FrameLayout getPaper2(FrameLayout lyt) {
		return (FrameLayout) lyt.findViewById(R.id.paper2);
	}
}
