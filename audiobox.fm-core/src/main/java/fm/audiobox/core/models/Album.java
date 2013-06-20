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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
 * This class represents the {@code Album} implementation
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
  
  private MediaFiles mediafiles;
  
  
  private static final Map<String, Method> setterMethods = new HashMap<String, Method>();
  private static Map<String, Method> getterMethods = null;
  
  static {
    try {
      setterMethods.put( ARTIST, Album.class.getMethod( "setArtist", String.class ) );
      setterMethods.put( ALBUM, Album.class.getMethod( "setAlbum", String.class ) );
      setterMethods.put( ARTWORK, Album.class.getMethod( "setArtwork", String.class ) );
      setterMethods.put( MediaFiles.TAGNAME, Album.class.getMethod( "setMediaFiles", MediaFiles.class )  );
    } catch (SecurityException e) {
      log.error("Security error", e);
    } catch (NoSuchMethodException e) {
      log.error("No method found", e);
    }
  }
  

  public Album(IConfiguration config) {
    super(config);
  }

  public String getNamespace() {
    return NAMESPACE;
  }

  public String getTagName() {
    return TAGNAME;
  }

  /**
   * @return the {@code artist} name
   */
  public String getArtist() {
    return artist;
  }

  
  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setArtist(String artist) {
    this.artist = artist;
  }

  /**
   * @return the {@code album} name
   */
  public String getAlbum() {
    return album;
  }

  
  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setAlbum(String album) {
    this.album = album;
  }

  
  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setArtwork(String artwork){
    this.artwork = artwork;
  }
  
  /**
   * @return the {@code artwork} associated with this album
   */
  public String getArtwork(){
    return this.artwork;
  }
  

  public Method getSetterMethod(String tagName) {
    if ( setterMethods.containsKey( tagName ) ) {
      return setterMethods.get( tagName );
    }
    return null;
  }
  
  
  public Map<String, Method> getGetterMethods() {
    if ( getterMethods == null ) {
      getterMethods = new HashMap<String, Method>();
      try {  
        getterMethods.put( ARTIST, Album.class.getMethod( "getArtist" ) );
        getterMethods.put( ALBUM, Album.class.getMethod( "getAlbum") );
        getterMethods.put( ARTWORK, Album.class.getMethod( "getArtwork") );
        getterMethods.put( MediaFiles.TAGNAME, Album.class.getMethod( "getMediaFiles" )  );
      } catch (SecurityException e) {
        log.error("Security error", e);
      } catch (NoSuchMethodException e) {
        log.error("No method found", e);
      }
    }
    
    return getterMethods;
  }
  
  
  /**
   * @return a {@link MediaFiles} related to this almbums
   * <p>
   *  <b>Note: each {@link MediaFile} contains {@code token} only</b>
   * </p>
   */
  public MediaFiles getMediaFiles() {
    if (this.mediafiles == null) {
      this.mediafiles = (MediaFiles) getConfiguration().getFactory().getEntity( MediaFiles.TAGNAME, getConfiguration());
      this.mediafiles.setParent(this);
    }
    return this.mediafiles;
  }
  
  
  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setMediaFiles(MediaFiles mfs) {
    this.mediafiles = mfs;
  }
  

  protected void copy(IEntity entity) {
    log.warn("Not implemented yet");
  }
  
  public String getApiPath() {
    return this.getParent().getApiPath() + IConnector.URI_SEPARATOR + this.getToken();
  }

  public IConnectionMethod load(boolean async) throws ServiceException, LoginException {
    return this.load(async, null);
  }

  public IConnectionMethod load(boolean async, IResponseHandler responseHandler) throws ServiceException, LoginException {
    throw new ServiceException("method not supported");
  }
  
  protected List<NameValuePair> toQueryParameters(boolean all) {
    String prefix = TAGNAME + "[";
    String suffix = "]";
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();

    params.add(new BasicNameValuePair(prefix + ARTIST + suffix,  this.artist )  );
    params.add(new BasicNameValuePair(prefix + ALBUM + suffix,  this.album )  );
    params.add(new BasicNameValuePair(prefix + ARTWORK + suffix,  this.artwork )  );
    
    return params;
  }

}
