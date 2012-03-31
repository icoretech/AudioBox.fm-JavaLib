//package fm.audiobox.sync.test;
//
//import java.io.File;
//import java.io.FileFilter;
//
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import fm.audiobox.core.exceptions.LoginException;
//import fm.audiobox.core.exceptions.ModelException;
//import fm.audiobox.core.exceptions.ServiceException;
//import fm.audiobox.core.models.AudioBoxClient;
//import fm.audiobox.core.models.User;
//import fm.audiobox.sync.interfaces.ThreadListener;
//import fm.audiobox.sync.task.Scan;
//import fm.audiobox.sync.test.mocks.fixtures.Fixtures;
//import fm.audiobox.sync.util.AsyncTask;
//import fm.audiobox.sync.util.AsyncTaskManager;
//
//public class ThreadTest extends junit.framework.TestCase {
//
//    private static Logger logger = LoggerFactory.getLogger(ThreadTest.class);
//	private boolean finish = false;
//	
//	@Test
//    public void testApp() throws InterruptedException, LoginException, ServiceException, ModelException {
//        
//	    AudioBoxClient abc = new AudioBoxClient();
//        
//        User user = abc.login( Fixtures.get( Fixtures.LOGIN), Fixtures.get(Fixtures.RIGHT_PASS) );
//        assertNotNull( user );
//        
//        final String[] allowed_formats = user.getAllowedFormats();
//	    
//        AsyncTaskManager taskManager = new AsyncTaskManager(5, true);
//        assertNotNull(taskManager);
//        
//        ThreadListener tl = new ThreadListener() {
//            
//            @Override
//            public void onStop(AsyncTask task) {
//                logger.info("[ MNGR ] Stop event: " + task);
//            }
//            
//            @Override
//            public boolean onStart(AsyncTask result) {
//                logger.info("[ MNGR ] Start: " + result);
//                return true;
//            }
//            
//            @Override
//            public void onProgress(AsyncTask result, long total, long completed,long remaining, Object item) {
//                logger.info("[ MNGR ] Progress: " + result + " | total: " + total + " | completed: " + completed + " | item: " + item);
//            }
//            
//            @Override
//            public void onComplete(AsyncTask result, Object item) {
//                logger.info("[ MNGR ] Complete: " + result + " | item: " + item);
//                finish = true;
//            }
//        };
//        
//        assertNotNull(tl);
//        
//        taskManager.setThreadListener(tl);
//        assertSame(taskManager.getThreadListener(), tl);
//        
//        
//        File folder = new File( Fixtures.get( Fixtures.SCAN_FOLDER ) );
//        assertNotNull( folder );
//        assertTrue( folder.exists() );
//        assertTrue( folder.isDirectory() );
//        
//    	Scan scanner = new Scan( folder, true );
//    	
//    	FileFilter ff = new FileFilter() {
//            public boolean accept(File file) {
//                if ( ! file.isDirectory() && ! file.isHidden() )
//                    for ( String ext : allowed_formats )
//                        if ( file.getName().toLowerCase().endsWith( ext.toLowerCase() ) )
//                            return true;
//                return false;
//            }
//        };
//        assertNotNull( ff );
//
//        scanner.setFilter(ff);
//    	assertSame( ff, scanner.getFilter());
//    	
//    	ThreadListener scannerListener = new ThreadListener() {
//            
//            @Override
//            public void onStop(AsyncTask task) {
//                logger.info("[ SCAN ] Stop: " + task);
//            }
//            
//            @Override
//            public boolean onStart(AsyncTask result) {
//                logger.info("[ SCAN ] Start: " + result);
//                return true;
//            }
//            
//            @Override
//            public void onProgress(AsyncTask result, long total, long completed, long remaining, Object item) {
//                logger.info("[ SCAN ] Progress: " + result + " | total: " + total + " | completed: " + completed + " | item: " + item );
//            }
//            
//            @Override
//            public void onComplete(AsyncTask result, Object item) {
//                logger.info("[ SCAN ] Complete: " + result + " | item: " + item);
//            }
//        };
//        
//        assertNotNull( scannerListener );
//    	scanner.setThreadListener(scannerListener);
//    	assertSame(scannerListener, scanner.getThreadListener() );
//        taskManager.newThread(scanner);
//        
//        while( !finish ) { Thread.sleep( 1000 ); }
//        	
//    }
//	
//	
//}
