/**
 * 
 */
package fm.audiobox.core.test;


import java.net.SocketException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.StaticAudioBox;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.test.mocks.fixtures.UserFixture;
import fm.audiobox.core.test.mocks.models.User;

/**
 * @author keytwo
 *
 */
public class UserTest extends junit.framework.TestCase {

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
        
        System.setProperty("org.apache.commons.logging.simplelog.log.fm.audiobox.api", "debug");
        
        abc = new StaticAudioBox();
        StaticAudioBox.initClass(StaticAudioBox.USER_KEY , User.class );
        abc.setForceTrust(true);
        
    }
    
    @Test
    public void testPreconditions() {
        assertNotNull( abc );
    }

    @Test
    public void testLoginShouldThrowsLoginExceptionOnWrongPassword() {
        
        try {
            user = (User) abc.login( UserFixture.LOGIN , UserFixture.WRONG_PASS );
        } catch (LoginException e) {
           assertEquals( 401, e.getErrorCode());
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoginShouldThrowsLoginExceptionOnWrongCredentials() {
        
        try {
            user = (User) abc.login( "wrong_user" , UserFixture.WRONG_PASS );
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
        loginCatched();
        assertNotNull( user.getTest() );
        assertEquals("test", user.getTest() );
        
        assertNotNull( user.getProfile() );
        assertNotNull( user.getProfile().getName() );
        assertEquals( UserFixture.USERNAME , user.getUsername());
    }

    
    @Test
    public void testUserLoginFailsOnUserNotActive() {
        
        try {
            loginInactiveUser();
        } catch (LoginException e) {
            assertEquals( LoginException.INACTIVE_USER_STATE, e.getErrorCode() );
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testUserGetUploadedTracks() {
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
    
    
    private void loginInactiveUser() throws LoginException, ServiceException {
        
    }
    
    
    private void loginCatched() {
        try {
            user = (User) abc.login( UserFixture.LOGIN , UserFixture.RIGHT_PASS );
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        }
        
    }
    
    
}
