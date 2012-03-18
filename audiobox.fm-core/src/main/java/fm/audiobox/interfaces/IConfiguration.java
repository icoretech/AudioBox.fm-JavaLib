
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

import java.util.concurrent.ExecutorService;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;

/**
 * This interface describes how an implementation of the AudioBox configuration should behave.<br/>
 * An implementing class is mainly used to configure every aspect of the library itself.
 * 
 * @author Fabio Tunno
 */
public interface IConfiguration {


  /**
   * Identifies each request and response format type 
   */
  public static enum ContentFormat {
    XML,
    JSON,
    TXT,
    BINARY
  }
  
  
  /**
   * Identifies all connector types.
   * Each connector can connect to its own host 
   */
  public static enum Connectors {
    RAILS,
    NODE
  }

  
  /**
   * Sets the default extension for each request
   * @param requestFormat
   */
  public void setRequestFormat(ContentFormat requestFormat);

  /**
   * @return the default extension for each request
   */
  public ContentFormat getRequestFormat();

  /**
   * @return current {@link IFactory} associated with this configuration
   */
  public IFactory getFactory();

  /**
   * Sets the application name sent to server as "User-Agent" request header
   * @param appName the application name
   */
  public void setApplicationName(String appName);

  /**
   * @return the application name sent to server
   */
  public String getApplicationName();


  /**
   * Sets the version sent to server as "User-Agent" request header.
   * Default should be 1.0.0
   * 
   * @param major
   * @param minor
   * @param revision
   */
  public void setVersion(int major, int minor, int revision);

  /**
   * @return the application version sent to server as "User-Agent"
   */
  public String getVersion();


  /**
   * @return the "User-Agent" sent to server
   */
  public String getUserAgent();


  /**
   * @return protocol used for connection
   */
  public String getProtocol(IConfiguration.Connectors server);

  /**
   * @return host used for connection
   */
  public String getHost(IConfiguration.Connectors server);

  /**
   * Returns server port used for connection
   * @return
   */
  public int getPort(IConfiguration.Connectors server);


  /**
   * Returns path used for connetion
   * @return
   */
  public String getPath(IConfiguration.Connectors server);


  /**
   * Passing true AudioBox will try to use FileSystem to store information as cache
   * @param useChache
   */
  public void setUseCache(boolean useCache);


  /**
   * Returns true if AudioBox is using cache sistem
   * @return
   */
  public boolean isCacheEnabled();



  /**
   * Sets the default handler for {@link LoginException}
   * @param handler
   */
  public void setDefaultLoginExceptionHandler(ILoginExceptionHandler handler);

  /**
   * Returns the default handler for {@link LoginException}
   * @return
   */
  public ILoginExceptionHandler getDefaultLoginExceptionHandler();



  /**
   * Sets the default handler for {@link ServiceException}
   * @param handler
   */
  public void setDefaultServiceExceptionHandler(IServiceExceptionHandler handler);

  /**
   * Returns the default handler for {@link ServiceException}
   * @return
   */
  public IServiceExceptionHandler getDefaultServiceExceptionHandler();

  /**
   * Sets the connection method class used for connection
   * @param method
   */
  public void setHttpMethodType(Class<? extends IConnectionMethod> method);

  /**
   * Returns the connection method class used for connection
   * @return
   */
  public Class<? extends IConnectionMethod> getHttpMethodType();


  /**
   * Use this method to get the configured {@link ExecutorService}
   * 
   * @return the configured {@link ExecutorService} used for requests
   */
  public ExecutorService getExecutor();


  /**
   * Use this method to set the {@link ICacheManager}
   * @param manager the {@link ICacheManager} to set
   */
  public void setCacheManager(ICacheManager manager);


  /**
   * Use this method to get the configured cache manager
   * @return the {@link ICacheManager} 
   */
  public ICacheManager getCacheManager();


}
