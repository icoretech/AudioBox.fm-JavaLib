//
///***************************************************************************
// *   Copyright (C) 2010 iCoreTech research labs                            *
// *   Contributed code from:                                                *
// *   - Valerio Chiodino - keytwo at keytwo dot net                         *
// *   - Fabio Tunno      - fat at fatshotty dot net                         *
// *                                                                         *
// *   This program is free software: you can redistribute it and/or modify  *
// *   it under the terms of the GNU General Public License as published by  *
// *   the Free Software Foundation, either version 3 of the License, or     *
// *   (at your option) any later version.                                   *
// *                                                                         *
// *   This program is distributed in the hope that it will be useful,       *
// *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
// *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
// *   GNU General Public License for more details.                          *
// *                                                                         *
// *   You should have received a copy of the GNU General Public License     *
// *   along with this program. If not, see http://www.gnu.org/licenses/     *
// *                                                                         *
// ***************************************************************************/
//
//package fm.audiobox.core.models;
//
//
//import java.io.InputStream;
//import java.io.Serializable;
//import java.io.UnsupportedEncodingException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.protocol.HTTP;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import fm.audiobox.configurations.DefaultResponseParser;
//import fm.audiobox.core.exceptions.LoginException;
//import fm.audiobox.core.exceptions.ServiceException;
//import fm.audiobox.core.models.Playlists.Types;
//import fm.audiobox.core.observables.Event;
//import fm.audiobox.interfaces.IConfiguration;
//import fm.audiobox.interfaces.IConfiguration.ContentFormat;
//import fm.audiobox.interfaces.IConnector;
//import fm.audiobox.interfaces.IConnector.IConnectionMethod;
//import fm.audiobox.interfaces.IEntity;
//import fm.audiobox.interfaces.IResponseHandler;
//
//
///**
// * Playlist class is a {@link ModelItem} specialization for playlists XML elements.
// * 
// * <p>
// * 
// * Profile XML looks like this: 
// * 
// * <pre>
// * @code
// * <playlist>
// *   <token>as89d7h4sa</token>
// *   <name>Music</name>
// *   <position>1</position>
// *   <playlist_type>audio</playlist_type>
// *   <playlist_tracks_count>12312</playlist_tracks_count>
// * </playlist>
// * @endcode
// * </pre>
// *
// * @author Valerio Chiodino
// * @author Fabio Tunno
// * @version 0.0.1
// */
//public class Playlist extends AbstractEntity implements Serializable {
//
//  private static final long serialVersionUID = 1L;
//
//  private static Logger log = LoggerFactory.getLogger(Playlist.class);
//  
//  /** The XML tag name for the Playlist element */
//  public static final String NAMESPACE = Playlists.TAGNAME;
//  public static final String TAGNAME = "playlist";
//  
//  /* Parameters */
//  /** Used as HTTP parameters to specify the tracks list */
//  private static final String HTTP_PARAM = "track_tokens[]";
//
//  private String name;
//  private int position = 0;
//  private Types type;
//  private long tracksCount;
//  private Tracks tracks;
//  private String updated_at;
//  private boolean last_accessed;
//  /**
//   * <p>Constructor for Playlist.</p>
//   */
//  public Playlist(IConnector connector, IConfiguration config){
//    super(connector, config);
//  }
//
//
//  @Override
//  public String getTagName(){
//    return TAGNAME;
//  }
//
//  @Override
//  public String getNamespace(){
//    return NAMESPACE;
//  }
//
//
//  /**
//   * Returns the playlist name
//   * @return the playlist name
//   */
//  public String getName() {
//    return name;
//  }
//
//  /**
//   * Sets the playlists name. Used by the parser
//   * @param name the playlist name
//   */
//  @Deprecated
//  public void setName(String name) {
//    this.name = name;
//  }
//
//  /**
//   * Return the playlist position index
//   * @return the playlist position index
//   */
//  public int getPosition() {
//    return position;
//  }
//
//
//  /**
//   * Sets the playlist position index. Used by the parser
//   * 
//   * @param position the playlist position index
//   */
//  public void setPosition(int position) {
//    this.position = position;
//  }
//
//  /**
//   * Returns the playlist type
//   * @return the playlist type
//   */
//  public Types getType() {
//    return type;
//  }
//
//  /**
//   * Sets the playlist type. Used by the parser
//   * @param playlistType the playlist type
//   */
//  public void setType(String type) {
//
//    if ( type.equals( Types.AUDIO.toString().toLowerCase() ) ){
//      this.type = Types.AUDIO;
//
//    } else if ( type.equals( Types.TRASH.toString().toLowerCase()  ) ){
//      this.type = Types.TRASH;
//
//    } else if ( type.equals( Types.VIDEO.toString().toLowerCase()  ) ){
//      this.type = Types.VIDEO;
//
//    } else if ( type.equals( Types.CUSTOM.toString().toLowerCase()  ) ){
//      this.type = Types.CUSTOM;
//
//    } else if ( type.equals( Types.OFFLINE.toString().toLowerCase()  ) ){
//      this.type = Types.OFFLINE;
//
//    }
//  }
//
//
//  /**
//   * Returns the playlist tracks count
//   * @return the playlist tracks count
//   */
//  public long getTracksCount() {
//    return tracksCount;
//  }
//
//  /**
//   * Sets the playlist track count. Used by the parser
//   * @param tracksCount the playlist tracks count
//   */
//  public void setTracksCount(long tracksCount) {
//    this.tracksCount = tracksCount;
//  }
//
//  
//  
//  public String getUpdated_at() {
//	return updated_at;
//  }
//
//
//  public void setUpdated_at(String updated_at) {
//	this.updated_at = updated_at;
//  }
//  
//  
//
//  public boolean isLast_accessed() {
//	return last_accessed;
//  }
//  
//  public void setLast_accessed(boolean last_accessed) {
//	this.last_accessed = last_accessed;
//  }
//
//
///**
//   * Returns a {@link Tracks} instance ready to be populated through {@link Tracks#load()} method
//   * 
//   * @return a {@link Tracks} instance
//   */
//  public Tracks getTracks(){
//    if ( this.tracks == null ){
//      this.tracks = (Tracks) getConfiguration().getFactory().getEntity( Tracks.TAGNAME , getConfiguration() );
//      this.tracks.setParent( this );
//    }
//    return this.tracks;
//  }
//  
//
//  @Override
//  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
//
//    if ( tagName.equals("token") ) {
//      return this.getClass().getMethod("setToken", String.class);
//
//    } else if ( tagName.equals("name") ) {
//      return this.getClass().getMethod("setName", String.class);
//
//    } else if ( tagName.equals("position") ) {
//      return this.getClass().getMethod("setPosition", int.class);
//
//    } else if ( tagName.equals("playlist_type") || tagName.equals("type")) {
//      return this.getClass().getMethod("setType", String.class);
//
//    } else if ( tagName.equals("playlist_tracks_count") || tagName.equals("media_files_count")) {
//      return this.getClass().getMethod("setTracksCount", long.class);
//
//    } else if ( tagName.equals("updated_at") ) {
//      return this.getClass().getMethod("setUpdated_at", String.class);
//      
//    } else if ( tagName.equals("last_accessed") ) {
//      return this.getClass().getMethod("setLast_accessed", boolean.class);
//    }
//    
//    return null;
//  }
//
//
//  @Override
//  protected void copy(IEntity entity) {
//    
//    Playlist pl = (Playlist) entity;
//    
//    this.name = pl.getName();
//    this.position = pl.getPosition();
//    this.type = pl.getType();
//    this.tracksCount = pl.getTracksCount();
//    
//    
//    this.setChanged();
//    Event event = new Event( this, Event.States.ENTITY_REFRESHED );
//    this.notifyObservers(event);
//    
//  }
//
//
//  /* ---------------------------- */
//  /* Playlists management methods */
//  /* ---------------------------- */
//  
//
//  /**
//   * Use this method to add a single {@link Track track} to this playlist
//   * 
//   * @param track the {@link Track} to add to playlist
//   * @return true if the action succeed false if something goes wrong.
//   * 
//   * @throws LoginException if any authentication problem during the request occurs.
//   * @throws ServiceException if any connection problem to AudioBox.fm occurs.
//   */
//  public boolean addTrack(Track track) throws LoginException, ServiceException {
//    List<Track> tracks = new ArrayList<Track>();
//    tracks.add(track);
//    return this.addTracks(tracks);
//  }
//
//  
//  /**
//   * Use this method to add multiple {@link Track tracks} to this playlist
//   * 
//   * @param tracks the {@link List} of {@link Track Tracks} to add to playlist
//   * @return true if the action succeed false if something goes wrong.
//   * 
//   * @throws LoginException if any authentication problem during the request occurs.
//   * @throws ServiceException if any connection problem to AudioBox.fm occurs.
//   */
//  public boolean addTracks(List<Track> tracks) throws LoginException, ServiceException {
//  	Tracks addedTracks = performAction(tracks, Playlists.ADD_TRACKS_ACTION);
//  	if ( addedTracks == null ) return false;
//  	if ( this.tracks != null ) {
//  		// Tracks already loaded, manual management
//    	for ( Track addedTrack : addedTracks ){
//    		for ( Track track : tracks ){
//    			if ( track.getToken().equals( addedTrack.getToken() )){
//    				this.tracks.add(track);
//    				this.tracksCount++;
//    			}
//    		}
//    	}
//    	return true;
//  	}
//    return false;
//  }
//      
//      
//  /**
//   * Use this method to remove a single {@link Track} from this playlist
//   * 
//   * @param track
//   *          the {@link Track} to remove from playlist
//   * @return true if the action succeed false if something goes wrong.
//   * 
//   * @throws LoginException if any authentication problem during the request occurs.
//   * @throws ServiceException if any connection problem to AudioBox.fm occurs.
//   */
//  public boolean removeTrack(Track track) throws LoginException, ServiceException {
//    List<Track> tracks = new ArrayList<Track>();
//    tracks.add(track);
//    return this.removeTracks(tracks);
//  }
//
//  /**
//   * Use this method to remove multiple {@link Track tracks} from this playlist
//   * 
//   * @param tracks
//   *          the {@link List} of {@link Track Tracks} to remove from playlist
//   * @return true if the action succeed false if something goes wrong.
//   * 
//   * @throws LoginException if any authentication problem during the request occurs.
//   * @throws ServiceException if any connection problem to AudioBox.fm occurs.
//   */
//  public boolean removeTracks(List<Track> tracks) throws LoginException, ServiceException {
//    Tracks removedTracks = performAction(tracks, Playlists.REMOVE_TRACKS_ACTION);
//    if (removedTracks == null)
//      return false;
//    if (this.tracks != null) {
//      // Tracks already loaded, manual management
//      for (Track removedTrack : removedTracks) {
//        for (Track track : tracks) {
//          if (track.getToken().equals(removedTrack.getToken())) {
//            if (this.tracks.remove(track.getToken()))
//              this.tracksCount--; // remove correctly
//          }
//        }
//      }
//      return true;
//    }
//    return false;
//  }
//      
//      
//      
//      
//  /* --------------- */
//  /* Private methods */
//  /* --------------- */
//  
//  
//  /**
//   * Use this method to add or remove multiple {@link Track tracks} from this playlist
//   * 
//   * @param tracks the {@link List} of {@link Track Tracks} to remove/add
//   * @param action the action to perform. Should be one of {@link Playlists#ADD_TRACKS_ACTION} or {@link Playlists#REMOVE_TRACKS_ACTION} 
//   * 
//   * @return Tracks instance that contains all added/removed tracks tokens
//   * 
//   * @throws LoginException if any authentication problem during the request occurs.
//   * @throws ServiceException if any connection problem to AudioBox.fm occurs.
//   */
//  private Tracks performAction(List<Track> tracks, String action) throws ServiceException, LoginException  {
//
//    List<NameValuePair> params = new ArrayList<NameValuePair>();
//    for (Track track : tracks) {
//      params.add(new BasicNameValuePair(HTTP_PARAM, track.getToken()));
//    }
//
//    Tracks _tracks = (Tracks) this.getConfiguration().getFactory().getEntity(Tracks.TAGNAME, this.getConfiguration());
//    IConnectionMethod method = this.getConnector().put(this, action);
//    IResponseHandler responseHandler = new PlaylistResponseHandler(_tracks);
//    
//    try {
//      
//      HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
//      method.send(false, entity, responseHandler);
//      
//    } catch (UnsupportedEncodingException e) {
//      log.error("An error occurred while instantiating UrlEncodedFormEntity", e);
//    }
//    
//    return _tracks;
//
//  }
//  
//  private class PlaylistResponseHandler extends DefaultResponseParser {
//    
//    private Tracks tracks;
//    
//    public PlaylistResponseHandler(Tracks _tracks) {
//      this.tracks = _tracks;
//    }
//
//    @Override
//    public String parse(InputStream in, IEntity destEntity, ContentFormat format) throws ServiceException {
//      return super.parse(in, this.tracks, format);
//    }
//  }
//}
