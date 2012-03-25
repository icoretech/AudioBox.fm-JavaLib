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
  public void testTrackActions() {
    
    
    try {
      MediaFile media = new MediaFile(this.abc);
      
      File fileToUpload = new File( Fixtures.get("file_to_upload") ); 
      assertTrue( media.upload( fileToUpload ) );
      
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (LoginException e) {
      e.printStackTrace();
    }

  }

}
