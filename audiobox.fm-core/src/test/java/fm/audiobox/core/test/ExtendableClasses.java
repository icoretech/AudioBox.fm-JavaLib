package fm.audiobox.core.test;

import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFiles;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.test.mocks.models.MediaFile;
import fm.audiobox.core.test.mocks.models.Playlist;

public class ExtendableClasses extends AudioBoxTestCase {
  
  
  
  
  @Test
  public void testExtendsMediaFiles() {
    
    abc.getConfiguration().getFactory().setEntity( Playlist.TAGNAME, Playlist.class);
    abc.getConfiguration().getFactory().setEntity( MediaFile.TAGNAME, MediaFile.class);
    
    loginCatched();
    
    Playlists pls = user.getPlaylists();
    assertNotNull(pls);
    
    try {
      pls.load(false);
      Playlist pl = (Playlist) pls.getPlaylistByType("DropboxPlaylist");
      
      MediaFiles mfs = (MediaFiles) pl.getMediaFiles();
      mfs.load(false);
      
      MediaFile mf = (MediaFile) mfs.get(0);
      
      assertEquals( MediaFile.class.getPackage().getName() , mf.getClass().getPackage().getName() );
      
      
    } catch (ServiceException e) {
      assertNull(e);
    } catch (LoginException e) {
      assertNull(e);
    }
    
    
    
  }

}
