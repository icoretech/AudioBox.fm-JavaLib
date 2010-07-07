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
import fm.audiobox.core.models.Album;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

/**
 * @author keytwo
 *
 */
public class PlaylistsTest extends junit.framework.TestCase {

	StaticAudioBox abc;
    User user;
    Playlists playlists;
    Fixtures fx = new Fixtures();
    
    @SuppressWarnings("deprecation")
    @Before
    public void setUp() throws Exception {
    	abc = new StaticAudioBox();
        abc.setForceTrust(true);
        
        user = abc.login( fx.get( Fixtures.LOGIN ), fx.get( Fixtures.RIGHT_PASS ) );
    }

    @Test
    public void testPreconditions() {
        assertNotNull( abc );
        assertNotNull( user );
    }


    @Test
    public void testPlaylistsShouldBePopulated() {
        loginCatched();
        try {

            loadPlaylists();
            assertNotNull(playlists);
            
            Playlist playlist = null;
            
            for (Model p : playlists.getCollection()) {
                Playlist pls = (Playlist) p;
                assertNotNull(pls);
                playlist = pls;
            }

            Playlist p = (Playlist) playlists.get(playlist.getToken());
            assertNotNull( p );
            assertSame( p, playlist);
            
            List<Playlist> list = new ArrayList<Playlist>();
            playlists.setCollection( list );
            
            assertNotNull( playlists.getCollection() );
            assertSame( list, playlists.getCollection() );
            
            playlist = (Playlist) playlists.get(playlist.getToken());
            assertNull( playlist );

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
    public void testPlaylistShouldBePopulatedAndContainsTracks() throws ServiceException, LoginException, InstantiationException, IllegalAccessException, ClassNotFoundException, ModelException {
        loginCatched();

        loadPlaylists();
        assertNotNull(playlists);

        
        Playlist testPl = null;
        
        for (Playlist pl : playlists.getCollection()) {
            assertNotNull( pl );
            assertNotNull( pl.getName() );
            assertNotNull( pl.getToken() );
            assertNotNull( pl.getPlaylistType() );
            assertNotNull( pl.getPlaylistTracksCount() );
            assertNotNull( pl.getPosition() );
            testPl = pl;
        }
        
        assertNotNull( testPl ); 

        Tracks trs = (Tracks) testPl.getTracks();
        assertNotNull(trs);
        
        //trs.invoke();
        Track tr = (Track) trs.get(0);
        assertNotNull(tr);
        assertNotNull( tr.getName() );
        assertNotNull( tr.getToken() );

        assertNotNull( tr.getArtist() );
        assertNotNull( tr.getArtist().getToken());

        assertNotNull( tr.getAlbum() );
        assertNotNull( tr.getAlbum().getToken() );

        assertNotNull( ((Album) tr.getAlbum()).getArtist() );
        assertNotNull( ((Album) tr.getAlbum()).getArtist().getToken() );

    }


    private void loadPlaylists() throws ServiceException, LoginException, InstantiationException, IllegalAccessException, ClassNotFoundException, ModelException {
        playlists = (Playlists) user.getPlaylists(false);
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
