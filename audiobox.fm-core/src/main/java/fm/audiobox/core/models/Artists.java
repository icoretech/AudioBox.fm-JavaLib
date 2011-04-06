
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;


/**
 * <p>Artists is a {@link Artist} collection.</p>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 */
public class Artists extends AbstractCollectionEntity<Artist> implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LoggerFactory.getLogger(Artists.class);   

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
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    
    if ( tagName.equals( Artist.TAGNAME ) ){
      return this.getClass().getMethod("add", Artist.class);
    }
    
    return null;
  }


}
