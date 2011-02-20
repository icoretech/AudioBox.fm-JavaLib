package fm.audiobox.core;

import java.util.Observable;
import java.util.Observer;

import org.junit.Test;

import fm.audiobox.AudioBox;
import fm.audiobox.configurations.DefaultConfiguration;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.RequestFormat;

public class AudioBoxRefactorTest extends junit.framework.TestCase {

  
  @Test
  public void testUserIsLoggedIn() {
    
    
    IConfiguration configuration = new DefaultConfiguration();
    
    configuration.setApplicationName("My App Name");
    configuration.setVersion(1, 0, 0);
    configuration.setDefaultRequestFormat(RequestFormat.XML);
    configuration.setShortResponse(false);
    configuration.setUseCache(false);
    
    AudioBox abx = new AudioBox(configuration);
    
    
    User user = abx.login("username", "password");
    Playlists pls = user.getPlaylists();
    pls.addObserver(new Observer() {
      public void update(Observable o, Object arg) {
      }
    });
    
    
    pls.invoke();
    
  }
  
}
