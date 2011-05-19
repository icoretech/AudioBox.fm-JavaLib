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
import fm.audiobox.core.models.Album;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.Playlists.PlaylistTypes;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
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
    
    pl = pls.getPlaylistByType( PlaylistTypes.AUDIO );
    assertEquals( pl.getName(), "Music" );
    
    pl = pls.getPlaylistByType( PlaylistTypes.OFFLINE );
    assertEquals( pl.getName(), "Offline" );
    
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
      assertNotNull(pl.getPlaylistType());
      assertNotNull(pl.getTracksCount());
      assertNotNull(pl.getPosition());
      testPl = pl;
    }

    assertNotNull(testPl);

    Tracks trs = (Tracks) testPl.getTracks();
    assertNotNull(trs);
    assertEquals(0, trs.size());

    trs.load(false);
    assertTrue(0 < trs.size());

    Track tr = (Track) trs.get(0);
    assertNotNull(tr);
    assertNotNull(tr.getName());
    assertNotNull(tr.getToken());

    assertNotNull(tr.getArtist());
    assertNotNull(tr.getArtist().getToken());

    assertNotNull(tr.getAlbum());
    assertNotNull(tr.getAlbum().getToken());

    assertNotNull(((Album) tr.getAlbum()).getArtist());

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

    List<Playlist> pls = playlists.getPlaylistsByType(PlaylistTypes.AUDIO);
    assertEquals(1, pls.size());

    pls = playlists.getPlaylistsByType(PlaylistTypes.TRASH);
    assertEquals(1, pls.size());

    pls = playlists.getPlaylistsByType(PlaylistTypes.VIDEO);
    assertEquals(1, pls.size());

    pls = playlists.getPlaylistsByType(PlaylistTypes.CUSTOM);
    assertNotNull(pls);
  }

  @Test
  public void testMoveSingleTrackToPlaylist() throws LoginException, ServiceException {

    Playlists playlists = user.getPlaylists();
    assertNotNull(playlists);
    playlists.load(false);
    
    Playlist smallPlaylist = playlists.getPlaylistByName(Fixtures.get(Fixtures.SMALL_PLAYLIST_NAME));
    Playlist dev = playlists.getPlaylistByName(Fixtures.get(Fixtures.DEV_PLAYLIST_NAME));
    
    smallPlaylist.getTracks().load(false);
    dev.getTracks().load(false);
    
    Track trk = smallPlaylist.getTracks().get(0);

    long previousTracksCount = dev.getTracksCount();

    boolean result = dev.addTrack(trk);
    if (result) {
      assertEquals(previousTracksCount + 1, dev.getTracksCount());
      assertNotNull(dev.getTracks().get(trk.getToken()));
    } else {
      // Fail and exit
      fail("Unable to move track to the selected playlist");
    }

    result = dev.removeTrack(trk);
    if (result) {
      assertEquals(previousTracksCount, dev.getTracksCount());
      assertNull(dev.getTracks().get(trk.getToken()));
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
    
    smallPlaylist.getTracks().load(false);
    dev.getTracks().load(false);
    
    Tracks musicTracks = smallPlaylist.getTracks();

    List<Track> tracks = new ArrayList<Track>();
    int numberOfTracksToAdd = 10;
    for (int i = 0; i < numberOfTracksToAdd; i++) {
      tracks.add(musicTracks.get(i));
    }

    long previousTracksCount = dev.getTracksCount();

    boolean result = dev.addTracks(tracks);
    if (result) {
      assertEquals(previousTracksCount + numberOfTracksToAdd, dev.getTracksCount());
      for (Track trk : tracks) {
        assertNotNull(dev.getTracks().get(trk.getToken()));
      }
    } else {
      // Fail and exit
      fail("Unable to move track to the selected playlist");
    }

    result = dev.removeTracks(tracks);
    if (result) {
      assertEquals(previousTracksCount , dev.getTracksCount());
      for (Track trk : tracks) {
        assertNull(dev.getTracks().get(trk.getToken()));
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
    Track trk = user.newTrack();

    assertNotNull(trk);

    trk.setToken("foo-bar");

    assertFalse(dev.addTrack(trk));
    assertFalse(dev.removeTrack(trk));

  }

}
