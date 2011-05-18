package fm.audiobox.core.test;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Artist;
import fm.audiobox.core.models.Artists;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

/**
 * @author keytwo
 *
 */
public class ArtistsTest extends AudioBoxTestCase {

  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void testArtists(){
    Artists arts = user.getArtists();
    
    assertNotNull(arts);
    
    try {
      arts.load(false);
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (LoginException e) {
      e.printStackTrace();
    }
    
    Artist art = arts.getArtistByName(Fixtures.get(Fixtures.ARTIST_NAME));
    assertNotNull(art);
    
    art = arts.getArtistByName("abc");
    assertNull(art);
    
  }
  
  @Test
  public void testArtistsShouldBePopulated() {

    Artists artists = user.getArtists();
    assertNotNull(artists);
    assertEquals(0, artists.size());
    
    try {
      artists.load(false);
      log.info("Found " + artists.size() + " artists");
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }

    Artist artist = null;
    for (Artist ar : artists) {
      assertNotNull(ar);
      artist = ar;
    }

    assertNotNull(artist);

    Artist ar = (Artist) artists.get(artist.getToken());
    assertNotNull( ar );
    assertSame( ar, artist );

  }

  @Test
  public void testArtistshouldBePopulatedAndContainsTracks() {
    Artists artists = user.getArtists();
    assertNotNull(artists);

    try {
      artists.load(false);
      assertTrue( 0 < artists.size());
      log.info("Found " + artists.size() + " artists");
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }
    
    
    Artist ar = (Artist) artists.get(0);
    log.info("Loading " + ar.getName() + " tracks...");
    assertNotNull(ar);

    Tracks trs = (Tracks) ar.getTracks();
    assertNotNull(trs);
    assertEquals(0, trs.size());

    try {
      trs.load(false);
      log.info("Found " + trs.size() + " tracks");
      assertTrue(0 < trs.size());
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }

    Track tr = (Track) trs.get(0);
    assertNotNull(tr);

  }


}
