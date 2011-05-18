/**
 * 
 */
package fm.audiobox.core.test;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Genre;
import fm.audiobox.core.models.Genres;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;

/**
 * @author keytwo
 * 
 */
public class GenresTest extends AudioBoxTestCase {

  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void testGenresShouldBePopulated() {
    try {
      Genres genres = user.getGenres();
      assertNotNull(genres);
      assertEquals(0, genres.size());

      genres.load(false);
      assertTrue(0 < genres.size());

      Genre genre = null;

      for (Genre g : genres) {
        assertNotNull(g);
        assertNotNull(g.getName());
        genre = g;
      }

      Genre g = (Genre) genres.get(genre.getToken());
      assertNotNull(g);
      assertSame(g, genre);

    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGenreShouldBePopulatedAndContainsTracks() {

    try {
      Genres genres = user.getGenres();
      assertNotNull(genres);
      assertEquals(0, genres.size());

      genres.load(false);
      assertTrue(0 < genres.size());

      genres.load(false);

      Genre gnr = (Genre) genres.get(0);
      assertNotNull(gnr);

      Tracks trs = (Tracks) gnr.getTracks();
      assertEquals(0, trs.size());
      assertNotNull(trs);

      trs.load(false);
      assertTrue(0 < trs.size());

      Track tr = (Track) trs.get(0);
      assertNotNull(tr);

    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }
  }

}
