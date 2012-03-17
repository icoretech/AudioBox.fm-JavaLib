package fm.audiobox.core.test;

import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFile;
import fm.audiobox.core.models.MediaFiles;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.observables.Event;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

/**
 * @author keytwo
 */
public class CollectionListenerTest extends AudioBoxTestCase {

  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void testCL() throws ServiceException, LoginException {

    assertNotNull(abc);

    Playlists pls = user.getPlaylists();
    assertNotNull(pls);
    pls.addObserver(new Observer() {

      @Override
      public void update(Observable o, Object arg) {
        Event ev = (Event) arg;
        log.debug("Event occurred: " + ev.state);
        if (ev.state == Event.States.ENTITY_ADDED) {
          assertTrue(ev.getSource() instanceof Playlist);
          Playlist pl = (Playlist) ((Event) arg).getSource();
          log.info("Playlists size is: " + ((Playlists) o).size() + " | " + pl.getName());
        }
      }
    });

    pls.load(false);

    Playlist dev = user.getPlaylists().getPlaylistByName(Fixtures.get(Fixtures.SMALL_PLAYLIST_NAME));

    /*
     * Let's test "reusability"
     */
    Playlist dev2 = null;
    for (Playlist p : pls) {
      if (p.getName().equals(Fixtures.get(Fixtures.SMALL_PLAYLIST_NAME))) {
        dev2 = p;
        break;
      }
    }

    assertNotNull(dev);
    assertSame(dev, dev2);

    dev.getMediaFiles().addObserver(new Observer() {

      @Override
      public void update(Observable o, Object arg) {
        Event ev = (Event) arg;
        if (ev.state == Event.States.ENTITY_ADDED) {
          assertTrue(ev.getSource() instanceof MediaFile);
          MediaFile trk = (MediaFile) ev.getSource();
          log.info("Tracks count is: " + ((MediaFiles) o).size() + " | " + trk.getTitle());
        }

      }

    });

    dev.getMediaFiles().load(false);

  }

}
