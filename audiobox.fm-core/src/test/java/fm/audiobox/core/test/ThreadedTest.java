package fm.audiobox.core.test;


import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Album;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.core.test.mocks.models.User;

public class ThreadedTest extends junit.framework.TestCase {

	StaticAudioBox abc;
    User user;
    
    @Before
    public void setUp() throws Exception {

        StaticAudioBox.setModelClassFor(StaticAudioBox.USER_KEY , User.class );

        abc = new StaticAudioBox();
        
        try {
            user = (User) abc.login( Fixtures.get( Fixtures.LOGIN ), Fixtures.get( Fixtures.RIGHT_PASS ) );
        } catch (LoginException e) {
            fail(e.getMessage());
        } catch (SocketException e) {
            fail(e.getMessage());
        } catch (ModelException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testThread() throws InterruptedException {
        
        assertNotNull( abc );
        assertNotNull( user );
        
        final Handle h1 = new Handle();
        final Handle h2 = new Handle();
        
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    
                    Log logger = LogFactory.getLog("Test thread #1");
                    logger.debug("Started thread #1");
                    
                    String[] tracks = user.getUploadedTracks();
                    assertNotNull(tracks);
                    assertTrue(tracks.length > 0);
                    for (int i = 0; i < tracks.length; i++)
                        assertTrue( tracks[i].length() == 32 );
                    h1.setDone(true);
                    
                    logger.debug("Ended thread #1");
                    
                } catch (LoginException e) {
                    e.printStackTrace();
                    fail( e.getMessage() );
                } catch (ServiceException e) {
                    e.printStackTrace();
                    fail( e.getMessage() );
                }
            }

        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    
                    Log logger = LogFactory.getLog("Test thread #2");
                    logger.debug("Started thread #2");
                    
                    Playlists pls = (Playlists) user.getPlaylists(false);
                    assertNotNull(pls.getCollection());
                    
                    Playlist pl = pls.getPlaylistByName("development");
                    
                    assertNotNull(pl);
                    
                    Tracks tracks = (Tracks) pl.getTracks();
                    
                    assertNotNull(tracks);
                    
                    Track tr = tracks.get(0);
                    
                    assertNotNull(tr);
                    assertNotNull( tr.getName() );
                    assertNotNull( tr.getToken() );

                    assertNotNull( tr.getArtist() );
                    assertNotNull( tr.getArtist().getToken());

                    assertNotNull( tr.getAlbum() );
                    assertNotNull( tr.getAlbum().getToken() );

                    assertNull( ((Album) tr.getAlbum()).getArtist() );
                    
                    h2.setDone(true);
                    
                    logger.debug("Ended thread #2");
                    
                } catch (ModelException e) {
                    e.printStackTrace();
                    fail( e.getMessage() );
                }
            }
        });

        t1.start();
        t2.start();
        
        while ( t1.isAlive() || t2.isAlive() ) {
            Thread.sleep(2500);
        }
        
        assertTrue( h1.getDone() && h2.getDone() );
    }

    
    private class Handle {
        
        private boolean done = false;
        
        public void setDone(boolean done) {
            this.done = done;
        }
        
        public boolean getDone() {
            return this.done;
        }
    }
}
