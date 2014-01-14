package fm.audiobox.core.test;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import fm.audiobox.core.exceptions.ForbiddenException;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFile;
import fm.audiobox.core.models.MediaFiles;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.observables.Event;
import fm.audiobox.core.parsers.UploadHandler;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

public class MediaFileUploaderTest extends AbxTestCase {

  
  private static String lastTokenMedia = "";
  boolean event_fired = false;
  
  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void simpleUploadMediaFile() {
    
    Playlists pls = user.getPlaylists();
    long initialMFSize = 0;
    
    try {
      pls.load(false);
      Playlist pl = pls.getPlaylistByType( Playlists.Type.CloudPlaylist );
      initialMFSize = pl.getMediaFilesCount();
    } catch (ServiceException e) {
      fail(e.getMessage());
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }


    MediaFile mf = user.newMediaFile();
    
    assertNull( mf.getToken() );
    
    File file = new File( Fixtures.class.getResource( Fixtures.get( Fixtures.FILE_TO_UPLOAD ) ).getFile() );
    
    try {
      mf.setTitle("My Custom Title");
      mf.upload( false, new UploadHandler(file), true );
      assertNotNull( mf.getToken() );
      lastTokenMedia = mf.getToken();
    } catch (ServiceException e) {
      fail(e.getMessage());
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    try {
      pls.load(false);
      Playlist pl = pls.getPlaylistByType( Playlists.Type.CloudPlaylist );
      assertEquals( pl.getMediaFilesCount() , initialMFSize + 1);
    } catch (ServiceException e) {
      fail(e.getMessage());
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

  }
  
  
  @Test
  public void mediaFileShouldHaveCustomTitle() {
    MediaFile mf = user.newMediaFile();
    mf.setToken( lastTokenMedia );
    
    try {
      mf.load(false);
    } catch (ServiceException e) {
      fail(e.getMessage());
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    assertEquals( mf.getTitle(), "My Custom Title");
  }
  
  
  @Test
  public void destroyMediaFiles() {
    Playlists pls = user.getPlaylists();
    Playlist pl = pls.getPlaylistByType( Playlists.Type.CloudPlaylist );
    if ( pl == null ) {
      try {
        pls.load(false);
        pl = pls.getPlaylistByType( Playlists.Type.CloudPlaylist );
      } catch (ServiceException e) {
        fail(e.getMessage());
      } catch (LoginException e) {
        fail(e.getMessage());
      } catch (ForbiddenException e) {
        fail(e.getMessage());
      }
    }
    MediaFiles mfs = pl.getMediaFiles();
    try {
      mfs.load(false);
    } catch (ServiceException e) {
      fail(e.getMessage());
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }


    MediaFile mf = mfs.get( lastTokenMedia );
    
    assertNotNull( mf );
    
    try {
      mf.load(false);
    } catch (ServiceException e) {
      fail(e.getMessage() );
    } catch (LoginException e) {
      fail(e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    mfs.addObserver(new Observer() {
      public void update(Observable _mfs, Object evt) {
        Event event = (Event) evt;
        if ( event.state == Event.States.ENTITY_REMOVED ) {
          event_fired = true;
        }
      }
    });

    try {
      assertTrue( mf.destroy() );
      assertTrue( event_fired );
    } catch (ServiceException e) {
      fail(e.getMessage() );
    } catch (LoginException e) {
      fail(e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

  }
  

}
