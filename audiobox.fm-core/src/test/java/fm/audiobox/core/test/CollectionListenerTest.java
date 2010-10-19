/**
 * 
 */
package fm.audiobox.core.test;


import java.net.SocketException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.interfaces.CollectionListener;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.AudioBoxClient;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

/**
 * @author keytwo
 *
 */
public class CollectionListenerTest extends junit.framework.TestCase {

    AudioBoxClient abc;
    User user;
    Albums albums;
    Fixtures fx = new Fixtures();
    
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

            CollectionListener cl1 = new CollectionListener() {
                
                Logger log = LoggerFactory.getLogger(getClass());
                
                public void onItemReady(int index, Object item) { 
                    log.trace("Playlist item ready: " + item ); 
                }
                public void onCollectionReady(int message, Object result) { 
                    log.trace("Playlists collection ready: " + message );
                }
            };
            
            
            CollectionListener cl2 = AudioBoxClient.getCollectionListenerFor(AudioBoxClient.PLAYLISTS_KEY);
            AudioBoxClient.setCollectionListenerFor(AudioBoxClient.PLAYLISTS_KEY, null);
            CollectionListener cl3 = AudioBoxClient.getCollectionListenerFor(AudioBoxClient.PLAYLISTS_KEY);
            
            assertSame( cl2, cl3 );
            
            AudioBoxClient.setCollectionListenerFor(AudioBoxClient.PLAYLISTS_KEY, cl1);
            assertNotNull(AudioBoxClient.getCollectionListenerFor(AudioBoxClient.PLAYLISTS_KEY) );
            assertSame( cl1, AudioBoxClient.getCollectionListenerFor(AudioBoxClient.PLAYLISTS_KEY) );

            Playlists pls = user.getPlaylists();
            assertNotNull( pls );
            
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }


    private void loginCatched() {
        try {
            user = abc.login( fx.get( Fixtures.LOGIN ), fx.get( Fixtures.RIGHT_PASS ) );
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        }

    }
}
