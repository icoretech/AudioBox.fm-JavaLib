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
//import fm.audiobox.sync.task.Scan;
//import fm.audiobox.sync.test.mocks.fixtures.Fixtures;
//
///**
// * Unit test for simple App.
// */
//public class ScanTest extends junit.framework.TestCase {
//    
//    private static Logger logger = LoggerFactory.getLogger(ScanTest.class);
//    
//    @Test
//    public void testApp() {
//        
//        assertTrue( true );
//        try {
//            
//            AudioBoxClient abc = new AudioBoxClient();
//            
//            User user = abc.login( Fixtures.get( Fixtures.LOGIN), Fixtures.get(Fixtures.RIGHT_PASS) );
//            assertNotNull( user );
//            
//            final String[] allowed_formats = user.getAllowedFormats();
//            
//            Scan scan = new Scan( new File( Fixtures.get( Fixtures.SCAN_FOLDER ) ), false, false);
//            
//            scan.setFilter(new FileFilter() {
//				public boolean accept(File file) {
//					if ( ! file.isDirectory() && ! file.isHidden() )
//						for ( String ext : allowed_formats )
//							if ( file.getName().toLowerCase().endsWith( ext.toLowerCase() ) )
//								return true;
//					return false;
//				}
//			});
//            
//            logger.info( "" + scan.scan().size() + " file was found");
//            
//            
//        } catch (LoginException e) {
//            fail(e.getMessage());
//        } catch (ServiceException e) {
//            fail(e.getMessage());
//        } catch (ModelException e) {
//            fail(e.getMessage());
//        }
//    }
//    
//}
