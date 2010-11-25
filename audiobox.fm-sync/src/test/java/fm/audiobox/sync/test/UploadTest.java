package fm.audiobox.sync.test;

import java.io.File;

import org.apache.http.HttpStatus;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.AudioBoxClient;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.UploadTrack;
import fm.audiobox.core.models.User;
import fm.audiobox.core.models.AudioBoxClient.AudioBoxConnector;
import fm.audiobox.sync.task.Upload;
import fm.audiobox.sync.test.mocks.fixtures.Fixtures;

/**
 * Unit test for simple App.
 */
public class UploadTest extends junit.framework.TestCase {

    // private static Logger logger = LoggerFactory.getLogger(DownloadTest.class);

    @Test
    public void testUploadTrack() throws InterruptedException {

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

            String token = upload.upload();

            assertNotNull( token );
            assertTrue( token.length() > 0 );

            // And move to the previous playlist
            Track tr = user.getTrackByToken( token );
            while ( Integer.parseInt( tr.refresh()[ AudioBoxConnector.RESPONSE_CODE ], 10) == HttpStatus.SC_NO_CONTENT) {
                Thread.sleep( 1000 );
            }
            
            assertNotNull( tr );
            
            assertEquals(token, tr.getToken());
            
            assertTrue( user.dropTrack(tr) );
            assertTrue( user.emptyTrash() );
            
        } catch (LoginException e) {
            fail( e.getMessage() );
        } catch (ServiceException e) {
            fail( e.getMessage() );
        } catch (ModelException e) {
            fail( e.getMessage() );
        }
    }
}
