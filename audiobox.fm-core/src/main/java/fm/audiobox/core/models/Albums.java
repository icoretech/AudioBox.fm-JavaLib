
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
 * <p>Albums is a {@link ModelsCollection} specialization for {@link Album} collections.</p>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Albums extends AbstractCollectionEntity<Album> implements Serializable {

  private static final long serialVersionUID = 1L;
  
  
  /** Tracks API end point */
  public static final String NAMESPACE = "albums";
  public static final String TAGNAME = NAMESPACE;

  
  /**
   * <p>Constructor for Albums.</p>
   */
  public Albums(IConnector connector, IConfiguration config){
    super(connector, config);
  }

  
  @Override
  public String getTagName() {
    return TAGNAME;
  }
  
  @Override
  public String getNamespace(){
    return NAMESPACE;
  }

  @Override
  public boolean add(Album entity) {
    String token = entity.getToken();
    if ( getConfiguration().hasAlbum( token ) ) {
      Album al = this.getConfiguration().getAlbum( token );
      al.copy( entity );
      entity = al;
    } else {
      getConfiguration().addAlbum( entity );
    }
    return super.addEntity(  entity );
  }


  /**
   * Returns the {@link Album} associated with the given <code>name</code>
   * @param name the Album name
   * @return the {@link Album} associated with the given <code>name</code>
   */
  public Album getAlbumByName(String name){
    for ( Iterator<Album> it = this.iterator(); it.hasNext();  ){
      Album alb = it.next();
      if (  name.equals( alb.getName() )  ) {
        return alb;
      }
    }
    return null;
  }
  
  
  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    
    if ( tagName.matches( Album.TAGNAME ) ){
      return this.getClass().getMethod("add", Album.class );
    }
    
    
    return null;
  }


  @Override
  protected void copy(IEntity entity) {
    // default: do nothing
  }


  @Override
  public String getSubTagName() {
	return Album.TAGNAME;
  }
  
}
