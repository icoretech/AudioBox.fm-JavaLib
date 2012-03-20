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

import java.io.Serializable;
import java.lang.reflect.Method;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntity;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFiles.Types;
import fm.audiobox.interfaces.IConfiguration;
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
 * { token: '5916d6ca-0aa0-450a-ae5d-56e8fab0ffc9',
 *     artist: 'Alborosie',
 *     album: 'Escape From Babylon',
 *     genre: 'Reggae',
 *     year: 2009,
 *     title: 'One Sound',
 *     len_str: '4:00',
 *     len_int: 240,
 *     position: 11,
 *     plays: 114,
 *     disc: 1,
 *     filename: '5916d6ca-0aa0-450a-ae5d-56e8fab0ffc9.mp3',
 *     type: 'AudioFile',
 *     rating: 5,
 *     mime: 'audio/mpeg' }
 * @endcode
 * </pre>
 *
 * @author Lucio Regina
 * @version 0.0.1
 */
public class MediaFile extends AbstractEntity implements Serializable{

  private static final long serialVersionUID = 1L;

  public static final String TAGNAME = "file";
  public static final String NAMESPACE = MediaFile. TAGNAME;

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
  private String filename;
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

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
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
    } else if ( tagName.equals("filename") ){
      return this.getClass().getMethod("setFilename", String.class);
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
    return this.parent.getApiPath() + "/" + this.getToken();
  }

  @Override
  public void setParent(IEntity parent) {
    this.parent = parent;
  }

  //POST http://staging.audiobox.fm:3000/upload     in Multipart-Data
  public void upload() throws ServiceException, LoginException {
    String path = "/".concat( Actions.upload.toString() );
    HttpEntity entity = new MultipartEntity();
    ((MultipartEntity)entity).addPart("",null);
    
    this.getConnector(IConfiguration.Connectors.NODE).post(this, path, "").send(false, entity);
    
  }

  
  // download   GET   http://staging.audiobox.fm:3000/stream/(file_name.ext)
  public void download() throws ServiceException, LoginException {
    String path = ("/".concat( Actions.stream.toString() )).concat("/".concat(this.filename));
    this.getConnector(IConfiguration.Connectors.NODE).get(this,  path , "", null).send(false);
  }

}
