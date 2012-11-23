package fm.audiobox.core.test;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFile;
import fm.audiobox.core.models.MediaFiles;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;

public class CoverFlowTest extends AudioBoxTestCase {

  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void coverFlowList() {
    
    
    Playlists playlists = this.user.getPlaylists();
    
    try {
      
      playlists.load(false);
      
    } catch (ServiceException e) {
      
      fail( e.getMessage() );
      
    } catch (LoginException e) {
      
      fail( e.getMessage() );
      
    }
    
    Playlist dropbox = playlists.getPlaylistByType( Playlists.Type.DropboxPlaylist );
    
    assertNotNull( dropbox );
    
    MediaFiles covers = dropbox.getCoverFlows();
    
    assertNotNull( covers );
    
    try {
      covers.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    int originalSize = covers.size();
    assertFalse( originalSize  == 0 );
    
    MediaFile mf = covers.get(0);
    assertNull( mf.getToken() );
    
    
  }
  
  
}
