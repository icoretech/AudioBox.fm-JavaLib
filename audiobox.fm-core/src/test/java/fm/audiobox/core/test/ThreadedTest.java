package fm.audiobox.core.test;

import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.configurations.Response;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.observables.Event;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;

public class ThreadedTest extends AbxTestCase {

  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void asyncPlaylistRequest() {
    
    final Playlists pls = user.getPlaylists();
    
    assertTrue( pls.size() == 0 );
    
    pls.addObserver(new Observer() {
      public void update(Observable obj, Object evt) {
        Event event = (Event) evt;
        if ( event.state == Event.States.END_LOADING ) {
          // playlists are now correctly populated
          assertFalse( pls.size() == 0 );
        }
      }
    });
    
    try {
      pls.load( true );
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }

    assertTrue( pls.size() == 0 );
  }
  
  
  @Test
  public void asyncPlaylistRequestAndWait() {
    
    final Playlists pls = user.getPlaylists();
    
    assertTrue( pls.size() == 0 );
    IConnectionMethod req = null;
    try {
      req = pls.load( true );
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }

    assertTrue( pls.size() == 0 );
    
    Response res =  null;
    try {
      res = req.getResponse();
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    assertTrue( res.isOK() );
    
    assertTrue( pls.size() > 0 );
  }
  
}
