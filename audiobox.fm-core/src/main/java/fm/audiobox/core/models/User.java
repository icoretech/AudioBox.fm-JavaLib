
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
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IAuthenticationHandle;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
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
 * created_at: '',
 * updated_at: '',
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

  public static final String ID = "id";
  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String REAL_NAME = "real_name";
  public static final String EMAIL = "email";
  public static final String AUTH_TOKEN = "auth_token";
  public static final String TIME_ZONE = "time_zone";
  public static final String ACCEPTED_EXTENSIONS = "accepted_extensions";
  public static final String ACCEPTED_FORMATS = "accepted_formats";
  public static final String COUNTRY = "country";
  public static final String PLAYLISTS_COUNT = "playlists_count";
  public static final String TOTAL_PLAY_COUNT = "total_play_count";
  public static final String MEDIA_FILES_COUNT = "media_files_count";
  public static final String SUBSCRIPTION_STATE = "subscription_state";
  public static final String COMET_CHANNEL = "comet_channel";
  public static final String CREATED_AT = "created_at";
  public static final String UPDATED_AT = "updated_at";

  public static enum SubscriptionState {
    nothing,
    active,
    trialing,
    past_due,
    canceled,
    unpaid
  }

  /** Separator used to split the allowed formats string */
  public static final String ALLOWED_EXTENSIONS_SEPARATOR = ",";

  private String id;
  private String username;
  private String password;
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
  private String createdAt;
  private String updatedAt;

  // User's collection relations
  private Playlists playlists;
  private Permissions permissions;
  private AccountStats accountStats;
  private Preferences preferences;
  private ExternalTokens externalTokens;
  
  
  
  private static Map<String, Method> setterMethods = new HashMap<String, Method>();
  
  static {
    try {
      setterMethods.put( ID, User.class.getMethod("setId", String.class) );
      setterMethods.put( USERNAME, User.class.getMethod("setUsername", String.class) );
      setterMethods.put( REAL_NAME, User.class.getMethod("setRealName", String.class) );
      setterMethods.put( COMET_CHANNEL, User.class.getMethod("setCometChannel", String.class) );
      setterMethods.put( EMAIL, User.class.getMethod("setEmail", String.class) );
      setterMethods.put( AUTH_TOKEN, User.class.getMethod("setAuthToken", String.class) );
      setterMethods.put( TIME_ZONE, User.class.getMethod("setTimeZone", String.class) );
      setterMethods.put( ACCEPTED_EXTENSIONS, User.class.getMethod("setAcceptedExtensions", String.class) );
      setterMethods.put( ACCEPTED_FORMATS, User.class.getMethod("setAcceptedFormats", String.class) );
      setterMethods.put( CREATED_AT, User.class.getMethod("setCreatedAt", String.class) );
      setterMethods.put( UPDATED_AT, User.class.getMethod("setUpdatedAt", String.class) );
      setterMethods.put( COUNTRY, User.class.getMethod("setCountry", String.class) );
      setterMethods.put( PLAYLISTS_COUNT, User.class.getMethod("setPlaylistsCount", int.class) );
      setterMethods.put( TOTAL_PLAY_COUNT, User.class.getMethod("setTotalPlayCount", long.class) );
      setterMethods.put( MEDIA_FILES_COUNT, User.class.getMethod("setMediaFilesCount", long.class) );
      setterMethods.put( SUBSCRIPTION_STATE, User.class.getMethod("setSubscriptionState", String.class) );
      setterMethods.put( Permissions.TAGNAME, User.class.getMethod("setPermissions", Permissions.class) );
      setterMethods.put( AccountStats.TAGNAME, User.class.getMethod("setAccountStats", AccountStats.class) );
      setterMethods.put( ExternalTokens.TAGNAME, User.class.getMethod("setExternalTokens", ExternalTokens.class) );
      setterMethods.put( Preferences.TAGNAME, User.class.getMethod("setPreferences", Preferences.class) );
      
    } catch (SecurityException e) {
      log.error("Security error", e);
    } catch (NoSuchMethodException e) {
      log.error("No method found", e);
    }
    
  }
  


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


  public String getId() {
    return id;
  }



  public void setId(String id) {
    this.id = id;
  }


  public String getPassword() {
    return password;
  }



  public void setPassword(String password) {
    this.password = password;
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

  

  public String getCreatedAt() {
    return createdAt;
  }



  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }



  public String getUpdatedAt() {
    return updatedAt;
  }



  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
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
  public MediaFiles getMediaFilesMap(String source) throws ServiceException, LoginException {
    MediaFiles mediaFiles = (MediaFiles) getConfiguration().getFactory().getEntity( MediaFiles.TAGNAME, getConfiguration() );

    IConnector connector = this.getConnector(IConfiguration.Connectors.RAILS);

    String path = IConnector.URI_SEPARATOR.concat( MediaFiles.NAMESPACE );
    String action = MediaFiles.Actions.hashes.toString();

    List<NameValuePair> params = null;
    params = new ArrayList<NameValuePair>();
    params.add( new BasicNameValuePair("source", source.toLowerCase() ) );

    connector.get(mediaFiles, path, action, params).send(false);

    return mediaFiles;
  }


  /**
   * Instantiates a new {@link MediaFile}.
   * Use this method to upload a new media file.
   *
   * @return a new {@link MediaFile} instance
   */
  public MediaFile newMediaFile() {
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
    this.load(false);
  }



  @Override
  protected void copy(IEntity entity) {
    // default: do nothing
  }


  public Method getSetterMethod(String tagName) {
    if ( setterMethods.containsKey( tagName) ) {
      return setterMethods.get( tagName );
    }
    return null;
  }

  @Override
  public String getApiPath() {
    return IConnector.URI_SEPARATOR + NAMESPACE;
  }

  public IConnectionMethod load(boolean async) throws ServiceException, LoginException {
    return this.load(async, null);
  }


  public IConnectionMethod load(boolean async, IResponseHandler responseHandler) throws ServiceException, LoginException {
  //add the object to be observed, the observer
    IConnectionMethod req = this.getConfiguration().getFactory().getConnector().get(this, null, null);

    if ( this.getUsername() != null && this.getPassword() != null ) {
      req.setAuthenticationHandle(new IAuthenticationHandle() {
        public void handle(IConnectionMethod request) {
          UsernamePasswordCredentials mCredentials = new UsernamePasswordCredentials( username, password );
          request.addHeader( BasicScheme.authenticate(mCredentials, Consts.UTF_8.name(), false ) );
        }
      });
    }

    req.send(async);

    return req;
  }
  
  protected List<NameValuePair> toQueryParameters(boolean all) {
    String prefix = TAGNAME + "[";
    String suffix = "]";
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    
    params.add(new BasicNameValuePair(prefix + ID + suffix, this.id )  );
    params.add(new BasicNameValuePair(prefix + USERNAME + suffix, this.username )  );
    params.add(new BasicNameValuePair(prefix + PASSWORD + suffix, this.password )  );
    params.add(new BasicNameValuePair(prefix + REAL_NAME + suffix, this.real_name )  );
    params.add(new BasicNameValuePair(prefix + EMAIL + suffix, this.email )  );
    params.add(new BasicNameValuePair(prefix + AUTH_TOKEN + suffix, this.auth_token )  );
    params.add(new BasicNameValuePair(prefix + TIME_ZONE + suffix, this.time_zone )  );
    params.add(new BasicNameValuePair(prefix + ACCEPTED_EXTENSIONS + suffix, this.accepted_extensions )  );
    params.add(new BasicNameValuePair(prefix + ACCEPTED_FORMATS + suffix, this.accepted_formats )  );
    params.add(new BasicNameValuePair(prefix + COUNTRY + suffix, this.country )  );
    params.add(new BasicNameValuePair(prefix + PLAYLISTS_COUNT + suffix, String.valueOf( this.playlists_count ) )  );
    params.add(new BasicNameValuePair(prefix + TOTAL_PLAY_COUNT + suffix, String.valueOf( this.total_play_count ) )  );
    params.add(new BasicNameValuePair(prefix + MEDIA_FILES_COUNT + suffix, String.valueOf( this.media_files_count ) )  );
    params.add(new BasicNameValuePair(prefix + SUBSCRIPTION_STATE + suffix, String.valueOf( this.subscription_state ) )  );
    params.add(new BasicNameValuePair(prefix + COMET_CHANNEL + suffix, this.comet_channel )  );
    
    return params;
  }


}
