/**
 * 
 */
package fm.audiobox.core.test;


import java.net.SocketException;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.interfaces.CollectionListener;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.AudioBoxClient;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.UserFixture;

/**
 * @author keytwo
 *
 */
public class CollectionListenerTest extends junit.framework.TestCase {

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

            CollectionListener cl1 = new CollectionListener() {
                public void onItemReady(int index, Object item) { 
                    System.out.println("Playlist item ready: " + item ); 
                }
                public void onCollectionReady(int message, Object result) { 
                    System.out.println("Playlists collection ready: " + message );
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
            user = abc.login( UserFixture.LOGIN , UserFixture.RIGHT_PASS );
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        }

    }
}
