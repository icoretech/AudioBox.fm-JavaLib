/**
 * 
 */
package fm.audiobox.core.test;

import java.util.Observable;
import java.util.Observer;

import org.apache.http.HttpStatus;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.AccountStats;
import fm.audiobox.core.models.Permissions;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

/**
 * @author keytwo
 * 
 */
public class UserTest extends AbxTestCase {

  private int number_events = 0;
  
  @Test
  public void userShouldBeLoggedIn() {
    loginCatched();
    
    User _user = abx.getUser();
    assertNotNull( _user );
    
  }
    
  @Test
  public void userShouldBeCorrectlyPopulated() {
    loginCatched();
    
    assertSame( user, abx.getUser() );
    
    assertNotSame( user.getAuthToken(), "" );

    assertNotNull( user.getEmail() );
    assertEquals( user.getUsername(), user.getEmail() );
    
    assertEquals( Fixtures.get(Fixtures.LOGIN), user.getEmail() );
    assertNotNull(user.getTimeZone());
    
    // Permission
    Permissions p = user.getPermissions();
    assertNotNull( p );
    
    // AccountStats
    AccountStats accountStats = user.getAccountStats();
    assertNotNull( accountStats );
    
    assertNotNull( user.getPreferences() );
    assertNotNull( user.getExternalTokens() );

    assertSame( user.getSubscriptionState(), User.SubscriptionState.active );
    
  }
  
  
  @Test
  public void userHasDaemonRunning() {
    loginCatched();
    boolean result = false;
    try {
      result = user.isDaemonRunning();
    } catch (LoginException e) {
      fail(e.getMessage());
    }
    
    try {
      String ip = user.remoteDaemonIp();
      assertSame( ip == null, !result );
    } catch (LoginException e) {
      fail(e.getMessage());
    }
    
    log.info("Daemon is running: ***  " + result + " ***");
    assertTrue(true);
    
  }
  
  
  @Test
  public void userHasAllPermissions() {
    loginCatched();
    
    Permissions permissions = user.getPermissions();
    assertTrue( permissions.isBox() );
    assertTrue( permissions.isCloud() );
    assertTrue( permissions.isDropbox() );
    assertTrue( permissions.isFacebook() );
    assertTrue( permissions.isGdrive() );
    assertTrue( permissions.isLastfm() );
    assertTrue( permissions.isLocal() );
    assertTrue( permissions.isMusixmatch() );
    assertTrue( permissions.isPlayer() );
    assertTrue( permissions.isSkydrive() );
    assertTrue( permissions.isSongkick() );
    assertTrue( permissions.isSoundcloud() );
    assertTrue( permissions.isTwitchtv() );
    assertTrue( permissions.isTwitter() );
    assertTrue( permissions.isYoutube() );
    
  }
  
  
  @Test
  public void userShouldFireEvents(){
    loginCatched();
    number_events = 0;
    abx.addObserver(new Observer() {
      public void update(Observable usr, Object evt) {
        number_events++;
      }
    });
    
    
    abx.logout();
    loginCatched();
    
    assertEquals( 2, number_events );
    
  }
  

  @Test
  public void testLoginShouldThrowsLoginExceptionOnWrongPassword() {
    try {
      user = abx.login(Fixtures.get(Fixtures.LOGIN), Fixtures.get(Fixtures.WRONG_PASS));
    } catch (LoginException e) {
      assertEquals(HttpStatus.SC_UNAUTHORIZED, e.getErrorCode() );
    } catch (ServiceException e) {
      fail( e.getMessage() );
    }
  }

}
