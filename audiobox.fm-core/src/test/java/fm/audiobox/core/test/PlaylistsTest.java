/**
 * 
 */
package fm.audiobox.core.test;

import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.observables.Event;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

/**
 * @author keytwo
 * 
 */
public class PlaylistsTest extends AbxTestCase {

  private int number_event = 0;
  
  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void allPlaylists() {
    Playlists pls = user.getPlaylists();
    assertNotNull(pls);
    
    assertSame( pls.size(), 0);
    
    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    assertFalse( pls.size() == 0 );
    assertEquals( user.getPlaylistsCount(), pls.size() );
  }
  
  @Test
  public void playlistShouldBeCorrectlyPopulated() {
    Playlists pls = user.getPlaylists();

    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    for (Playlist pl : pls) {
      assertNotNull( pl );
      assertNotNull( pl.getName() );
      assertNotNull( pl.getToken() );
      assertNotNull( pl.getType() );
      assertNotNull( pl.getMediaFilesCount() );
      assertNotNull( pl.getPosition() );
    }
    
    
    Playlist playlist = pls.getPlaylistByType( Playlists.Type.CloudPlaylist );

    Playlist p = (Playlist) pls.get( playlist.getToken() );
    assertNotNull(p);
    assertSame(p, playlist);
    
    assertNull( pls.get("fake_token") );
    
    assertEquals( p.getType(), Playlists.Type.CloudPlaylist.toString() );
    
    Playlist pl = pls.getPlaylistByName( Fixtures.get( Fixtures.CUSTOM_PLAYLIST_NAME ) );
    assertNotNull( pl );
    assertTrue( pl.isCustom() );
    assertFalse( pl.isSmart() );
    
    pl = pls.getPlaylistByName( Fixtures.get( Fixtures.SMART_PLAYLIST_NAME ) );
    assertNotNull( pl );
    assertTrue( pl.isSmart() );
    assertFalse( pl.isCustom() );
    
    pl = pls.getPlaylistByType( Playlists.Type.LocalPlaylist );
    assertNotNull( pl );
    assertEquals( pl.getName(), "AudioBox Desktop" );
    assertEquals( pl.getSystemName(), "local" );
    assertTrue( pl.isDrive() );
    
    pl = pls.getPlaylistByType( Playlists.Type.CloudPlaylist );
    assertNotNull( pl );
    assertEquals( pl.getName(), "AudioBox Cloud" );
    assertEquals( pl.getSystemName(), "cloud" );
    assertTrue( pl.getMediaFilesCount() > 0 );
    assertTrue( pl.isDrive() );
    
  }
  
  
  @Test
  public void playlistsEvents() {
    number_event = 0;
    Playlists pls = user.getPlaylists();
    pls.addObserver(new Observer() {
      public void update(Observable pls, Object evt) {
        Event event = (Event) evt;
        if ( event.state == Event.States.START_LOADING ) {
          number_event++;
        } else if ( event.state == Event.States.COLLECTION_CLEARED ) {
          number_event++;
        } else if ( event.state == Event.States.END_LOADING ) {
          number_event++;
        } else if ( event.state == Event.States.ENTITY_ADDED ) {
          number_event++;
        }
      }
    });
    
    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    assertEquals( number_event, pls.size() + 3 );
  }
  
  
  
  @Test
  public void createNewCustomPlaylist() {
    
    Playlists pls = user.getPlaylists();

    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    int plsCount = pls.size();
    pls.createPlaylist();
    
    assertEquals( plsCount + 1, pls.size() );
    
  }


}
