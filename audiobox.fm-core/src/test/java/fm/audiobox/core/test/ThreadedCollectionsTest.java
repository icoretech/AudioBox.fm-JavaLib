package fm.audiobox.core.test;


import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.interfaces.CollectionListener;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.Artists;
import fm.audiobox.core.models.AudioBoxClient;
import fm.audiobox.core.models.Genres;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

public class ThreadedCollectionsTest extends junit.framework.TestCase {

    private static Logger log = LoggerFactory.getLogger(ThreadedCollectionsTest.class);
    
    StaticAudioBox abc;
    User user;
    Fixtures fx = new Fixtures();
    
    @SuppressWarnings("deprecation")
    @Before
    public void setUp() throws Exception {

        abc = new StaticAudioBox();
        abc.setForceTrust(true);

        try {
            user = (User) abc.login( fx.get( Fixtures.LOGIN ), fx.get( Fixtures.RIGHT_PASS ) );
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            log.error("Service exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testPreconditions() {
        assertNotNull( abc );
        assertNotNull( user );
    }

    @Test
    public void testTrhead() throws InterruptedException  {

        final Handle h1 = new Handle();
        final Handle h2 = new Handle();
        final Handle h3 = new Handle();
        final Handle h4 = new Handle();
        
        try {

            CollectionListener cl1 = new CollectionListener() {
                Logger log = LoggerFactory.getLogger("fm.audiobox.core.test.CL1");
                public void onItemReady(int index, Object item) {  log.trace("Playlist item ready: " + item ); }
                public void onCollectionReady(int message, Object result) {  log.trace("Playlists collection ready: " + message ); h1.setDone(true); }
            };

            CollectionListener cl2 = new CollectionListener() {
                Logger log = LoggerFactory.getLogger("fm.audiobox.core.test.CL2");
                public void onItemReady(int index, Object item) { log.trace("Artist item ready: " + item ); }
                public void onCollectionReady(int message, Object result) { log.trace("Artists collection ready: " + message ); h2.setDone(true); }
            };

            CollectionListener cl3 = new CollectionListener() {
                Logger log = LoggerFactory.getLogger("fm.audiobox.core.test.CL3");
                public void onItemReady(int index, Object item) { log.trace("Genre item ready: " + item ); }
                public void onCollectionReady(int message, Object result) { log.trace("Genres collection ready: " + message ); h3.setDone(true); }
            };

            CollectionListener cl4 = new CollectionListener() {
                Logger log = LoggerFactory.getLogger("fm.audiobox.core.test.CL4");
                public void onItemReady(int index, Object item) { log.trace("Album item ready: " + item ); }
                public void onCollectionReady(int message, Object result) { log.trace("Albums collection ready: " + message ); h4.setDone(true); }
            };

            AudioBoxClient.setCollectionListenerFor(AudioBoxClient.PLAYLISTS_KEY, cl1);
            CollectionListener cl5 = AudioBoxClient.getCollectionListenerFor(AudioBoxClient.PLAYLISTS_KEY);
            assertSame(cl1, cl5);
            
            AudioBoxClient.setCollectionListenerFor(AudioBoxClient.ARTISTS_KEY, cl2);
            cl5 = AudioBoxClient.getCollectionListenerFor(AudioBoxClient.ARTISTS_KEY);
            assertSame(cl2, cl5);
            
            AudioBoxClient.setCollectionListenerFor(AudioBoxClient.GENRES_KEY, cl3);
            cl5 = AudioBoxClient.getCollectionListenerFor(AudioBoxClient.GENRES_KEY);
            assertSame(cl3, cl5);
            
            AudioBoxClient.setCollectionListenerFor(AudioBoxClient.ALBUMS_KEY, cl4);
            cl5 = AudioBoxClient.getCollectionListenerFor(AudioBoxClient.ALBUMS_KEY);
            assertSame(cl4, cl5);

            Playlists pls = user.getPlaylists(true);

            Artists ars = user.getArtists(true);

            Genres gr = user.getGenres(true);

            Albums alb = user.getAlbums(true);

            assertNotNull( pls );
            assertNotNull( ars );
            assertNotNull( gr );
            assertNotNull( alb );

            boolean success = false;
            while( !success ) {
                Thread.sleep(2000);
                success = h1.getDone() && h2.getDone() && h3.getDone() && h4.getDone();
            }
            
            assertNotNull( pls.get(0) );
            assertNotNull( ars.get(0) );
            assertNotNull( gr.get(0) );
            assertNotNull( alb.get(0) );
            
        } catch (ModelException e) {
            e.printStackTrace();
        }

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
