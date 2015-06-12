package com.markjmind.sample.propose.estory;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.sample.propose.estory.book.Book;
import com.markjmind.sample.propose.estory.common.LeftMenu;
import com.markjmind.sample.propose.estory.page.Page1;
import com.markjmind.sample.propose.estory.page.Page2;
import com.markjmind.sample.propose.estory.page.Page3;
import com.markjmind.sample.propose.estory.page.Page4;
import com.markjmind.sample.propose.estory.page.Page5;
import com.markjmind.sample.propose.estory.page.Page6;
import com.markjmind.sample.propose.estory.sound.Music;
import com.markjmind.sample.propose.estory.sound.Sound;
import com.markjmind.sample.propose.estory.sound.Sound.AllLoadComplete;


public class MainActivity extends Activity {
	Music music;
	Sound sound;
	Book book;
	LeftMenu leftMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        View book_layout = findViewById(R.id.book_layout);
        
//        initPage();
//        book.setFolio(0);
//        book.loadBook();
//        book_layout.post(new Runnable() {
//			@Override
//			public void run() {
//				leftMenu.initLeftMenu();
//			}
//		});
        book = new Book(findViewById(R.id.book_layout));
        leftMenu = new LeftMenu(book, (ViewGroup)findViewById(R.id.banner_lyt));
        music = new Music();
        music.setMusic(this,R.raw.back_music);
        music.playMusic(true);
        sound = new Sound(new AllLoadComplete() {
			@Override
			public void onAllComplete() {
				initPage();
		        book.setFolio(0);
		        book.loadBook();
				leftMenu.initLeftMenu();
			}
		});
        sound.addSound(R.raw.bells);
        sound.addSound(R.raw.car);
        sound.addSound(R.raw.frog);
        sound.addSound(R.raw.mouse);
        sound.load(this);
        book.setSound(sound);
    }
    
    
    private void initPage(){
    	/** page1 */
    	book.addPage(new Page1(this,R.layout.page1));
    	
    	/** page2 */
    	book.addPage(new Page2(this,R.layout.page2));
    	
    	/** page3 */
    	book.addPage(new Page3(this,R.layout.page3));
    	
    	/** page4 */
    	book.addPage(new Page4(this,R.layout.page4));
    	
    	/** page5 */
    	book.addPage(new Page5(this,R.layout.page5));
    	
    	/** page6 */
    	book.addPage(new Page6(this,R.layout.page6));
   	 
   }
   
    @Override
    protected void onResume() {
    	sound.resume();
    	music.playMusic(true);
    	super.onResume();
    }
    @Override
    protected void onStop() {
    	super.onStop();
    	sound.pause();
    	music.stop();
    	book.disposeAll();
    }
    
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    }
    
    @Override
    protected void onDestroy() {
    	sound.dispose();
    	music.dispose();
    	book.disposeAll();
    	super.onDestroy();
    }
    
    
}
