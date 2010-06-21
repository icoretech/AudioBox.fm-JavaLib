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
import fm.audiobox.core.models.Album;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.models.User;

/**
 * @author keytwo
 *
 */
public class PlaylistsTest extends junit.framework.TestCase {

    AudioBoxClient abc;
    User user;
    Playlists playlists;

    @Before
    public void setUp() throws Exception {
        abc = new AudioBoxClient();
        user = abc.login(UserFixture.LOGIN, UserFixture.RIGHT_PASS);
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

            Playlist al = (Playlist) playlists.get(0);
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
    public void testPlaylistShouldBePopulatedAndContainsTracks() throws ServiceException, LoginException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        loginCatched();

        loadPlaylists();
        assertNotNull(playlists);

        Playlist pl = (Playlist) playlists.get(0);
        assertNotNull(pl);
        assertNotNull( pl.getName() );
        assertNotNull( pl.getToken() );

        Tracks trs = (Tracks) pl.getTracks();
        assertNotNull(trs);
        
        trs.invoke();
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


    @After
    public void tearDown() throws Exception {

    }

    private void loadPlaylists() throws ServiceException, LoginException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        playlists = (Playlists) user.getPlaylists();
        playlists.invoke();
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
