package fm.audiobox.util;

import java.util.*;

import fm.audiobox.api.interfaces.ThreadListener;

public abstract class ThreadItem implements Runnable{

	protected Map<String, Object> properties = new HashMap<String,Object>();
	private ThreadListener threadListener = new ThreadListener() {
		@Override
		public void onStart(ThreadItem result) {}
		
		@Override
		public void onComplete(ThreadItem result) {}
	};
	
	
	protected void setThreadListener(ThreadListener tl){
		this.threadListener = tl;
	}
	
	public void setProperty(String key, Object value){
		properties.put( key, value );
	}
	public Object getProperty(String key){
		return properties.get( key );
	}
	
	protected abstract void start();
	protected abstract void _run();
	protected abstract void end();
	
	
	@Override
	public final void run() {
		// TODO Auto-generated method stub
		
		this.threadListener.onStart( this );
		this.start();
		
		this._run();
		
		this.end();
		this.threadListener.onComplete(this);
		
		
	}
	
	
	
	
	
}
