package fm.audiobox.api.interfaces;

import fm.audiobox.util.ThreadItem;

public interface ThreadListener {

	public void onStart( ThreadItem result );
	public void onComplete( ThreadItem result );
	
}
