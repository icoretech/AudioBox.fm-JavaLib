package fm.audiobox.sync.test;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import fm.audiobox.sync.interfaces.ThreadListener;
import fm.audiobox.sync.task.Scan;
import fm.audiobox.sync.test.mocks.fixtures.Fixtures;
import fm.audiobox.sync.util.AsyncTask;
import fm.audiobox.sync.util.AsyncTaskManager;

public class ThreadTest extends junit.framework.TestCase {

	private boolean finish = false;
	
	
	@Test
    public void testApp() {
        
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire.header", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.fm.audiobox.core", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.fm.audiobox.sync", "debug");
        
        assertTrue( true );
        
        AsyncTaskManager taskManager = new AsyncTaskManager(5,true);
        
        taskManager.setThreadListener(new ThreadListener() {
			
			@Override
			public void onStop(AsyncTask task) {
				System.out.println("Stop event");
				
			}
			
			@Override
			public boolean onStart(AsyncTask result) {
				System.out.println("Start");
				return true;
			}
			
			@Override
			public void onProgress(AsyncTask result, long total, long completed,long remaining, Object item) {
				System.out.println("Progress");
			}
			
			@Override
			public void onComplete(AsyncTask result, Object item) {
				System.out.println("Complete");
				finish = true;
			}
		});
        
        File[] _files = new File( Fixtures.get( Fixtures.SCAN_FOLDER ) ).listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
        List<File> files = Arrays.asList( _files ); 
        System.out.println("Threads totali: " + files.size());
        for( File file: files){
        	Scan _scan = new Scan( file, true);
            _scan.setFilter(new FileFilter() {
    			@Override
    			public boolean accept(File pathname) {
    				return  true;
    			}
    		});
            _scan.setThreadListener(new ThreadListener() {
				
				@Override
				public void onStop(AsyncTask task) {
					System.out.println("Stop Scan");
				}
				
				@Override
				public boolean onStart(AsyncTask result) {
					System.out.println("Start Scan");
					return true;
				}
				
				@Override
				public void onProgress(AsyncTask result, long total, long completed,long remaining, Object item) {
					System.out.println("Progress Scan");
				}
				
				@Override
				public void onComplete(AsyncTask result, Object item) {
					System.out.println("\tComplete Scan");
				}
			});
            taskManager.newThread(_scan);
        }
        
        
        while( !finish){
        	System.out.println("\t\t\tWhile");
        }
        
        	
    }
	
	
}
