/**
 * 
 */
package fm.audiobox.core.test;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFile;
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
  private boolean event_fired = false;
  
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

    Playlist p = pls.get(playlist.getToken());
    assertNotNull(p);
    assertSame(p, playlist);
    
    assertNull( pls.get("fake_token") );
    
    assertEquals( p.getType(), Playlists.Type.CloudPlaylist.toString() );
    
    Playlist pl = pls.getPlaylistByType( Playlists.Type.LocalPlaylist );
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
  public void testToggleVisible() {
    
    Playlists pls = user.getPlaylists();

    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    Playlist pl = pls.getPlaylistByType(Playlists.Type.DropboxPlaylist);
    boolean curr_visible = pl.isVisible();
    try {
      boolean result = pl.toggleVisible();
      if ( result ) {
        assertFalse( curr_visible && pl.isVisible() );
      } else {
        fail( "Toggle visible fails" );
      }
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
  }
  
  
  @Test
  public void testSyncDropbox() {
    
    Playlists pls = user.getPlaylists();

    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    Playlist pl = pls.getPlaylistByType(Playlists.Type.DropboxPlaylist);
    
    try {
      pl.sync();
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
  }
  
  
  @Test
  public void testMakeDropboxVisible() {
    Playlists pls = user.getPlaylists();

    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    Playlist pl = pls.getPlaylistByType(Playlists.Type.DropboxPlaylist);
    boolean curr_visible = pl.isVisible();
    if ( !curr_visible ) {
      try {
        boolean result = pl.toggleVisible();
        if ( result ) {
          assertFalse( curr_visible && pl.isVisible() );
        } else {
          fail( "Toggle visible fails" );
        }
      } catch (ServiceException e) {
        fail( e.getMessage() );
      } catch (LoginException e) {
        fail( e.getMessage() );
      }
    }
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
    
    
    try {
      Playlist pl = user.createPlaylist( Fixtures.get( Fixtures.CUSTOM_PLAYLIST_NAME ) );
      assertNotNull( pl );
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    Playlist pl = pls.getPlaylistByName( Fixtures.get( Fixtures.CUSTOM_PLAYLIST_NAME ) );
    assertNotNull( pl );
    assertTrue( pl.isCustom() );
    assertFalse( pl.isSmart() );
    
    assertEquals( plsCount + 1, pls.size() );
    
  }


  @Test
  public void addMediaFilesInToCustomPlaylist() {
    
    Playlists pls = user.getPlaylists();

    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    Playlist pl = pls.getPlaylistByName( Fixtures.get( Fixtures.CUSTOM_PLAYLIST_NAME ) );
    Playlist cloud = pls.getPlaylistByType( Playlists.Type.CloudPlaylist );
    
    assertNotNull( pl );
    assertNotNull( cloud );
    
    try {
      pl.getMediaFiles().load(false);
      cloud.getMediaFiles().load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    long original_size = pl.getMediaFilesCount();
    List<MediaFile> sublist = cloud.getMediaFiles().subList(0, 2);
    
    try {
      pl.addMediaFiles(sublist);
      pl.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    
    assertEquals( original_size + sublist.size(), pl.getMediaFilesCount());
    
    try {
      pl.getMediaFiles().load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    assertEquals( original_size + sublist.size(), pl.getMediaFiles().size() );
    
  }
  
  
  
  
  
  @Test
  public void removeMediaFilesFromCustomPlaylist() {
    
    Playlists pls = user.getPlaylists();

    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    Playlist pl = pls.getPlaylistByName( Fixtures.get( Fixtures.CUSTOM_PLAYLIST_NAME ) );
    
    assertNotNull( pl );
    
    try {
      pl.getMediaFiles().load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    number_event = 0;
    pl.getMediaFiles().addObserver(new Observer() {
      public void update(Observable obj, Object evt) {
        Event event = (Event) evt;
        if ( event.state == Event.States.ENTITY_REMOVED ) {
          event_fired = true;
        }
        number_event++;
      }
    });
    
    long original_size = pl.getMediaFilesCount();
    List<MediaFile> sublist = pl.getMediaFiles().subList(0, pl.getMediaFiles().size() );
    int sublist_size = sublist.size();
    try {
      pl.removeMediaFiles(sublist);
      pl.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    
    assertEquals( original_size - sublist_size, pl.getMediaFilesCount());
    
    assertTrue( event_fired );
    assertEquals( number_event, sublist_size);
    
    try {
      pl.getMediaFiles().load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    assertEquals( original_size - sublist_size, pl.getMediaFiles().size() );
    
  }
  
  @Test
  public void destroyCustomPlaylist() {
    
    Playlists pls = user.getPlaylists();

    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    Playlist pl = pls.getPlaylistByName( Fixtures.get( Fixtures.CUSTOM_PLAYLIST_NAME ) );
    assertNotNull( pl );
    
    event_fired = false; number_event = 0;
    pls.addObserver(new Observer() {
      public void update(Observable obj, Object evt) {
        Event event = (Event) evt;
        if ( event.state == Event.States.ENTITY_REMOVED ) {
          event_fired = true;
        }
        number_event++;
      }
    });
    
    try {
      assertTrue( pl.destroy() );
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    assertTrue( event_fired );
    assertEquals( number_event, 1 );
    
  }
  
}
