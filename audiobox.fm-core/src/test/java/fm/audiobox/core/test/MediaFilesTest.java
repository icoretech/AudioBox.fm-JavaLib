package fm.audiobox.core.test;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFile;
import fm.audiobox.core.models.MediaFiles;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;

public class MediaFilesTest extends AudioBoxTestCase {

  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void mediaFilesListForHomeDrive() {
    
    
    Playlists playlists = this.user.getPlaylists();
    
    try {
      
      playlists.load(false);
      
    } catch (ServiceException e) {
      
      fail( e.getMessage() );
      
    } catch (LoginException e) {
      
      fail( e.getMessage() );
      
    }
    
    Playlist local = playlists.getPlaylistByType( "LocalPlaylist" );
    
    assertNotNull( local );
    
    MediaFiles mediaFiles = local.getMediaFiles();
    
    assertNotNull( mediaFiles );
    
    try {
      mediaFiles.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    int originalSize = mediaFiles.size();
    assertFalse( originalSize  == 0 );
    
  }
  
  @Test
  public void destroyMediaFilesFromHomeDrive() {
    
    MediaFiles mediaFiles = this.getMediaFilesLocal();
    
    int originalSize = mediaFiles.size();
    
    MediaFile mediaFile = mediaFiles.get(0);
    
    try {
      assertTrue( mediaFile.destroy() );
    } catch (ServiceException e) {
      e.printStackTrace();
      fail( e.getMessage() );
    } catch (LoginException e) {
      e.printStackTrace();
      fail( e.getMessage() );
    }
    
    assertTrue( mediaFiles.size() == (originalSize - 1) );
  }
  
  
  
  
  private MediaFiles getMediaFilesLocal() {

    Playlists playlists = this.user.getPlaylists();
    
    try {
      
      playlists.load(false);
      
    } catch (ServiceException e) {
      
      fail( e.getMessage() );
      
    } catch (LoginException e) {
      
      fail( e.getMessage() );
      
    }
    
    Playlist local = playlists.getPlaylistByType( "LocalPlaylist" );
    
    MediaFiles mediaFiles = local.getMediaFiles();
    
    try {
      mediaFiles.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }

    return mediaFiles;
    
  }
  
}
