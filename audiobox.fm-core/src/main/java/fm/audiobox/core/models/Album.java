
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;


/**
 * Album class is one of the lighter models and offers defaults informations only.
 * 
 * <p>
 * 
 * Album XML looks like this: 
 * 
 * <pre>
 * {@code
 * 
 * <album>
 *   <name>Album name</name>
 *   <token>coq8FfgK</token>
 *   <covers> 
 *     <l>http://media.audiobox.fm/images/albums/HOBh1lMt/l.jpg?1277867432</l> 
 *     <m>http://media.audiobox.fm/images/albums/HOBh1lMt/m.jpg?1277867432</m> 
 *     <s>http://media.audiobox.fm/images/albums/HOBh1lMt/s.jpg?1277867432</s> 
 *   </covers> 
 *   <artist>
 *     <name>Artist name</name>
 *     <token>fs8d8g9d</token>
 *   </artist>
 * </album>
 * }
 * </pre>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 */
public class Album extends AbstractEntity implements Serializable {
  
  private static final long serialVersionUID = 1L;
  private static Logger log = LoggerFactory.getLogger(Album.class);
  
  /** The XML tag name for the Album element */
  public static final String NAMESPACE = Albums.TAGNAME;
  public static final String TAGNAME = "album";

  private String name; 
  private Covers covers;
  private Artist artist;
  private Tracks tracks;

  /**
   * <p>Constructor for Album.</p>
   */
  public Album(IConnector connector, IConfiguration config){
    super(connector, config);
    log.trace("New Album instantiated");
  }

  
  @Override
  public String getTagName() {
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
   * Returns the playlist name
   * @return the playlist name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the playlists name. Used by the parser
   * @param name the playlist name
   */
  @Deprecated
  public void setName(String name) {
    this.name = name;
  }
  
  

  /**
   * <p>Setter for the field <code>coverUrls</code>.</p>
   *
   * @param coverUrls a {@link Covers} object.
   */
  public void setCovers(Covers covers ) {
    this.covers = covers;
  }

  /**
   * <p>Getter for the field <code>coverUrls</code>.</p>
   *
   * @return the coverUrls
   */
  public Covers getCovers() {
    return this.covers;
  }



  /**
   * <p>Setter for the field <code>artist</code>.</p>
   *
   * @param artist a {@link fm.audiobox.core.models.Artist} object.
   */
  public void setArtist(Artist artist ) {
    this.artist = artist;
  }

  /**
   * <p>Getter for the field <code>artist</code>.</p>
   *
   * @return the artist
   */
  public Artist getArtist() {
    return artist;
  }

  
  /**
   * Returns a {@link Tracks} instance ready to be populated through {@link Tracks#load()} method
   * 
   * @return a {@link Tracks} instance
   */
  public Tracks getTracks(){
    if ( this.tracks == null ){
      this.tracks = (Tracks) getConfiguration().getFactory().getEntity( Tracks.TAGNAME , getConfiguration() );
      this.tracks.setParent( this );
    }
    return this.tracks;
  }
  
  
  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    
    if ( tagName.equals("token") || tagName.equals("tk") ){
      return this.getClass().getMethod("setToken", String.class);
      
    } else if ( tagName.equals("name") || tagName.equals("n") ){
      return this.getClass().getMethod("setName", String.class);
      
    } else if ( tagName.equals( Artist.TAGNAME ) || tagName.equals("ar") ){
      return this.getClass().getMethod("setArtist", Artist.class);
      
    } else if ( tagName.equals(Covers.TAGNAME ) || tagName.equals("c") ){
      return this.getClass().getMethod("setCovers", Covers.class);
      
    }
    
    return null;
  }

}
