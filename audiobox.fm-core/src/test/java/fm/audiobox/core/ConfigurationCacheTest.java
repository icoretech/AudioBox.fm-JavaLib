package fm.audiobox.core;

import java.util.Observable;
import java.util.Observer;

import org.junit.Test;

import fm.audiobox.AudioBox;
import fm.audiobox.configurations.DefaultConfiguration;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.Playlists.PlaylistTypes;
import fm.audiobox.core.models.User;
import fm.audiobox.core.observables.Event;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.ContentFormat;

public class ConfigurationCacheTest extends junit.framework.TestCase {

  
  private User user;
  
  
  @Override
  protected void setUp() throws Exception {
    
    IConfiguration configuration = new DefaultConfiguration("My test application");
    
    configuration.setVersion(1, 0, 0);
    configuration.setRequestFormat(ContentFormat.XML);
    configuration.setShortResponse(false);
    configuration.setUseCache(true);
    
    AudioBox abx = new AudioBox(configuration);
    
    user = abx.login( Fixtures.get( Fixtures.LOGIN ),  Fixtures.get( Fixtures.RIGHT_PASS ));
    
    assertNotNull(user);
    assertEquals(user.getUsername(), Fixtures.get( Fixtures.USERNAME ));
    
    super.setUp();
  }



  @Test
  public void testOne() {
  
    Playlists pls = this.user.getPlaylists();
    
    try {
      pls.load(false);
    } catch (ServiceException e) {
    } catch (LoginException e) {
    }
    
    
    Playlist pl = pls.getPlaylistByType( PlaylistTypes.AUDIO );
    
    pl.addObserver(new Observer() {
      public void update(Observable o, Object arg) {
        System.out.println( o );
        Event event = (Event) arg;
        
        System.out.println( event.state.toString() );
      }
    });
    
    
    pls.clear();
    
    try {
      pls.load(false);
    } catch (ServiceException e) {
    } catch (LoginException e) {
    }
    
  }
  
  
}
