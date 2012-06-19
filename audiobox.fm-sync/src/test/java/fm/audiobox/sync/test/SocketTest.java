//package fm.audiobox.sync.test;
//
//import java.util.Observable;
//import java.util.Observer;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import fm.audiobox.core.exceptions.ServiceException;
//import fm.audiobox.core.models.Action;
//import fm.audiobox.core.models.Args;
//import fm.audiobox.core.test.AudioBoxTestCase;
//import fm.audiobox.sync.stream.SocketClient;
//
//
//
//public class SocketTest extends AudioBoxTestCase {
//  
//  @Before
//  public void setUp() {
//    loginCatched();
//  }
//  
//  
//  @Test
//  public void testConnection() {
//    SocketClient socket = null;
//    try {
//      socket = new SocketClient(this.abc);
//    } catch (ServiceException e) {
//      e.printStackTrace();
//      assertNull(e);
//      return;
//    }
//    
//    
//    try {
//      socket.connect();
//    } catch (ServiceException e1) {
//      assertNull(e1);
//      return;
//    }
//    
//    socket.addObserver( new Observer() {
//      public void update(Observable arg0, Object obj) {
//        System.out.println("Message received in observer");
//        assertTrue( obj instanceof Action );
//        if ( obj instanceof Action ) {
//          Action action = (Action) obj;
//          Args args = action.getArgs();
//          assertNotNull( args.getServerIp() );
//        }
//      }
//    });
//    
////    try {
////      Thread.currentThread().sleep(10000);
////    } catch (InterruptedException e) {
////      // TODO Auto-generated catch block
////      e.printStackTrace();
////    }
//    while(true){}
//    
////    assertTrue(socket.isConnected());
////    
////    socket.disconnect();
////    
////    assertFalse(socket.isConnected());
//  }
//  
//  
//  
//
//}
