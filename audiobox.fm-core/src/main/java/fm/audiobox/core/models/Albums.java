
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

import fm.audiobox.core.api.ModelsCollection;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IResponseHandler;


/**
 * <p>Albums is a {@link ModelsCollection} specialization for {@link Album} collections.</p>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Albums extends AbstractCollectionEntity<Album> implements Serializable {

  private static final long serialVersionUID = 1L;
  private static final Logger log = LoggerFactory.getLogger(Albums.class);
  
  
  /** Tracks API end point */
  public static final String NAMESPACE = "albums";
  public static final String TAGNAME = NAMESPACE;

  
  /**
   * <p>Constructor for Albums.</p>
   */
  public Albums(IConnector connector, IConfiguration config){
    super(connector, config);
    log.info("New Album collection instantiated");
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
    return super.addEntity(  entity );
  }

  
  
  @Override
  public void load(IResponseHandler responseHandler) throws ServiceException, LoginException {
    this.clear();
    super.load(responseHandler);
  }
  
  
  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    
    if ( tagName.equals( Album.TAGNAME ) ){
      return this.getClass().getMethod("add", Album.class );
    }
    
    
    return null;
  }

  
  
  
  
  
  
}
