package fm.audiobox.core;

import java.util.Observable;
import java.util.Observer;

import org.junit.Test;

import fm.audiobox.AudioBox;
import fm.audiobox.configurations.DefaultConfiguration;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.User;
import fm.audiobox.core.observables.Event;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.RequestFormat;

public class ObserversTests extends junit.framework.TestCase {

  
  private User user;
  
  
  @Override
  protected void setUp() throws Exception {
    
    IConfiguration configuration = new DefaultConfiguration("My test application");
    
    configuration.setVersion(1, 0, 0);
    configuration.setRequestFormat(RequestFormat.XML);
    configuration.setShortResponse(false);
    configuration.setUseCache(false);
    
    AudioBox abx = new AudioBox(configuration);
    
    user = abx.login( Fixtures.get( Fixtures.LOGIN ),  Fixtures.get( Fixtures.RIGHT_PASS ));
    
    assertNotNull(user);
    assertEquals(user.getUsername(), "fatshotty");
    
    super.setUp();
  }



  @Test
  public void testPlaylists() {
    
    final Playlists pls = user.getPlaylists();
    
    pls.addObserver(new Observer() {
      private int total = 0;
      public void update(Observable o, Object arg) {
        Event event = (Event)arg;
        if ( event.state == Event.States.ENTITY_ADDED ){
          pls.setProperty("count", ++total);
        } else if ( event.state == Event.States.ENTITY_REMOVED ){
          pls.setProperty("count", --total);
        }
      }
    });
    
    try {
      pls.load(false);
    } catch (ServiceException e) {
      assertNull(e);
    } catch (LoginException e) {
      assertNull(e);
    }
    
    pls.remove(0);
    
    int total = ((Integer) pls.getProperty("count")).intValue();
    assertEquals(total, pls.size() );
    
    
  }

}
