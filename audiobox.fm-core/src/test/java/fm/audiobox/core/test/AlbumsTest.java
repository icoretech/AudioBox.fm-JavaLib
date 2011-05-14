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
import fm.audiobox.core.models.Album;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.Covers;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.ContentFormat;

/**
 * @author keytwo
 *
 */
public class AlbumsTest extends junit.framework.TestCase {

  AudioBox abx;
  User user;

  @Before
  public void setUp() throws Exception {
    IConfiguration configuration = new DefaultConfiguration("My test application");

    configuration.setVersion(1, 0, 0);
    configuration.setRequestFormat(ContentFormat.XML);
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
  public void testAlbumsShouldBePopulated() {
    Albums albums = user.getAlbums();
    assertNotNull(albums);

    try {
      albums.load(false);
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }

    Album album = null;

    for (Album al : albums) {
      Album alb = (Album) al;
      assertNotNull( alb );
      assertNotNull( alb.getCovers() );
      assertNotNull( alb.getArtist() );
      album = alb;
    }

    Album al = (Album) albums.get( album.getToken() );
    assertNotNull( al );
    assertSame( al, album);

    Covers c = al.getCovers();
    assertNotNull( c.getLarge() );
    assertNotNull( c.getSmall() );
    assertNotNull( c.getMedium() );

  }


  @Test
  public void testAlbumShouldBePopulatedAndContainsTracks() {


    Albums albums = user.getAlbums();
    assertNotNull(albums);

    try {
      albums.load(false);
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }

    
    Album al = (Album) albums.get(0);
    assertNotNull(al);

    Tracks trs = (Tracks) al.getTracks();
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
