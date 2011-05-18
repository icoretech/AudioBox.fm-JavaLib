
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
import java.util.Iterator;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;


/**
 * <p>Artists is a {@link Artist} collection.</p>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 */
public class Artists extends AbstractCollectionEntity<Artist> implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Tracks API end point */
  public static final String NAMESPACE = "artists";
  public static final String TAGNAME = NAMESPACE;


  /**
   * <p>Constructor for Artists.</p>
   */
  public Artists(IConnector connector, IConfiguration config){
    super(connector, config);
  }
  
  
  
  @Override
  public String getTagName(){
    return TAGNAME;
  }
  
  @Override
  public String getNamespace() {
    return NAMESPACE;
  }


  @Override
  public boolean add(Artist entity) {
    String token = entity.getToken();
    if ( getConfiguration().hasArtist( token ) ) {
      Artist ar = this.getConfiguration().getArtist( token );
      ar.copy( entity );
      entity = ar;
    } else {
      getConfiguration().addArtist( entity );
    }
    return super.addEntity( entity );
  }


  /**
   * Returns the {@link Artist} associated with the given <code>name</code>
   * @param name the Artist name
   * @return the {@link Artist} associated with the given <code>name</code>
   */
  public Artist getArtistByName(String name){
    for ( Iterator<Artist> it = this.iterator(); it.hasNext();  ){
      Artist art = it.next();
      if (  name.equals( art.getName() )  ) {
        return art;
      }
    }
    return null;
  }
  
  @Override
  protected void copy(IEntity entity) {
    // default: do nothing
  }
  
  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    
    if ( tagName.equals( Artist.TAGNAME ) ){
      return this.getClass().getMethod("add", Artist.class);
    }
    
    return null;
  }


}
