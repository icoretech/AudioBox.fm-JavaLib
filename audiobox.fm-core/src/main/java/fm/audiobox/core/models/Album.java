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
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

/**
 * The JSON response looks like this:
 *
 * <pre>
 * @code
 *  {
 *    artist: 'Artist',
 *    album: 'Album',
 *    artwork: 'http://assets.development.audiobox.fm/a/l.jpg'
 *  }
 * @endcode
 * </pre>
 *
 * @version 0.0.1
 */
public class Album extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private static Logger log = LoggerFactory.getLogger(Album.class);
  
  public static final String NAMESPACE = Albums.NAMESPACE;
  public static final String TAGNAME = "album";
  
  
  public static final String ARTIST =             "artist";
  public static final String ALBUM =              "album";
  public static final String ARTWORK =            "artwork";
  
  private String artist;
  private String album;
  private String artwork;
  
  
  private static final Map<String, Method> setterMethods = new HashMap<String, Method>();
  static {
    try {
      setterMethods.put( ARTIST, Album.class.getMethod( "setArtist", String.class ) );
      setterMethods.put( ALBUM, Album.class.getMethod( "setAlbum", String.class ) );
      setterMethods.put( ALBUM, Album.class.getMethod( "setArtwork", String.class ) );
    } catch (SecurityException e) {
      log.error("Security error", e);
    } catch (NoSuchMethodException e) {
      log.error("No method found", e);
    }
  }
  

  public Album(IConfiguration config) {
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
  
  @Override
  public String getApiPath() {
    return this.getParent().getApiPath() + IConnector.URI_SEPARATOR + this.getToken();
  }

  @Override
  public IConnectionMethod load(boolean async) throws ServiceException, LoginException {
    return null;
  }

  @Override
  public IConnectionMethod load(boolean async, IResponseHandler responseHandler) throws ServiceException, LoginException {
    throw new ServiceException("method not supported");
  }
  
  

}
