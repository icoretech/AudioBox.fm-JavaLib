/**
 * 
 */
package fm.audiobox.core.test;


import java.net.SocketException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.AudioBox;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.interfaces.CollectionListener;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

/**
 * @author keytwo
 *
 */
public class CollectionListenerTest extends junit.framework.TestCase {

    AudioBox abc;
    User user;
    Albums albums;
    Fixtures fx = new Fixtures();
    
    @Before
    public void setUp() throws Exception {
        abc = new AudioBox();
    }


    @Test
    public void testCL() {
        
        assertNotNull( abc );
        loginCatched();
        assertNotNull( user );
        
        try {

            Playlists pls = user.getPlaylists();
            assertNotNull( pls );
            
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }


    private void loginCatched() {
        try {
            user = abc.login( Fixtures.get( Fixtures.LOGIN ), Fixtures.get( Fixtures.RIGHT_PASS ) );
        } catch (LoginException e) {
            fail(e.getMessage());
        } catch (SocketException e) {
            fail(e.getMessage());
        } catch (ModelException e) {
            fail(e.getMessage());
        }
    }
    
}
