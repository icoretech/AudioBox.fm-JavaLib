
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

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
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
 * <user> 
 *  <bytes_served>25352551153</bytes_served> 
 *  <email>user@domain.com</email> 
 *  <play_count>3378</play_count> 
 *  <quota>1318739344</quota> 
 *  <tracks_count>273</tracks_count> 
 *  <username>username</username> 
 *  <available_storage>162135015424</available_storage> 
 *  <allowed_formats>aac;mp3;mp2;m4a;m4b;m4r;mp4;3gp;ogg;oga;flac;spx;wma;rm;ram;wav;mpc;mp+;mpp;aiff;aif;aifc;tta</allowed_formats> 
 *  <time_zone>Rome</time_zone> 
 *  <trial_ends_at>2011-03-05 08:20:38 +0100</trial_ends_at> 
 *  <profile>....</profile>
 *  <plan>...</plan>
 * </user>
 * @endcode
 * </pre>
 *
 * Through the User object you have access to its library that can be browsed by:
 * <ul>
 *  <li>Playlists</li>
 *  <li>Genres</li>
 *  <li>Artists</li>
 *  <li>Albums</li>
 * </ul>
 * 
 * using its respective getter method.
 * 
 * <p>
 * 
 * Once obtained the desired collection you can get the tracks collection of each contained element 
 * by getting its tracks:
 * 
 * <pre>
 * Artists artists = user.getArtists();
 * artists.invoke();
 * Artists artist = artists.get( "token" or "index" );
 * Tracks trs = artist.getTracks();
 * trs.invoke();
 * Track track = trs.get( "token" or "index" );
 * </pre>
 * 
 * Or you can get informations about a specific, token-known track's, by calling {@link User#getTrackByToken(String)}
 * 
 * @author Valerio Chiodino
 * @author Fabio Tunno
 */
public final class User extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /** User API namespace */
  public static final String NAMESPACE = "user";
  public static final String TAGNAME = NAMESPACE;

  /** Separator used to split the allowed formats string */
  public static final String ALLOWED_EXTENSIONS_SEPARATOR = ";";

  private long bytesServed;
  private String email;
  private long quota;
  private String username;
  private long availableStorage;
  private String timeZone;
  private String trialEndsAt;
  private String[] allowedFormats;
  private int media_files_count;
  private int playlists_count;
  private int total_play_count;
  private boolean can_upload;

  // User's collection relations
  private Playlists playlists;

  private String auth_token;

  private String name;
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


  /**
   * <p>Setter for the user bytes served: used by the parser.</p>
   *
   * @param bytes the String value of the bytes served.
   */
  public void setBytesServed(long bytes) {
    this.bytesServed = bytes;
  }

  /**
   * <p>Getter for the user bytes served.</p>
   *
   * @return the user bytes served
   */
  public long getBytesServed() {
    return this.bytesServed;
  }



  /**
   * <p>Setter for the user email: used by the parser.<p>
   *
   * @param email the user email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * <p>Getter for the user email.</p>
   *
   * @return the user email
   */
  public String getEmail() {
    return this.email;
  }


  public int getTotal_play_count() {
    return total_play_count;
  }



  public void setTotal_play_count(int total_play_count) {
    this.total_play_count = total_play_count;
  }



  /**
   * <p>Setter for the user allowed-formats: used by the parser.</p>
   *
   * @param allowedFormats the allowed format semicolon-separated {@link String}.
   */
  @Deprecated
  public void setAllowedFormats(String allowedFormats){
    this.setAllowedFormats( allowedFormats.split( ALLOWED_EXTENSIONS_SEPARATOR ) );
  }

  /**
   * <p>Setter for the user allowed-formats</p>
   *
   * @param allowedFormats a String array the contains the user subscription allowed media formats 
   */
  public void setAllowedFormats(String[] allowedFormats){
    this.allowedFormats =  allowedFormats;
  }

  /**
   * <p>Getter for the allowed-formats</p>
   *
   * @return String[] the allowed-formats
   */
  public String[] getAllowedFormats(){
    return this.allowedFormats;
  }


  /**
   * <p>Setter for the user quota in bytes: used by the parser.</p>
   *
   * @param quota the String representing the user quota bytes
   */
  public void setQuota(long quota) {
    this.quota = quota;
  }

  /**
   * <p>Getter for the user quota bytes.</p>
   *
   * @return the user quota bytes
   */
  public long getQuota() {
    return this.quota;
  }


  public int getPlaylists_count() {
    return playlists_count;
  }


  public void setPlaylists_count(int playlists_count) {
    this.playlists_count = playlists_count;
  }



  /**
   * <p>Setter for the user nickname: used by the parser.</p>
   *
   * @param username the user nickname
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * <p>Getter for the nickname.</p>
   *
   * @return the user nickname
   */
  public String getUsername() {
    return this.username;
  }



  /**
   * <p>Setter for the user remote available storage: used by the parser.</p>
   *
   * @param availableStorage a {@link String} representing numbers of available storage bytes.
   */
  public void setAvailableStorage(long availableStorage) {
    this.availableStorage = availableStorage;
  }

  /**
   * <p>Getter for the user remote available storage.</p>
   *
   * @return the user available storage
   */
  public long getAvailableStorage() {
    return this.availableStorage;
  }


  /**
   * <p>Setter for the user time zone: used by the parser.</p>
   *
   * @param timeZone the time zone {@link String}.
   */
  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  /**
   * <p>Getter for the user time zone.</p>
   *
   * @return the user time zone
   */
  public String getTimeZone() {
    return this.timeZone;
  }


  /**
   * <p>Sets the expiration date of trial plan: used by the parser.</p>
   *
   * @param trialEndsAt the expiration date as {@link String}.
   */
  public void setTrialEndsAt(String trialEndsAt) {
    this.trialEndsAt = trialEndsAt;
  }


  /**
   * @return the expiration date of trial plan
   */
  public String getTrialEndsAt() {
    return this.trialEndsAt;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getMedia_files_count() {
    return media_files_count;
  }


  public void setMedia_files_count(int media_files_count) {
    this.media_files_count = media_files_count;
  }



  public boolean isCan_upload() {
    return can_upload;
  }



  public void setCan_upload(boolean can_upload) {
    this.can_upload = can_upload;
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




  /**
   * Use this method to get a String array of MD5 hashes of user's already uploaded and ready media files.
   * 
   * <p>
   * 
   * This method is useful for sync tools.
   *
   * @return an array of {@link String} objects containing MD5 hashes for each uploaded track.
   * 
   * @throws ServiceException if any connection problem to AudioBox.fm services occurs.
   * @throws LoginException if any authentication problem occurs.
   */
  public String[] getUploadedTracks() throws ServiceException, LoginException {
    MediaFiles files = (MediaFiles) getConfiguration().getFactory().getEntity( MediaFiles.TAGNAME, getConfiguration() );

    files.load(false);
    String requestFormat = this.getConfiguration().getRequestFormat().toString().toLowerCase();
    
    IConnectionMethod method = getConnector().get(files, null, requestFormat, null);
    method.send( true );

    String result = method.getResponse().getBody();
    String[] resultSplitted = result.split( ";" , result.length() );
    String[] hashes = new String[ resultSplitted.length ];
    int pos = 0;
    for ( String hash : resultSplitted )
      hashes[ pos++ ] = hash.trim();

    return hashes;
  }


  //  public boolean dropTracks(List<Track> tracks) throws LoginException, ServiceException {
  //    try {
  //      return this.getPlaylists().getPlaylistsByType( PlaylistTypes.TRASH ).get( AudioBox.FIRST ).addTracks(tracks);
  //    } catch (ModelException e) {
  //      e.printStackTrace();
  //    }
  //    return false;
  //  }

  //  public boolean dropTrack(Track track) throws LoginException, ServiceException {
  //    List<Track> tracks = new ArrayList<Track>();
  //    tracks.add(track);
  //    return dropTracks( tracks );
  //  }


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
    String requestFormat = this.getConfiguration().getRequestFormat().toString().toLowerCase();
    getConnector().get(this, null, requestFormat, null).send(false, null, responseHandler);
  }



  @Override
  protected void copy(IEntity entity) {
    // default: do nothing
  }


  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException{

    if ( tagName.equals("token") ){
      return this.getClass().getMethod("setToken", long.class);

    } else if ( tagName.equals("bytes_served") ){
      return this.getClass().getMethod("setBytesServed", long.class);

    } else if ( tagName.equals("email") ){
      return this.getClass().getMethod("setEmail", String.class);

    } else if ( tagName.equals("total_play_count") ){
      return this.getClass().getMethod("setTotal_play_count", int.class);

    } else if ( tagName.equals("quota") ){
      return this.getClass().getMethod("setQuota", long.class);

    } else if ( tagName.equals("playlists_count") ){
      return this.getClass().getMethod("setPlaylists_count", int.class);

    } else if ( tagName.equals("username") ){
      return this.getClass().getMethod("setUsername", String.class);

    } else if ( tagName.equals("available_storage") ){
      return this.getClass().getMethod("setAvailableStorage", long.class);

    } else if ( tagName.equals("time_zone") ){
      return this.getClass().getMethod("setTimeZone", String.class);

    } else if ( tagName.equals("trial_ends_at") ){
      return this.getClass().getMethod("setTrialEndsAt", String.class);

    } else if ( tagName.equals("allowed_formats") ){
      return this.getClass().getMethod("setAllowedFormats", String.class);

    } else if ( tagName.equals("auth_token")){
      return this.getClass().getMethod("setAuthToken", String.class);

    } else if ( tagName.equals("name")){
      return this.getClass().getMethod("setName", String.class);
      
    } else if ( tagName.equals("media_files_count")){
      return this.getClass().getMethod("setMedia_files_count", int.class);
      
    } else if ( tagName.equals("can_upload")){
      return this.getClass().getMethod("setCan_upload", boolean.class);
    }
    
    
    return null;
  }

  @Override
  public String getApiPath() {
    return "/" + NAMESPACE;
  }



  @Override
  public void setParent(IEntity parent) {}

}
