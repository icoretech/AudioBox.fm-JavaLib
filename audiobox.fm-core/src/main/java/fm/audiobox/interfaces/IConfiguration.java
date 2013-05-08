
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
 * This interface describes how an implementation of the AudioBox configuration should behave.
 * <br />
 * An implementing class is mainly used to configure every aspect of the library itself.
 */
public interface IConfiguration {


  /**
   * Identifies each managed environments
   */
  public static enum Environments {
    development,
    test,
    live
  }
  
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
    NODE,
    DAEMON
  }
  
  
  /**
   * The log level used by internal logger
   */
  public static enum LogLevel {
    DEBUG,
    INFO,
    WARN,
    ERROR
  }

  
  /**
   * Sets the default extension for each request
   * 
   * @param requestFormat the {@link ContentFormat} used by each request
   */
  public void setRequestFormat(ContentFormat requestFormat);

  /**
   * @return the default extension for each request
   */
  public ContentFormat getRequestFormat();
  
  
  /**
   * @return the global {@link IResponseHandler} used for parsing the response
   */
  public Class<? extends IResponseHandler> getResponseDeserializer();
  
  /**
   * Sets the global {@link IResponseHandler} used for parsing the response
   */
  public void setResponseDeserializer(Class<? extends IResponseHandler> responseParser);
  
  
  /**
   * @return the global {@link IResponseHandler} used for parsing the response
   */
  public Class<? extends IRequestHandler> getRequestSerializer();
  
  /**
   * Sets the global {@link IResponseHandler} used for parsing the response
   */
  public void setRequestSerializer(Class<? extends IRequestHandler> responseParser);
  
  

  /**
   * @return current {@link IFactory} associated with this configuration
   */
  public IFactory getFactory();
  
  
  /**
   * Get/Set method for the global {@link IAuthenticationHandle}. It handles the authentication method
   */
  public IAuthenticationHandle getAuthenticationHandle();
  
  public void setAuthenticationHandle(IAuthenticationHandle handle);
  

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
   * @param major the number indentifing the major version
   * @param minor the number indentifing the minor version
   * @param revision the number indentifing the revision version
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
   * @return env used for connection
   */
  public IConfiguration.Environments getEnvironment();
  
  /**
   * Set the {@link IConfiguration.Environments environment}
   * @param env the environment to be use
   */
  public void setEnvironment(IConfiguration.Environments env);

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
   * @param handler the {@link ILoginExceptionHandler} instance
   */
  public void setDefaultLoginExceptionHandler(ILoginExceptionHandler handler);

  /**
   * Returns the default handler for {@link LoginException}
   * @return the {@link ILoginExceptionHandler}
   */
  public ILoginExceptionHandler getDefaultLoginExceptionHandler();



  /**
   * Sets the default handler for {@link ServiceException}
   * @param handler the {@link IServiceExceptionHandler} instance
   */
  public void setDefaultServiceExceptionHandler(IServiceExceptionHandler handler);

  /**
   * Returns the default handler for {@link ServiceException}
   * @return the {@link IServiceExceptionHandler}
   */
  public IServiceExceptionHandler getDefaultServiceExceptionHandler();

  /**
   * Sets the connection method class used for connection
   * @param method the {@link IConnectionMethod}
   */
  public void setHttpMethodType(Class<? extends IConnectionMethod> method);

  /**
   * Returns the connection method class used for connection
   * @return the {@link IConnectionMethod}
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
