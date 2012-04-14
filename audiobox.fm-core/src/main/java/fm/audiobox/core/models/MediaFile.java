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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.AudioBox;
import fm.audiobox.configurations.DefaultResponseParser;
import fm.audiobox.configurations.Response;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFiles.Type;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

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
 *    artworks: {
 *      l: 'http://assets.development.audiobox.fm/a//l.jpg',
 *      s: 'http://assets.development.audiobox.fm/a//s.jpg'
 *    }
 *  }
 * @endcode
 * </pre>
 *
 * @author Lucio Regina
 * @version 0.0.1
 */
public class MediaFile extends AbstractEntity implements Serializable{

  private static final long serialVersionUID = 1L;

  private static Logger log = LoggerFactory.getLogger(MediaFile.class);
  
  public static final String TAGNAME = "media_file";
  public static final String NAMESPACE = MediaFile.TAGNAME;
  
  public static final String TITLE_FIELD =            "title";
  public static final String ARTIST_FIELD =           "artist";
  public static final String ALBUM_FIELD =            "album";
  public static final String GENRE_FIELD =            "genre";
  public static final String LEN_STR_FIELD =          "len_str";
  public static final String MEDIA_FILE_NAME_FIELD =  "media_file_name";
  public static final String MIME_FIELD =             "mime";
  public static final String YEAR_FIELD =             "year";
  public static final String LEN_INT_FIELD =          "len_int";
  public static final String POSITION_FIELD =         "position";
  public static final String PLAYS_FIELD =            "plays";
  public static final String DISC_FIELD =             "disc";
  public static final String TYPE_FIELD =             "type";
  public static final String SIZE_FIELD =             "size";
  public static final String AUDIO_SAMPLE_RATE_FIELD = "audio_sample_rate";
  public static final String AUDIO_BIT_RATE_FIELD =    "audio_bit_rate";
  public static final String SOURCE_FIELD =            "source";
  public static final String ORIGINAL_FILE_NAME_FIELD = "original_file_name";
  public static final String MD5_FIELD =                "md5";
  
  private static final String TOKENS_PARAMETER =        "tokens[]";
  
  
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
  private Type type;
  private int rating;
  private long size;
  private String mime;
  private Source source;
  private String audioSampleRate;
  private String audioBitRate;
  private String originalFileName;
  private String md5;

  private ArtWorks artworks;
   
  private IEntity parent;

  private enum Actions {
    stream,
    upload,
    local,
    destroy_multiple
  }
  
  public static enum Source {
    local,
    cloud,
    dropbox
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

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
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
  
  public Source getSource() {
    return this.source;
  }
  
  public void setSource(Source s) {
    this.source = s;
  }

  public void setSource(String s) {

    if( Source.cloud.toString().equals( s ) ){
      this.source = Source.cloud; 
    } else if( Source.local.toString().equals( s ) ){
      this.source = Source.local;
    } else if( Source.dropbox.toString().equals( s ) ){
      this.source = Source.dropbox;
    }
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
  

  public String getMd5() {
    return md5;
  }

  public void setMd5(String md5) {
    this.md5 = md5;
  }
  
  public void setArtWorks(ArtWorks artworks){
    this.artworks = artworks;
  }
  
  public ArtWorks getArtWorks(){
    return this.artworks;
  }
  

  @Override
  public Method getSetterMethod(String tagName) throws SecurityException,	NoSuchMethodException {
    if ( tagName.equals( TOKEN_FIELD ) || tagName.equals( TOKEN_SHORT_FIELD ) ){
      return this.getClass().getMethod("setToken", String.class);
    } else if ( tagName.equals(ARTIST_FIELD) ){
      return this.getClass().getMethod("setArtist", String.class);
    } else if ( tagName.equals(ALBUM_FIELD) ){
      return this.getClass().getMethod("setAlbum", String.class);
    } else if ( tagName.equals(GENRE_FIELD) ){
      return this.getClass().getMethod("setGenre", String.class);
    } else if ( tagName.equals(YEAR_FIELD) ){
      return this.getClass().getMethod("setYear", int.class);
    } else if ( tagName.equals(TITLE_FIELD) ){
      return this.getClass().getMethod("setTitle", String.class);
    } else if ( tagName.equals(LEN_STR_FIELD) ){
      return this.getClass().getMethod("setLenStr", String.class);
    } else if ( tagName.equals(LEN_INT_FIELD) ){
      return this.getClass().getMethod("setLenInt", int.class);
    } else if ( tagName.equals(POSITION_FIELD) ){
      return this.getClass().getMethod("setPosition", int.class);
    } else if ( tagName.equals(PLAYS_FIELD) ){
      return this.getClass().getMethod("setPlays", int.class);
    } else if ( tagName.equals(DISC_FIELD) ){
      return this.getClass().getMethod("setDisc", int.class);
    } else if ( tagName.equals(MEDIA_FILE_NAME_FIELD) ){
      return this.getClass().getMethod("setMediaFileName", String.class);
    } else if ( tagName.equals(MIME_FIELD) ){
      return this.getClass().getMethod("setMime", String.class);
    } else if ( tagName.equals(TYPE_FIELD) ){
      return this.getClass().getMethod("setType", String.class);
    } else if ( tagName.equals(SOURCE_FIELD) ){
      return this.getClass().getMethod("setSource", String.class);
    } else if ( tagName.equals(SIZE_FIELD) ){
      return this.getClass().getMethod("setSize", long.class);
    } else if ( tagName.equals(AUDIO_SAMPLE_RATE_FIELD) ){
      return this.getClass().getMethod("setAudioSampleRate", String.class);
    } else if ( tagName.equals(AUDIO_BIT_RATE_FIELD) ){
      return this.getClass().getMethod("setAudioBitRate", String.class);
    } else if ( tagName.equals( ArtWorks.NAMESPACE )  ) {
      return this.getClass().getMethod("setArtWorks", ArtWorks.class);
    }
    
    return null;
  }


  @Override
  protected void copy(IEntity entity) {
    // TODO Auto-generated method stub
  }

  //  /**
  //   * Executes request populating this class
  //   * 
  //   * @throws ServiceException
  //   * @throws LoginException
  //   */
  //  public void load() throws ServiceException, LoginException {
  //    this.load(null);
  //  }
  //
  //  /**
  //   * Executes request populating this class and passing the {@link IResponseHandler} as response parser
  //   * 
  //   * @param responseHandler the {@link IResponseHandler} used as response content parser
  //   * @throws ServiceException
  //   * @throws LoginException
  //   */
  //  public void load(IResponseHandler responseHandler) throws ServiceException, LoginException {
  //    getConnector().get(this, null, null).send(false, null, responseHandler);
  //  }

  @Override
  public String getApiPath() {
    return this.parent.getApiPath() + IConnector.URI_SEPARATOR + this.getToken();
  }
  
  
  // DELETE /api/v1/media_files/destroy_multiple.json?tokens[]=
  public boolean delete() throws ServiceException, LoginException {
    String namespace = IConnector.URI_SEPARATOR.concat( MediaFiles.NAMESPACE );
    String action = IConnector.URI_SEPARATOR.concat( Actions.destroy_multiple.toString() );
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(  new BasicNameValuePair(TOKENS_PARAMETER,               this.getToken() ) );
    
    Response response = this.getConnector(IConfiguration.Connectors.RAILS).post(this, namespace, action, null).send(false, params);
    
    return response.isOK();
  }
  
  
  
  // PUT /api/v1/media_files/:token.json
  public boolean update() throws ServiceException, LoginException {
    String namespace = MediaFiles.NAMESPACE;
    String action = this.getToken();
    
    List<NameValuePair> params = this.toQueryParameters(true);
    Response response = this.getConnector(IConfiguration.Connectors.RAILS).post(this, namespace, action, null).send(false, params);
    
    return response.isOK();
  }
  
  

  @Override
  public void setParent(IEntity parent) {
    this.parent = parent;
  }

  public boolean upload(File file) throws ServiceException, LoginException {
    String path = IConnector.URI_SEPARATOR.concat( Actions.upload.toString() );

    MultipartEntity entity = new MultipartEntity();

    entity.addPart("media", new FileBody(file, new MimetypesFileTypeMap().getContentType(file)));

    Response response = this.getConnector(IConfiguration.Connectors.NODE).post(this, path, null, null).send(false, entity);
    return response.isOK();
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
      IResponseHandler responseparser = new DefaultResponseParser(){
        @Override
        public void parseAsBinary(InputStream inputStream, IEntity destEntity) throws ServiceException {
          try {
            OutputStream out = new FileOutputStream(file);
            byte buf[] = new byte[1024 * 64];
            int len;
            while((len=inputStream.read(buf))>0){
              out.write(buf,0,len);
            }          
            out.close();
            inputStream.close();

          } catch (FileNotFoundException e) {
            log.error("File not found: " + file.getName());
          } catch (IOException e) {
            log.error("IOException : " + e.getMessage());
          }
        }
      };
      download(file, responseparser);
    }else
      log.warn("Input file is null");
  }

  public void download(final File file, IResponseHandler responseHandler) throws ServiceException, LoginException {

    if( file != null ){
      // In this case we are using 'path' for the action
      // and 'action' for the filename
      String path = IConnector.URI_SEPARATOR.concat( Actions.stream.toString() );
      String action = this.getMediaFileName();

      // TODO: perform this action only when filename property has been correctly populated
      this.getConnector(IConfiguration.Connectors.NODE).get(this,  path , action, null , null).send(false,null,responseHandler);

    }else
      log.warn("Input file is null");

  }
  
  /**
   * This method makes a POST request for notifying AudioBox.fm this {@link MediaFile}
   * is a local file which can be used through AudioBox.fm - PC feauture
   */
  public void notifyAsLocal() throws ServiceException, LoginException {
    String path = IConnector.URI_SEPARATOR.concat( Actions.local.toString() );
    String action = Actions.upload.toString();
    
    List<NameValuePair> params = this.toQueryParameters(false);
    
    this.getConnector(IConfiguration.Connectors.NODE).post(this, path, action, null).send(false, params);
  }
  
  
  private List<NameValuePair> toQueryParameters(boolean withPrefix) {
    String prefix = withPrefix ? NAMESPACE + "." : "";
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    
    params.add(  new BasicNameValuePair( prefix + TITLE_FIELD,               this.title ) );
    params.add(  new BasicNameValuePair( prefix + ARTIST_FIELD,              this.artist )  );
    params.add(  new BasicNameValuePair( prefix + ALBUM_FIELD,               this.album )  );
    params.add(  new BasicNameValuePair( prefix + GENRE_FIELD,               this.genre )  );
    params.add(  new BasicNameValuePair( prefix + LEN_STR_FIELD,             this.lenStr )  );
    params.add(  new BasicNameValuePair( prefix + MEDIA_FILE_NAME_FIELD,     this.mediaFileName )  );
    params.add(  new BasicNameValuePair( prefix + MIME_FIELD,                this.mime )  );
    params.add(  new BasicNameValuePair( prefix + YEAR_FIELD,                String.valueOf( this.year ) )  );
    params.add(  new BasicNameValuePair( prefix + LEN_INT_FIELD,             String.valueOf( this.lenInt ) )  );
    params.add(  new BasicNameValuePair( prefix + POSITION_FIELD,            String.valueOf( this.position ) )  );
    params.add(  new BasicNameValuePair( prefix + PLAYS_FIELD,               String.valueOf( this.plays ) )  );
    params.add(  new BasicNameValuePair( prefix + DISC_FIELD,                String.valueOf( this.disc ) )  );
    params.add(  new BasicNameValuePair( prefix + SIZE_FIELD,                String.valueOf( this.size )  )  );
    params.add(  new BasicNameValuePair( prefix + TYPE_FIELD,                this.type.toString() )  );
    params.add(  new BasicNameValuePair( prefix + SOURCE_FIELD,              this.source.toString() )  );
    params.add(  new BasicNameValuePair( prefix + AUDIO_SAMPLE_RATE_FIELD,   this.audioSampleRate )  );
    params.add(  new BasicNameValuePair( prefix + AUDIO_BIT_RATE_FIELD,      this.audioBitRate )  );
    params.add(  new BasicNameValuePair( prefix + ORIGINAL_FILE_NAME_FIELD,  this.originalFileName )  );
    params.add(  new BasicNameValuePair( prefix + MD5_FIELD,                 this.md5 )  );
    
    return params;
  }

}
