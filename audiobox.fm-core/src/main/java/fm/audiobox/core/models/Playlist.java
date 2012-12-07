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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.configurations.Response;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.observables.Event;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IResponseHandler;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;

/**
 * Playlist class is a {@link ModelItem} specialization for playlists XML
 * elements.
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
  public static final String NAMESPACE = Playlists.NAMESPACE;
  public static final String TAGNAME = "playlist";
  
  public static final String NAME = "name";
  public static final String POSITION = "position";
  public static final String TYPE = "type";
  public static final String MEDIA_FILES_COUNT = "media_files_count";
  public static final String UPDATED_AT = "updated_at";
  public static final String LAST_ACCESSED = "last_accessed";
  public static final String SYSTEM_NAME = "system_name";
  public static final String OFFLINE = "offline";
  public static final String EMBEDDABLE = "embeddable";
  public static final String VISIBLE = "visible";
  public static final String SYNCABLE = "syncable";
  

  /* Parameters */
  /** Used as HTTP parameters to specify the tracks list */
  private static final String HTTP_PARAM = "track_tokens[]";

  private String name;
  private int position = 0;
  private String type;
  private long media_files_count;
  private MediaFiles mediafiles;
  private Albums albums;
  private String updated_at;
  private boolean last_accessed;
  private boolean offline;
  private boolean embeddable;
  private boolean visible;
  private String system_name;
  private boolean syncable;
  
  
  private static final Map<String, Method> setterMethods = new HashMap<String, Method>();
  static {
    try {
      setterMethods.put( TOKEN, Playlist.class.getMethod( "setToken", String.class )  );
      setterMethods.put( NAME, Playlist.class.getMethod( "setName", String.class )  );
      setterMethods.put( POSITION, Playlist.class.getMethod( "setPosition", int.class )  );
      setterMethods.put( TYPE, Playlist.class.getMethod( "setType", String.class )  );
      setterMethods.put( MEDIA_FILES_COUNT, Playlist.class.getMethod( "setMediaFilesCount", long.class )  );
      setterMethods.put( UPDATED_AT, Playlist.class.getMethod( "setUpdatedAt", String.class )  );
      setterMethods.put( LAST_ACCESSED, Playlist.class.getMethod( "setLastAccessed", boolean.class )  );
      setterMethods.put( SYSTEM_NAME, Playlist.class.getMethod( "setSystemName", String.class )  );
      setterMethods.put( OFFLINE, Playlist.class.getMethod( "setOffline", boolean.class )  );
      setterMethods.put( EMBEDDABLE, Playlist.class.getMethod( "setEmbeddable", boolean.class )  );
      setterMethods.put( VISIBLE, Playlist.class.getMethod( "setVisible", boolean.class )  );
      setterMethods.put( SYNCABLE, Playlist.class.getMethod( "setSyncable", boolean.class )  );
    } catch (SecurityException e) {
      log.error("Security error", e);
    } catch (NoSuchMethodException e) {
      log.error("No method found", e);
    }
  }
  
  

  /**
   * <p>
   * Constructor for Playlist.
   * </p>
   */
  public Playlist(IConfiguration config) {
    super(config);
  }

  @Override
  public String getTagName() {
    return TAGNAME;
  }

  @Override
  public String getNamespace() {
    return NAMESPACE;
  }

  /**
   * Returns the playlist name
   * 
   * @return the playlist name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the playlists name. Used by the parser
   * 
   * @param name
   *          the playlist name
   */
  @Deprecated
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the playlist name
   * 
   * @return the playlist name
   */
  public String getSystemName() {
    return this.system_name;
  }

  /**
   * Sets the playlists name. Used by the parser
   * 
   * @param name
   *          the playlist name
   */
  @Deprecated
  public void setSystemName(String system_name) {
    this.system_name = system_name;
  }

  /**
   * This method deletes entirly content of the
   * {@link Playlists.Types.LocalPlaylist} drive. Use this method carefully
   * Note: this action will be asynchronously performed by the server
   * 
   * @return {@code true} if everything went ok. {@code false} if not
   */
  public boolean clearContent() throws ServiceException, LoginException {

    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("confirm", "YES"));

    Response response = this.getConnector(IConfiguration.Connectors.RAILS).put(this, "empty").send(false, params);

    return response.isOK();
  }

  /**
   * Return the playlist position index
   * 
   * @return the playlist position index
   */
  public int getPosition() {
    return position;
  }

  /**
   * Sets the playlist position index. Used by the parser
   * 
   * @param position
   *          the playlist position index
   */
  public void setPosition(int position) {
    this.position = position;
  }

  /**
   * Returns the playlist type
   * 
   * @return the playlist type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the playlist type. Used by the parser
   * 
   * @param playlistType
   *          the playlist type
   */
  public void setType(String type) {

    this.type = type;

  }

  /**
   * Returns the playlist mediafiles count
   * 
   * @return the playlist mediafiles count
   */
  public long getMediaFilesCount() {
    return media_files_count;
  }

  /**
   * Sets the playlist mediafile count. Used by the parser
   * 
   * @param media_files_count
   *          the playlist mediafiles count
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
   * Returns a {@link MediaFiles} instance ready to be populated through
   * {@link MediaFiles#load()} method
   * 
   * @return a {@link MediaFiles} instance
   */
  public MediaFiles getMediaFiles() {
    if (this.mediafiles == null) {
      this.mediafiles = (MediaFiles) getConfiguration().getFactory().getEntity( MediaFiles.TAGNAME, getConfiguration());
      this.mediafiles.setParent(this);
    }
    return this.mediafiles;
  }

  public Albums getAlbums() {
    if (this.albums == null) {
      this.albums = (Albums) getConfiguration().getFactory().getEntity( Albums.TAGNAME, getConfiguration());
      this.albums.setParent(this);
    }
    return this.albums;
  }

  public Method getSetterMethod(String tagName) {
    if ( setterMethods.containsKey( tagName) ) {
      return setterMethods.get( tagName );
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
    Event event = new Event(this, Event.States.ENTITY_REFRESHED);
    this.notifyObservers(event);

  }

  /* ---------------------------- */
  /* Playlists management methods */
  /* ---------------------------- */

  /**
   * Use this method to add a single {@link MediaFile track} to this playlist
   * 
   * @param mediafile
   *          the {@link MediaFile} to add to playlist
   * @return true if the action succeed false if something goes wrong.
   * 
   * @throws LoginException
   *           if any authentication problem during the request occurs.
   * @throws ServiceException
   *           if any connection problem to AudioBox.fm occurs.
   */
  public boolean addMediaFile(MediaFile mediafile) throws LoginException, ServiceException {
    List<MediaFile> mdfs = new ArrayList<MediaFile>();
    mdfs.add(mediafile);
    return this.addMediaFiles(mdfs);
  }

  /**
   * Use this method to add multiple {@link MediaFile tracks} to this playlist
   * 
   * @param tracks
   *          the {@link List} of {@link MediaFile Tracks} to add to playlist
   * @return true if the action succeed false if something goes wrong.
   * 
   * @throws LoginException
   *           if any authentication problem during the request occurs.
   * @throws ServiceException
   *           if any connection problem to AudioBox.fm occurs.
   */
  public boolean addMediaFiles(List<MediaFile> mediafiles) throws LoginException, ServiceException {
    MediaFiles addedMediaFiles = performAction(mediafiles, Playlists.ADD_TRACKS_ACTION);
    if (addedMediaFiles == null)
      return false;
    if (this.mediafiles != null) {
      // MediaFile already loaded, manual management
      for (MediaFile addedMediaFile : addedMediaFiles) {
        for (MediaFile mediafile : mediafiles) {
          if (mediafile.getToken().equals(addedMediaFile.getToken())) {
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
   * @throws LoginException
   *           if any authentication problem during the request occurs.
   * @throws ServiceException
   *           if any connection problem to AudioBox.fm occurs.
   */
  public boolean removeMediaFile(MediaFile mediafile) throws LoginException, ServiceException {
    List<MediaFile> mediafiles = new ArrayList<MediaFile>();
    mediafiles.add(mediafile);
    return this.removeMediaFiles(mediafiles);
  }

  /**
   * Use this method to remove multiple {@link MediaFile tracks} from this
   * playlist
   * 
   * @param tracks
   *          the {@link List} of {@link MediaFile Tracks} to remove from
   *          playlist
   * @return true if the action succeed false if something goes wrong.
   * 
   * @throws LoginException
   *           if any authentication problem during the request occurs.
   * @throws ServiceException
   *           if any connection problem to AudioBox.fm occurs.
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
   * Use this method to add or remove multiple {@link MediaFile mediafiles} from
   * this playlist
   * 
   * @param mediafiles
   *          the {@link List} of {@link MediaFile MediaFiles} to remove/add
   * @param action
   *          the action to perform. Should be one of
   *          {@link Playlists#ADD_TRACKS_ACTION} or
   *          {@link Playlists#REMOVE_TRACKS_ACTION}
   * 
   * @return {@link MediaFiles} instance that contains all added/removed tracks
   *         tokens
   * 
   * @throws LoginException
   *           if any authentication problem during the request occurs.
   * @throws ServiceException
   *           if any connection problem to AudioBox.fm occurs.
   */
  private MediaFiles performAction(List<MediaFile> mediafiles, String action) throws ServiceException, LoginException {

    log.info("Performing action " + action + " on " + mediafiles.size() + " mediafiles");

    List<NameValuePair> params = new ArrayList<NameValuePair>();
    for (MediaFile track : mediafiles) {
      params.add(new BasicNameValuePair(HTTP_PARAM, track.getToken()));
    }

    MediaFiles _mediafiles = (MediaFiles) this.getConfiguration().getFactory().getEntity(MediaFiles.TAGNAME, this.getConfiguration());

    IConnectionMethod method = this.getConnector().put(this, action);

    HttpEntity entity = new UrlEncodedFormEntity(params, Consts.UTF_8);
    method.send(false, entity);

    return _mediafiles;
  }

  public String getApiPath() {
    return this.getParent().getApiPath() + IConnector.URI_SEPARATOR + this.getToken();
  }

  @Override
  public IConnectionMethod load(boolean async) throws ServiceException, LoginException {
    return this.load(false, null);
  }

  @Override
  public IConnectionMethod load(boolean async, IResponseHandler responseHandler) throws ServiceException, LoginException {
    IConnectionMethod request = getConnector(IConfiguration.Connectors.RAILS).get(this, null, null);
    request.send(async, null, responseHandler);
    return request;
  }

}
