package fm.audiobox.sync.test;

import fm.audiobox.core.exceptions.ForbiddenException;
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
  public void playlistSwitchToCloud() {
    Playlists pls = user.getPlaylists();
    
    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    Playlist pl = pls.getPlaylistByType( Playlists.Type.CloudPlaylist );
    
    try {
      player.switchPlaylist( pl );
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    try {
      Thread.sleep( 3000 );
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
  }
  
  
  
  @Test
  public void play() {
    
    Playlists pls = user.getPlaylists();
    
    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
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
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }


    MediaFile mf = mediaFiles.get(3);
    
    try {
      player.play( mf );
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    try {
      Thread.sleep( 3000 );
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
  }
  
  @Test
  public void pause() {
    
    try {
      player.pause();
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    try {
      Thread.sleep( 3000 );
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
  }
  
  
  @Test
  public void togglePlayPause() {
    
    try {
      player.togglePlay();
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    try {
      Thread.sleep( 3000 );
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
  }
  
  
  @Test
  public void playlistSwitchToSkydrive() {
    Playlists pls = user.getPlaylists();
    
    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    Playlist pl = pls.getPlaylistByType( Playlists.Type.SkydrivePlaylist );
    
    try {
      player.switchPlaylist( pl );
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }


    try {
      Thread.sleep( 3000 );
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
  }
  
  
  @Test
  public void showInfoCloudMediaFile() {
    Playlists pls = user.getPlaylists();
    
    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
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
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }


    MediaFile mf = mediaFiles.get(1);
    
    try {
      player.showInfo( mf );
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }


    try {
      Thread.sleep( 3000 );
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
  }
  
  
  
  @Test
  public void switchAndPlay() {
    Playlists pls = user.getPlaylists();
    
    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
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
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }


    MediaFile mf = mediaFiles.get(2);
    
    try {
      player.switchAndPlay(pl, mf);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    try {
      Thread.sleep( 3000 );
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
    
  }
  
  
  
  @Test
  public void testNext() {
    try {
      player.next();
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    try {
      Thread.sleep( 3000 );
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
  }
  
  @Test
  public void testPrev() {
    try {
      player.prev();
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    try {
      Thread.sleep( 3000 );
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
  }
  
  
  @Test
  public void testShuffle() {
    try {
      player.toggleShuffle();
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    try {
      Thread.sleep( 3000 );
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
    this.testNext();
    
  }
  
  @Test
  public void testRepeat() {
    try {
      player.toggleRepeat();
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    try {
      Thread.sleep( 3000 );
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  
}
