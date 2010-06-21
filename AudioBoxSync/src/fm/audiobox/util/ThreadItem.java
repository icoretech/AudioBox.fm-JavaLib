package fm.audiobox.util;

import java.util.*;

import fm.audiobox.api.interfaces.ThreadListener;

public abstract class ThreadItem implements Runnable{

	protected Map<String, Object> properties = new HashMap<String,Object>();
	private ThreadManager threadManager = null;
	
	private ThreadListener threadListener = new ThreadListener() {
		@Override
		public void onStart(ThreadItem result) {}
		
		@Override
		public void onProgress(ThreadItem result, long total, long completed, long remaining, Object item) {}
		
		@Override
		public void onComplete(ThreadItem result , Object item) {}
	};
	
	
	protected final void setManager( ThreadManager tm ){
		this.threadManager = tm;
	}
	
	public void setThreadListener(ThreadListener tl){
		this.threadListener = tl;
	}
	
	public ThreadListener getThreadListener(){
		return this.threadListener;
	}
	
	public void setProperty(String key, Object value){
		properties.put( key, value );
	}
	public Object getProperty(String key){
		return properties.get( key );
	}
	
	protected abstract void start();
	protected abstract void _run();
	protected abstract Object end();
	
	
	@Override
	public final void run() {
		this.threadManager.onStart( this );
		this.threadListener.onStart( this );
		this.start();
		
		this._run();
		
		Object result = this.end();
		this.threadListener.onComplete(this, result);
		this.threadManager.onComplete( this );
		
	}
	
	
	
	
	
}
