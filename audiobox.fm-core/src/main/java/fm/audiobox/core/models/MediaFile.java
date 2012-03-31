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

import javax.activation.MimetypesFileTypeMap;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.AudioBox;
import fm.audiobox.configurations.DefaultResponseParser;
import fm.audiobox.configurations.Response;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFiles.Types;
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

  private String artist;
  private String album;
  private String genre;
  private int year;
  private String title;
  private String len_str;
  private int len_int;
  private int position;
  private int plays;
  private int disc;
  private String mediaFileName;
  private Types type;
  private int rating;
  private String mime;
  private IEntity parent;

  private enum Actions{
    stream,
    upload
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

  public String getLen_str() {
    return len_str;
  }

  public void setLen_str(String len_str) {
    this.len_str = len_str;
  }

  public int getLen_int() {
    return len_int;
  }

  public void setLen_int(int len_int) {
    this.len_int = len_int;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
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

  public Types getType() {
    return type;
  }

  public void setType(String type) {

    if( Types.AudioFile.toString().equals( type ) ){
      this.type = Types.AudioFile;	
    } else if( Types.VideoFile.toString().equals( type ) ){
      this.type = Types.VideoFile;
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

  @Override
  public Method getSetterMethod(String tagName) throws SecurityException,	NoSuchMethodException {
    if ( tagName.equals("token") || tagName.equals("tk") ){
      return this.getClass().getMethod("setToken", String.class);
    } else if ( tagName.equals("artist") ){
      return this.getClass().getMethod("setArtist", String.class);
    } else if ( tagName.equals("album") ){
      return this.getClass().getMethod("setAlbum", String.class);
    } else if ( tagName.equals("genre") ){
      return this.getClass().getMethod("setGenre", String.class);
    } else if ( tagName.equals("year") ){
      return this.getClass().getMethod("setYear", int.class);
    } else if ( tagName.equals("title") ){
      return this.getClass().getMethod("setTitle", String.class);
    } else if ( tagName.equals("len_str") ){
      return this.getClass().getMethod("setLen_str", String.class);
    } else if ( tagName.equals("len_int") ){
      return this.getClass().getMethod("setLen_int", int.class);
    } else if ( tagName.equals("position") ){
      return this.getClass().getMethod("setPosition", int.class);
    } else if ( tagName.equals("plays") ){
      return this.getClass().getMethod("setPlays", int.class);
    } else if ( tagName.equals("disc") ){
      return this.getClass().getMethod("setDisc", int.class);
    } else if ( tagName.equals("media_file_name") ){
      return this.getClass().getMethod("setMediaFileName", String.class);
    } else if ( tagName.equals("rating") ){
      return this.getClass().getMethod("setRating", int.class);
    } else if ( tagName.equals("mime") ){
      return this.getClass().getMethod("setMime", String.class);
    } else if ( tagName.equals("type") ){
      return this.getClass().getMethod("setType", String.class);
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

}
