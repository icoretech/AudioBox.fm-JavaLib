package fm.audiobox.core.test;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Album;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.Covers;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.interfaces.IConfiguration;

/**
 * @author keytwo
 */
public class AlbumsShortTest extends AudioBoxTestCase {

  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void testAlbums(){
    Albums albs = user.getAlbums();
    
    assertNotNull(albs);
    
    try {
      albs.load(false);
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (LoginException e) {
      e.printStackTrace();
    }
    
    
    Album alb = albs.getAlbumByName(Fixtures.get(Fixtures.ALBUM_NAME));
    assertNotNull( alb );
    
    Tracks trks = alb.getTracks();
    assertNotNull( trks );
    assertSame(trks, alb.getTracks());
    
    alb = albs.getAlbumByName("abc");
    assertNull( alb );
    
  }
  
  @Test
  public void testAlbumsShouldBePopulated() {
    Albums albums = user.getAlbums();
    assertNotNull(albums);

    try {
      albums.load(false);
      log.info("Found " + albums.size() + " albums");
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }

    Album album = null;
    for (Album alb : albums) {
      assertNotNull(alb);
      assertNotNull(alb.getArtist());
      assertNotNull(alb.getCovers());
      assertNotNull(alb.getToken());
      album = alb;
    }

    Album al = (Album) albums.get( album.getToken() );
    assertNotNull(al);
    assertSame(al, album);

    Covers c = al.getCovers();
    assertNotNull(c.getLarge());
    assertNotNull(c.getSmall());
    assertNotNull(c.getMedium());

  }

  @Test
  public void testAlbumShouldBePopulatedAndContainsTracks() {

    try {
      Albums albums = user.getAlbums();
      assertNotNull(albums);
      assertEquals(0, albums.size());

      albums.load(false);
      assertTrue(0 < albums.size());
      
      Album al = (Album) albums.get(0);
      assertNotNull(al);

      Tracks trs = (Tracks) al.getTracks();
      assertNotNull(trs);
      assertEquals(0, trs.size());

      trs.load(false);
      assertTrue(0 < trs.size());
      log.info("Found " + trs.size() + " tracks");

      Track tr = (Track) trs.get(0);
      assertNotNull(tr);
      
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }
  }
  
  @Override
  protected IConfiguration getConfig() {
    IConfiguration config = super.getConfig();
    config.setShortResponse(true);
    return config;
  }

}
