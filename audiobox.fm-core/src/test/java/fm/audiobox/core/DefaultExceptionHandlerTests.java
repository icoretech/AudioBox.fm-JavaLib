package fm.audiobox.core;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import fm.audiobox.AudioBox;
import fm.audiobox.configurations.DefaultConfiguration;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.RequestFormat;
import fm.audiobox.interfaces.ILoginExceptionHandler;
import fm.audiobox.interfaces.IServiceExceptionHandler;

public class DefaultExceptionHandlerTests extends junit.framework.TestCase {

  
  private User user;
  private IConfiguration config;
  
  
  @Override
  protected void setUp() throws Exception {
    
    config = new DefaultConfiguration("My test application");
    
    config.setVersion(1, 0, 0);
    config.setRequestFormat(RequestFormat.XML);
    config.setShortResponse(false);
    config.setUseCache(false);
    
    AudioBox abx = new AudioBox(config);
    
    user = abx.login( Fixtures.get( Fixtures.LOGIN ),  Fixtures.get( Fixtures.RIGHT_PASS ));
    
    assertNotNull(user);
    assertEquals(user.getUsername(), Fixtures.get( Fixtures.USERNAME ));
    
    super.setUp();
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
      t = user.getTrackByToken("token_fake");
    } catch (ServiceException e) {
      assertEquals( new Boolean( params.containsKey("serviceException") ) , new Boolean(true) );
    } catch (LoginException e) {
      assertNull(e);
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
      abx.login("fale_username", "fake_passwrd");
    } catch (LoginException e) {
      assertEquals( new Boolean( params.containsKey("loginException") ) , new Boolean(true) );
    } catch (ServiceException e) {
      assertNull(e);
    }
    
  }
  

}
