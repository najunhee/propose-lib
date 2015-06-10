package com.markjmind.sample.propose.estory.sound;

import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.AsyncTask;

public class Sound implements OnLoadCompleteListener{
	public interface AllLoadComplete{
		public void onAllComplete();
	}
	
	SoundPool soundPool;
	HashMap<Integer,SoundInfo> soundMap = new HashMap<Integer, SoundInfo>();
	HashMap<Integer,Integer> loadSoundMap = new HashMap<Integer, Integer>();
	AllLoadComplete completeListener;
	
	public Sound(AllLoadComplete loadListener){
		soundMap.clear();
		loadSoundMap.clear();
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		
		soundPool.setOnLoadCompleteListener(this);
		this.completeListener = loadListener;
	}
	
	public void addSound(int raw_id){
		SoundInfo sInfo = new SoundInfo();
		soundMap.put(raw_id, sInfo);
	}
	
	public void load(Context context){
		Iterator<Integer> iter = soundMap.keySet().iterator();
		int i=0;
		while(iter.hasNext()){
			int key = iter.next();
			int soundId = soundPool.load(context, key, 1);
			loadSoundMap.put(soundId, key);
			i++;
		}
	}
	
	public void resume(){
		soundPool.autoResume();
	}
	
	public void pause(){
		soundPool.autoPause();
	}
	
	public void play(int raw_id,boolean loop){
		int intLoop = 0;
		if(loop){
			intLoop = -1;
		}
		SoundInfo sInfo = soundMap.get(raw_id);
		soundPool.play(sInfo.getSoundId(), 1.0f, 1.0f, 10, intLoop, 1);
	}
	
	public void stop(int raw_id){
		SoundInfo info = soundMap.get(raw_id);
		if(info.isComplete){
			soundPool.stop(info.getSoundId());
		}
	}
	
	public void stopAll(){
		Iterator<Integer> iter = soundMap.keySet().iterator();
		int i=0;
		while(iter.hasNext()){
			int key = iter.next();
			stop(key);
			i++;
		}
	}
	
	public void dispose(){
		stopAll();
		soundPool.release();
	}
	
	@Override
	public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
		if(loadSoundMap.containsKey(sampleId)){
			SoundInfo info = soundMap.get(loadSoundMap.get(sampleId)); 
			info.setSoundId(sampleId);
			if(status==0){
				info.setComplete(true);
			}else{
				info.setComplete(false);
				
			}
			loadSoundMap.remove(sampleId);
		}
		if(loadSoundMap.size()==0){
			completeListener.onAllComplete();
		}
	}
	
	
	
	private class SoundInfo{
		private int soundId;
		private boolean isComplete;
		
		public SoundInfo(){
			isComplete = false;
		}

		public void setSoundId(int soundId){
			this.soundId = soundId;
		}
		
		public int getSoundId() {
			return soundId;
		}
		public void setComplete(boolean isComplete) {
			this.isComplete = isComplete;
		}
		public boolean isComplete() {
			return isComplete;
		}
	}
}
