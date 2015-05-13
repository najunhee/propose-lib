package com.markjmind.sample.propose.estory;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.sample.propose.estory.book.Book;
import com.markjmind.sample.propose.estory.book.Page;
import com.markjmind.sample.propose.estory.common.LeftMenu;
import com.markjmind.sample.propose.estory.page.Page2;
import com.markjmind.sample.propose.estory.page.Page3;


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
    	book.addPage(new Page2(this,R.layout.page2));
    	
    	/** page3 */
    	book.addPage(new Page3(this,R.layout.page3));
    	
    	/** page4 */
    	book.addPage(new Page(this,R.layout.page4) {
			@Override
			public void initAnimation(int index, ViewGroup pageView, Page page1, Page page2) {}
		});
    	
    	/** page5 */
    	book.addPage(new Page(this,R.layout.page5) {
			@Override
			public void initAnimation(int index, ViewGroup pageView, Page page1, Page page2) {}
		});
    	
    	/** page6 */
    	book.addPage(new Page(this,R.layout.page6) {
			@Override
			public void initAnimation(int index, ViewGroup pageView, Page page1, Page page2) {}
		});
    
    
   	 
   }
    
   
    
    
}
