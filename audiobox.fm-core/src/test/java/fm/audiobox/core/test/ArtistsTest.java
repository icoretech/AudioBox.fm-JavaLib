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
import fm.audiobox.core.models.Artist;
import fm.audiobox.core.models.Artists;
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
public class ArtistsTest extends junit.framework.TestCase {

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
  public void testArtistsShouldBePopulated() {

    Artists artists = user.getArtists();
    assertNotNull(artists);
    
    try {
      artists.load(false);
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }

    Artist artist = null;

    for (Artist ar : artists) {
      Artist art = (Artist) ar;
      assertNotNull(art);
      artist = art;
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
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }
    
    
    Artist ar = (Artist) artists.get(0);
    assertNotNull(ar);

    Tracks trs = (Tracks) ar.getTracks();
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
