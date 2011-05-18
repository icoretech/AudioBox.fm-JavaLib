
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

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;


/**
 * <p>Tracks is a {@link Track} collection.</p>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 */
public class Tracks extends AbstractCollectionEntity<Track> implements Serializable {


  private static final long serialVersionUID = 1L;


  /** Tracks API end point */
  public static final String NAMESPACE = "tracks";
  public static final String TAGNAME = NAMESPACE;
  
  private IEntity parent;
  

  /**
   * <p>Constructor for Tracks.</p>
   */
  public Tracks(IConnector connector, IConfiguration config){
    super(connector, config);
  }
  
  
  @Override
  public String getTagName() {
    return TAGNAME;
  }
  
  
  /**
   * Returns the parent token if parent is set.
   * Retuns null if not.
   */
  public String getToken(){
    if ( parent != null ){
      return parent.getToken();
    }
    return super.getToken();
  }
  
  /**
   * Returns the parent namespace if parent is set.
   * Retuns the {@link Tracks#NAMESPACE} if not.
   */
  public String getNamespace(){
    if ( parent != null ){
      return parent.getNamespace();
    }
    return NAMESPACE;
  }
  
  
  /**
   * Sets the parent {@link IEntity}
   * <p>
   * <code>Tracks</code> can be a {@link Playlist} or {@link Genre} or {@link Artist} or {@link Album} child.
   * So we have to manage each case setting this attribute
   * </p>
   * @param parent the {@link IEntity} parent object
   */
  protected void setParent(IEntity parent){
    this.parent = parent;
  }
  
  
  
  @Override
  protected void copy(IEntity entity) {
    // default: do nothing
  }


  @Override
  public boolean add(Track entity) {
    String token = entity.getToken();
    if ( getConfiguration().hasTrack( token ) ) {
      Track track = this.getConfiguration().getTrack( token );
      track.copy( entity );
      entity = track;
    } else {
      getConfiguration().addTrack( entity );
    }
    return super.addEntity(entity);
  }

  
  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    
    if ( tagName.equals( Track.TAGNAME ) ){
      return this.getClass().getMethod("add", Track.class);
    }
    
    return null;
  }
  
  
}
