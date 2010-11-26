/**
 * 
 */
package fm.audiobox.core.test;


import java.net.SocketException;
import java.util.Arrays;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.Artists;
import fm.audiobox.core.models.Genres;
import fm.audiobox.core.models.ModelFactory;
import fm.audiobox.core.models.Plan;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.Profile;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.core.test.mocks.models.Album;
import fm.audiobox.core.test.mocks.models.User;

/**
 * @author keytwo
 *
 */
public class UserTest extends junit.framework.TestCase {

    private static final String[] ADMIN_ALLOWED_FORMATS = "aac;mp3;mp2;m4a;m4b;m4p;m4v;m4r;mp4;3gp;ogg;flac;spx;wma;rm;ram;wav;mpc;mp+;mpp;aiff;aif;aifc;tta".split(";");
    StaticAudioBox abc;
    User user;

    @Before
    public void setUp() throws Exception {
        ModelFactory mf = new ModelFactory();
    	mf.setModelClassFor( ModelFactory.USER_KEY, Album.class );
        abc = new StaticAudioBox();
        abc.setModelFactory(mf);
    }

    @Test
    public void testUser() {
        try {
            StaticAudioBox.getConnector().setTimeout(5000);
            assertEquals( 5000, StaticAudioBox.getConnector().getTimeout());

            user = (User) abc.login( Fixtures.get( Fixtures.LOGIN ), Fixtures.get( Fixtures.RIGHT_PASS ) );
            assertSame( user, abc.getUser() );

            assertNotNull( user.getBytesServed() );
            assertNotNull( user.getEmail() );
            assertEquals( Fixtures.get( Fixtures.LOGIN ), user.getEmail() );
            assertNotNull( user.getPlayCount() );
            assertNotNull( user.getQuota() );
            assertNotNull( user.getTracksCount() );
            assertNotNull( user.getAvailableStorage() );
            assertEquals( 162135015424L, user.getAvailableStorage() );
            assertNotNull( user.getName() );
            assertEquals( Fixtures.get( Fixtures.USERNAME ), user.getName() );
            assertNotNull( user.getTimeZone() );
            assertTrue( user.getQuota() < user.getAvailableStorage() );
            assertNotNull( user.getAllowedFormats() );
            for (String fmt : user.getAllowedFormats()) {
                assertTrue( Arrays.asList( ADMIN_ALLOWED_FORMATS ).contains( fmt ) ); 
            }


            // Profile
            Profile p = user.getProfile();
            assertNotNull( p.hasAutoplay() );
            assertNotNull( p.getRealName() );
            assertEquals( Fixtures.get( Fixtures.REAL_NAME ), p.getRealName() );
            assertEquals( p.getName(), p.getRealName() );
            assertNull( p.getToken() );

            assertNotNull( p.hasMaximumPortability() );
            if ( p.hasMaximumPortability() ) {
                p.setMaximumPortability( false );
                assertFalse( p.hasMaximumPortability() );
            } else {
                p.setMaximumPortability( true );
                assertTrue( p.hasMaximumPortability() );
            }



            assertTrue( p.hasMaximumPortability() );

            // Plan
            Plan plan = user.getPlan();
            assertEquals( plan.getName(), "admin" );
            assertTrue( plan.hasApi() );
            assertTrue( plan.hasDropbox() );
            assertTrue( plan.hasManager() );
            assertTrue( plan.hasLastfm() );
            assertTrue( plan.hasMultiformat() );
            assertTrue( plan.hasSocialNetworks() );
            assertTrue( plan.hasCloudWebPlayer() );
            assertTrue( plan.hasYoutubeChannel() );

        } catch (LoginException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
        } catch (ServiceException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
        } catch (ModelException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
        }
    }


    @Test
    public void testLoginShouldThrowsLoginExceptionOnWrongPassword() {
        assertNotNull( abc );
        try {
            user = (User) abc.login( Fixtures.get( Fixtures.LOGIN ), Fixtures.get( Fixtures.WRONG_PASS ) );
        } catch (LoginException e) {
            assertEquals( HttpStatus.SC_UNAUTHORIZED, e.getErrorCode());
        } catch (SocketException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
        } catch (ModelException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
        }
    }

    @Test
    public void testLoginShouldThrowsLoginExceptionOnWrongCredentials() {
        assertNotNull( abc );
        try {
            user = (User) abc.login( "wrong_user" , Fixtures.get( Fixtures.WRONG_PASS ));
        } catch (LoginException e) {
            assertEquals( 401, e.getErrorCode());
        } catch (SocketException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
        } catch (ModelException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
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
        assertEquals( Fixtures.get( Fixtures.USERNAME ), user.getUsername());
    }

    @Test
    public void testUserLoginFailsOnUserNotActive() {
        assertNotNull( abc );

        try {
            loginInactiveUser();
        } catch (LoginException e) {
            assertEquals( HttpStatus.SC_UNAUTHORIZED, e.getErrorCode() );
        } catch (SocketException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
        } catch (ModelException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
        }
    }

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
            assertNull( e );	// development purpose
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

        } catch (LoginException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
        } catch (ServiceException e) {
            e.printStackTrace();
            assertNull( e );	// development purpose
        }
    }

    @After
    public void tearDown() throws Exception {

    }


    private void loginInactiveUser() throws LoginException, ServiceException, ModelException {
        user = (User) abc.login( Fixtures.get( Fixtures.INACTIVE_LOGIN ), Fixtures.get( Fixtures.INACTIVE_RIGHT_PASS ) );
    }


    private void loginCatched() {
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


}
