package fm.audiobox.sync.interfaces;

import fm.audiobox.sync.util.AsyncTask;


public interface ThreadListener {

    public boolean onStart( AsyncTask result );
    public void onProgress(AsyncTask result, long total, long completed, long remaining, Object item);
    public void onComplete( AsyncTask result , Object item);

}
