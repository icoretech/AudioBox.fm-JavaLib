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
import fm.audiobox.core.models.Playlists.PlaylistTypes;
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
    Fixtures fx = new Fixtures();
    
    @Before
    public void setUp() throws Exception {
    	abc = new StaticAudioBox();
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

            Playlists playlists = user.getPlaylists(false);
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

        } catch (ModelException e) {
           fail(e.getMessage());
        }
    }

    @Test
    public void testPlaylistShouldBePopulatedAndContainsTracks() throws ModelException {
        loginCatched();

        Playlists playlists = user.getPlaylists(false);
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

        assertNull( ((Album) tr.getAlbum()).getArtist() );

    }

    
    @Test
    public void testPlaylistShouldBeNullIfItDoesNotExists() throws ModelException {
        loginCatched();

        Playlists playlists = user.getPlaylists(false);
        assertNotNull(playlists);
        
        assertNull( playlists.getPlaylistByName("Not existings playlist") );
        
    }
    
    @Test
    public void testPlaylistsTypes() throws ModelException {
        loginCatched();

        Playlists playlists = user.getPlaylists(false);
        assertNotNull(playlists);
        
        List<Playlist> pls = playlists.getPlaylistsByType(PlaylistTypes.AUDIO);
        assertEquals(1, pls.size());
        
        pls = playlists.getPlaylistsByType(PlaylistTypes.NATIVE);
        assertEquals(1, pls.size());
        
        pls = playlists.getPlaylistsByType(PlaylistTypes.TRASH);
        assertEquals(1, pls.size());
        
        pls = playlists.getPlaylistsByType(PlaylistTypes.VIDEO);
        assertEquals(1, pls.size());
        
        pls = playlists.getPlaylistsByType(PlaylistTypes.CUSTOM);
        assertNotNull( pls );
    }
    
    
    @Test
    public void testMoveSingleTrackToPlaylist() throws ModelException, LoginException, ServiceException {
        loginCatched();
        
        Playlists playlists = user.getPlaylists(false);
        assertNotNull(playlists);
        
        Playlist soundtracks = playlists.getPlaylistByName("soundtracks");
        Playlist dev = playlists.getPlaylistByName("development");
        Track trk = soundtracks.getTracks().get(0);
        
        int previousTracksCount = dev.getPlaylistTracksCount();
        
        boolean result = dev.addTrack(trk);
        if (result) {
            assertEquals( previousTracksCount + 1 , dev.getPlaylistTracksCount() );
            assertNotNull( dev.getTrack( trk.getToken() ) );
        } else {
            // Fail and exit
            fail( "Unable to move track to the selected playlist" );
        }
        
        result = dev.removeTrack( trk );
        if (result) {
            assertNull( dev.getTrack( trk.getToken() ));
        } else {
            // Fail and exit
            fail( "Unable to move track to the selected playlist" );
        }
        
    }
    
    @Test
    public void testMoveMultipleTracksToPlaylist() throws ModelException, LoginException, ServiceException {
        loginCatched();
        
        Playlists playlists = user.getPlaylists(false);
        assertNotNull(playlists);
        
        Playlist soundtracks = playlists.getPlaylistByName("soundtracks");
        Playlist dev = playlists.getPlaylistByName("development");
        
        Tracks musicTracks = soundtracks.getTracks();
        
        List<Track> tracks = new ArrayList<Track>();
        int numberOfTracksToAdd = 10;
        for (int i = 0; i < numberOfTracksToAdd; i++) {
            tracks.add( musicTracks.get(i));
        }
        
        int previousTracksCount = dev.getPlaylistTracksCount();
        
        boolean result = dev.addTracks(tracks);
        if (result) {
            assertEquals( previousTracksCount + numberOfTracksToAdd, dev.getPlaylistTracksCount() );
            for (Track trk : tracks) {
                assertNotNull( dev.getTrack( trk.getToken() ) );
            }
        } else {
            // Fail and exit
            fail( "Unable to move track to the selected playlist" );
        }
        
        result = dev.removeTracks( tracks );
        if (result) {
            for (Track trk : tracks)
                assertNull( dev.getTrack( trk.getToken() ));
        } else {
            // Fail and exit
            fail( "Unable to move track to the selected playlist" );
        }
        
        
    }

    private void loginCatched() {
        try {
            user = abc.login( fx.get( Fixtures.LOGIN ), fx.get( Fixtures.RIGHT_PASS ) );
        } catch (LoginException e) {
            fail(e.getMessage());
        } catch (SocketException e) {
            fail(e.getMessage());
        } catch (ModelException e) {
            fail(e.getMessage());
        }
    }
    
}
