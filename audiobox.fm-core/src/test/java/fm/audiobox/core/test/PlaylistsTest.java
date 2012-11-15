/**
 * 
 */
package fm.audiobox.core.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFile;
import fm.audiobox.core.models.MediaFiles;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

/**
 * @author keytwo
 * 
 */
public class PlaylistsTest extends AudioBoxTestCase {

  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void testPlaylists() {
    Playlists pls = user.getPlaylists();
    assertNotNull(pls);
    
    try {
      pls.load(false);
    } catch (ServiceException e) {
      assertNull(e);
    } catch (LoginException e) {
      assertNull(e);
    }
    
    Playlist pl = pls.getPlaylistByName(Fixtures.get( Fixtures.SMALL_PLAYLIST_NAME ));
    assertEquals( pl.getName(), Fixtures.get( Fixtures.SMALL_PLAYLIST_NAME ) );
    
    pl = pls.getPlaylistByType( "LocalPlaylist" );
    assertNotNull( pl.getName() );
    
    pl = pls.getPlaylistByType( "CloudPlaylist" );
    assertNotNull( pl.getName() );
    
  }
  
  @Test
  public void testPlaylistsShouldBePopulated() throws ServiceException, LoginException {
    Playlists playlists = user.getPlaylists();
    assertNotNull(playlists);
    assertEquals(0, playlists.size());

    playlists.load(false);
    assertTrue(0 < playlists.size());
    
    Playlist playlist = null;

    for (Playlist pls : playlists) {
      assertNotNull(pls);
      playlist = pls;
    }

    Playlist p = (Playlist) playlists.get(playlist.getToken());
    assertNotNull(p);
    assertSame(p, playlist);

  }

  @Test
  public void testPlaylistShouldBePopulatedAndContainsTracks() throws ServiceException, LoginException {

    Playlists playlists = user.getPlaylists();
    assertNotNull(playlists);
    assertEquals(0, playlists.size());

    playlists.load(false);
    assertTrue(0 < playlists.size());
    
    Playlist testPl = null;

    for (Playlist pl : playlists) {
      assertNotNull(pl);
      assertNotNull(pl.getName());
      assertNotNull(pl.getToken());
      assertNotNull(pl.getType());
      assertNotNull(pl.getMediaFilesCount());
      assertNotNull(pl.getPosition());
      testPl = pl;
    }

    assertNotNull(testPl);

    MediaFiles trs = (MediaFiles) testPl.getMediaFiles();
    assertNotNull(trs);
    assertEquals(0, trs.size());

    trs.load(false);
    assertTrue(0 < trs.size());

    MediaFile tr = (MediaFile) trs.get(0);
    assertNotNull(tr);
    assertNotNull(tr.getTitle());
    assertNotNull(tr.getToken());
  }

  @Test
  public void testPlaylistShouldBeNullIfItDoesNotExists() throws ServiceException, LoginException {

    Playlists playlists = user.getPlaylists();
    assertNotNull(playlists);
    assertEquals(0, playlists.size());
    
    playlists.load(false);
    assertTrue(0 < playlists.size());
    
    assertNull(playlists.getPlaylistByName("Not existings playlist"));

  }

  @Test
  public void testPlaylistsTypes() throws ServiceException, LoginException {

    Playlists playlists = user.getPlaylists();
    assertNotNull(playlists);
    assertEquals(0, playlists.size());
    
    playlists.load(false);
    assertTrue(0 < playlists.size());

    List<Playlist> pls = playlists.getPlaylistsByType( "LocalPlaylist" );
    assertEquals(1, pls.size());

    pls = playlists.getPlaylistsByType( "CloudPlaylist" );
    assertEquals(1, pls.size());

    pls = playlists.getPlaylistsByType( "CustomPlaylist" );
    assertNotNull(pls);
  }

  @Test
  public void testMoveSingleTrackToPlaylist() throws LoginException, ServiceException {

    Playlists playlists = user.getPlaylists();
    assertNotNull(playlists);
    playlists.load(false);
    
    Playlist smallPlaylist = playlists.getPlaylistByName(Fixtures.get(Fixtures.SMALL_PLAYLIST_NAME));
    Playlist dev = playlists.getPlaylistByName(Fixtures.get(Fixtures.DEV_PLAYLIST_NAME));
    
    smallPlaylist.getMediaFiles().load(false);
    dev.getMediaFiles().load(false);
    
    MediaFile trk = smallPlaylist.getMediaFiles().get(0);

    long previousTracksCount = dev.getMediaFilesCount();

    boolean result = dev.addMediaFile(trk);
    if (result) {
      assertEquals(previousTracksCount + 1, dev.getMediaFilesCount());
      assertNotNull(dev.getMediaFiles().get(trk.getToken()));
    } else {
      // Fail and exit
      fail("Unable to move track to the selected playlist");
    }

    result = dev.removeMediaFile(trk);
    if (result) {
      assertEquals(previousTracksCount, dev.getMediaFilesCount());
      assertNull(dev.getMediaFiles().get(trk.getToken()));
    } else {
      // Fail and exit
      fail("Unable to move track to the selected playlist");
    }

  }

  @Test
  public void testMoveMultipleTracksToPlaylist() throws LoginException, ServiceException {

    Playlists playlists = user.getPlaylists();
    assertNotNull(playlists);
    playlists.load(false);

    Playlist smallPlaylist = playlists.getPlaylistByName(Fixtures.get(Fixtures.SMALL_PLAYLIST_NAME));
    Playlist dev = playlists.getPlaylistByName(Fixtures.get(Fixtures.DEV_PLAYLIST_NAME));
    
    smallPlaylist.getMediaFiles().load(false);
    dev.getMediaFiles().load(false);
    
    MediaFiles musicTracks = smallPlaylist.getMediaFiles();

    List<MediaFile> tracks = new ArrayList<MediaFile>();
    int numberOfTracksToAdd = 10;
    for (int i = 0; i < numberOfTracksToAdd; i++) {
      tracks.add(musicTracks.get(i));
    }

    long previousTracksCount = dev.getMediaFilesCount();

    boolean result = dev.addMediaFiles(tracks);
    if (result) {
      assertEquals(previousTracksCount + numberOfTracksToAdd, dev.getMediaFilesCount());
      for (MediaFile trk : tracks) {
        assertNotNull(dev.getMediaFiles().get(trk.getToken()));
      }
    } else {
      // Fail and exit
      fail("Unable to move track to the selected playlist");
    }

    result = dev.removeMediaFiles(tracks);
    if (result) {
      assertEquals(previousTracksCount , dev.getMediaFilesCount());
      for (MediaFile trk : tracks) {
        assertNull(dev.getMediaFiles().get(trk.getToken()));
      }
    } else {
      // Fail and exit
      fail("Unable to move track to the selected playlist");
    }

  }

  @Test
  public void testMoveUnexistingsTrackShouldRiseErrors() throws LoginException, ServiceException {

    Playlists playlists = user.getPlaylists();
    assertNotNull(playlists);
    playlists.load(false);

    Playlist dev = playlists.getPlaylistByName(Fixtures.get(Fixtures.DEV_PLAYLIST_NAME));
    MediaFile trk = user.newTrack();

    assertNotNull(trk);

    trk.setToken("foo-bar");

    assertFalse(dev.addMediaFile(trk));
    assertFalse(dev.removeMediaFile(trk));

  }

}
