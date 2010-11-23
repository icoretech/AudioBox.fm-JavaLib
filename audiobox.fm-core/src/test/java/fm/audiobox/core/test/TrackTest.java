/**
 * 
 */
package fm.audiobox.core.test;


import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.entity.mime.content.FileBody;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.core.test.mocks.models.Album;

/**
 * @author keytwo
 *
 */
public class TrackTest extends junit.framework.TestCase {

    StaticAudioBox abc;
    User user;
    Albums albums;
    Fixtures fx = new Fixtures();

    @Before
    public void setUp() throws Exception {
        StaticAudioBox.setModelClassFor( StaticAudioBox.ALBUM_KEY, Album.class );
        abc = new StaticAudioBox();
        user = abc.login( fx.get( Fixtures.LOGIN ), fx.get( Fixtures.RIGHT_PASS ) );
    }


    @Test
    public void testTrack() {
    	
        assertNotNull( abc );
        assertNotNull( user );
        
        loginCatched();
        
        try {

            FileBody fb = new FileBody( new File(System.getProperty("user.home") + System.getProperty("path.separator") + "TEST_FILE.mp3") );
            Track trfb = new Track(fb);
            assertNotNull(trfb);
            assertSame( fb, trfb.getFileBody() );
            
            String[] tracks = user.getUploadedTracks();

            assertNotNull( tracks );
            assertTrue( tracks.length > 0);


            Playlists pls = user.getPlaylists();
            assertNotNull( pls );

            Playlist pl = pls.getPlaylistByName("development");
            assertNotNull( pl );

            Tracks trs = pl.getTracks();
            assertNotNull( trs );

            Track testTr = null;
            
            for (Track tr : trs.getCollection()) {

                assertNotNull( tr );
                
                assertNotNull( tr.getToken() );

                assertNotNull( tr.isLoved() );

                assertNotNull( tr.getAlbum() );
                assertNotNull( tr.getArtist() );
                assertNotNull( tr.getAudioFileSize() );
                assertNotNull( tr.getDuration() );
                assertNotNull( tr.getDurationInSeconds() );
                assertNotNull( tr.getEndPoint() );
                assertNotNull( tr.getPlayCount() );
                assertNotNull( tr.getYear() );
                assertNull( tr.getFileBody() );
                assertNotNull( tr.getName() );
                assertTrue( Track.State.IDLE == tr.getState() );
                assertNotNull( tr.getTitle() );
                assertNotNull( tr.getTrackNumber() );
                assertNotNull( tr.getTrack( tr.getToken() ) );
                assertSame( tr, tr.getTrack( tr.getToken() ) );
                assertNull( tr.getTrack( "aaa" ) );
                assertNull( tr.getTracks() );
                assertNotNull( tr.getDiscNumber() );
                assertNotNull( tr.getOriginalFileName() );
                
                assertNotNull( tr.getStreamUrl() );
                assertNotNull( tr.getStreamUrl( true ) );
                
                testTr = tr;
            }

            // Fullfil test coverage
            try {
               testTr.upload();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            
            assertNotNull( testTr.getStreamUrl() );
            
            assertNull( testTr.getFileHash() );
            testTr.setFileHash( "abcasddsad");
            assertNotNull( testTr.getFileHash() );
            
            assertFalse( testTr.hasErrors() );
            testTr.setState( Track.State.ERROR );
            assertTrue( testTr.hasErrors() );
            
            assertFalse( testTr.isBuffering() );
            testTr.setState( Track.State.BUFFERING );
            assertTrue( testTr.isBuffering() );
            
            assertFalse( testTr.isPaused() );
            testTr.setState( Track.State.PAUSED );
            assertTrue( testTr.isPaused() );
            
            assertFalse( testTr.isPlaying() );
            testTr.setState( Track.State.PLAYING );
            assertTrue( testTr.isPlaying() );
            
            if (testTr.isLoved()) {
                testTr.setLoved(false);
                assertFalse( testTr.isLoved() );
            } else {
                testTr.setLoved(true);
                assertTrue( testTr.isLoved() );
            }
            
            /* TODO: Restore this
            if (testTr.isLoved()) {
                assertTrue( testTr.unlove() );
                assertFalse( testTr.isLoved() );
                assertTrue( testTr.love() ); // restore previous state
            } else {
                assertTrue( testTr.love() );
                assertTrue( testTr.isLoved() );
                assertTrue( testTr.unlove() ); // restore previous state
            }
            */
            int playcount = testTr.getPlayCount();
            testTr.setPlayCount(playcount + 1);
            assertTrue( testTr.getPlayCount() == ( playcount + 1) );
            
            
            
            Track tr = user.getTrackByToken( testTr.getToken() );
            
            assertNotNull(tr);
            
            assertEquals(tr.getToken(), testTr.getToken());
            assertNotSame(tr, testTr);
            
            String uuid = testTr.getToken();
            
            Track track = trs.get(uuid);
            assertNotNull( track );
            assertSame(testTr, track);
            
            
            try {
                Track t = user.getTrackByToken( "aaa ");
                assertNull( t );
            } catch( ServiceException e ) {
                assertEquals( HttpStatus.SC_NOT_FOUND, e.getErrorCode() );
            }
            
            List<Track> list = new ArrayList<Track>();
            trs.setCollection( list );
            
            assertNotNull( trs.getCollection() );
            assertSame( list, trs.getCollection() );
            
            track = trs.get(uuid);
            assertNull( track );

        } catch (LoginException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (ServiceException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (ModelException e) {
            e.printStackTrace();
            fail(e.getMessage());
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
