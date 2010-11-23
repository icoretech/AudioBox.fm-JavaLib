/**
 * 
 */
package fm.audiobox.core.test;


import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.api.Model;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.CoverUrls;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.core.test.mocks.models.Album;
import fm.audiobox.core.test.mocks.models.Track;
import fm.audiobox.core.test.mocks.models.Tracks;

/**
 * @author keytwo
 *
 */
public class CustomAlbumsTest extends junit.framework.TestCase {

    StaticAudioBox abc;
    User user;
    Albums albums;
    Fixtures fx = new Fixtures();
    
    @Before
    public void setUp() throws Exception {
        StaticAudioBox.setModelClassFor( StaticAudioBox.ALBUM_KEY, Album.class );
        StaticAudioBox.setModelClassFor( StaticAudioBox.TRACKS_KEY, Tracks.class );
        StaticAudioBox.setModelClassFor( StaticAudioBox.TRACK_KEY, Track.class );
        abc = new StaticAudioBox();
    }

    @Test
    public void testAlbumsShouldBePopulated() {
        assertNotNull( abc );
        loginCatched();
        assertNotNull( user );
        try {

            loadAlbums();

            assertNotNull(albums);
            
            Album album = null;
            
            for (Model al : albums.getCollection()) {
                Album alb = (Album) al;
                assertNotNull( alb );
                assertNotNull( alb.getCoverUrls() );
                assertNotNull( alb.getArtist() );
                album = alb;
            }
            
            Album al = (Album) albums.get(album.getToken());
            assertNotNull( al );
            assertSame( al, album);
            
            CoverUrls c = al.getCoverUrls();
            assertNotNull( c.getBig() );
            assertNotNull( c.getThumb() );
            assertNotNull( c.getTiny() );
            assertNotNull( c.getTinyNormal() );
            
            List<Album> list = new ArrayList<Album>();
            albums.setCollection( list );
            
            assertNotNull( albums.getCollection() );
            assertSame( list, albums.getCollection() );
            
            al = (Album) albums.get(al.getToken());
            assertNull( al );

        } catch (LoginException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
        } catch (SocketException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
        } catch (InstantiationException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
        } catch (ModelException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
        }
    }


    @Test
    public void testAlbumShouldBePopulatedAndContainsTracks() {
        
        assertNotNull( abc );
        loginCatched();
        assertNotNull( user );
        try {

            loadAlbums();

            assertNotNull(albums);
            Album al = (Album) albums.get(0);
            assertNotNull(al);

            Tracks trs = (Tracks) al.getTracks();
            assertNotNull(trs);

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
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }


    private void loadAlbums() throws ServiceException, LoginException, InstantiationException, IllegalAccessException, ClassNotFoundException, ModelException {
        albums = (Albums) user.getAlbums(false);
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
