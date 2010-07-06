/**
 * 
 */
package fm.audiobox.core.test;


import java.net.SocketException;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.StaticAudioBox;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.Artists;
import fm.audiobox.core.models.Genres;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.Profile;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.core.test.mocks.models.User;

/**
 * @author keytwo
 *
 */
public class UserTest extends junit.framework.TestCase {

	StaticAudioBox abc;
    User user;
    Fixtures fx = new Fixtures();
    
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
        StaticAudioBox.setModelClassFor(StaticAudioBox.USER_KEY , User.class );
        abc.setForceTrust(true);
        
    }
    
    @Test
    public void testUser() {
        try {
            StaticAudioBox.getConnector().setTimeout(5000);
            assertEquals( 5000, StaticAudioBox.getConnector().getTimeout());
            
            user = (User) abc.login( fx.get( Fixtures.LOGIN ), fx.get( Fixtures.RIGHT_PASS ) );
            assertSame( user, abc.getUser() );
            
            assertNotNull( user.getBytesServed() );
            assertNotNull( user.getEmail() );
            assertNotNull( user.getPlayCount() );
            assertNotNull( user.getQuota() );
            assertNotNull( user.getTracksCount() );
            assertNotNull( user.getAvailableStorage() );
            assertNotNull( user.getAvatarUrl() );
            assertNotNull( user.getName() );
            
            
            assertTrue( user.getQuota() < user.getAvailableStorage() );
            
            // Profile
            Profile p = user.getProfile();
            assertNotNull( p.hasAutoplay() );
            assertNotNull( p.getBirthDate() );
            assertNotNull( p.getCountry() );
            assertNotNull( p.getGender() );
            assertNotNull( p.getHomePage() );
            assertNotNull( p.getRealName() );
            assertNotNull( p.getTimeZone() );
            assertEquals( p.getName(), p.getRealName() );
            assertNull( p.getToken() );
            
            
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }
    
    
    @Test
    public void testLoginShouldThrowsLoginExceptionOnWrongPassword() {
        assertNotNull( abc );
        try {
            user = (User) abc.login( fx.get( Fixtures.LOGIN ), fx.get( Fixtures.WRONG_PASS ) );
        } catch (LoginException e) {
           assertEquals( HttpStatus.SC_UNAUTHORIZED, e.getErrorCode());
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoginShouldThrowsLoginExceptionOnWrongCredentials() {
        assertNotNull( abc );
        try {
            user = (User) abc.login( "wrong_user" , fx.get( Fixtures.WRONG_PASS ));
        } catch (LoginException e) {
           assertEquals( 401, e.getErrorCode());
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testUserShouldBePopulatedWithRightCredentials() {
        assertNotNull( abc );
        loginCatched();
        assertNotNull( user.getTest() );
        assertEquals("test", user.getTest() );
        
        assertNotNull( user.getProfile() );
        assertNotNull( user.getProfile().getName() );
        assertEquals( fx.get( Fixtures.USERNAME ), user.getUsername());
    }

    /*
    @Test
    public void testUserLoginFailsOnUserNotActive() {
        assertNotNull( abc );
        
        try {
            loginInactiveUser();
            assertFalse( ! User.ACTIVE_STATE.equals( user.getState() ) ); // Make test fails
        } catch (LoginException e) {
            assertEquals( LoginException.INACTIVE_USER_STATE, e.getErrorCode() );
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }
    */
    
    @Test
    public void testUserCollections() {
        loginCatched();
        try {
            
            Playlists pls = user.getPlaylists();
            assertNotNull( pls );
            
            Genres gnr = user.getGenres();
            assertNotNull( gnr );
            
            Artists art = user.getArtists();
            assertNotNull( art );
            
            Albums alb = user.getAlbums();
            assertNotNull( alb );
            
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testUserGetUploadedTracks() {
        assertNotNull( abc );
        loginCatched();
        
        try {
            String[] tracks = user.getUploadedTracks();
            assertNotNull(tracks);
            assertNotNull(tracks[0]);
            
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
    
    @After
    public void tearDown() throws Exception {
        
    }
    
    
    private void loginInactiveUser() throws LoginException, ServiceException, ModelException {
        user = (User) abc.login( fx.get( Fixtures.INACTIVE_LOGIN ), fx.get( Fixtures.INACTIVE_RIGHT_PASS ) );
    }
    
    
    private void loginCatched() {
        try {
            user = (User) abc.login( fx.get(Fixtures.LOGIN) , fx.get( Fixtures.RIGHT_PASS) );
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        }
        
    }
    
    
}
