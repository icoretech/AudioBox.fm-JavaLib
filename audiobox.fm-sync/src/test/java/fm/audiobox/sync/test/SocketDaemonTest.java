package fm.audiobox.sync.test;

import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Action;
import fm.audiobox.core.models.Args;
import fm.audiobox.core.observables.Event;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.sync.stream.SocketClient;



public class SocketDaemonTest extends AbxTestCase {
  
  @Before
  public void setUp() {
    loginCatched();
  }
  
  
  @Test
  public void testConnection() {
    SocketClient socket = null;
    try {
      socket = new SocketClient( this.abx, IConfiguration.Connectors.DAEMON );
    } catch (ServiceException e) {
      e.printStackTrace();
      fail( e.getMessage() );
      return;
    }
    
    
    try {
      socket.connect();
    } catch (ServiceException e) {
      fail( e.getMessage() );
      return;
    }
    
    socket.addObserver( new Observer() {
      public void update(Observable arg0, Object obj) {
        System.out.println("Message received in observer");
        assertTrue( obj instanceof Action || obj instanceof Event );
        if ( obj instanceof Action ) {
          Action action = (Action) obj;
          Args args = action.getArgs();
          assertNotNull( args.getServerIp() );
        }
      }
    });
    
//    try {
//      Thread.currentThread().sleep(10000);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
    
    
//    assertTrue(socket.isConnected());
//    
//    socket.disconnect();
//    
//    assertFalse(socket.isConnected());
    while(true){}
  }
  
  
  

}
