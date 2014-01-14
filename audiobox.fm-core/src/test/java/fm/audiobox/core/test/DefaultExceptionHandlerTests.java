package fm.audiobox.core.test;

import java.util.HashMap;
import java.util.Map;

import fm.audiobox.core.exceptions.ForbiddenException;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.interfaces.ILoginExceptionHandler;

public class DefaultExceptionHandlerTests extends AbxTestCase {

  
  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void testLoginException() {
    
    final Map<String,String> params = new HashMap<String,String>();
    
    abx.getConfiguration().setDefaultLoginExceptionHandler(new ILoginExceptionHandler() {
      public void handle(LoginException loginException) {
        params.put("loginException", loginException.getMessage() );
      }
    });
    
    try {
      abx.login( Fixtures.get(Fixtures.LOGIN), Fixtures.get(Fixtures.WRONG_PASS));
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      // Fire globally the exception
      e.fireGlobally();
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    assertTrue( params.containsKey("loginException") );
    
    
    params.clear();
    try {
      abx.login(Fixtures.get(Fixtures.LOGIN), Fixtures.get(Fixtures.WRONG_PASS));
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      // not globally
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    assertFalse( params.containsKey("loginException") ); 
  }
  
}
