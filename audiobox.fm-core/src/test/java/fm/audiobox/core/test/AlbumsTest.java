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
import fm.audiobox.core.models.Album;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.CoverUrls;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

/**
 * @author keytwo
 *
 */
public class AlbumsTest extends junit.framework.TestCase {

    StaticAudioBox abc;
    User user;
    
    @Before
    public void setUp() throws Exception {
        abc = new StaticAudioBox();
    }

    @Test
    public void testAlbumsShouldBePopulated() {
        assertNotNull( abc );
        loginCatched();
        assertNotNull( user );
        try {

            Albums albums = user.getAlbums(false);
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

        } catch (ModelException e) {
            fail( e.getMessage() );
        }
    }


    @Test
    public void testAlbumShouldBePopulatedAndContainsTracks() {
        
        assertNotNull( abc );
        loginCatched();
        assertNotNull( user );
        try {

            Albums albums = user.getAlbums(false);

            assertNotNull(albums);
            Album al = (Album) albums.get(0);
            assertNotNull(al);

            Tracks trs = (Tracks) al.getTracks();
            assertNotNull(trs);

            Track tr = (Track) trs.get(0);
            assertNotNull(tr);

        } catch (ModelException e) {
            fail( e.getMessage() );
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
