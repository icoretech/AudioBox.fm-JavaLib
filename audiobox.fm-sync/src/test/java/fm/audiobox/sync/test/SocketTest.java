package fm.audiobox.sync.test;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.test.AudioBoxTestCase;
import fm.audiobox.sync.stream.SocketClient;



public class SocketTest extends AudioBoxTestCase {
  
  @Before
  public void setUp() {
    loginCatched();
  }
  
  
  @Test
  public void testConnection() {
    SocketClient socket = null;
    try {
      socket = new SocketClient(this.abc);
    } catch (ServiceException e) {
      e.printStackTrace();
      assertNull(e);
      return;
    }
    
    
    socket.connect();
    
    try {
      Thread.currentThread().sleep(3000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    assertTrue(socket.isConnected());
    
    socket.disconnect();
    
    assertFalse(socket.isConnected());
  }
  
  
  

}
