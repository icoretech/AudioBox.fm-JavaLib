/**
 * 
 */
package fm.audiobox.core;


import java.net.SocketException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.mocks.fixtures.UserFixture;
import fm.audiobox.core.mocks.models.Album;
import fm.audiobox.core.mocks.models.Track;
import fm.audiobox.core.mocks.models.Tracks;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.User;

/**
 * @author keytwo
 *
 */
public class AlbumsTest extends junit.framework.TestCase {

    AudioBoxClient abc;
    User user;
    Albums albums;
    
    @Before
    public void setUp() throws Exception {
        AudioBoxClient.setCustomModelsPackage(Album.class.getPackage().getName());
        abc = new AudioBoxClient();
        user = abc.login(UserFixture.LOGIN, UserFixture.RIGHT_PASS);
    }
    
    
    @Test
    public void testPreconditions() {
        assertNotNull( abc );
        assertNotNull( user );
    }

   
    @Test
    public void testAlbumsShouldBePopulated() {
        loginCatched();
        try {
            
            loadAlbums();
            
            assertNotNull(albums);
            Album al = (Album) albums.get(0);
            assertNotNull(al);

        } catch (LoginException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    
    @Test
    public void testAlbumShouldBePopulatedAndContainsTracks() {
        loginCatched();
        try {
            
            loadAlbums();
            
            assertNotNull(albums);
            Album al = (Album) albums.get(0);
            assertNotNull(al);
            
            Tracks trs = (Tracks) al.getTracks();
            assertNotNull(trs);
            
            trs.invoke();
            
            Track tr = (Track) trs.get(0);
            assertNotNull(tr.getTest());
            assertNotNull(tr);

        } catch (LoginException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    
    @After
    public void tearDown() throws Exception {
        
    }
    
    private void loadAlbums() throws ServiceException, LoginException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        albums = (Albums) user.getAlbums();
        albums.invoke();
    }
    
    private void loginCatched() {
        try {
            user = abc.login( UserFixture.LOGIN , UserFixture.RIGHT_PASS );
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        
    }
}
