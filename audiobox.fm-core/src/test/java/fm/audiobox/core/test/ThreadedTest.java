package fm.audiobox.core.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFile;
import fm.audiobox.core.models.MediaFiles;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

public class ThreadedTest extends AudioBoxTestCase {

  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void testThread() throws InterruptedException {

    assertNotNull(abc);
    assertNotNull(user);

    final Handle h1 = new Handle();
    final Handle h2 = new Handle();

    Thread t1 = new Thread(new Runnable() {

      @Override
      public void run() {
        try {

          Log logger = LogFactory.getLog("Test thread #1");
          logger.debug("Started thread #1");

          MediaFiles mediaFiles = user.getMediaFilesMap( MediaFile.Source.cloud.toString() );
          assertNotNull(mediaFiles);
          assertTrue(mediaFiles.size() > 0);
          for (int i = 0, l = mediaFiles.size(); i < l; i++){
            assertNotNull( mediaFiles.get(i).getHash() );
            assertTrue( mediaFiles.get(i).getHash().length() == 32 );            
          }
            
          h1.setDone(true);

          logger.debug("Ended thread #1");

        } catch (LoginException e) {
          e.printStackTrace();
          fail(e.getMessage());
        } catch (ServiceException e) {
          e.printStackTrace();
          fail(e.getMessage());
        }
      }

    });

    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {

          Log logger = LogFactory.getLog("Test thread #2");
          logger.debug("Started thread #2");

          Playlists pls = (Playlists) user.getPlaylists();
          assertNotNull(pls);
          pls.load(false);

          Playlist pl = pls.getPlaylistByName(Fixtures.get(Fixtures.SMALL_PLAYLIST_NAME));
          assertNotNull(pl);

          MediaFiles tracks = (MediaFiles) pl.getMediaFiles();
          assertNotNull(tracks);
          tracks.load(false);

          MediaFile tr = tracks.get(0);

          assertNotNull(tr);
          assertNotNull(tr.getTitle());
          assertNotNull(tr.getToken());

          h2.setDone(true);

          logger.debug("Ended thread #2");

        } catch (LoginException e) {
          e.printStackTrace();
          fail(e.getMessage());
        } catch (ServiceException e) {
          e.printStackTrace();
          fail(e.getMessage());
        }
      }
    });

    t1.start();
    t2.start();

    while (t1.isAlive() || t2.isAlive()) {
      Thread.sleep(2500);
    }

    assertTrue(h1.getDone() && h2.getDone());
  }

  private class Handle {

    private boolean done = false;

    public void setDone(boolean done) {
      this.done = done;
    }

    public boolean getDone() {
      return this.done;
    }
  }
}
