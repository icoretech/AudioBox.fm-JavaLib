package fm.audiobox.core;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.AudioBox;
import fm.audiobox.configurations.DefaultConfiguration;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.Playlists.PlaylistTypes;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.ContentFormat;


public class CacheTest {

  
  private static Logger log = LoggerFactory.getLogger(CacheTest.class);
  
  private User user;
  private IConfiguration config;
  
  @Before
  public void setUp() throws Exception {
    
    config = new DefaultConfiguration("My test application");
    
    config.setVersion(1, 0, 0);
    config.setRequestFormat(ContentFormat.XML);
    config.setShortResponse(false);
    config.setUseCache(true);
    
    AudioBox abx = new AudioBox(config);
    
    user = abx.login( Fixtures.get( Fixtures.LOGIN ),  Fixtures.get( Fixtures.RIGHT_PASS ));
    
    assertNotNull(user);
    assertEquals(user.getUsername(), Fixtures.get( Fixtures.USERNAME ));
    
  }


  @Test
  public void timingManagement() throws ServiceException, LoginException {
    
    Playlists pls = user.getPlaylists();
    
    pls.load(false);
    
    
    
    Playlist pl =  pls.getPlaylistByType( PlaylistTypes.AUDIO );
    
    assertNotNull(pl);
    
    
    Tracks trs = pl.getTracks();

    long average = 0;
    int i = 0;
    
    for (; i < 2; i++){
      long start = System.currentTimeMillis();
      long end = 0;
      log.debug( "Start request at: " + start );
      
      trs.load(false);
      
      end = System.currentTimeMillis();
      if ( i > 0 ){
        average += (end-start);
      }
      log.debug("Total time: " + (end-start) );
    }
    
    log.info("Average: " + (average/(i-1) )  );
    
  }
  
  
  
  
}
