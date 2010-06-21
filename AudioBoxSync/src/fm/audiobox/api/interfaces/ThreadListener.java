package fm.audiobox.api.interfaces;

import fm.audiobox.util.ThreadItem;

public interface ThreadListener {

	public void onStart( ThreadItem result );
	public void onProgress(ThreadItem result, long total, long completed, long remaining, Object item);
	public void onComplete( ThreadItem result , Object item);
	
}
