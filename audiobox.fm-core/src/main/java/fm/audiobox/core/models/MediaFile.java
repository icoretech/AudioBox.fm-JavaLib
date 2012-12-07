/***************************************************************************
 *   Copyright (C) 2010 iCoreTech research labs                            *
 *   Contributed code from:                                                *
 *   - Lucio Regina 							                           *
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

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.AudioBox;
import fm.audiobox.configurations.MimeTypes;
import fm.audiobox.configurations.Response;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFiles.Type;
import fm.audiobox.core.parsers.DownloadHandler;
import fm.audiobox.core.parsers.UploadHandler;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IResponseHandler;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;

/**
 * File is the main subject of the scope of these libraries.
 * 
 * <p>
 * 
 * Once browsed till arriving to a File, many operations can be done with it.
 * 
 * <p>
 * 
 * The JSON response looks like this:
 *
 * <pre>
 * @code
 *  { type: 'AudioFile',
 *    token: 'ce4c51285e3e6a087434a0',
 *    artist: 'Artist',
 *    album: 'Album',
 *    genre: 'Genre',
 *    year: 2012,
 *    title: 'Title',
 *    len_str: '5:05',
 *    len_int: 305,
 *    position: 6,
 *    plays: 0,
 *    filename: '{token}.mp3',
 *    loved: false,
 *    disc: 1,
 *    mime: 'audio/mpeg',
 *    size: 7309853,
 *    md5: 'a6e89bd8a081f0736cf89961d66884d4',
 *    video_bitrate: null,
 *    video_codec: null,
 *    video_resolution: null,
 *    video_fps: null,
 *    video_aspect: null,
 *    video_container: null,
 *    audio_bitrate: '192',
 *    audio_codec: null,
 *    audio_sample_rate: '44100',
 *    artworks: 'http://assets.development.audiobox.fm/a/l.jpg'
 *  }
 * @endcode
 * </pre>
 *
 * @author Lucio Regina
 * @version 0.0.1
 */
public class MediaFile extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private static Logger log = LoggerFactory.getLogger(MediaFile.class);
  
  public static final String TAGNAME = "media_file";
  public static final String NAMESPACE = MediaFile.TAGNAME;
  
  public static final String TITLE =              "title";
  public static final String ARTIST =             "artist";
  public static final String ALBUM =              "album";
  public static final String GENRE =              "genre";
  public static final String LEN_STR =            "len_str";
  public static final String MEDIA_FILE_NAME =    "media_file_name";
  public static final String MIME =               "mime";
  public static final String YEAR =               "release_year";
  public static final String LEN_INT =            "len_int";
  public static final String POSITION =           "position";
  public static final String PLAYS =              "plays";
  public static final String DISC =               "disc_number";
  public static final String TYPE =               "type";
  public static final String SIZE =               "size";
  public static final String AUDIO_SAMPLE_RATE =  "audio_sample_rate";
  public static final String AUDIO_BIT_RATE =     "audio_bit_rate";
  public static final String SOURCE =             "source";
  public static final String ORIGINAL_FILE_NAME = "original_file_name";
  public static final String HASH =               "hash";
  public static final String FILENAME =           "filename";
  public static final String LOVED =              "loved";
  public static final String REMOTE_PATH  =       "remote_path";
  public static final String SHARE_TOKEN =        "share_token";
  public static final String ARTWORK =            "artwork";
  
  private String artist;
  private String album;
  private String genre;
  private int year;
  private String title;
  private String lenStr;
  private int lenInt;
  private int position;
  private int plays;
  private int disc;
  private String mediaFileName;
  private Type type = MediaFiles.Type.AudioFile;
  private int rating;
  private long size;
  private String mime;
  private String source;
  private String audioSampleRate;
  private String audioBitRate;
  private String originalFileName;
  private String hash;
  private String filename;
  private String remotePath;
  private String shareToken;
  private boolean loved;

  private String artwork;
   
  private enum Actions {
    stream,
    upload,
    local,
    download
  }
  
  public static enum Source {
    local,
    cloud,
    dropbox,
    youtube,
    soundcloud,
    gdrive,
    skydrive
  }
  
  
  private static final Map<String, Method> setterMethods = new HashMap<String, Method>();
  static {
    try {
      setterMethods.put( TOKEN_SHORT, MediaFile.class.getMethod( "setToken", String.class ) );
      setterMethods.put( TOKEN, MediaFile.class.getMethod( "setToken", String.class ) );
      setterMethods.put( ARTIST, MediaFile.class.getMethod( "setArtist", String.class ) );
      setterMethods.put( ALBUM, MediaFile.class.getMethod( "setAlbum", String.class ) );
      setterMethods.put( GENRE, MediaFile.class.getMethod( "setGenre", String.class ) );
      setterMethods.put( YEAR, MediaFile.class.getMethod( "setYear", int.class ) );
      setterMethods.put( TITLE, MediaFile.class.getMethod( "setTitle", String.class ) );
      setterMethods.put( LEN_STR, MediaFile.class.getMethod( "setLenStr", String.class ) );
      setterMethods.put( LEN_INT, MediaFile.class.getMethod( "setLenInt", int.class ) );
      setterMethods.put( POSITION, MediaFile.class.getMethod( "setPosition", int.class ) );
      setterMethods.put( PLAYS, MediaFile.class.getMethod( "setPlays", int.class ) );
      setterMethods.put( DISC, MediaFile.class.getMethod( "setDisc", int.class ) );
      setterMethods.put( MEDIA_FILE_NAME, MediaFile.class.getMethod( "setMediaFileName", String.class ) );
      setterMethods.put( MIME, MediaFile.class.getMethod( "setMime", String.class ) );
      setterMethods.put( TYPE, MediaFile.class.getMethod( "setType", String.class ) );
      setterMethods.put( SOURCE, MediaFile.class.getMethod( "setSource", String.class ) );
      setterMethods.put( SIZE, MediaFile.class.getMethod( "setSize", long.class ) );
      setterMethods.put( AUDIO_SAMPLE_RATE, MediaFile.class.getMethod( "setAudioSampleRate", String.class ) );
      setterMethods.put( AUDIO_BIT_RATE, MediaFile.class.getMethod( "setAudioBitRate", String.class ) );
      setterMethods.put( ARTWORK, MediaFile.class.getMethod( "setArtwork", String.class ) );
      setterMethods.put( HASH, MediaFile.class.getMethod( "setHash", String.class ) );
      setterMethods.put( FILENAME, MediaFile.class.getMethod( "setFilename", String.class ) );
      setterMethods.put( LOVED, MediaFile.class.getMethod( "setLoved", boolean.class ) );
      setterMethods.put( REMOTE_PATH, MediaFile.class.getMethod( "setRemotePath", String.class ) );
      setterMethods.put( SHARE_TOKEN, MediaFile.class.getMethod( "setShareToken", String.class ) );
    } catch (SecurityException e) {
      log.error("Security error", e);
    } catch (NoSuchMethodException e) {
      log.error("No method found", e);
    }
  }
  

  public MediaFile(IConfiguration config) {
    super(config);
  }

  /**
   * Contructor method
   * This class can be instantiated always.
   * We should link the new class to the current {@link AudioBox} instance
   * @param config
   */
  public MediaFile(AudioBox abxClient) {
    super( abxClient.getConfiguration() );
  }

  @Override
  public String getNamespace() {
    return NAMESPACE;
  }

  @Override
  public String getTagName() {
    return TAGNAME;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }
  
  public String getFilename() {
    return this.filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }
  
  public boolean isLoved() {
    return this.loved;
  }

  public void setLoved(boolean loved) {
    this.loved = loved;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getLenStr() {
    return lenStr;
  }

  public void setLenStr(String len_str) {
    this.lenStr = len_str;
  }

  public int getLenInt() {
    return lenInt;
  }

  public void setLenInt(int len_int) {
    this.lenInt = len_int;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }
  
  public long getSize() {
    return this.size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public int getPlays() {
    return plays;
  }

  public void setPlays(int plays) {
    this.plays = plays;
  }

  public int getDisc() {
    return disc;
  }

  public void setDisc(int disc) {
    this.disc = disc;
  }

  public String getMediaFileName() {
    return this.mediaFileName;
  }

  public void setMediaFileName(String filename) {
    this.mediaFileName = filename;
  }

  public Type getType() {
    return type;
  }
  
  public void setType(Type t) {
    this.type = t;
  }

  public void setType(String type) {

    if( Type.AudioFile.toString().equals( type ) ){
      this.type = Type.AudioFile;	
    } else if( Type.VideoFile.toString().equals( type ) ){
      this.type = Type.VideoFile;
    }
  }
  
  public String getSource() {
    return this.source;
  }
  
  public void setSource(String s) {
    this.source = s;
  }

  public String getRemotePath() {
    return remotePath;
  }

  public void setRemotePath(String remotePath) {
    this.remotePath = remotePath;
  }

  public String getShareToken() {
    return shareToken;
  }

  public void setShareToken(String shareToken) {
    this.shareToken = shareToken;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public String getMime() {
    return mime;
  }

  public void setMime(String mime) {
    this.mime = mime;
  }
  
  public String getAudioSampleRate() {
    return audioSampleRate;
  }

  public void setAudioSampleRate(String audioSampleRate) {
    this.audioSampleRate = audioSampleRate;
  }

  public String getAudioBitRate() {
    return audioBitRate;
  }

  public void setAudioBitRate(String audioBitRate) {
    this.audioBitRate = audioBitRate;
  }
  

  public String getOriginalFileName() {
    return originalFileName;
  }

  public void setOriginalFileName(String originalFileName) {
    this.originalFileName = originalFileName;
  }
  

  public String getHash() {
    return this.hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }
  
  public void setArtwork(String artwork){
    this.artwork = artwork;
  }
  
  public String getArtwork(){
    return this.artwork;
  }
  

  public Method getSetterMethod(String tagName) {
    if ( setterMethods.containsKey( tagName) ) {
      return setterMethods.get( tagName );
    }
    return null;
  }
  
  

  @Override
  protected void copy(IEntity entity) {
    log.warn("Not implemented yet");
  }
  
  public String getStreamUrl() {
    return IConnector.URI_SEPARATOR + Actions.stream + IConnector.URI_SEPARATOR + this.getFilename();
  }
  
  public String computeStreamUrl() {
    String path = IConnector.URI_SEPARATOR.concat( Actions.stream.toString() );
    String action = this.getFilename();

    IConnectionMethod request = this.getConnector(IConfiguration.Connectors.NODE).get(this,  path , action, null , null);
    return request.getHttpMethod().getURI().toString();
  }


  @Override
  public String getApiPath() {
    return this.getParent().getApiPath() + IConnector.URI_SEPARATOR + this.getToken();
  }
  
  
  @Override
  public IConnectionMethod load(boolean async) throws ServiceException, LoginException {
    return this.load(false, null);
  }

  @Override
  public IConnectionMethod load(boolean async, IResponseHandler responseHandler) throws ServiceException, LoginException {
    IConnectionMethod request = getConnector(IConfiguration.Connectors.RAILS).get(this, IConnector.URI_SEPARATOR + MediaFiles.NAMESPACE, this.getToken(), null);
    request.send(async, null, responseHandler);
    return request;
  }
  
  
  // DELETE /api/v1/media_files/destroy_multiple.json?tokens[]=
  public boolean destroy() throws ServiceException, LoginException {
    
    if ( this.getParent() == null ){
      // We can delete an arbitrary MediaFile
      return this._destroy();
    }
    
    List<MediaFile> toRemove = new ArrayList<MediaFile>();
    toRemove.add( this );
    
    return ((MediaFiles) this.getParent()).destroy( toRemove );
  }
  
  
  
  private boolean _destroy() throws ServiceException, LoginException {
    
    String path = IConnector.URI_SEPARATOR.concat( MediaFiles.NAMESPACE );
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(  new BasicNameValuePair(MediaFiles.TOKENS_PARAMETER, this.getToken() ) );
    
    Response response = this.getConnector(IConfiguration.Connectors.RAILS).delete(this, path, MediaFiles.Actions.multidestroy.toString(), params).send(false);
    
    return response.isOK();
  }
  
  
  
  // PUT /api/v1/media_files/:token.json
  public boolean update() throws ServiceException, LoginException {
    String namespace = IConnector.URI_SEPARATOR + MediaFiles.NAMESPACE;
    String action = this.getToken();
    
    List<NameValuePair> params = this.toQueryParameters(true);
    Response response = this.getConnector(IConfiguration.Connectors.RAILS).put(this, namespace, action, null).send(false, params);
    
    return response.isOK();
  }
  
  

  
  public IConnectionMethod upload(boolean async, UploadHandler uploadHandler ) throws ServiceException, LoginException {
    return this.upload(async, uploadHandler, false);
  }
  
  public IConnectionMethod upload(boolean async, UploadHandler uploadHandler, boolean customFields) throws ServiceException, LoginException {
    String path = IConnector.URI_SEPARATOR.concat( Actions.upload.toString() );

    MultipartEntity entity = new MultipartEntity();
    
    String mime = MimeTypes.getMime( uploadHandler.getFile() );
    if ( mime == null ) {
      throw new ServiceException("mime type error: file is not supported by AudioBox2");
    }
    
    entity.addPart("files[]", uploadHandler );
    
    if ( customFields ) {
      
      List<NameValuePair> fields = this.toQueryParametersCustomFields(false);
      for ( NameValuePair field : fields ) {
        try {
          entity.addPart( field.getName(), new StringBody( field.getValue(), "text/plain", Charset.forName( "UTF-8" )));
        } catch (UnsupportedEncodingException e) {
          log.warn("Entity " + field.getName() + " cannot be added due to: " + e.getMessage() );
        }
      }
    }
    

    IConnectionMethod request = this.getConnector(IConfiguration.Connectors.NODE).post(this, path, null, null);
    request.send(async, entity);
    return request;
  }


  /**
   * Downloads this current {@link MediaFile} file and store it into given {@link File} file 
   * 
   * @param file which store media file into
   * 
   * @throws ServiceException generic error 
   * @throws LoginException generic LoginException
   */
  public void download(final File file) throws ServiceException, LoginException {
    if( file != null ){
      
      download(file, new DownloadHandler(file, IConnector.DEFAULT_CHUNK ));
      
    } else {
      throw new ServiceException("No file found for downloading media");
    }
  }

  public IConnectionMethod download(final File file, DownloadHandler downloadHandler) throws ServiceException, LoginException {

    if( file != null ){
      // In this case we are using 'path' for the action
      // and 'action' for the filename
      String path = IConnector.URI_SEPARATOR.concat( Actions.download.toString() );
      String action = this.getFilename();

      IConnectionMethod request = this.getConnector(IConfiguration.Connectors.NODE).get(this,  path , action, null , null);
      request.send(false, null, downloadHandler);
      return request;

    } else {
      throw new ServiceException("No file found for downloading media");
    }
  }
  
  /**
   * This method makes a POST request for notifying AudioBox.fm this {@link MediaFile}
   * is a local file which can be used through AudioBox.fm - PC feauture
   */
  public boolean notifyAsLocal() throws ServiceException, LoginException {
    String path = IConnector.URI_SEPARATOR.concat( Actions.local.toString() );
    String action = Actions.upload.toString();
    
    List<NameValuePair> params = this.toQueryParameters(false);
    
    Response response = this.getConnector(IConfiguration.Connectors.NODE).post(this, path, action, null).send(false, params);
    return response.isOK();
  }
  
  
  private List<NameValuePair> toQueryParameters(boolean withPrefix) {
    String prefix = withPrefix ? NAMESPACE + "." : "";
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    
    params.add(  new BasicNameValuePair( prefix + TITLE,               this.title ) );
    params.add(  new BasicNameValuePair( prefix + ARTIST,              this.artist )  );
    params.add(  new BasicNameValuePair( prefix + ALBUM,               this.album )  );
    params.add(  new BasicNameValuePair( prefix + GENRE,               this.genre )  );
    params.add(  new BasicNameValuePair( prefix + LEN_STR,             this.lenStr )  );
    params.add(  new BasicNameValuePair( prefix + MEDIA_FILE_NAME,     this.mediaFileName )  );
    params.add(  new BasicNameValuePair( prefix + MIME,                this.mime )  );
    params.add(  new BasicNameValuePair( prefix + YEAR,                String.valueOf( this.year ) )  );
    params.add(  new BasicNameValuePair( prefix + LEN_INT,             String.valueOf( this.lenInt ) )  );
    params.add(  new BasicNameValuePair( prefix + POSITION,            String.valueOf( this.position ) )  );
    params.add(  new BasicNameValuePair( prefix + PLAYS,               String.valueOf( this.plays ) )  );
    params.add(  new BasicNameValuePair( prefix + DISC,                String.valueOf( this.disc ) )  );
    params.add(  new BasicNameValuePair( prefix + SIZE,                String.valueOf( this.size )  )  );
    params.add(  new BasicNameValuePair( prefix + TYPE,                this.type.toString() )  );
    params.add(  new BasicNameValuePair( prefix + SOURCE,              this.source != null ? this.source.toString() : null )  );
    params.add(  new BasicNameValuePair( prefix + AUDIO_SAMPLE_RATE,   this.audioSampleRate )  );
    params.add(  new BasicNameValuePair( prefix + AUDIO_BIT_RATE,      this.audioBitRate )  );
    params.add(  new BasicNameValuePair( prefix + ORIGINAL_FILE_NAME,  this.originalFileName )  );
    params.add(  new BasicNameValuePair( prefix + HASH,                 this.hash )  );
    
    return params;
  }
  
  
  private List<NameValuePair> toQueryParametersCustomFields(boolean withPrefix) {
    String prefix = withPrefix ? NAMESPACE + "." : "";
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    
    params.add(  new BasicNameValuePair( prefix + TITLE,               this.title ) );
    params.add(  new BasicNameValuePair( prefix + ARTIST,              this.artist )  );
    params.add(  new BasicNameValuePair( prefix + ALBUM,               this.album )  );
    params.add(  new BasicNameValuePair( prefix + GENRE,               this.genre )  );
    params.add(  new BasicNameValuePair( prefix + YEAR,                String.valueOf( this.year ) )  );
    params.add(  new BasicNameValuePair( prefix + POSITION,            String.valueOf( this.position ) )  );
    params.add(  new BasicNameValuePair( prefix + PLAYS,               String.valueOf( this.plays ) )  );
    params.add(  new BasicNameValuePair( prefix + HASH,                this.hash )  );
    
    return params;
  }

}
