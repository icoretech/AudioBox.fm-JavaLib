/**
 * 
 */
package fm.audiobox.core.test;

import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFiles;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

/**
 * @author keytwo
 * 
 */
public class UserTest extends AudioBoxTestCase {

//  private static final String[] ADMIN_ALLOWED_FORMATS = "aac;mp3;mp2;m4a;m4b;m4r;mp4;3gp;ogg;oga;flac;spx;rm;ram;wav;mpc;mp+;mpp;aiff;aif;aifc;tta".split(";");

  @Test
  public void testUser() {

    loginCatched();
    
    assertSame(user, abc.getUser());

    assertNotNull(user.getBytesServed());
    assertNotNull(user.getEmail());
    assertEquals(Fixtures.get(Fixtures.LOGIN), user.getEmail());
    assertNotNull(user.getQuota());
    assertNotNull(user.getAvailableStorage());
    //assertEquals(162135015424L, user.getAvailableStorage());
    assertNotNull(user.getUsername());
    //assertEquals(Fixtures.get(Fixtures.USERNAME), user.getUsername());
    assertNotNull(user.getTimeZone());
    //assertTrue(user.getQuota() < user.getAvailableStorage());
//    assertNotNull(user.getAllowedFormats());
//    for (String fmt : user.getAllowedFormats()) {
//      assertTrue(Arrays.asList(ADMIN_ALLOWED_FORMATS).contains(fmt));
//    }

    
    try {
      Playlists pls = user.getPlaylists();
      pls.load(false);
      MediaFiles mdfs =  pls.get(0).getMediaFiles();
       mdfs.load(false);
//      mdfs.get(0).load();
      
//    	user.getUploadedTracks();
	} catch (ServiceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (LoginException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    // Profile
//    Profile p = user.getProfile();
//    assertNotNull(p.hasAutoplay());
//    assertNotNull(p.getRealName());
//    assertEquals(Fixtures.get(Fixtures.REAL_NAME), p.getRealName());
//    assertNull(p.getToken());
//
//    // Plan
//    Plan plan = user.getPlan();
//    assertEquals(plan.getName(), "admin");
//    assertTrue(plan.hasApi());
//    assertTrue(plan.hasDropbox());
//    assertTrue(plan.hasManager());
//    assertTrue(plan.hasLastfm());
//    assertTrue(plan.hasMultiformat());
//    assertTrue(plan.hasSocialNetworks());
//    assertTrue(plan.hasCloudWebPlayer());
//    assertTrue(plan.hasYoutubeChannel());
  }

//  @Test
//  public void testLoginShouldThrowsLoginExceptionOnWrongPassword() {
//    assertNotNull(abc);
//    try {
//      user = (User) abc.login(Fixtures.get(Fixtures.LOGIN), Fixtures.get(Fixtures.WRONG_PASS));
//    } catch (LoginException e) {
//      assertEquals(HttpStatus.SC_UNAUTHORIZED, e.getErrorCode());
//    } catch (ServiceException e) {
//      e.printStackTrace();
//      fail(e.getMessage());
//    }
//  }
//
//  @Test
//  public void testLoginShouldThrowsLoginExceptionOnWrongCredentials() {
//    assertNotNull(abc);
//    try {
//      user = (User) abc.login("wrong_user", Fixtures.get(Fixtures.WRONG_PASS));
//    } catch (LoginException e) {
//      assertEquals(401, e.getErrorCode());
//    } catch (ServiceException e) {
//      e.printStackTrace();
//      fail(e.getMessage());
//    }
//  }
//
//  @Test
//  public void testUserShouldBePopulatedWithRightCredentials() {
//    assertNotNull(abc);
//    loginCatched();
//    
//    assertNotNull(user.getProfile());
//    assertNotNull(user.getProfile().getRealName());
//    assertEquals(Fixtures.get(Fixtures.USERNAME), user.getUsername());
//  }
//
//  @Test
//  public void testUserLoginFailsOnUserNotActive() {
//    assertNotNull(abc);
//
//    try {
//      loginInactiveUser();
//    } catch (LoginException e) {
//      assertEquals(HttpStatus.SC_UNAUTHORIZED, e.getErrorCode());
//    } catch (ServiceException e) {
//      e.printStackTrace();
//      fail(e.getMessage());
//    }
//  }
//
//  @Test
//  public void testSingleTrack() {
//    try {
//      
//      assertNotNull(abc);
//
//      loginCatched();
//      
//      assertNotNull(user);
//
//      Track t = user.newTrackByToken(Fixtures.get(Fixtures.TRACK_TOKEN));
//
//      assertNotNull(t.getArtist());
//
//      assertEquals(t.getArtist().getName(), Fixtures.get(Fixtures.ARTIST_NAME));
//    } catch (ServiceException e) {
//      assertNull(e);
//    } catch (LoginException e) {
//      assertNull(e);
//    }
//
//  }
//
//  @Test
//  public void testUserCollections() {
//    loginCatched();
//
//    try {
//
//      Playlists pls = user.getPlaylists();
//      assertNotNull(pls);
//      assertEquals(0, pls.size());
//      pls.load(false);
//      assertTrue(0 < pls.size());
//
//      Genres gnr = user.getGenres();
//      assertNotNull(gnr);
//      assertEquals(0, gnr.size());
//      gnr.load(false);
//      assertTrue(0 < gnr.size());
//
//      Artists art = user.getArtists();
//      assertNotNull(art);
//      assertEquals(0, art.size());
//      art.load(false);
//      assertTrue(0 < art.size());
//
//      Albums alb = user.getAlbums();
//      assertNotNull(alb);
//      assertEquals(0, alb.size());
//      alb.load(false);
//      assertTrue(0 < alb.size());
//
//    } catch (LoginException e) {
//      e.printStackTrace();
//      fail(e.getMessage());
//    } catch (ServiceException e) {
//      e.printStackTrace();
//      fail(e.getMessage());
//    }
//  }
//
//  @Test
//  public void testUserGetUploadedTracks() {
//    assertNotNull(abc);
//    loginCatched();
//
//    try {
//      String[] tracks = user.getUploadedTracks();
//      assertNotNull(tracks);
//      assertNotNull(tracks[0]);
//      Playlists pls = user.getPlaylists();
//      pls.load(false);
//      Playlist music = pls.getPlaylistByType(PlaylistTypes.AUDIO);
//      assertNotNull(music);
//      music.getTracks().load(false);
//      assertEquals(tracks.length, music.getTracks().size());
//
//    } catch (LoginException e) {
//      e.printStackTrace();
//      fail(e.getMessage());
//    } catch (ServiceException e) {
//      e.printStackTrace();
//      fail(e.getMessage());
//    }
//  }
//
//  @Test
//  public void testUploadedTracks() {
//    
//    loginCatched();
//    
//    Playlists pls = user.getPlaylists();
//
//    assertNotNull(pls);
//
//    try {
//      pls.load(false);
//    } catch (ServiceException e) {
//      assertNull(e);
//    } catch (LoginException e) {
//      assertNull(e);
//    }
//
//    Playlist pl = pls.getPlaylistByType(PlaylistTypes.AUDIO);
//    assertNotNull(pl);
//    long tracksCount = pl.getTracksCount();
//
//    pl = pls.getPlaylistByType(PlaylistTypes.TRASH);
//    assertNotNull(pl);
//    tracksCount += pl.getTracksCount();
//
//    try {
//      String[] hashes = user.getUploadedTracks();
//      assertEquals(tracksCount, hashes.length);
//    } catch (ServiceException e) {
//      assertNull(e);
//    } catch (LoginException e) {
//      assertNull(e);
//    }
//
//  }

//  private void loginInactiveUser() throws LoginException, ServiceException {
//    user = (User) abc.login(Fixtures.get(Fixtures.INACTIVE_LOGIN), Fixtures.get(Fixtures.INACTIVE_RIGHT_PASS));
//  }

}
