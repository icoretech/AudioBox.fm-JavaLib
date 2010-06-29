/**
 * 
 */
package fm.audiobox.core.test;


import java.net.SocketException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.StaticAudioBox;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.UserFixture;
import fm.audiobox.core.test.mocks.models.Album;
import fm.audiobox.core.test.mocks.models.Track;
import fm.audiobox.core.test.mocks.models.Tracks;

/**
 * @author keytwo
 *
 */
public class AlbumsTest extends junit.framework.TestCase {

	StaticAudioBox abc;
    User user;
    Albums albums;
    
    @SuppressWarnings("deprecation")
    @Before
    public void setUp() throws Exception {
        StaticAudioBox.initClass( StaticAudioBox.ALBUM_KEY, Album.class );
        StaticAudioBox.initClass( StaticAudioBox.TRACKS_KEY, Tracks.class );
        StaticAudioBox.initClass( StaticAudioBox.TRACK_KEY, Track.class );
        abc = new StaticAudioBox();
        abc.setForceTrust(true);
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
        } catch (ModelException e) {
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
            
            //trs.invoke();
            
            Track tr = (Track) trs.get(0);
            assertNotNull(tr.getTest());
            assertNotNull(tr);
            
            System.out.println( tr.getStreamUrl() );

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
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }
    
    
    @After
    public void tearDown() throws Exception {
        
    }
    
    private void loadAlbums() throws ServiceException, LoginException, InstantiationException, IllegalAccessException, ClassNotFoundException, ModelException {
        albums = (Albums) user.getAlbums(false);
        //albums.invoke();
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
