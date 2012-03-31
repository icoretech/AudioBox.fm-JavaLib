package fm.audiobox.core.test;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFile;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

public class MediaFileTest extends AudioBoxTestCase {

  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void testMediaFileUpload() {
    
    try {
      MediaFile media = new MediaFile(this.abc);
      
      assertNull(media.getToken());
      assertNull(media.getMediaFileName());
      
      File fileToUpload = new File( Fixtures.get("file_to_upload") );
      
      assertTrue( media.upload( fileToUpload ) );
      
      assertNotNull(media.getToken());
      assertNotNull(media.getMediaFileName());
      
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (LoginException e) {
      e.printStackTrace();
    }

  }
  
  @Test
  public void testMediaFileUploadFailed() {
    
    MediaFile media = new MediaFile(this.abc);
    Exception ex = null;
    try {
      
      assertNull(media.getToken());
      assertNull(media.getMediaFileName());
      
      File fileToUpload = new File( Fixtures.get("file_to_upload") );
      
      assertFalse( media.upload( fileToUpload ) );
      
    } catch (ServiceException e) {
      ex = e;
    } catch (LoginException e) {
      ex = e;
    }
    
    assertNotNull(ex);
    assertTrue( ex instanceof ServiceException );
    
    ServiceException se = (ServiceException)ex;
    
    assertEquals( se.getErrorCode(), 422 );
    assertNotNull( se.getMessage() );
    
    assertNull(media.getToken());
    assertNull(media.getMediaFileName());

  }
  
  
  

}
