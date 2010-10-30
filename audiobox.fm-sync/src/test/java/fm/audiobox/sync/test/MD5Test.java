package fm.audiobox.sync.test;

import java.io.File;

import org.junit.Test;

import fm.audiobox.sync.interfaces.ThreadListener;
import fm.audiobox.sync.task.MD5;
import fm.audiobox.sync.test.mocks.fixtures.Fixtures;
import fm.audiobox.sync.util.AsyncTask;

public class MD5Test extends junit.framework.TestCase {
    
    Fixtures fx = new Fixtures(); 

    @Test
    public void testApp() {
        File file = new File(fx.get( Fixtures.UPLOAD_TEST_FILE ));
        
        MD5 task = new MD5( file );
        task.setThreadListener(new ThreadListener() {
			
			@Override
			public void onStop(AsyncTask task) {
				System.out.println( "Stop" );
			}
			
			@Override
			public boolean onStart(AsyncTask result) {
				System.out.println( "Start" );
				return true;
			}
			
			@Override
			public void onProgress(AsyncTask result, long total, long completed,long remaining, Object item) {
				System.out.println( "Progress: " + (  ( 100 * completed ) / total ) );
			}
			
			@Override
			public void onComplete(AsyncTask result, Object item) {
				System.out.println( "Complete: " + item );
			}
		});
        
        System.out.println( "now: " + task.digest() );
        
    }

}
