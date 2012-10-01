
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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.configurations.Response;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Playlists.Types;
import fm.audiobox.core.observables.Event;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;


/**
 * Playlist class is a {@link ModelItem} specialization for playlists XML elements.
 * 
 * <p>
 * 
 * Profile XML looks like this: 
 * 
 * <pre>
 * @code
 * <playlist>
 *   <token>as89d7h4sa</token>
 *   <name>Music</name>
 *   <position>1</position>
 *   <playlist_type>audio</playlist_type>
 *   <playlist_tracks_count>12312</playlist_tracks_count>
 * </playlist>
 * @endcode
 * </pre>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Playlist extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private static Logger log = LoggerFactory.getLogger(Playlist.class);

  /** The XML tag name for the Playlist element */
  public static final String NAMESPACE = Playlists.TAGNAME;
  public static final String TAGNAME = "playlist";

  /* Parameters */
  /** Used as HTTP parameters to specify the tracks list */
  private static final String HTTP_PARAM = "track_tokens[]";

  private String name;
  private int position = 0;
  private Types type;
  private long media_files_count;
  private MediaFiles mediafiles;
  private String updated_at;
  private boolean last_accessed;
  private IEntity parent;
  private boolean offline;
  private boolean embeddable;
  private boolean visible;
  private String system_name;
  private boolean syncable;
  
  
  /**
   * <p>Constructor for Playlist.</p>
   */
  public Playlist(IConfiguration config){
    super(config);
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
   * Returns the playlist name
   * @return the playlist name
   */
  public String getSystemName() {
    return this.system_name;
  }

  /**
   * Sets the playlists name. Used by the parser
   * @param name the playlist name
   */
  @Deprecated
  public void setSystemName(String system_name) {
    this.system_name = system_name;
  }
  
  
  /**
   * This method deletes entirly content of the {@link Playlists.Types.LocalPlaylist} drive.
   * Use this method carefully
   * Note: this action will be asynchronously performed by the server 
   * @return {@code true} if everything went ok. {@code false} if not 
   */
  public boolean clearContent() throws ServiceException, LoginException {
    
    if ( this.getType() != Playlists.Types.LocalPlaylist ) return false;
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add( new BasicNameValuePair("confirm", "YES") );
    
    Response response = this.getConnector(IConfiguration.Connectors.RAILS).put(this, "empty").send(false, params);
    
    return response.isOK();
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
  public void setPosition(int position) {
    this.position = position;
  }

  /**
   * Returns the playlist type
   * @return the playlist type
   */
  public Types getType() {
    return type;
  }

  /**
   * Sets the playlist type. Used by the parser
   * @param playlistType the playlist type
   */
  public void setType(String type) {

    if ( type.equals( Types.LocalPlaylist.toString() ) ){
      this.type = Types.LocalPlaylist;

    } else if ( type.equals( Types.CloudPlaylist.toString() ) ){
      this.type = Types.CloudPlaylist;

    }  else if ( type.equals( Types.DropboxPlaylist.toString() ) ){
      this.type = Types.DropboxPlaylist;

    } else if ( type.equals( Types.GdrivePlaylist.toString() ) ){
      this.type = Types.GdrivePlaylist;

    } else if ( type.equals( Types.SkydrivePlaylist.toString() ) ){
      this.type = Types.SkydrivePlaylist;

    } else if ( type.equals( Types.YoutubePlaylist.toString() ) ){
      this.type = Types.YoutubePlaylist;
    
    } else if ( type.equals( Types.SoundcloudPlaylist.toString() ) ){
      this.type = Types.SoundcloudPlaylist;

    } else if ( type.equals( Types.CustomPlaylist.toString() ) ){
      this.type = Types.CustomPlaylist;

    } else if ( type.equals( Types.SmartPlaylist.toString() ) ){
      this.type = Types.SmartPlaylist;

    }
  }


  /**
   * Returns the playlist mediafiles count
   * @return the playlist mediafiles count
   */
  public long getMediaFilesCount() {
    return media_files_count;
  }

  /**
   * Sets the playlist mediafile count. Used by the parser
   * @param media_files_count the playlist mediafiles count
   */
  public void setMediaFilesCount(long media_files_count) {
    this.media_files_count = media_files_count;
  }


  
  public boolean isOffline() {
    return offline;
  }


  public void setOffline(boolean offline) {
    this.offline = offline;
  }
  
  
  public boolean isSyncable() {
    return this.syncable;
  }


  public void setSyncable(boolean syncable) {
    this.syncable = syncable;
  }


  public boolean isEmbeddable() {
    return embeddable;
  }


  public void setEmbeddable(boolean embeddable) {
    this.embeddable = embeddable;
  }


  public boolean isVisible() {
    return visible;
  }


  public void setVisible(boolean visible) {
    this.visible = visible;
  }
  
  

  public String getUpdatedAt() {
    return updated_at;
  }


  public void setUpdatedAt(String updated_at) {
    this.updated_at = updated_at;
  }



  public boolean isLastAccessed() {
    return last_accessed;
  }

  public void setLastAccessed(boolean last_accessed) {
    this.last_accessed = last_accessed;
  }


  /**
   * Returns a {@link MediaFiles} instance ready to be populated through {@link MediaFiles#load()} method
   * 
   * @return a {@link MediaFiles} instance
   */
  public MediaFiles getMediaFiles(){
    if ( this.mediafiles == null ){
      this.mediafiles = (MediaFiles) getConfiguration().getFactory().getEntity( MediaFiles.TAGNAME , getConfiguration() );
      this.mediafiles.setParent( this );
    }
    return this.mediafiles;
  }


  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {

    if ( tagName.equals("token") ) {
      return this.getClass().getMethod("setToken", String.class);

    } else if ( tagName.equals("name") ) {
      return this.getClass().getMethod("setName", String.class);

    } else if ( tagName.equals("position") ) {
      return this.getClass().getMethod("setPosition", int.class);

    } else if ( tagName.equals("type")) {
      return this.getClass().getMethod("setType", String.class);

    } else if ( tagName.equals("media_files_count")) {
      return this.getClass().getMethod("setMediaFilesCount", long.class);

    } else if ( tagName.equals("updated_at") ) {
      return this.getClass().getMethod("setUpdatedAt", String.class);

    } else if ( tagName.equals("last_accessed") ) {
      return this.getClass().getMethod("setLastAccessed", boolean.class);
    
    } else if ( tagName.equals("system_name") ) {
      return this.getClass().getMethod("setSystemName", String.class);
    
    } else if ( tagName.equals("offline") ) {
      return this.getClass().getMethod("setOffline", boolean.class);
    
    } else if ( tagName.equals("embeddable") ) {
      return this.getClass().getMethod("setEmbeddable", boolean.class);
    
    } else if ( tagName.equals("visible") ) {
      return this.getClass().getMethod("setVisible", boolean.class);
    
    } else if ( tagName.equals("syncable") ) {
      return this.getClass().getMethod("setSyncable", boolean.class);
    
    }
    
    

    return null;
  }


  @Override
  protected void copy(IEntity entity) {

    Playlist pl = (Playlist) entity;

    this.name = pl.getName();
    this.position = pl.getPosition();
    this.type = pl.getType();
    this.media_files_count = pl.getMediaFilesCount();


    this.setChanged();
    Event event = new Event( this, Event.States.ENTITY_REFRESHED );
    this.notifyObservers(event);

  }


  /* ---------------------------- */
  /* Playlists management methods */
  /* ---------------------------- */


  /**
   * Use this method to add a single {@link MediaFile track} to this playlist
   * 
   * @param mediafile the {@link MediaFile} to add to playlist
   * @return true if the action succeed false if something goes wrong.
   * 
   * @throws LoginException if any authentication problem during the request occurs.
   * @throws ServiceException if any connection problem to AudioBox.fm occurs.
   */
  public boolean addMediaFile(MediaFile mediafile) throws LoginException, ServiceException {
    List<MediaFile> mdfs = new ArrayList<MediaFile>();
    mdfs.add(mediafile);
    return this.addMediaFiles(mdfs);
  }


  /**
   * Use this method to add multiple {@link MediaFile tracks} to this playlist
   * 
   * @param tracks the {@link List} of {@link MediaFile Tracks} to add to playlist
   * @return true if the action succeed false if something goes wrong.
   * 
   * @throws LoginException if any authentication problem during the request occurs.
   * @throws ServiceException if any connection problem to AudioBox.fm occurs.
   */
  public boolean addMediaFiles(List<MediaFile> mediafiles) throws LoginException, ServiceException {
    MediaFiles addedMediaFiles = performAction(mediafiles, Playlists.ADD_TRACKS_ACTION);
    if ( addedMediaFiles == null ) return false;
    if ( this.mediafiles != null ) {
      // MediaFile already loaded, manual management
      for ( MediaFile addedMediaFile : addedMediaFiles ){
        for ( MediaFile mediafile : mediafiles ){
          if ( mediafile.getToken().equals( addedMediaFile.getToken() )){
            this.mediafiles.add(mediafile);
            this.media_files_count++;
          }
        }
      }
      return true;
    }
    return false;
  }


  /**
   * Use this method to remove a single {@link MediaFile} from this playlist
   * 
   * @param mediafile
   *          the {@link MediaFile} to remove from playlist
   * @return true if the action succeed false if something goes wrong.
   * 
   * @throws LoginException if any authentication problem during the request occurs.
   * @throws ServiceException if any connection problem to AudioBox.fm occurs.
   */
  public boolean removeMediaFile(MediaFile mediafile) throws LoginException, ServiceException {
    List<MediaFile> mediafiles = new ArrayList<MediaFile>();
    mediafiles.add(mediafile);
    return this.removeMediaFiles(mediafiles);
  }

  /**
   * Use this method to remove multiple {@link MediaFile tracks} from this playlist
   * 
   * @param tracks
   *          the {@link List} of {@link MediaFile Tracks} to remove from playlist
   * @return true if the action succeed false if something goes wrong.
   * 
   * @throws LoginException if any authentication problem during the request occurs.
   * @throws ServiceException if any connection problem to AudioBox.fm occurs.
   */
  public boolean removeMediaFiles(List<MediaFile> mediafiles) throws LoginException, ServiceException {
    MediaFiles removedMediaFiles = performAction(mediafiles, Playlists.REMOVE_TRACKS_ACTION);
    if (removedMediaFiles == null)
      return false;
    if (this.mediafiles != null) {
      // Mediafiles already loaded, manual management
      for (MediaFile removedTrack : removedMediaFiles) {
        for (MediaFile track : mediafiles) {
          if (track.getToken().equals(removedTrack.getToken())) {
            if (this.mediafiles.remove(track.getToken()))
              this.media_files_count--; // remove correctly
          }
        }
      }
      return true;
    }
    return false;
  }


  /* --------------- */
  /* Private methods */
  /* --------------- */


  /**
   * Use this method to add or remove multiple {@link MediaFile mediafiles} from this playlist
   * 
   * @param mediafiles the {@link List} of {@link MediaFile MediaFiles} to remove/add
   * @param action the action to perform. Should be one of {@link Playlists#ADD_TRACKS_ACTION} or {@link Playlists#REMOVE_TRACKS_ACTION} 
   * 
   * @return {@link MediaFiles} instance that contains all added/removed tracks tokens
   * 
   * @throws LoginException if any authentication problem during the request occurs.
   * @throws ServiceException if any connection problem to AudioBox.fm occurs.
   */
  private MediaFiles performAction(List<MediaFile> mediafiles, String action) throws ServiceException, LoginException  {

    List<NameValuePair> params = new ArrayList<NameValuePair>();
    for (MediaFile track : mediafiles) {
      params.add(new BasicNameValuePair(HTTP_PARAM, track.getToken()));
    }

    MediaFiles _mediafiles = (MediaFiles) this.getConfiguration().getFactory().getEntity(MediaFiles.TAGNAME, this.getConfiguration());
    
    IConnectionMethod method = this.getConnector().put(this, action);

    try {

      HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
      method.send(false, entity);

    } catch (UnsupportedEncodingException e) {
      log.error("An error occurred while instantiating UrlEncodedFormEntity", e);
    }

    return _mediafiles;

  }

  /**
   * Sets the parent {@link IEntity}
   * @param parent the {@link IEntity} parent object
   */
  public void setParent(IEntity parent){
    this.parent = parent;
  }


  @Override
  public String getApiPath() {
    return this.parent.getApiPath() + "/" + this.getToken();
  }
}
