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
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

/**
 * This class represents the main class of the MediaFile entity.
 */
public class MediaFile extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private static Logger log = LoggerFactory.getLogger(MediaFile.class);

  public static final String NAMESPACE = MediaFiles.TAGNAME;
  public static final String TAGNAME = "media_file";
  
  protected static final String ORIGINAL_FILE_NAME = "original_file_name";

  public static final String TYPE = "type";
  public static final String ARTIST = "artist";
  public static final String ALBUM = "album";
  public static final String GENRE = "genre";
  public static final String RELEASE_YEAR = "release_year";
  public static final String TITLE = "title";
  public static final String LEN_STR = "len_str";
  public static final String LEN_INT = "len_int";
  public static final String POSITION = "position";
  public static final String FILENAME = "filename";
  public static final String LOVED = "loved";
  public static final String DISC_NUMBER = "disc_number";
  public static final String MIME = "mime";
  public static final String REMOTE_PATH = "remote_path";
  public static final String SOURCE = "source";
  public static final String SHARE_TOKEN = "share_token";
  public static final String ARTWORK = "artwork";
  public static final String SIZE = "size";
  public static final String HASH = "hash";
  public static final String VIDEO_BITRATE = "video_bitrate";
  public static final String VIDEO_CODEC = "video_codec";
  public static final String VIDEO_RESOLUTION = "video_resolution";
  public static final String VIDEO_FPS = "video_fps";
  public static final String VIDEO_ASPECT = "video_aspect";
  public static final String VIDEO_CONTAINER = "video_container";
  public static final String AUDIO_BITRATE = "audio_bitrate";
  public static final String AUDIO_CODEC = "audio_codec";
  public static final String AUDIO_SAMPLE_RATE = "audio_sample_rate";
  public static final String PLAYS = "plays";
  public static final String LYRICS = "lyrics";


  private MediaFiles.Type type;
  private String artist;
  private String album;
  private String genre;
  private int release_year;
  private String title;
  private String len_str;
  private int len_int;
  private int position;
  private String filename;
  private boolean loved = false;
  private int disc_number;
  private String mime;
  private String remote_path;
  private String source;
  private String share_token;
  private String artwork;
  private long size;
  private String hash;
  private String video_bitrate;
  private String video_codec;
  private String video_resolution;
  private String video_fps;
  private String video_aspect;
  private String video_container;
  private String audio_bitrate;
  private String audio_codec;
  private String audio_sample_rate;
  private int plays;
  private String lyrics = ""; // Default empty string
  

  /**
   * Use to invoke the correct remote action
   */
  private enum Actions {
    stream, upload, local, download, lyrics, scrobble, love, unlove
  }
  

  /**
   * This is a public enum for identifing all sources of the MediaFile 
   */
  public static enum Source {
    local, cloud, dropbox, youtube, soundcloud, gdrive, skydrive
  }

  private static final Map<String, Method> setterMethods = new HashMap<String, Method>();
  static {
    try {
      setterMethods.put( TOKEN_SHORT, MediaFile.class.getMethod("setToken", String.class));
      setterMethods.put( TOKEN, MediaFile.class.getMethod("setToken", String.class));
      setterMethods.put( ARTIST, MediaFile.class.getMethod("setArtist", String.class));
      setterMethods.put( ALBUM, MediaFile.class.getMethod("setAlbum", String.class));
      setterMethods.put( GENRE, MediaFile.class.getMethod("setGenre", String.class));
      setterMethods.put( RELEASE_YEAR, MediaFile.class.getMethod("setReleaseYear", int.class));
      setterMethods.put( TITLE, MediaFile.class.getMethod("setTitle", String.class));
      setterMethods.put( LEN_STR, MediaFile.class.getMethod("setLenStr", String.class));
      setterMethods.put( LEN_INT, MediaFile.class.getMethod("setLenInt", int.class));
      setterMethods.put( POSITION, MediaFile.class.getMethod("setPosition", int.class));
      setterMethods.put( PLAYS, MediaFile.class.getMethod("setPlays", int.class));
      setterMethods.put( DISC_NUMBER, MediaFile.class.getMethod("setDiscNumber", int.class));
      setterMethods.put( MIME, MediaFile.class.getMethod("setMime", String.class));
      setterMethods.put( TYPE, MediaFile.class.getMethod("setType", String.class));
      setterMethods.put( SOURCE, MediaFile.class.getMethod("setSource", String.class));
      setterMethods.put( SIZE, MediaFile.class.getMethod("setSize", long.class));
      setterMethods.put( AUDIO_SAMPLE_RATE, MediaFile.class.getMethod("setAudioSampleRate", String.class));
      setterMethods.put( AUDIO_BITRATE, MediaFile.class.getMethod("setAudioBitrate", String.class));
      setterMethods.put( ARTWORK, MediaFile.class.getMethod("setArtwork", String.class));
      setterMethods.put( HASH, MediaFile.class.getMethod("setHash", String.class));
      setterMethods.put( FILENAME, MediaFile.class.getMethod("setFilename", String.class));
      setterMethods.put( LOVED, MediaFile.class.getMethod("setLoved", boolean.class));
      setterMethods.put( REMOTE_PATH, MediaFile.class.getMethod("setRemotePath", String.class));
      setterMethods.put( SHARE_TOKEN, MediaFile.class.getMethod("setShareToken", String.class));
      setterMethods.put( VIDEO_BITRATE,  MediaFile.class.getMethod( "setVideoBitrate", String.class )  );
      setterMethods.put( VIDEO_CODEC,  MediaFile.class.getMethod( "setVideoCodec", String.class )  );
      setterMethods.put( VIDEO_RESOLUTION,  MediaFile.class.getMethod( "setVideoResolution", String.class )  );
      setterMethods.put( VIDEO_FPS,  MediaFile.class.getMethod( "setVideoFps", String.class )  );
      setterMethods.put( VIDEO_ASPECT,  MediaFile.class.getMethod( "setVideoAspect", String.class )  );
      setterMethods.put( VIDEO_CONTAINER,  MediaFile.class.getMethod( "setVideoContainer", String.class )  );
      setterMethods.put( AUDIO_CODEC,  MediaFile.class.getMethod( "setAudioCodec", String.class )  );
      setterMethods.put( LYRICS,  MediaFile.class.getMethod( "setLyrics", String.class )  );
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
   * This class can be instantiated always.
   *
   * @param abxClient the {@link AudioBox} current instance which this MediaFile should be linked to
   */
  public MediaFile(AudioBox abxClient) {
    super(abxClient.getConfiguration());
  }

  public String getNamespace() {
    return NAMESPACE;
  }

  public String getTagName() {
    return TAGNAME;
  }

  
  /**
   * @return the {@code artist} of the MediaFile
   */
  public String getArtist() {
    return artist;
  }

  /**
   * Sets the {@code artist} of this media file
   */
  public void setArtist(String artist) {
    this.artist = artist;
  }

  /**
   * @return the {@code ablum} of the MediaFile
   */
  public String getAlbum() {
    return album;
  }

  /**
   * Sets the {@code album} of this media file
   */
  public void setAlbum(String album) {
    this.album = album;
  }

  
  /**
   * @return the {@code genre} of the MediaFile
   */
  public String getGenre() {
    return genre;
  }

  /**
   * Sets the {@code genre} of this media file
   */
  public void setGenre(String genre) {
    this.genre = genre;
  }

  
  /**
   * @return the {@code filename} of the MediaFile
   */
  public String getFilename() {
    return this.filename;
  }

  /**
   * Sets the {@code filename} of this media file
   * <br/>
   * <b>This is method should be used for uploading media file</b>
   */
  public void setFilename(String filename) {
    this.filename = filename;
  }
  
  
  /**
   * @return the {@code release year} of the MediaFile
   */
  public int getReleaseYear() {
    return this.release_year;
  }

  /**
   * Sets the {@code release year} of this media file
   */
  public void setReleaseYear(int year) {
    this.release_year = year;
  }

  
  /**
   * @return the {@code true} if {@link User} has marked this MediaFile as {@code favorite}
   */
  public boolean isLoved() {
    return this.loved;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setLoved(boolean loved) {
    this.loved = loved;
  }

  
  /**
   * @return the {@code title} of the MediaFile
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the {@code title} of this media file
   */
  public void setTitle(String title) {
    this.title = title;
  }

  
  /**
   * @return the String representation of the {@code length} of the MediaFile
   */
  public String getLenStr() {
    return len_str;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setLenStr(String len_str) {
    this.len_str = len_str;
  }

  
  /**
   * @return the Integer of the {@code length} in seconds of the MediaFile 
   */
  public int getLenInt() {
    return this.len_int;
  }

  /**
   * Sets the {@code length} in seconds of this media file
   */
  public void setLenInt(int len_int) {
    this.len_int = len_int;
  }

  
  /**
   * @return the {@code track number} of the MediaFile
   */
  public int getPosition() {
    return position;
  }

  /**
   * Sets the {@code track number} of this media file
   */
  @Deprecated
  public void setPosition(int position) {
    this.position = position;
  }

  
  /**
   * @return the {@code size} in bytes of the MediaFile
   */
  public long getSize() {
    return this.size;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setSize(long size) {
    this.size = size;
  }

  
  /**
   * @return the {@code play count} of the MediaFile
   */
  public int getPlays() {
    return plays;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setPlays(int plays) {
    this.plays = plays;
  }

  
  /**
   * @return the {@code disc number} of the MediaFile
   */
  public int getDiscNumber() {
    return disc_number;
  }

  /**
   * Sets the {@code disc number} of this media file
   */
  public void setDiscNumber(int disc) {
    this.disc_number = disc;
  }

  
  /**
   * @return {@link MediaFiles.Type} based on the MediaFile
   */
  public Type getType() {
    return type;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setType(Type t) {
    this.type = t;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setType(String type) {

    if (Type.AudioFile.toString().equals(type)) {
      this.type = Type.AudioFile;
    } else if (Type.VideoFile.toString().equals(type)) {
      this.type = Type.VideoFile;
    }
  }

  
  /**
   * @return the String representation of the {@link MediaFile.Source} of the MediaFile
   */
  public String getSource() {
    return this.source;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setSource(String s) {
    this.source = s;
  }

  /**
   * @return the {@code remote file path} of the MediaFile
   */
  public String getRemotePath() {
    return remote_path;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setRemotePath(String remotePath) {
    this.remote_path = remotePath;
  }

  
  /**
   * @return the {@code unique token} of the MediaFile used for sharing information
   */
  public String getShareToken() {
    return share_token;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setShareToken(String shareToken) {
    this.share_token = shareToken;
  }


  /**
   * @return the {@code mime type} of the MediaFile
   */
  public String getMime() {
    return mime;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setMime(String mime) {
    this.mime = mime;
  }

  /**
   * @return the {@code audio sample rate} of the MediaFile
   */
  public String getAudioSampleRate() {
    return audio_sample_rate;
  }

  /**
   * Sets the {@code sample rate} of this media file
   */
  public void setAudioSampleRate(String audioSampleRate) {
    this.audio_sample_rate = audioSampleRate;
  }

  
  /**
   * @return the {@code audio bit rate} of the MediaFile
   */
  public String getAudioBitrate() {
    return audio_bitrate;
  }

  /**
   * Sets the {@code bit rate} of this media file
   */
  public void setAudioBitrate(String audioBitRate) {
    this.audio_bitrate = audioBitRate;
  }

  
  /**
   * @return the {@code unique hash} of the MediaFile
   */
  public String getHash() {
    return this.hash;
  }

  /**
   * Sets the {@code hash} of this media file
   * <br/>
   * <b>This method is used for uploading media file</b>
   */
  public void setHash(String hash) {
    this.hash = hash;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setArtwork(String artwork) {
    this.artwork = artwork;
  }

  
  /**
   * @return the {@code artwork url} of the MediaFile
   */
  public String getArtwork() {
    return this.artwork;
  }
  
  /**
   * @return the {@code video bit rate} of the MediaFile
   */
  public String getVideoBitrate() {
    return video_bitrate;
  }

  /**
   * Sets the {@code bit rate} of this media file
   */
  public void setVideoBitrate(String video_bitrate) {
    this.video_bitrate = video_bitrate;
  }

  /**
   * @return the {@code video codec} of the MediaFile
   */
  public String getVideoCodec() {
    return video_codec;
  }

  /**
   * Sets the {@code codec} of this media file
   */
  public void setVideoCodec(String video_codec) {
    this.video_codec = video_codec;
  }

  /**
   * @return the {@code video resolution} of the MediaFile
   */
  public String getVideoResolution() {
    return video_resolution;
  }

  /**
   * Sets the {@code resolution} of this media file
   */
  public void setVideoResolution(String video_resolution) {
    this.video_resolution = video_resolution;
  }

  /**
   * @return the {@code video fps} of the MediaFile
   */
  public String getVideoFps() {
    return video_fps;
  }

  /**
   * Sets the {@code fps} of this media file
   */
  public void setVideoFps(String video_fps) {
    this.video_fps = video_fps;
  }

  /**
   * @return the {@code video aspect} of the MediaFile
   */
  public String getVideoAspect() {
    return video_aspect;
  }

  /**
   * Sets the {@code aspect} of this media file
   */
  public void setVideoAspect(String video_aspect) {
    this.video_aspect = video_aspect;
  }

  /**
   * @return the {@code video container} of the MediaFile
   */
  public String getVideoContainer() {
    return video_container;
  }

  /**
   * Sets the {@code container} of this media file
   */
  public void setVideoContainer(String video_container) {
    this.video_container = video_container;
  }

  /**
   * @return the {@code audio codec} of the MediaFile
   */
  public String getAudioCodec() {
    return audio_codec;
  }

  /**
   * Sets the {@code codec} of this media file
   */
  public void setAudioCodec(String audio_codec) {
    this.audio_codec = audio_codec;
  }
  

  /**
   * @return the {@code lyrics} of the MediaFile
   */
  public String getLyrics() {
    return this.lyrics;
  }
  
  
  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setLyrics(String lyrics) {
    this.lyrics = lyrics != null ? lyrics : "";
  }
  

  public Method getSetterMethod(String tagName) {
    if (setterMethods.containsKey(tagName)) {
      return setterMethods.get(tagName);
    }
    return null;
  }


  protected void copy(IEntity entity) {
    log.warn("Not implemented yet");
  }

  
  /**
   * @return the last part of the url for the stream
   */
  public String getStreamUrl() {
    return IConnector.URI_SEPARATOR + Actions.stream + IConnector.URI_SEPARATOR + this.getFilename();
  }

  /**
   * @return the full string representing the stream url containing also {@code protocol}, {@code domain} and {@code port}
   * <p>
   * NOTE: this method doesn't return the authentication method
   * </p>
   */
  public String computeStreamUrl() {
    String path = IConnector.URI_SEPARATOR.concat(Actions.stream.toString());
    String action = this.getFilename();

    IConnectionMethod request = this.getConnector(IConfiguration.Connectors.NODE).get(this, path, action, null, null);
    return request.getHttpMethod().getURI().toString();
  }

  public String getApiPath() {
    return this.getParent().getApiPath() + IConnector.URI_SEPARATOR + this.getToken();
  }

  public IConnectionMethod load(boolean async) throws ServiceException, LoginException {
    return this.load(false, null);
  }

  public IConnectionMethod load(boolean async, IResponseHandler responseHandler) throws ServiceException, LoginException {
    IConnectionMethod request = getConnector(IConfiguration.Connectors.RAILS).get(this, IConnector.URI_SEPARATOR + MediaFiles.NAMESPACE, this.getToken(), null);
    request.send(async, null, responseHandler);
    return request;
  }
  
  
  /**
   * Use this method for getting {@code lyrics} from AudioBox.fm
   * 
   * @return {@code lyrics} of the MediaFile
   * @throws ServiceException if any connection error occurs
   * @throws LoginException if any login error occurs
   */
  public String lyrics(boolean async) throws ServiceException, LoginException {
    IConnectionMethod request = getConnector(IConfiguration.Connectors.RAILS).get(this, Actions.lyrics.toString(), null);
    request.send(async);
    return this.getLyrics();
  }
  
  
  /**
   * Use this method for marking media file as {@code loved}
   * <br />
   * <b>This method is always executed synchronously</b>
   * 
   * @return {@code true} if everything went ok. {@code false} if not
   * 
   * @throws ServiceException if any connection error occurs
   * @throws LoginException if any login error occurs
   */
  public boolean love() throws ServiceException, LoginException {
    IConnectionMethod request = getConnector(IConfiguration.Connectors.RAILS).post(this, Actions.love.toString());
    request.send(false);
    boolean result = request.getResponse().isOK();
    if ( result ) {
      this.loved = true;
    }
    return result;
  }
  
  
  /**
   * Use this method for marking media file as {@code not loved}
   * <br />
   * <b>This method is always executed synchronously</b>
   * 
   * @return {@code true} if everything went ok. {@code false} if not
   * 
   * @throws ServiceException if any connection error occurs
   * @throws LoginException if any login error occurs
   */
  public boolean unlove() throws ServiceException, LoginException {
    IConnectionMethod request = getConnector(IConfiguration.Connectors.RAILS).post(this, Actions.unlove.toString());
    request.send(false);
    boolean result = request.getResponse().isOK();
    if ( result ) {
      this.loved = false;
    }
    return result;
  }
  
  
  /**
   * This method executes {@code scrobble} to AudioBox.fm
   * 
   * @return {@code true} if everything went fine. {@code false} if not.
   * @throws ServiceException if any connection error occurs
   * @throws LoginException if any login error occurs
   */
  public boolean scrobble() throws ServiceException, LoginException {
    IConnectionMethod request = getConnector(IConfiguration.Connectors.RAILS).post(this, Actions.scrobble.toString());
    request.send(false);
    boolean result = request.getResponse().isOK();
    if ( result ) {
      this.plays += 1;
    }
    return result;
  }
  
  
  
  /**
   * Complete destroy MediaFile from AudioBox.fm
   * <p>
   *  <b>This method cannot be reverted</b>
   * </p>
   * @return {@code true} if everything went ok. {@code false} if not.
   * @throws ServiceException if any connection error occurs
   * @throws LoginException if any login error occurs
   */
  public boolean destroy() throws ServiceException, LoginException {

    if (this.getParent() == null) {
      // We can delete an arbitrary MediaFile
      return this._destroy();
    }

    List<MediaFile> toRemove = new ArrayList<MediaFile>();
    toRemove.add(this);

    return ((MediaFiles) this.getParent()).destroy(toRemove);
  }

  private boolean _destroy() throws ServiceException, LoginException {

    String path = IConnector.URI_SEPARATOR.concat(MediaFiles.NAMESPACE);

    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair(MediaFiles.TOKENS_PARAMETER, this.getToken()));

    Response response = this.getConnector(IConfiguration.Connectors.RAILS).delete(this, path, MediaFiles.Actions.multidestroy.toString(), params).send(false);

    return response.isOK();
  }

  /**
   * Updates current {@link MediaFile} information
   * 
   * @return {@code true} if everything went ok. {@code false} if not
   * @throws ServiceException if any connection error occurrs
   * @throws LoginException if any login error occurrs
   */
  public boolean update() throws ServiceException, LoginException {
    String namespace = IConnector.URI_SEPARATOR + MediaFiles.NAMESPACE;
    String action = this.getToken();

    List<NameValuePair> params = this.toQueryParameters(true);
    Response response = this.getConnector(IConfiguration.Connectors.RAILS).put(this, namespace, action, null).send(false, params);

    return response.isOK();
  }

  /**
   * See {@link MediaFile#upload(boolean, UploadHandler, boolean)}
   */
  public IConnectionMethod upload(boolean async, UploadHandler uploadHandler) throws ServiceException, LoginException {
    return this.upload(async, uploadHandler, false);
  }

  
  /**
   * Uploads media file to {@code AudioBox.fm Cloud} drive
   * 
   * @param async when {@code true} the upload request will be processed asynchronously
   * @param uploadHandler the {@link UploadHandler} used to upload file
   * @param customFields when {@code true} it will upload also {@code title artist album ...} fields
   * @return the {@link IConnectionMethod} instance used for this upload
   * @throws ServiceException if any connection error occurrs
   * @throws LoginException if any login error occurrs
   */
  public IConnectionMethod upload(boolean async, UploadHandler uploadHandler, boolean customFields) throws ServiceException, LoginException {
    String path = IConnector.URI_SEPARATOR.concat(Actions.upload.toString());

    MultipartEntity entity = new MultipartEntity();

    String mime = MimeTypes.getMime(uploadHandler.getFile());
    if (mime == null) {
      throw new ServiceException("mime type error: file is not supported by AudioBox.fm");
    }

    entity.addPart("files[]", uploadHandler);

    if (customFields) {

      List<NameValuePair> fields = this.toQueryParameters(false);
      for (NameValuePair field : fields) {
        try {
          if ( field.getValue() != null ) {
            entity.addPart(field.getName(), new StringBody(field.getValue(), "text/plain", Charset.forName("UTF-8")));
          }
        } catch (UnsupportedEncodingException e) {
          log.warn("Entity " + field.getName() + " cannot be added due to: " + e.getMessage());
        }
      }
    }

    IConnectionMethod request = this.getConnector(IConfiguration.Connectors.NODE).post(this, path, null, null);
    request.send(async, entity);
    return request;
  }

  /**
   * See {@link MediaFile#download(DownloadHandler)}
   */
  public void download(final File file) throws ServiceException, LoginException {
    if (file != null) {

      download(new DownloadHandler(file, IConnector.DEFAULT_CHUNK));

    } else {
      throw new ServiceException("No file found for downloading media");
    }
  }

  
  /**
   * Downloads current {@link MediaFile} into {@code File} specified in the given {@link DownloadHandler} parameter
   *  
   * @param downloadHandler the {@link DownloadHandler} used for downloading the file
   * @return the {@link IConnectionMethod} used for this request
   * @throws ServiceException if any connection error occurrs
   * @throws LoginException if any login error occurrs
   */
  public IConnectionMethod download(DownloadHandler downloadHandler) throws ServiceException, LoginException {

    if (downloadHandler != null) {
      // In this case we are using 'path' for the action
      // and 'action' for the filename
      String path = IConnector.URI_SEPARATOR.concat(Actions.download.toString());
      String action = this.getFilename();

      IConnectionMethod request = this.getConnector(IConfiguration.Connectors.NODE).get(this, path, action, null, null);
      request.send(false, null, downloadHandler);
      return request;

    } else {
      throw new ServiceException("No file found for downloading media");
    }
  }
  
  
  
  

  /**
   * This method creates a MediaFile entity on AudioBox.fm Desktop drive.
   * <p>
   *  It posts all MediaFile fields such as {@code title artist album ...}
   * </p>
   * @param file local File will be used for retrieving information ({@code mimetype, path ...})
   * @return {@code true} if everything went ok. {@code false} if not.
   * @throws ServiceException if any connection error occurrs
   * @throws LoginException if any login error occurrs
   */
  public boolean notifyAsLocal(File file) throws ServiceException, LoginException {
    String path = IConnector.URI_SEPARATOR.concat(Actions.local.toString());
    String action = Actions.upload.toString();
    
    if ( ! file.exists() ) {
      log.warn("File doesn't exist");
      throw new ServiceException("File doesn't exist");
    }
    
    String mime = MimeTypes.getMime(file);
    if (mime == null) {
      throw new ServiceException("mime type error: file is not supported by AudioBox.fm");
    }

    List<NameValuePair> params = this.toQueryParameters( true );
    params.add( new BasicNameValuePair(TAGNAME + "[" + REMOTE_PATH + "]", file.getAbsolutePath() ) );
    params.add( new BasicNameValuePair(TAGNAME + "[" + ORIGINAL_FILE_NAME + "]", file.getName() ) );
    
    Response response = this.getConnector(IConfiguration.Connectors.NODE).post(this, path, action, null).send(false, params);
    return response.isOK();
  }

  
  public List<NameValuePair> toQueryParameters(boolean all) {
    String prefix = TAGNAME + "[";
    String suffix = "]";
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();

    params.add(new BasicNameValuePair(prefix + ARTIST + suffix,  this.artist )  );
    params.add(new BasicNameValuePair(prefix + ALBUM + suffix,  this.album )  );
    params.add(new BasicNameValuePair(prefix + GENRE + suffix,  this.genre )  );
    params.add(new BasicNameValuePair(prefix + RELEASE_YEAR + suffix, String.valueOf( this.release_year )  )  );
    params.add(new BasicNameValuePair(prefix + TITLE + suffix,  this.title )  );
    params.add(new BasicNameValuePair(prefix + POSITION + suffix,  String.valueOf( this.position )  )  );
    params.add(new BasicNameValuePair(prefix + LOVED + suffix,  String.valueOf( this.loved ) )  );
    params.add(new BasicNameValuePair(prefix + DISC_NUMBER + suffix,  String.valueOf( this.disc_number ) )  );
    
    if ( all ) {
      params.add(new BasicNameValuePair(prefix + LEN_INT + suffix, String.valueOf( this.len_int ) )  );
      params.add(new BasicNameValuePair(prefix + FILENAME + suffix, this.filename )  );
      params.add(new BasicNameValuePair(prefix + SIZE + suffix, String.valueOf( this.size ) )  );
      params.add(new BasicNameValuePair(prefix + HASH + suffix, this.hash )  );
      params.add(new BasicNameValuePair(prefix + VIDEO_BITRATE + suffix, this.video_bitrate )  );
      params.add(new BasicNameValuePair(prefix + VIDEO_CODEC + suffix, this.video_codec )  );
      params.add(new BasicNameValuePair(prefix + VIDEO_RESOLUTION + suffix, this.video_resolution )  );
      params.add(new BasicNameValuePair(prefix + VIDEO_FPS + suffix, this.video_fps )  );
      params.add(new BasicNameValuePair(prefix + VIDEO_ASPECT + suffix, this.video_aspect )  );
      params.add(new BasicNameValuePair(prefix + VIDEO_CONTAINER + suffix, this.video_container )  );
      params.add(new BasicNameValuePair(prefix + AUDIO_BITRATE + suffix, this.audio_bitrate )  );
      params.add(new BasicNameValuePair(prefix + AUDIO_CODEC + suffix, this.audio_codec )  );
      params.add(new BasicNameValuePair(prefix + AUDIO_SAMPLE_RATE + suffix, this.audio_sample_rate )  );
    }
    

    return params;
  }
  
}
