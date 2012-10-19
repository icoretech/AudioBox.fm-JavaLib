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


package fm.audiobox.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.http.HttpResponse;

import fm.audiobox.configurations.Response;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;

/**
 * This interface describes how a cache manager should behave.
 *
 * @author Fabio Tunno
 */
public interface ICacheManager extends Serializable {

  
  /**
   * Sets the current configuration environment
   * @param config the {@link IConfiguration current configuration}
   */
  public void setConfiguration(IConfiguration config);
  
  /**
   * This method must return the content of the original response
   * 
   * @param destEntity the {@link IEntity} to be populated
   * @param ecode the String representing the {@code unique indentify} of the cache data
   * @return the {@link InputStream} where reading the response from
   */
  public Response getResponse(IEntity destEntity, String ecode);
  
  /**
   * This method must save the response content represented by {@link InputStream}
   * 
   * @param destEntity the unique {@link IEntity} to store
   * @param ecode the unique string linked to the {@link IEntity destEntity}
   * @param response the {@link InputStream} where reading the response from
   */
  public void store(IEntity destEntity, String ecode, Response response, HttpResponse httpResponse);
  
  
  /**
   * This method must return the {@code unique identify} used while invoking server
   * 
   * @param destEntity the {@link IEntity}
   * @param url the request url
   * @return String representing the {@code unique identify} of the cache data
   */
  public String setup(IEntity destEntity, String url, IConnectionMethod request);
  
  /**
   * This method must clear the whole cache
   * @throws IOException
   */
  public void clear() throws IOException;
    
  
  
}
