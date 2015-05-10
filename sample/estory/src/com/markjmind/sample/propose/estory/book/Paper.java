package com.markjmind.sample.propose.estory.book;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.sample.propose.estory.R;

public class Paper {
	
	private ViewGroup parents, paperLayout;
	protected ViewGroup front, back;
	private Page frontPage, backPage;
	private int direction;
	private float paper_x;
	private int paper_width;
	
	protected Paper(ViewGroup paperLayout, int direction){
		this.paperLayout = paperLayout;
		this.front = (ViewGroup)paperLayout.findViewById(R.id.paper1);
		this.back = (ViewGroup)paperLayout.findViewById(R.id.paper2);
		this.frontPage = null;
		this.backPage = null;
		this.direction = direction;
		this.parents = (ViewGroup)paperLayout.getParent();
	}
	
	protected void initSize(){
		if(direction==Book.LEFT){
			paper_x = 0;
		}else{
			paper_x = parents.getWidth()/2;
		}
		
		paper_width = paperLayout.getWidth();
	}
	
	protected ViewGroup getPaperLayout(){
		return paperLayout;
	}
	
	protected Page getFrontPage(){
		return frontPage;
	}
	
	protected Page getBackPage(){
		return backPage;
	}
	
	protected void flip(Page frontPage, Page backPage){
		this.frontPage = frontPage;
		this.backPage = backPage;
		front.addView(this.frontPage.getView());
		back.addView(this.backPage.getView());
	}
	
	protected void showFront(){
		front.setVisibility(View.VISIBLE);
		back.setVisibility(View.GONE);

		if (direction == Book.RIGHT) {
			paperLayout.setX(paper_x);
			paperLayout.setPivotX(0);
		} else {
			paperLayout.setX(paper_x);
			paperLayout.setPivotX(paper_width);
		}
	}
	
	protected void showBack(){
		front.setVisibility(View.GONE);
		back.setVisibility(View.VISIBLE);
		
		if (direction == Book.RIGHT) {
			paperLayout.setX(paper_x - paper_width * direction);
			paperLayout.setPivotX(paper_width);
		} else {
			paperLayout.setX(paper_x - paper_width * direction);
			paperLayout.setPivotX(0);
		}
	}
	
	
	protected void clear(){
		front.removeAllViews();
		back.removeAllViews();
		this.frontPage = null;
		this.backPage = null;
		initSize();
	}
}
