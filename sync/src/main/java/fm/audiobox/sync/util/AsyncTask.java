package fm.audiobox.sync.util;


import java.util.HashMap;
import java.util.Map;

import fm.audiobox.sync.interfaces.ThreadListener;

public abstract class AsyncTask implements Runnable {

    protected Map<String, Object> properties = new HashMap<String,Object>();
    private AsyncTaskManager threadManager = null;

    private ThreadListener threadListener = new ThreadListener() {
        @Override
        public boolean onStart(AsyncTask result) {return true;}

        @Override
        public void onProgress(AsyncTask result, long total, long completed, long remaining, Object item) {}

        @Override
        public void onComplete(AsyncTask result , Object item) {}
    };


    protected final void setManager( AsyncTaskManager tm ){
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
    protected abstract void doTask();
    protected abstract Object end();


    @Override
    public final void run() {
        this.threadManager.onStart( this );
        if ( this.threadListener.onStart( this ) ){
            this.start();

            this.doTask();

            Object result = this.end();
            this.threadListener.onComplete(this, result);
        } else
            this.threadListener.onComplete(this, null);

        this.threadManager.onComplete( this );

    }


}
