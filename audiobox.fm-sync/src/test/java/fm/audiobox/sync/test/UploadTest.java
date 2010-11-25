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

    // private static Logger logger = LoggerFactory.getLogger(DownloadTest.class);

    @Test
    public void testUploadTrack() {

        try {
            
            AudioBoxClient abc = new AudioBoxClient();
            AudioBoxClient.setModelClassFor( AudioBoxClient.NEW_TRACK_KEY , UploadTrack.class );
            
            User user = abc.login( Fixtures.get( Fixtures.LOGIN), Fixtures.get(Fixtures.RIGHT_PASS) );
            assertNotNull( user );

            File f = new File( Fixtures.get( Fixtures.TEST_FILE ) );
            assertNotNull( f );
            assertTrue( f.exists() );

            Track up =  user.newTrack();
            assertNotNull( up );
            assertTrue( up instanceof UploadTrack);
            
            Upload upload = new Upload( (UploadTrack) up, f);

            String uuid = upload.upload();

            assertNotNull( uuid );
            assertTrue( uuid.length() > 0 );

            Track tr = user.getTrackByToken( uuid );
            assertNotNull( tr );


        } catch (LoginException e) {
            fail( e.getMessage() );
        } catch (ServiceException e) {
            fail( e.getMessage() );
        } catch (ModelException e) {
            fail( e.getMessage() );
        }
    }
}
