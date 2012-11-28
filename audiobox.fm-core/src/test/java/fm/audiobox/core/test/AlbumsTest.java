package fm.audiobox.core.test;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Album;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;

public class AlbumsTest extends AudioBoxTestCase {

  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void albumsList() {
    
    
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
    
    Albums covers = dropbox.getAlbums();
    
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
    
    Album mf = covers.get(0);
    assertNull( mf.getToken() );
    
    
  }
  
  
}
