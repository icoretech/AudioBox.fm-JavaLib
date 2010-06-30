/**
 * 
 */
package fm.audiobox.core.test;


import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.StaticAudioBox;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.UserFixture;
import fm.audiobox.core.test.mocks.models.Album;

/**
 * @author keytwo
 *
 */
public class TrackTest extends junit.framework.TestCase {

    StaticAudioBox abc;
    User user;
    Albums albums;

    @SuppressWarnings("deprecation")
    @Before
    public void setUp() throws Exception {
        StaticAudioBox.setModelClassFor( StaticAudioBox.ALBUM_KEY, Album.class );
        abc = new StaticAudioBox();
        abc.setForceTrust(true);
        user = abc.login(UserFixture.LOGIN, UserFixture.RIGHT_PASS);
    }


    @Test
    public void testTrack() {
        
        assertNotNull( abc );
        assertNotNull( user );
        
        loginCatched();
        
        try {

            String[] tracks = user.getUploadedTracks();

            assertNotNull( tracks );
            assertTrue( tracks.length > 0);


            Playlists pls = user.getPlaylists();
            assertNotNull( pls );

            Playlist pl = pls.get(0);
            assertNotNull( pl );

            Tracks trs = pl.getTracks();
            assertNotNull( trs );

            Track testTr = null;
            
            for (Track tr : trs.getCollection()) {

                // Fullfill tests report
                tr.setTracks( null );
                
                assertNotNull( tr );
                
                assertNotNull( tr.getUuid() );

                assertNotNull( tr.isLoved() );

                assertNotNull( tr.getAlbum() );
                assertNotNull( tr.getArtist() );
                assertNotNull( tr.getAudioFileSize() );
                assertNotNull( tr.getDuration() );
                assertNotNull( tr.getDurationInSeconds() );
                assertNotNull( tr.getEndPoint() );
                assertNotNull( tr.getPlayCount() );
                assertNotNull( tr.getYear() );
                assertNull( tr.getFileEntity() );
                assertNotNull( tr.getName() );
                assertTrue( Track.State.IDLE == tr.getState() );
                assertNotNull( tr.getTitle() );
                assertNotNull( tr.getTrack( tr.getUuid() ) );
                assertSame( tr, tr.getTrack( tr.getUuid() ) );
                assertNull( tr.getTrack( "aaa" ) );
                assertNull( tr.getTracks() );
                
                testTr = tr;
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
            
            
            if (testTr.isLoved()) {
                assertTrue( testTr.unlove() );
                assertFalse( testTr.isLoved() );
                assertTrue( testTr.love() ); // restore previous state
            } else {
                assertTrue( testTr.love() );
                assertTrue( testTr.isLoved() );
                assertTrue( testTr.unlove() ); // restore previous state
            }
            
            int playcount = testTr.getPlayCount();
            testTr.setPlayCount(playcount + 1);
            assertTrue( testTr.getPlayCount() == ( playcount + 1) );
            
            
            Track tr = user.getTrackByUuid( testTr.getUuid() );
            
            assertNotNull(tr);
            
            assertEquals(tr.getUuid(), testTr.getUuid());
            assertNotSame(tr, testTr);
            
            String uuid = testTr.getUuid();
            
            Track track = trs.get(uuid);
            assertNotNull( track );
            assertSame(testTr, track);
            
            
            try {
                Track t = user.getTrackByUuid( "aaa ");
                assertNull( t );
            } catch( ServiceException e ) {
                assertEquals( ServiceException.RESOURCE_NOT_FOUND, e.getErrorCode() );
            }
            
            List<Track> list = new ArrayList<Track>();
            trs.setCollection( list );
            
            assertNotNull( trs.getCollection() );
            assertSame( list, trs.getCollection() );
            
            track = trs.get(uuid);
            assertNull( track );

        } catch (LoginException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
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
