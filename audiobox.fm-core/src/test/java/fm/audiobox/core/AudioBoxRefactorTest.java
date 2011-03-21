package fm.audiobox.core;

import org.junit.Test;

import fm.audiobox.AudioBox;
import fm.audiobox.configurations.DefaultConfiguration;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.User;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.RequestFormat;

public class AudioBoxRefactorTest extends junit.framework.TestCase {

  
  @Test
  public void testUserIsLoggedIn() {
    
    
    IConfiguration configuration = new DefaultConfiguration("My test application");
    
    configuration.setVersion(1, 0, 0);
    configuration.setRequestFormat(RequestFormat.XML);
    configuration.setShortResponse(false);
    configuration.setUseCache(false);
    
    AudioBox abx = new AudioBox(configuration);
    
    
    User user = null;
    try {
      user = abx.login("fat@fatshotty.net", "?audi0b0x!");
    } catch (LoginException e) {
      e.printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    }
    
    assertNotNull(user);
    
    assertEquals(user.getUsername(), "fatshotty");
    
    Playlists pls = user.getPlaylists();
    
    assertNotNull(pls);
    
    
  }
  
}
