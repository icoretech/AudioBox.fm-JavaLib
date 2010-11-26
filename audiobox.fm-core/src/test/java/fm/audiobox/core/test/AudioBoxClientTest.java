/**
 * 
 */
package fm.audiobox.core.test;


import java.net.SocketException;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.AudioBox;
import fm.audiobox.core.api.Model;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.ModelFactory;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

/**
 * @author keytwo
 *
 */
public class AudioBoxClientTest extends junit.framework.TestCase {

    AudioBox abx;
    User user;
    Albums albums;
    Fixtures fx = new Fixtures();
    
    @Before
    public void setUp() throws Exception {
        abx = new AudioBox();
    }


    @Test
    public void testModelLoading() {
        
        assertNotNull( abx );
        loginCatched();
        assertNotNull( abx.getUser() );
        
    }


    private void loginCatched() {
        try {
            user = abx.login( Fixtures.get( Fixtures.LOGIN),  Fixtures.get( Fixtures.RIGHT_PASS ) );
        } catch (LoginException e) {
            fail(e.getMessage());
        } catch (SocketException e) {
            fail(e.getMessage());
        } catch (ModelException e) {
            fail(e.getMessage());
        }
    }
}
