
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

import fm.audiobox.core.observables.Event;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;


/**
 * Artist class is one of the lighter models and offers defaults informations only.
 * 
 * <p>
 * 
 * Artist XML looks like this: 
 * 
 * <pre>
 * @code
 * <artist>
 *   <name>Artist name</name>
 *   <token>cd8sa09df</token>
 * </artist>
 * @endcode
 * </pre>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 */
public class Artist extends AbstractEntity implements Serializable {

  
  private static final long serialVersionUID = 1L;
  
  /** The XML tag name for the Artist element */
  public static final String NAMESPACE = Artists.TAGNAME;
  public static final String TAGNAME = "artist|ar";

  private String name;
  private Files files;
  

  /**
   * <p>Constructor for Artist.</p>
   */
  public Artist(IConnector connector, IConfiguration config){
    super(connector, config);
  }


  @Override
  public String getTagName() {
     return TAGNAME;
  }
  
  @Override
  public String getNamespace() {
    return NAMESPACE;
  }


  /**
   * Returns the artist name
   * @return the artist name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the artist name. Used by the parser
   * @param name the artist name
   */
  @Deprecated
  public void setName(String name) {
    this.name = name;
  }
  
  
  /**
   * Returns a {@link Files} instance ready to be populated through {@link Files#load()} method
   * 
   * @return a {@link Files} instance
   */
  public Files getFiles(){
    if ( this.files == null ){
      this.files = (Files) getConfiguration().getFactory().getEntity( Files.TAGNAME , getConfiguration() );
      this.files.setParent( this );
    }
    return this.files;
  }
  
  
  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    
    if ( tagName.equals("token")  || tagName.equals("tk") ) {
      return this.getClass().getMethod("setToken", String.class);

    } else if ( tagName.equals("name") || tagName.equals("n") ){
      return this.getClass().getMethod("setName", String.class);

    }
    
    return null;
  }


  @Override
  protected void copy(IEntity entity) {
    Artist artist = (Artist) entity;
    this.name = artist.getName();
    
    this.setChanged();
    Event event = new Event( this, Event.States.ENTITY_REFRESHED );
    this.notifyObservers(event);
  }
  
  
  
  
  
}
