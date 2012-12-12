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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.AudioBox;
import fm.audiobox.configurations.Response;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.observables.Event;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

/**
 * Playlist class is a {@link IEntity} specialization for playlists XML
 * elements.
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
  
  public static final String CUSTOM_SYSTEM_NAME = "custom";
  public static final String SMART_SYSTEM_NAME = "smart";
  
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
    
    // By default a playlist is set as 'custom' playlist
    this.setSystemName( CUSTOM_SYSTEM_NAME );
  }
  
  /**
   * Contructor method
   * This class can be instantiated always.
   * We should link the new class to the current {@link AudioBox} instance
   *
   * @param config
   */
  public Playlist(AudioBox abxClient) {
    this( abxClient.getConfiguration() );
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
   * @param name the playlist name
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
   * @param name the playlist name
   */
  @Deprecated
  public void setSystemName(String system_name) {
    this.system_name = system_name;
  }

  
  /**
   * Use this method to know if {@code playlist} is @{value smart} or not
   * @return boolean
   */
  public boolean isSmart() {
    return SMART_SYSTEM_NAME.equals( this.getSystemName() );
  }
  
  /**
   * Use this method to know if {@code playlist} is @{value custom} or not
   * @return boolean
   */
  public boolean isCustom() {
    return CUSTOM_SYSTEM_NAME.equals( this.getSystemName() );
  }
  
  
  /**
   * Use this method to know if {@code playlist} is @{value drive} or not
   * @return boolean
   */
  public boolean isDrive() {
    return ! ( isCustom() || isSmart() );
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
   * @param position the playlist position index
   */
  public void setPosition(int position) {
    this.position = position;
  }

  /**
   * Returns the playlist type
   * @return the playlist type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the playlist type. Used by the parser.
   * 
   * @param playlistType the playlist type
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

  /**
   * This method perform a creation of this playlist remotely
   * 
   * @return {@code true} if everything went ok. {@code false} if not.
   * <br />
   * Note: playlist should contains no {@code token} set
   * 
   * @throws ServiceException something went wrong
   * @throws LoginException Login error occurs
   */
  public boolean create() throws ServiceException, LoginException {
    return this.create( new ArrayList<MediaFile>() );
  }
  
  /**
   * This method perform a creation of this playlist remotely.
   * Note: this method also adds media files to this {@code custom} playlist
   * 
   * @param mediaFiles list of mediafiles to be added to this playlist.
   * @return {@code true} if everything went ok. {@code false} if not.
   * <br />
   * Note: playlist should contains no {@code token} set
   * 
   * @throws ServiceException something went wrong
   * @throws LoginException Login error occurs
   */
  public boolean create(List<MediaFile> mediaFiles) throws ServiceException, LoginException {
    
    if ( this.getToken() != null && ! "".equals( this.getToken() )  ) {
      // playlists seems to be already created
      return false;
    }
    
    IConnectionMethod request = this.getConnector( IConfiguration.Connectors.RAILS ).post( this, Playlists.NAMESPACE, null );
    Response response = request.send(false, this.toQueryParameters() );
    
    return response.isOK();
  }
  
  
  
  private List<NameValuePair> toQueryParameters() {
    String prefix = TAGNAME + "[";
    String suffix = "]";
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    
    params.add(  new BasicNameValuePair( prefix + NAME + suffix,  this.name ) );
    
    return params;
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

  
  public String getApiPath() {
    return this.getParent().getApiPath() + IConnector.URI_SEPARATOR + this.getToken();
  }
  
  
  protected List<NameValuePair> toQueryParameters(boolean all) {
    String prefix = TAGNAME + "[";
    String suffix = "]";
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    
    params.add(new BasicNameValuePair(prefix + NAME + suffix, this.name )  );
    params.add(new BasicNameValuePair(prefix + POSITION + suffix, String.valueOf( this.position ) )  );
    params.add(new BasicNameValuePair(prefix + TYPE + suffix, this.type )  );
    params.add(new BasicNameValuePair(prefix + MEDIA_FILES_COUNT + suffix, String.valueOf( this.media_files_count ) ) );
    params.add(new BasicNameValuePair(prefix + UPDATED_AT + suffix, this.updated_at )  );
    params.add(new BasicNameValuePair(prefix + LAST_ACCESSED + suffix, String.valueOf( this.last_accessed ) )  );
    params.add(new BasicNameValuePair(prefix + SYSTEM_NAME + suffix, this.system_name )  );
    params.add(new BasicNameValuePair(prefix + OFFLINE + suffix, String.valueOf( this.offline ) )  );
    params.add(new BasicNameValuePair(prefix + EMBEDDABLE + suffix, String.valueOf( this.embeddable ) )  );
    params.add(new BasicNameValuePair(prefix + VISIBLE + suffix, String.valueOf( this.visible ) )  );
    params.add(new BasicNameValuePair(prefix + SYNCABLE + suffix, String.valueOf( this.syncable ) )  );
    
    
    return params;
  }

}
