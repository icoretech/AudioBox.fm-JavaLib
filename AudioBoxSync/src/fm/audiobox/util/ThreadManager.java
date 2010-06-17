package fm.audiobox.util;

import java.util.*;

import fm.audiobox.api.interfaces.ThreadListener;

public class ThreadManager {

	private int MAX = 4;
	private int current_thread_index = -1;
	private int started_thread = 0;
	private int max_threads = MAX;
	
	private List<ThreadItem> _threads = null;
	
	
	public ThreadManager(){
		this.max_threads = MAX;
	}
	
	public ThreadManager(int maxThread){
		this.max_threads = maxThread;
	}
	
	public void newThread(ThreadItem item){
		
		if ( this._threads == null ) this._threads = new ArrayList<ThreadItem>();
		
		item.setManager( this );
		this._threads.add( item );
		this.start();
	}

	
	private synchronized void start(){
		
		if ( started_thread == this.max_threads ) return;
		if ( (current_thread_index+1) >= this._threads.size() ) return;
		
		current_thread_index++;
		
		ThreadItem item = this._threads.get(current_thread_index);
		Thread thread = new Thread( item );
		
		thread.start();
		
	}

	public synchronized void onComplete(ThreadItem item) {
		started_thread--;
		this.start();
	}


	public synchronized void onStart(ThreadItem item) {
		started_thread++;
	}
	
	
	
}