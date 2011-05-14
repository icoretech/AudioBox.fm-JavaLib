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
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.core.test.mocks.models.Album;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.ContentFormat;

/**
 * @author keytwo
 *
 */
public class CustomAlbumsTest extends junit.framework.TestCase {

  AudioBox abx;
  User user;

  @Before
  public void setUp() throws Exception {
    IConfiguration configuration = new DefaultConfiguration("My test application");

    configuration.setVersion(1, 0, 0);
    configuration.setRequestFormat(ContentFormat.XML);
    configuration.setShortResponse(false);
    configuration.setUseCache(false);
    
    configuration.getFactory().setEntity( Album.TAGNAME, Album.class);

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
      try {
        albums.load(false);
      } catch (LoginException e) {
        fail(e.getMessage());
      } catch (ServiceException e) {
        fail(e.getMessage());
      }
      
      
      for ( fm.audiobox.core.models.Album al : albums ){
        assertTrue( al instanceof Album );
      }
      
      
    }


}
