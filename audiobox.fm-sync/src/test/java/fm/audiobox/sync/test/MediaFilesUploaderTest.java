package fm.audiobox.sync.test;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFile;
import fm.audiobox.core.models.MediaFiles;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.parsers.UploadHandler;
import fm.audiobox.sync.task.MD5;
import fm.audiobox.sync.test.mocks.fixtures.Fixtures;

/**
 * Unit test for simple App.
 */
public class MediaFilesUploaderTest extends AbxTestCase {
    
  @SuppressWarnings("unused")
  private static Logger logger = LoggerFactory.getLogger(MediaFilesUploaderTest.class);
  
  private static String md5 = null;
  
  @Before
  public void setUp() {
    loginCatched();
  }
  
  @Test
  public void mediaUpload() {
    MediaFile mf = user.newMediaFile();
    
    assertNull( mf.getToken() );
    
    File file = new File( Fixtures.class.getResource( Fixtures.get( Fixtures.FILE_TO_UPLOAD ) ).getFile() );
    
    try {
      mf.setTitle("My Custom Title");
      mf.upload( false, new UploadHandler(file), true );
      assertNotNull( mf.getToken() );
    } catch (ServiceException e) {
      fail(e.getMessage());
    } catch (LoginException e) {
      fail(e.getMessage());
    }
  }
  
  
  @Test
  public void mediaAlreadyExists() {
    
    MediaFile mf = user.newMediaFile();
    
    assertNull( mf.getToken() );
    
    File file = new File( Fixtures.class.getResource( Fixtures.get( Fixtures.FILE_TO_UPLOAD ) ).getFile() );
    
    MD5 md5Task = new MD5("md5 test", file);
    md5 = md5Task.digest();
    
    mf.setHash( md5 );
    
    try {
      mf.upload(false, new UploadHandler(file) );
    } catch (ServiceException e) {
      assertTrue( e.getErrorCode() == 409 );
    } catch (LoginException e) {
      fail(e.getMessage());
    }

  }
  
  
  @Test
  public void deleteAlreadyExistingMedia() {
    
    Playlists pls = user.getPlaylists();
    
    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail(e.getMessage());
    } catch (LoginException e) {
      fail(e.getMessage());
    }

    Playlist cloud = pls.getPlaylistByType( Playlists.Type.CloudPlaylist );
    
    
    MediaFile mf = null;
    
    try {
      MediaFiles mfs = cloud.getMediaFilesHashesMap(false);
      
      for( MediaFile m : mfs ) {
        if ( m.getHash().equals( md5 ) ) {
          mf = m;
          break;
        }
      }
      
    } catch (ServiceException e) {
      fail(e.getMessage());
    } catch (LoginException e) {
      fail(e.getMessage());
    }


    assertNotNull( mf );
    
    try {
      assertTrue( mf.destroy() );
    } catch (ServiceException e) {
      fail(e.getMessage());
    } catch (LoginException e) {
      fail(e.getMessage());
    }

  }
  
    
}
