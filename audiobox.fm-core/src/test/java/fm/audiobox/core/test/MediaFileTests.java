package fm.audiobox.core.test;

import java.io.File;

import org.junit.Before;

import fm.audiobox.configurations.DefaultConfiguration;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFile;

public class MediaFileTests extends AudioBoxTestCase {

  @Before
  public void setUp() {
    loginCatched();
  }

  //@Test
  public void testTrackActions() {
    
    
    try {
      MediaFile media = new MediaFile(new DefaultConfiguration("TEST UP LOAD MEDIA FILE"));
      media.setFileBody(new File("/home/achille/Musica/12 - Hell Broke Luce.mp3"));
      media.upload();
      
    } catch (ServiceException e) {      
      e.printStackTrace();
    } catch (LoginException e) {
      e.printStackTrace();
    }

  }

}
