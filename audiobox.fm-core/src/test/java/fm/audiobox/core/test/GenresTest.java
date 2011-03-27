/**
 * 
 */
package fm.audiobox.core.test;


import org.junit.Before;
import org.junit.Test;

import fm.audiobox.AudioBox;
import fm.audiobox.configurations.DefaultConfiguration;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Genre;
import fm.audiobox.core.models.Genres;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.RequestFormat;

/**
 * @author keytwo
 *
 */
public class GenresTest extends junit.framework.TestCase {

  AudioBox abx;
  User user;

  @Before
  public void setUp() throws Exception {
    IConfiguration configuration = new DefaultConfiguration("My test application");

    configuration.setVersion(1, 0, 0);
    configuration.setRequestFormat(RequestFormat.XML);
    configuration.setShortResponse(false);
    configuration.setUseCache(false);

    abx = new AudioBox(configuration);

    try {
      user = abx.login( Fixtures.get( Fixtures.LOGIN ), Fixtures.get( Fixtures.RIGHT_PASS ) );
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }

    assertNotNull( user );

  }


  @Test
  public void testGenresShouldBePopulated() {
      Genres genres = user.getGenres();
      assertNotNull(genres);

      try {
        genres.load(false);
      } catch (LoginException e) {
        fail(e.getMessage());
      } catch (ServiceException e) {
        fail(e.getMessage());
      }
      
      Genre genre = null;

      for (Genre g : genres) {
        assertNotNull(g);
        genre = g;
      }

      Genre g = (Genre) genres.get( genre.getToken() );
      assertNotNull( g );
      assertSame( g, genre);

  }

  @Test
  public void testGenreShouldBePopulatedAndContainsTracks() {

      Genres genres = user.getGenres();
      assertNotNull(genres);
      
      try {
        genres.load(false);
      } catch (LoginException e) {
        fail(e.getMessage());
      } catch (ServiceException e) {
        fail(e.getMessage());
      }

      Genre gnr = (Genre) genres.get(0);
      assertNotNull(gnr);

      Tracks trs = (Tracks) gnr.getTracks();
      assertNotNull(trs);
      
      try {
        trs.load(false);
      } catch (LoginException e) {
        fail(e.getMessage());
      } catch (ServiceException e) {
        fail(e.getMessage());
      }

      Track tr = (Track) trs.get(0);
      assertNotNull(tr);

  }

}
