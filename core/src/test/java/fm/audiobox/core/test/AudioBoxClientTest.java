/**
 * 
 */
package fm.audiobox.core.test;


import java.net.SocketException;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.api.Model;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.AudioBoxClient;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.UserFixture;

/**
 * @author keytwo
 *
 */
public class AudioBoxClientTest extends junit.framework.TestCase {

    AudioBoxClient abc;
    User user;
    Albums albums;

    @SuppressWarnings("deprecation")
    @Before
    public void setUp() throws Exception {
        abc = new AudioBoxClient();
        abc.setForceTrust(true);
    }


    @Test
    public void testCL() {
        
        assertNotNull( abc );
        loginCatched();
        assertNotNull( user );
        
        try {
            
            Track tr = (Track) AudioBoxClient.getModelInstance(AudioBoxClient.TRACK_KEY, null);
            assertTrue(tr instanceof Track);
            
            AudioBoxClient.getModelInstance("ABC", null);
            
        } catch (ModelException e) {
            assertEquals( ModelException.CLASS_NOT_FOUND, e.getErrorCode());
        }
        
        
        try {
            
            AudioBoxClient.setModelClassFor(AudioBoxClient.TRACK_KEY, TestModel.class);
            TestModel tm = (TestModel) AudioBoxClient.getModelInstance(AudioBoxClient.TRACK_KEY, null);
            assertTrue(tm instanceof TestModel);
            
            
        } catch (ModelException e) {
            assertEquals( ModelException.INSTANTIATION_FAILED, e.getErrorCode());
        }
    }


    private void loginCatched() {
        try {
            user = abc.login( UserFixture.LOGIN , UserFixture.RIGHT_PASS );
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        }

    }
    
    private abstract class TestModel extends Model {
        
    }
}
