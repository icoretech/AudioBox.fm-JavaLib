package fm.audiobox.core;

import org.junit.Test;

import fm.audiobox.AudioBox;
import fm.audiobox.configurations.DefaultConfiguration;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Album;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.Artist;
import fm.audiobox.core.models.Artists;
import fm.audiobox.core.models.Genre;
import fm.audiobox.core.models.Genres;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.Playlists.PlaylistTypes;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.RequestFormat;

public class AudioBoxRefactorTest extends junit.framework.TestCase {

  
  private User user;
  
  
  @Override
  protected void setUp() throws Exception {
    
    IConfiguration configuration = new DefaultConfiguration("My test application");
    
    configuration.setVersion(1, 0, 0);
    configuration.setRequestFormat(RequestFormat.XML);
    configuration.setShortResponse(false);
    configuration.setUseCache(false);
    
    AudioBox abx = new AudioBox(configuration);
    
    user = abx.login( Fixtures.get( Fixtures.LOGIN ),  Fixtures.get( Fixtures.RIGHT_PASS ));
    
    assertNotNull(user);
    assertEquals(user.getUsername(), "fatshotty");
    
    super.setUp();
  }



  @Test
  public void testUser() {
    
    assertEquals(user.getUsername(), "fatshotty");
    
    // used temporarly
    user.setUsername("");
    
    try {
      user.load();
    } catch (ServiceException e) {
      assertNull(e);
    } catch (LoginException e) {
      assertNull(e);
    }
    
    assertEquals(user.getUsername(), "fatshotty");
  }


  @Test
  public void testPlaylists() {
    
    Playlists pls = user.getPlaylists();
    
    assertNotNull(pls);
    
    try {
      pls.load();
    } catch (ServiceException e) {
      assertNull(e);
    } catch (LoginException e) {
      assertNull(e);
    }
    
    Playlist pl = pls.getPlaylistByName("Last uploaded");
    
    assertEquals( pl.getName(), "Last uploaded" );
    
    pl = pls.getPlaylistByType( PlaylistTypes.AUDIO );
    
    assertEquals( pl.getName(), "Music" );
    
    
    pl = pls.getPlaylistByType( PlaylistTypes.OFFLINE );
    
    assertEquals( pl.getName(), "Offline" );
    
  }
  
  public void testArtists(){
    Artists arts = user.getArtists();
    
    assertNotNull(arts);
    
    try {
      arts.load();
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (LoginException e) {
      e.printStackTrace();
    }
    
    Artist art = arts.getArtistByName("Alborosie");
    
    assertNotNull(art);
    
    art = arts.getArtistByName("abc");
    
    assertNull(art);
    
  }
  
  
  public void testAlbums(){
    Albums albs = user.getAlbums();
    
    assertNotNull(albs);
    
    try {
      albs.load();
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (LoginException e) {
      e.printStackTrace();
    }
    
    
    Album alb = albs.getAlbumByName("Escape From Babylon");
    
    assertNotNull( alb );
    
    
    alb = albs.getAlbumByName("abc");
    
    assertNull( alb );
    
  }
  
  
  public void testGenres(){
    Genres gnrs = user.getGenres();
    
    
    assertNotNull(gnrs);
    
    try {
      gnrs.load();
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (LoginException e) {
      e.printStackTrace();
    }
    
    
    Genre alb = gnrs.getGenreByName("Raggae");
    
    assertNotNull( alb );
    
    
    alb = gnrs.getGenreByName("abc");
    
    assertNull( alb );
    
  }
  
}
