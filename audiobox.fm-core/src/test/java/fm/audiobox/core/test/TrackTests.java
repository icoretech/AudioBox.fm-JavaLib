package fm.audiobox.core.test;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

public class TrackTests extends AudioBoxTestCase {

  @Before
  public void setUp() {
    loginCatched();
  }

  //@Test
  public void testTrackActions() {
    Track t = null;
    try {
      t = user.newTrackByToken(Fixtures.get(Fixtures.TRACK_TOKEN));
      assertNotNull(t);

      int playCount = t.getPlayCount();
      t.scrobble();
      assertEquals(t.getPlayCount(), playCount + 1);

      boolean loved = t.isLoved();
      t.toggleLove();
      assertTrue(loved != t.isLoved());

    } catch (ServiceException e) {
      fail(e.getMessage());
    } catch (LoginException e) {
      fail(e.getMessage());
    }
  }

}
