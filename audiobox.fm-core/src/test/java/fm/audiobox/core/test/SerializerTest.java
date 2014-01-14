package fm.audiobox.core.test;

import fm.audiobox.core.exceptions.ForbiddenException;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import fm.audiobox.configurations.DefaultRequestSerializer;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFiles;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;

public class SerializerTest extends AbxTestCase {

  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void serializeUser() {
    
    DefaultRequestSerializer drs = new DefaultRequestSerializer();
    JsonElement obj = null;
    
    obj = drs.serializeJson( this.abx.getUser() );
    
    Gson g = new Gson();
    System.out.println( g.toJson( obj ) );
    
    
  }
  
  
  @Test
  public void serializePlaylistsAndMediaFiles() {
    
    DefaultRequestSerializer drs = new DefaultRequestSerializer();
    
    Playlists pls = this.abx.getUser().getPlaylists();
    
    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail(e.getMessage());
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ForbiddenException e) {
      fail(e.getMessage());
    }

    Playlist pl = pls.getPlaylistByType( Playlists.Type.CloudPlaylist );
    
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


    Gson g = new Gson();
    JsonElement obj = null;
    
    obj = drs.serializeJson( pls );
    System.out.println( g.toJson( obj ) );
    
    
    obj = drs.serializeJson( mfs );
    System.out.println( g.toJson( obj ) );
    
    
  }
  
}
