package fm.audiobox.core.test;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.User;
import fm.audiobox.interfaces.IAuthenticationHandle;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;

public class AuthenticationTest extends AudioBoxTestCase {
  
  private static String auth_token;
  
  @Test
  public void standardAuth() throws ServiceException, LoginException {
    
    loginCatched();
    auth_token = user.getAuthToken();
    
  }
  
  
  @Test
  public void headerAuthtokenAuth() throws ServiceException, LoginException {
    this.abc.getConfiguration().setAuthenticationHandle(new IAuthenticationHandle() {
      public void handle(IConnectionMethod request) {
        request.addHeader( IConnector.X_AUTH_TOKEN_HEADER, auth_token );
      }
    });
    
    
    try {
      user = (User) abc.login(null, null);
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }
    assertNotNull(user);
    
    assertEquals( user.getAuthToken(), auth_token );
    
  }
  
  
  @Test
  public void urlAuthtokenAuth() throws ServiceException, LoginException {
    this.abc.getConfiguration().setAuthenticationHandle(new IAuthenticationHandle() {
      public void handle(IConnectionMethod request) {
        URI uri = request.getHttpMethod().getURI();
        String url = uri.toString() + "?auth_token=" + auth_token;
        try {
          request.getHttpMethod().setURI( new URI(url) );
        } catch (URISyntaxException e) {
          fail( e.getMessage() );
        }
      }
    });
    
    
    try {
      user = (User) abc.login(null, null);
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }
    assertNotNull(user);
    
    assertEquals( user.getAuthToken(), auth_token );
    
  }

}
