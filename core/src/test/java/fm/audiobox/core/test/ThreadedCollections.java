package fm.audiobox.core.test;


import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.StaticAudioBox;
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
import fm.audiobox.core.test.mocks.fixtures.UserFixture;

public class ThreadedCollections extends junit.framework.TestCase {

    StaticAudioBox abc;
    User user;

    @SuppressWarnings("deprecation")
    @Before
    public void setUp() throws Exception {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire.header", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl", "debug");

        System.setProperty("org.apache.commons.logging.simplelog.log.fm.audiobox.core", "debug");

        abc = new StaticAudioBox();
        abc.setForceTrust(true);

        try {
            user = (User) abc.login( UserFixture.LOGIN , UserFixture.RIGHT_PASS );
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            System.out.println("Service exception: " + e.getMessage());
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
                public void onItemReady(int index, Object item) { 
                    System.out.println("Playlist item ready: " + item ); 
                }
                public void onCollectionReady(int message, Object result) { 
                    System.out.println("Playlists collection ready: " + message );
                    h1.setDone(true);
                }
            };

            CollectionListener cl2 = new CollectionListener() {
                public void onItemReady(int index, Object item) { System.out.println("Artist item ready: " + item ); }
                public void onCollectionReady(int message, Object result) { System.out.println("Artists collection ready: " + message ); h2.setDone(true); }
            };

            CollectionListener cl3 = new CollectionListener() {
                public void onItemReady(int index, Object item) { System.out.println("Genre item ready: " + item ); }
                public void onCollectionReady(int message, Object result) { System.out.println("Genres collection ready: " + message ); h3.setDone(true); }
            };

            CollectionListener cl4 = new CollectionListener() {
                public void onItemReady(int index, Object item) { System.out.println("Album item ready: " + item ); }
                public void onCollectionReady(int message, Object result) { System.out.println("Albums collection ready: " + message ); h4.setDone(true); }
            };

            AudioBoxClient.setCollectionListenerFor(AudioBoxClient.PLAYLISTS_KEY, cl1);
            assertSame(cl1, AudioBoxClient.getCollectionListenerFor(AudioBoxClient.PLAYLISTS_KEY));
            
            AudioBoxClient.setCollectionListenerFor(AudioBoxClient.ARTISTS_KEY, cl2);
            assertSame(cl2, AudioBoxClient.getCollectionListenerFor(AudioBoxClient.ARTISTS_KEY));
            
            AudioBoxClient.setCollectionListenerFor(AudioBoxClient.GENRES_KEY, cl3);
            assertSame(cl3, AudioBoxClient.getCollectionListenerFor(AudioBoxClient.GENRES_KEY));
            
            AudioBoxClient.setCollectionListenerFor(AudioBoxClient.ALBUMS_KEY, cl4);
            assertSame(cl4, AudioBoxClient.getCollectionListenerFor(AudioBoxClient.ALBUMS_KEY));

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
