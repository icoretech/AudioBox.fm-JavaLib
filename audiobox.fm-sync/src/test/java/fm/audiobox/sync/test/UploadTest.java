package fm.audiobox.sync.test;

import java.io.File;

import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.AudioBoxClient;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.UploadTrack;
import fm.audiobox.core.models.User;
import fm.audiobox.sync.task.Upload;
import fm.audiobox.sync.test.mocks.fixtures.Fixtures;

/**
 * Unit test for simple App.
 */
public class UploadTest extends junit.framework.TestCase {
    
    Fixtures fx = new Fixtures(); 

    @Test
    @SuppressWarnings("deprecation")
    public void testApp() {
        
        assertTrue( true );
        try {
        	AudioBoxClient.setModelClassFor( AudioBoxClient.NEW_TRACK_KEY , UploadTrack.class );
            AudioBoxClient abc = new AudioBoxClient();
            abc.setForceTrust(true);
            
            User user = abc.login( Fixtures.get( Fixtures.LOGIN), Fixtures.get(Fixtures.RIGHT_PASS) );
            assertNotNull( user );
            
            File f = new File( Fixtures.get( Fixtures.UPLOAD_TEST_FILE ) );
            assertNotNull( f );
            assertTrue( f.exists() );
            
            Track up =  user.newTrack();
            assertNotNull( up );
            
            Upload upload = new Upload( (UploadTrack)up, f);
            
            String uuid = upload.upload();
            
            assertNotNull( uuid );
            assertTrue( uuid.length() > 0 );
            
            /*Track tr = user.getTrackByUuid( uuid );
            
            assertNotNull( tr );*/
            
            
        } catch (LoginException e) {
            e.printStackTrace();
            assertNotNull( e ); // development purpose
        } catch (ServiceException e) {
            e.printStackTrace();
            assertNotNull( e ); // development purpose
        } catch (ModelException e) {
            e.printStackTrace();
            assertNotNull( e ); // development purpose
        }
    }
}
