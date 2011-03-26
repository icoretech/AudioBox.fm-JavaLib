
/***************************************************************************
 *   Copyright (C) 2010 iCoreTech research labs                            *
 *   Contributed code from:                                                *
 *   - Valerio Chiodino - keytwo at keytwo dot net                         *
 *   - Fabio Tunno      - fat at fatshotty dot net                         *
 *                                                                         *
 *   This program is free software: you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation, either version 3 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program. If not, see http://www.gnu.org/licenses/     *
 *                                                                         *
 ***************************************************************************/

package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.api.ModelItem;
import fm.audiobox.core.models.Playlists.PlaylistTypes;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;


/**
 * Playlist class is a {@link ModelItem} specialization for playlists XML elements.
 * 
 * <p>
 * 
 * Profile XML looks like this: 
 * 
 * <pre>
 * {@code
 * <playlist>
 *   <name>Music</name>
 *   <playlist_tracks_count type="integer">1591</playlist_tracks_count>
 *   <playlist_type>audio</playlist_type>
 *   <position type="integer">1</position>
 *   <token>ass8sad909sh</token>
 * </playlist>
 * }
 * </pre>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Playlist extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LoggerFactory.getLogger(Playlist.class);

  /** The XML tag name for the Playlist element */
  public static final String NAMESPACE = Playlists.TAGNAME;
  public static final String TAGNAME = "playlist";

  private String name;
  private int position = 0;
  private PlaylistTypes playlistType;
  private long tracksCount;
  private Tracks tracks;



  /**
   * <p>Constructor for Playlist.</p>
   */
  public Playlist(IConnector connector, IConfiguration config){
    super(connector, config);
    log.info("New Playlist instantiated");
  }


  @Override
  public String getTagName(){
    return TAGNAME;
  }

  @Override
  public String getNamespace(){
    return NAMESPACE;
  }


  /**
   * Returns the playlist name
   * @return the playlist name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the playlists name. Used by the parser
   * @param name the playlist name
   */
  @Deprecated
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Return the playlist position index
   * @return the playlist position index
   */
  public int getPosition() {
    return position;
  }


  /**
   * Sets the playlist position index. Used by the parser
   * 
   * @param position the playlist position index
   */
  @Deprecated
  public void setPosition(int position) {
    this.position = position;
  }

  /**
   * Returns the playlist type
   * @return the playlist type
   */
  public PlaylistTypes getPlaylistType() {
    return playlistType;
  }

  /**
   * Sets the playlist type. Used by the parser
   * @param playlistType the playlist type
   */
  @Deprecated
  public void setPlaylistType(String playlistType) {

    if ( playlistType.equals( PlaylistTypes.AUDIO.toString().toLowerCase() ) ){
      this.playlistType = PlaylistTypes.AUDIO;

    } else if ( playlistType.equals( PlaylistTypes.TRASH.toString().toLowerCase()  ) ){
      this.playlistType = PlaylistTypes.TRASH;

    } else if ( playlistType.equals( PlaylistTypes.VIDEO.toString().toLowerCase()  ) ){
      this.playlistType = PlaylistTypes.VIDEO;

    } else if ( playlistType.equals( PlaylistTypes.CUSTOM.toString().toLowerCase()  ) ){
      this.playlistType = PlaylistTypes.CUSTOM;

    } else if ( playlistType.equals( PlaylistTypes.OFFLINE.toString().toLowerCase()  ) ){
      this.playlistType = PlaylistTypes.OFFLINE;

    }
  }


  /**
   * Returns the playlist tracks count
   * @return the playlist tracks count
   */
  public long getTracksCount() {
    return tracksCount;
  }

  /**
   * Sets the playlist track count. Used by the parser
   * @param tracksCount the playlist tracks count
   */
  public void setTracksCount(long tracksCount) {
    this.tracksCount = tracksCount;
  }


  
  /**
   * Returns a {@link Tracks} instance ready to be populated through {@link Tracks#load()} method
   * 
   * @return a {@link Tracks} instance
   */
  public Tracks getTracks(){
    if ( this.tracks == null ){
      this.tracks = (Tracks) getConfiguration().getFactory().getEntity( Tracks.TAGNAME , getConfiguration() );
      this.tracks.setParent( this );
    }
    return this.tracks;
  }
  

  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {

    if ( tagName.equals("token") ) {
      return this.getClass().getMethod("setToken", String.class);

    } else if ( tagName.equals("name") ) {
      return this.getClass().getMethod("setName", String.class);

    } else if ( tagName.equals("position") ) {
      return this.getClass().getMethod("setPosition", int.class);

    } else if ( tagName.equals("playlist_type") ) {
      return this.getClass().getMethod("setPlaylistType", String.class);

    } else if ( tagName.equals("playlist_tracks_count") ) {
      return this.getClass().getMethod("setTracksCount", long.class);

    }

    return null;
  }


  //    /* ---------------------------- */
  //    /* Playlists management methods */
  //    /* ---------------------------- */
  //    
  //
  //    /**
  //     * Use this method to add a single {@link Track track} to this playlist
  //     * 
  //     * @param track the {@link Track} to add to playlist
  //     * @return true if the action succeed false if something goes wrong.
  //     * 
  //     * @throws LoginException if any authentication problem during the request occurs.
  //     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
  //     */
  //    public boolean addTrack(Track track) throws LoginException, ServiceException {
  //    	List<Track> tracks = new ArrayList<Track>();
  //    	tracks.add(track);
  //        return this.addTracks(tracks);
  //    }
  //
  //    
  //    /**
  //     * Use this method to add multiple {@link Track tracks} to this playlist
  //     * 
  //     * @param tracks the {@link List} of {@link Track Tracks} to add to playlist
  //     * @return true if the action succeed false if something goes wrong.
  //     * 
  //     * @throws LoginException if any authentication problem during the request occurs.
  //     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
  //     */
  //    public boolean addTracks(List<Track> tracks) throws LoginException, ServiceException {
  //    	Tracks addedTracks = performAction(tracks, Playlists.ADD_TRACKS_ACTION);
  //    	if ( addedTracks == null ) return false;
  //    	if ( this.mTracks != null ) {
  //    		// Tracks already loaded, manual management
  //	    	for ( ModelItem addedTrack : addedTracks.getCollection() ){
  //	    		for ( Track track : tracks ){
  //	    			if ( track.getToken().equals( addedTrack.getToken() )){
  //	    				// TODO: fire collection listener events
  //	    				this.mTracks.addTrack(track);
  //	    				this.playlistTracksCount++;
  //	    			}
  //	    		}
  //	    	}
  //    	}
  //        return true;
  //    }
  //    
  //    
  //    /**
  //     * Use this method to remove a single {@link Track} from this playlist
  //     * 
  //     * @param track the {@link Track} to remove from playlist
  //     * @return true if the action succeed false if something goes wrong.
  //     * 
  //     * @throws LoginException if any authentication problem during the request occurs.
  //     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
  //     */
  //    public boolean removeTrack(Track track) throws LoginException, ServiceException {
  //    	List<Track> tracks = new ArrayList<Track>();
  //    	tracks.add(track);
  //        return this.removeTracks(tracks);
  //    }
  //
  //    
  //    /**
  //     * Use this method to remove multiple {@link Track tracks} from this playlist
  //     * 
  //     * @param tracks the {@link List} of {@link Track Tracks} to remove from playlist
  //     * @return true if the action succeed false if something goes wrong.
  //     * 
  //     * @throws LoginException if any authentication problem during the request occurs.
  //     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
  //     */
  //    public boolean removeTracks(List<Track> tracks) throws LoginException, ServiceException {
  //    	Tracks removedTracks = performAction(tracks, Playlists.REMOVE_TRACKS_ACTION);
  //    	if ( removedTracks == null ) return false;
  //    	if ( this.mTracks != null ) {
  //    		// Tracks already loaded, manual management
  //	    	for ( ModelItem removedTrack : removedTracks.getCollection() ){
  //	    		for ( Track track : tracks ){
  //	    			if ( track.getToken().equals(removedTrack.getToken() )){
  //	    				// TODO: fire collection listener events
  //	    				if (  this.mTracks.removeFromCollection( track.getToken()  ) != null  )
  //	    					this.playlistTracksCount--;	// remove correctly
  //	    			}
  //	    		}
  //	    	}
  //    	}
  //        return true;
  //    }
  //    
  //    
  //    
  //    
  //    /* --------------- */
  //    /* Private methods */
  //    /* --------------- */
  //    
  //    
  //    /**
  //     * Use this method to add or remove multiple {@link Track tracks} from this playlist
  //     * 
  //     * @param tracks the {@link List} of {@link Track Tracks} to remove/add
  //     * @param action the action to perform. Should be one of {@link Playlists#ADD_TRACKS_ACTION} or {@link Playlists#REMOVE_TRACKS_ACTION} 
  //     * 
  //     * @return Tracks instance that contains all added/removed tracks tokens
  //     * 
  //     * @throws LoginException if any authentication problem during the request occurs.
  //     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
  //     */
  //    private Tracks performAction(List<Track> tracks, String action) throws LoginException, ServiceException {
  //        
  //    	try {
  //    		
  //        	List<NameValuePair> params = new ArrayList<NameValuePair>();
  //            for (Track track : tracks)
  //                params.add(new BasicNameValuePair(HTTP_PARAM, track.getToken()));
  //        	
  //            HttpEntity entity = new UrlEncodedFormEntity(params, CHAR_ENCODING);
  //            
  //            Tracks _tracks = new Tracks();
  //            
  //            String[] result = this.getConnector().put( this, _tracks, action, entity);
  //            
  //            
  //            if ( HttpStatus.SC_OK == Integer.parseInt( result[ Connector.RESPONSE_CODE ] )  ) {
  //            	return _tracks;
  //            }
  //            
  //        } catch (UnsupportedEncodingException e) {
  //        	log.error("An error occurred while encoding", e);
  //        }
  //        
  //        return null;
  //    }
}
