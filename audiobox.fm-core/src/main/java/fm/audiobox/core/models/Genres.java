
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

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IResponseHandler;



/**
 * <p>Genres is a {@link Genre} collection.</p>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 */
public class Genres extends AbstractCollectionEntity<Genre> implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private static final Logger log = LoggerFactory.getLogger(Genres.class);
  
  /** Tracks API end point */
  public static final String NAMESPACE = "genres";
  public static final String TAGNAME = NAMESPACE;

  /**
   * <p>Constructor for Genres.</p>
   */
  public Genres(IConnector connector, IConfiguration config){
    super(connector, config);
    log.info("New Genre collection instantiated");
  }

  
  @Override
  public String getTagName() {
    return NAMESPACE;
  }
  
  @Override
  public String getNamespace() {
    return TAGNAME;
  }

  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    if ( tagName.equals( Genre.TAGNAME ) ){
      return this.getClass().getMethod("add", Genre.class);
      
    }
    
    return null;
  }

  @Override
  public boolean add(Genre entity) {
    return super.addEntity( entity );
  }

  
  @Override
  public void load(IResponseHandler responseHandler) throws ServiceException, LoginException {
    this.clear();
    super.load(responseHandler);
  }
  

}
