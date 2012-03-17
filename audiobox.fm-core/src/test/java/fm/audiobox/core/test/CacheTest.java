package fm.audiobox.core.test;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.configurations.Response;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.Playlists.Types;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.interfaces.IConfiguration;


public class CacheTest extends AudioBoxTestCase {

  @Before
  public void setUp() throws Exception {
    loginCatched();
  }


  @Test
  public void timingManagement() throws ServiceException, LoginException {
    
    Playlists pls = user.getPlaylists();
    pls.load(false);
    
    
    Playlist pl =  pls.getPlaylistByType( Types.OFFLINE );
    assertNotNull(pl);
    
    
    Tracks trs = pl.getTracks();

    long average = 0;
    int i = 0;
    
    for (; i < 5; i++){
      long start = System.currentTimeMillis();
      long end = 0;
      log.debug( "**** Start request at: " + start );
      
      Response rsp = trs.load(false);
      if (i == 0) {
        assertEquals(HttpStatus.SC_OK, rsp.getStatus());
      } else {
        assertEquals(HttpStatus.SC_NOT_MODIFIED, rsp.getStatus());
      }
      end = System.currentTimeMillis();
      if ( i > 0 ){
        average += (end-start);
      }
      log.debug("**** Request completed in " + (end-start) + "ms" );
    }
    
    log.info("Average requests time: " + (average/(i-1) ) + "ms" );
    
  }


  @Override
  protected IConfiguration getConfig() {
    IConfiguration config = super.getConfig();
    config.setUseCache(true);
    return config;
  }
  
  
}
