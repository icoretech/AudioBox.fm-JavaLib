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

/**
 * @author keytwo
 */
public class AlbumsTest extends AudioBoxTestCase {

  @Before
  public void setUp() {
    loginCatched();
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
      assertFalse(0 != albums.size());
      
      Album al = (Album) albums.get(0);
      assertNotNull(al);

      Tracks trs = (Tracks) al.getTracks();
      assertNotNull(trs);
      assertEquals(0, trs.size());

      trs.load(false);
      assertFalse(0 != trs.size());
      log.info("Found " + trs.size() + " tracks");

      Track tr = (Track) trs.get(0);
      assertNotNull(tr);
      
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }
  }

}
