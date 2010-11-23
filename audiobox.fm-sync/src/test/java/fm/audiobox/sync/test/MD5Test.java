package fm.audiobox.sync.test;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.sync.interfaces.ThreadListener;
import fm.audiobox.sync.task.MD5;
import fm.audiobox.sync.test.mocks.fixtures.Fixtures;
import fm.audiobox.sync.util.AsyncTask;

public class MD5Test extends junit.framework.TestCase {
    
    private static Logger logger = LoggerFactory.getLogger(MD5Test.class);
    
    @Test
    public void testApp() {
        File file = new File(Fixtures.get( Fixtures.TEST_FILE ));
        
        MD5 task = new MD5( file );
        task.setThreadListener(new ThreadListener() {
			
			@Override
			public void onStop(AsyncTask task) {
			    logger.info( "Stop" );
			}
			
			@Override
			public boolean onStart(AsyncTask result) {
			    logger.info( "Start" );
				return true;
			}
			
			@Override
			public void onProgress(AsyncTask result, long total, long completed,long remaining, Object item) {
			    logger.info( "Progress: " + (  ( 100 * completed ) / total ) );
			}
			
			@Override
			public void onComplete(AsyncTask result, Object item) {
			    logger.info( "Complete: " + item );
			}
		});
        
        logger.info( "MD5 hash: " + task.digest() );
        
    }

}
