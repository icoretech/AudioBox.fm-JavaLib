package fm.audiobox.core.test;

import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFiles;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.test.mocks.models.MediaFile;
import fm.audiobox.core.test.mocks.models.Playlist;

public class ExtendableClasses extends AbxTestCase {
  
  
  
  
  @Test
  public void testExtendsMediaFiles() {
    
    abx.getConfiguration().getFactory().setEntity( Playlist.TAGNAME, Playlist.class);
    abx.getConfiguration().getFactory().setEntity( MediaFile.TAGNAME, MediaFile.class);
    
    loginCatched();
    
    Playlists pls = user.getPlaylists();
    assertNotNull(pls);
    
    try {
      pls.load(false);
      Playlist pl = (Playlist) pls.getPlaylistByType( Playlists.Type.CloudPlaylist );
      
      MediaFiles mfs = pl.getMediaFiles();
      mfs.load(false);
      
      MediaFile mf = (MediaFile) mfs.get(0);
      
      assertSame( MediaFile.class, mf.getClass() );
      assertSame( Playlist.class, pl.getClass() );
      
      
    } catch (ServiceException e) {
      assertNull(e);
    } catch (LoginException e) {
      assertNull(e);
    }
    
    
    
  }

}
