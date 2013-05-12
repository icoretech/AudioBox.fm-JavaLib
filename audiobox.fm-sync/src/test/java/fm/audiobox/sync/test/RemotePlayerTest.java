package fm.audiobox.sync.test;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFile;
import fm.audiobox.core.models.MediaFiles;
import fm.audiobox.core.models.Player;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;


public class RemotePlayerTest extends AbxTestCase {

  
  private Player player;
  
  
  @Before
  public void setUp() {
    loginCatched();
  }
  
  @Override
  public void loginCatched() {
    super.loginCatched();
    player = new Player( user );
  }

  @Test
  public void testApp() {
    Playlists pls = user.getPlaylists();
    
    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    Playlist pl = pls.getPlaylistByType( Playlists.Type.CloudPlaylist );
    
    MediaFiles mediaFiles = pl.getMediaFiles();
    
    assertFalse( mediaFiles.size() == pl.getMediaFilesCount() );
    assertFalse( mediaFiles.size() > 0 );
    
    try {
      mediaFiles.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    
    MediaFile mf = mediaFiles.get(0);
    
    try {
      player.play( mf );
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    
  }
  
  
}
