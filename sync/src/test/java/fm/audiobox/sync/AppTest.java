package fm.audiobox.sync;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.AudioBoxClient;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.TrackFixture;
import fm.audiobox.core.test.mocks.fixtures.UserFixture;
import fm.audiobox.sync.task.Upload;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName ) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    @SuppressWarnings("deprecation")
    public void testApp() {
        
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire.header", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.fm.audiobox.core", "debug");
        
        try {
            AudioBoxClient abc = new AudioBoxClient();
            abc.setForceTrust(true);
            
            User user = abc.login(UserFixture.LOGIN, UserFixture.RIGHT_PASS);
            assertNotNull( user );
            
            File f = new File(TrackFixture.TEST_FILE);
            assertNotNull( f );
            assertTrue( f.exists() );
            
            Upload up = new Upload( f, abc );
            assertNotNull( up );
            
            String uuid = up.upload();
            
            assertNotNull( uuid );
            assertTrue( uuid.length() > 0 );
            
            Track tr = user.getTrackByUuid( uuid );
            
            assertNotNull( tr );
            
            
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        }
        
    }
}
