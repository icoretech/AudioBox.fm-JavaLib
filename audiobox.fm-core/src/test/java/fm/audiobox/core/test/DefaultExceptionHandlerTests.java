package fm.audiobox.core.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.AudioBox;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Track;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.ILoginExceptionHandler;
import fm.audiobox.interfaces.IServiceExceptionHandler;

public class DefaultExceptionHandlerTests extends AudioBoxTestCase {

  
  private IConfiguration config;
  
  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void testServiceException() {
    
    final Map<String,String> params = new HashMap<String,String>();
    
    config.setDefaultServiceExceptionHandler(new IServiceExceptionHandler() {
      public void handle(ServiceException serivceException) {
        params.put("serviceException", serivceException.getMessage() );
      }
    });
    
    Track t = null;
    try {
      t = user.newTrackByToken("token_fake");
    } catch (ServiceException e) {
      assertEquals(e.getErrorCode(), HttpStatus.SC_NOT_FOUND);
      assertEquals( new Boolean( params.containsKey("serviceException") ) , new Boolean(true) );
    } catch (LoginException e) {
      fail(e.getMessage());
    }
    
    assertNull(t);
    
  }
  
  
  @Test
  public void testLoginException() {
    
    final Map<String,String> params = new HashMap<String,String>();
    
    config.setDefaultLoginExceptionHandler(new ILoginExceptionHandler() {
      public void handle(LoginException serivceException) {
        params.put("loginException", serivceException.getMessage() );
      }
    });
    
    try {
      AudioBox abx = new AudioBox(config);
      abx.login("fake_username", "fake_passwrd");
    } catch (LoginException e) {
      assertEquals(e.getErrorCode(), HttpStatus.SC_UNAUTHORIZED);
      assertTrue( params.containsKey("loginException") );
    } catch (ServiceException e) {
      fail(e.getMessage());
    }
    
  }
  
  protected IConfiguration getConfig() {
    config = super.getConfig();
    return config;
  }

}
