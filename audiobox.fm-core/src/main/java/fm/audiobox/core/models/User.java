
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
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;


/**
 * User model is a special {@link Model} just because almost every library browse action is performed through this
 * object.
 *
 * <p>
 *
 * When a login is successfully performed an XML like the following is received and parsed:
 * 
 * <pre>
 * @code
 * real_name: null
 * email: 'fat@fatshotty.net'
 * auth_token: 'g88eu2f....'
 * media_files_count: 33
 * playlists_count: 13
 * total_play_count: 3
 * country: null
 * time_zone: 'UTC'
 * accepted_extensions: 'mp3,m4a,m4b,m4r,mp4,flv,webm'
 * accepted_formats: 'audio/mpeg,audio/mp4,video/mp4,video/x-flv,video/webm'
 * permissions:
 *   player: true
 *   local: true
 *   cloud: true
 *   dropbox: true
 *   gdrive: true
 *   skydrive: true
 *   soundcloud: true
 *   youtube: true
 *   box: true
 *   lastfm: true
 *   twitchtv: true
 *   facebook: true
 *   twitter: true
 * external_tokens:
 *   dropbox: false
 *   gdrive: true
 *   skydrive: false
 *   soundcloud: false
 *   youtube: true
 *   box: false
 *   lastfm: false
 *   twitchtv: false
 *   facebook: false
 *   twitter: false
 * comet_channel: 'private-e03...'
 * subscription_state: 'active'
 * stats:
 *   data_served_this_month: 2737914
 *   data_served_overall: 2737914
 *   cloud_data_stored_overall: 56684036
 *   cloud_data_stored_this_month: 101905396
 *   local_data_stored_overall: 0
 *   local_data_stored_this_month: 40356240
 *   dropbox_data_stored_overall: 0
 *   dropbox_data_stored_this_month: 0
 *   gdrive_data_stored_this_month: 38403869
 *   gdrive_data_stored_overall: 38403869
 *   skydrive_data_stored_this_month: 0
 *   skydrive_data_stored_overall: 0
 *   box_data_stored_this_month: 0
 *   box_data_stored_overall: 0
 *   soundcloud_data_stored_this_month: 0
 *   soundcloud_data_stored_overall: 0
 * preferences:
 *   accept_emails: true
 *   autoplay: false
 *   volume_level: 85
 *   color: 'audiobox-fm-blue'
 *   top_bar_bg: 'default'
 * @endcode
 * </pre>
 *
 * Through the User object you have access to its library that can be browsed by:
 * <ul>
 *  <li>Playlists</li>
 * </ul>
 * 
 * using its respective getter method.
 * 
 * <p>
 * 
 * Once obtained the desired collection you can get the media files collection of each contained element 
 * by getting its files:
 * 
 * <pre>
 * Playlists playlists = user.getPlaylists();
 * playlists.load();
 * MediaFiles media = playlists.get(0).getMediaFiles();
 * media.load();
 * </pre>
 * 
 * @author Valerio Chiodino
 * @author Fabio Tunno
 */
public final class User extends AbstractEntity implements Serializable {
  
  private static final Logger log = LoggerFactory.getLogger(User.class);

  private static final long serialVersionUID = 1L;

  /** User API namespace */
  public static final String NAMESPACE = "user";
  public static final String TAGNAME = NAMESPACE;
  
  public static enum SubscriptionState {
    nothing,
    active,
    trialing,
    past_due,
    canceled,
    unpaid
  }

  /** Separator used to split the allowed formats string */
  public static final String ALLOWED_EXTENSIONS_SEPARATOR = ";";

  private String username;
  private String real_name;
  private String email;
  private String auth_token;
  private String time_zone;
  private String accepted_extensions;
  private String accepted_formats;
  private String country;
  private int playlists_count;
  private long total_play_count;
  private long media_files_count;
  private SubscriptionState subscription_state = SubscriptionState.nothing;
  private String comet_channel;

  // User's collection relations
  private Playlists playlists;
  private Permissions permissions;
  private AccountStats accountStats;
  private Preferences preferences;
  private ExternalTokens externalTokens;

  /**
   * <p>Constructor for User.</p>
   */
  public User(IConfiguration config) {
    super(config);
  }



  public String getTagName(){
    return TAGNAME;
  }

  @Override
  public String getNamespace(){
    return NAMESPACE;
  }


  /* ------------------- */
  /* Getters and setters */
  /* ------------------- */
  
  
  public String getUsername() {
    return username;
  }



  public void setUsername(String username) {
    this.username = username;
  }


  public String getRealName() {
    return real_name;
  }



  public void setRealName(String real_name) {
    this.real_name = real_name;
  }



  public String getEmail() {
    return email;
  }



  public void setEmail(String email) {
    this.email = email;
  }



  public String getCountry() {
    return country;
  }



  public void setCountry(String country) {
    this.country = country;
  }



  public String getTimeZone() {
    return time_zone;
  }



  public void setTimeZone(String time_zone) {
    this.time_zone = time_zone;
  }



  public String getAcceptedExtensions() {
    return accepted_extensions;
  }



  public void setAcceptedExtensions(String accepted_extensions) {
    this.accepted_extensions = accepted_extensions;
  }


  public String getAcceptedFormats() {
    return accepted_formats;
  }


  public void setAcceptedFormats(String accepted_formats) {
    this.accepted_formats = accepted_formats;
  }


  public void setCometChannel(String cometChl) {
    this.comet_channel = cometChl;
  }


  public String getCometChannel() {
    return this.comet_channel;
  }
  
  

  public int getPlaylistsCount() {
    return playlists_count;
  }



  public void setPlaylistsCount(int playlists_count) {
    this.playlists_count = playlists_count;
  }



  public long getMediaFilesCount() {
    return media_files_count;
  }



  public void setMediaFilesCount(long media_files_count) {
    this.media_files_count = media_files_count;
  }



  public long getTotalPlayCount() {
    return total_play_count;
  }



  public void setTotalPlayCount(long total_play_count) {
    this.total_play_count = total_play_count;
  }


  public void setSubscriptionState(String flag) {
    log.warn("subscription_state is not implemented yet");
    SubscriptionState state = SubscriptionState.valueOf( flag );
    if ( state != null ) {
      this.subscription_state = state;
    } else {
      log.warn( "no vaild subscription_state given");
    }
  }
  
  public void setSubscriptionState(SubscriptionState state) {
    this.subscription_state = state;
  }
  
  public SubscriptionState getSubscriptionState() {
    return this.subscription_state;
  }


  
  public void setPermissions(Permissions permissions) {
    this.permissions = permissions;
  }
  
  public Permissions getPermissions() {
    return this.permissions;
  }
  
  public void setAccountStats(AccountStats accountStats) {
    this.accountStats = accountStats;
  }
  
  public AccountStats getAccountStats() {
    return this.accountStats;
  }
  
  
  public void setPreferences(Preferences preferences) {
    this.preferences = preferences;
  }
  
  public Preferences getPreferences() {
    return this.preferences;
  }
  
  
  public void setExternalTokens(ExternalTokens externalTokens) {
    this.externalTokens = externalTokens;
  }
  
  public ExternalTokens getExternalTokens() {
    return this.externalTokens;
  }
  

  /**
   * <p>Getter for the auth_token</p>
   *
   * @return String[] the auth_token
   */
  public String getAuthToken() {
    return auth_token;
  }

  /**
   * <p>Setter for the user auth_token</p>
   *
   * @param auth_token a String the contains the user authentication 
   */
  public void setAuthToken(String auth_token) {
    this.auth_token = auth_token;
    setChanged();
    notifyObservers();
  }
  
  
  
  /* ------------------- */
  /* Collection Browsing */
  /* ------------------- */


  /**
   * Given a known track Token this method will requests AudioBox.fm and returns a valid {@link MediaFile} object.
   *
   * @param token the token of the track you are asking for.
   * 
   * @return the requested track if exists.
   * 
   * @throws LoginException if user has not been authenticated
   * @throws ServiceException if the requested resource doesn't exists or any other ServiceException occur.
   * @throws ModelException 
   */
  public MediaFile newTrackByToken(String token) throws ServiceException, LoginException {
    MediaFile file = (MediaFile) getConfiguration().getFactory().getEntity( MediaFile.TAGNAME, getConfiguration() );
    file.setToken(token);
//    file.load();
    return file;
  }



  
  public MediaFiles getMediaFilesMap() throws ServiceException, LoginException {
    return this.getMediaFilesMap(null);
  }

  /**
   * Use this method to get a {@link MediaFiles} instance containing 
   * all the {@code MD5} and {@code token} for media files owned by this User.
   * You can specify the Source for filtering the results
   * 
   * This method is useful for sync tools.
   *
   * @param source a {@link MediaFile.Source}
   *
   * @return a {@link MediaFiles} instance
   * 
   * @throws ServiceException if any connection problem to AudioBox.fm services occurs.
   * @throws LoginException if any authentication problem occurs.
   */
  public MediaFiles getMediaFilesMap(MediaFile.Source source) throws ServiceException, LoginException {
    MediaFiles mediaFiles = (MediaFiles) getConfiguration().getFactory().getEntity( MediaFiles.TAGNAME, getConfiguration() );
    
    IConnector connector = this.getConnector(IConfiguration.Connectors.RAILS);
    
    String path = IConnector.URI_SEPARATOR.concat( MediaFiles.NAMESPACE );
    String action = MediaFiles.Actions.hashes.toString();
    
    List<NameValuePair> params = null;
    if ( source != null ){
      params = new ArrayList<NameValuePair>();
      params.add( new BasicNameValuePair("source", source.toString().toLowerCase() ) );
    }
    
    connector.get(mediaFiles, path, action, params).send(false);
    
    return mediaFiles;
  }



  public void emptyTrash() throws LoginException, ServiceException {
    Playlists pls = (Playlists)getConfiguration().getFactory().getEntity( Playlists.NAMESPACE, getConfiguration() );
    String requestFormat = this.getConfiguration().getRequestFormat().toString().toLowerCase();
    getConnector().put( pls, Playlists.EMPTY_TRASH_ACTION , requestFormat).send(false);
  }



  /**
   * Instantiates a new Track. This method is used to upload a track
   * 
   * @return a new {@link MediaFile} instance
   */
  public MediaFile newTrack() {
    return (MediaFile) getConfiguration().getFactory().getEntity( MediaFile.TAGNAME, getConfiguration() );
  }




  public Playlists getPlaylists() {
    if ( this.playlists == null ){
      this.playlists = (Playlists) getConfiguration().getFactory().getEntity(Playlists.TAGNAME, getConfiguration());
    }
    return playlists;
  }


  /**
   * Executes request populating this class
   * 
   * @throws ServiceException
   * @throws LoginException
   */
  public void load() throws ServiceException, LoginException {
    this.load(null);
  }

  /**
   * Executes request populating this class and passing the {@link IResponseHandler} as response parser
   * 
   * @param responseHandler the {@link IResponseHandler} used as response content parser
   * @throws ServiceException
   * @throws LoginException
   */
  public void load(IResponseHandler responseHandler) throws ServiceException, LoginException {
    this.getConnector().get(this, null, null).send(false, null, responseHandler);
  }



  @Override
  protected void copy(IEntity entity) {
    // default: do nothing
  }


  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException{

    if ( tagName.equals("username")  )  {
      return this.getClass().getMethod("setUsername", String.class);

    } else if ( tagName.equals("real_name")  )  {
      return this.getClass().getMethod("setRealName", String.class);

    } else if ( tagName.equals("comet_channel")  )  {
      return this.getClass().getMethod("setCometChannel", String.class);

    } else if ( tagName.equals("email")  )  {
      return this.getClass().getMethod("setEmail", String.class);

    } else if ( tagName.equals("auth_token")  )  {
      return this.getClass().getMethod("setAuthToken", String.class);

    } else if ( tagName.equals("time_zone")  )  {
      return this.getClass().getMethod("setTimeZone", String.class);

    } else if ( tagName.equals("accepted_extensions")  )  {
      return this.getClass().getMethod("setAcceptedExtensions", String.class);

    } else if ( tagName.equals("accepted_formats")  )  {
      return this.getClass().getMethod("setAcceptedFormats", String.class);

    } else if ( tagName.equals("country")  )  {
      return this.getClass().getMethod("setCountry", String.class);

    } else if ( tagName.equals("playlists_count")  )  {
      return this.getClass().getMethod("setPlaylistsCount", int.class);

    } else if ( tagName.equals("total_play_count")  )  {
      return this.getClass().getMethod("setTotalPlayCount", long.class);

    } else if ( tagName.equals("media_files_count")  )  {
      return this.getClass().getMethod("setMediaFilesCount", long.class);
     
    } else if ( tagName.equals("subscription_state") ) {
      return this.getClass().getMethod("setSubscriptionState", String.class);

    } else if ( tagName.equals("permissions") ) {
      return this.getClass().getMethod("setPermissions", Permissions.class);
      
    } else if ( tagName.equals("stats") ) {
      return this.getClass().getMethod("setAccountStats", AccountStats.class);
      
    } else if ( tagName.equals("external_tokens") ) {
      return this.getClass().getMethod("setExternalTokens", ExternalTokens.class);
      
    } else if ( tagName.equals("preferences") ) {
      return this.getClass().getMethod("setPreferences", Preferences.class);
    }
    
    
    return null;
  }

  @Override
  public String getApiPath() {
    return IConnector.URI_SEPARATOR + NAMESPACE;
  }


}
