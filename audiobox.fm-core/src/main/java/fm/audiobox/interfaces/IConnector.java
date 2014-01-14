
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

import java.util.List;

import fm.audiobox.core.exceptions.ForbiddenException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HttpContext;

import fm.audiobox.configurations.Response;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.User;
import fm.audiobox.interfaces.IConfiguration.ContentFormat;

/**
 * This interface specifies how a connector should behave.
 * 
 * @author Fabio Tunno
 */
public interface IConnector {
  
  
  public static final String URI_SEPARATOR = "/";
  public static final String DOT = ".";
  public static final String X_AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
  
  public static final int DEFAULT_CHUNK = 1024 * 256;
  

  /**
   * Builds {@link HttpMethodBase} using HEAD method and passing parameters
   * 
   * @param destEntity the {@link IEntity} to populate retriving response content
   * @param action the action to invoke
   * @param params the request parameters to send
   * 
   * @return the {@link IConnectionMethod} used for a GET request
   */
  public IConnectionMethod head(IEntity destEntity, String action, List<NameValuePair> params);
  
  public IConnectionMethod head(IEntity destEntity, String path, String action, List<NameValuePair> params);
  
  public IConnectionMethod head(IEntity destEntity, String path, String action, ContentFormat format, List<NameValuePair> params);
  
  
  /**
   * Builds {@link HttpMethodBase} using GET method and passing parameters
   * 
   * @param destEntity the {@link IEntity} to populate retriving response content
   * @param action the action to invoke
   * @param params the request parameters to send
   * 
   * @return the {@link IConnectionMethod} used for a GET request
   */
  public IConnectionMethod get(IEntity destEntity, String action, List<NameValuePair> params);
  
  public IConnectionMethod get(IEntity destEntity, String path, String action, List<NameValuePair> params);
  
  public IConnectionMethod get(IEntity destEntity, String path, String action, ContentFormat format, List<NameValuePair> params);
  
  /**
   * Builds {@link HttpMethodBase} using PUT method
   * 
   * @param destEntity the {@link IEntity} to populate retriving response content
   * @param action the action to invoke
   * 
   * @return the {@link IConnectionMethod} used for a PUT request
   */
  public IConnectionMethod put(IEntity destEntity, String action);
  
  public IConnectionMethod put(IEntity destEntity, String path, String action);
  
  public IConnectionMethod put(IEntity destEntity, String path, String action, ContentFormat format);
  
  /**
   * Builds {@link HttpMethodBase} using POST method
   * 
   * @param destEntity {@link IEntity} to populate retriving response content
   * @param action the action to invoke
   * 
   * @return the {@link IConnectionMethod} used for a POST request
   */
  public IConnectionMethod post(IEntity destEntity, String action);
  
  public IConnectionMethod post(IEntity destEntity, String path, String action);
  
  public IConnectionMethod post(IEntity destEntity, String path, String action, ContentFormat format);

  
  /**
   * Builds {@link HttpMethodBase} using DELETE method
   * 
   * @param destEntity the {@link IEntity} to populate retriving response content
   * @param action the action to invoke
   * 
   * @return the {@link IConnectionMethod} used for a DELETE request
   */
  public IConnectionMethod delete(IEntity destEntity, String action, List<NameValuePair> params);
  
  public IConnectionMethod delete(IEntity destEntity, String path, String action, List<NameValuePair> params);
  
  public IConnectionMethod delete(IEntity destEntity, String path, String action, ContentFormat format, List<NameValuePair> params);
  
  
  /**
   * Aborts all pending requests
   */
  public void abort();
  
  /**
   * Destroys the client.
   * The same of abort but it prevents to create a new Client
   */
  public void destroy();
  
  
  public void setTimeout(int timeout);
  public int getTimeout();
  
  /**
   * The {@link IConnectionMethod} interface is used to build a specific request using
   * one of four Http verbs in POST, PUT, GET or DELETE.
   */
  public interface IConnectionMethod {
    
    /* Constants */
    public static final String METHOD_HEAD = HttpHead.METHOD_NAME;
    public static final String METHOD_GET = HttpGet.METHOD_NAME;
    public static final String METHOD_POST = HttpPost.METHOD_NAME;
    public static final String METHOD_PUT = HttpPut.METHOD_NAME;
    public static final String METHOD_DELETE = HttpDelete.METHOD_NAME;
    
    public static final String HTTP_HEADER_ETAG = "ETag";
    public static final String HTTP_HEADER_IF_NONE_MATCH = "If-None-Match";
    
    public HttpContext getRequestContext();
    
    /**
     * Initializes an {@link IConnectionMethod} object with given values.
     * 
     * @param destEntity the {@link IEntity} to pupulate while parsing response content
     * @param method the original {@link HttpRequestBase} used for connection
     * @param connector the original {@link HttpClient} used as connector
     * @param config the {@link IConfiguration} object to reffer configuration to
     */
    public void init(IEntity destEntity, HttpRequestBase method, HttpClient connector, IConfiguration config, IConfiguration.ContentFormat format);
    
    
    /**
     * Use this method if you want your request to follow redirects.
     * 
     * @param followRedirect if <coode>true</code> forces request to follow redirects ( used by download method )
     */
    public void setFollowRedirect(boolean followRedirect);
    
    
    /**
     * Returns the {@link HttpRequestBase} used by the {@link HttpClient} wrapped by this object.
     * 
     * @return the {@link HttpRequestBase} object
     */
    public HttpRequestBase getHttpMethod();
    
    
    /**
     * Sets the {@link IAuthenticationHandle} for handling the authentication method
     * @param handle
     */
    public void setAuthenticationHandle(IAuthenticationHandle handle);
    
    /**
     * Returns the {@link IAuthenticationHandle}
     */
    public IAuthenticationHandle getAuthenticationHandle();
    
    
    
    /**
     * Sets the current {@link User}
     */
    public void setUser( User user );
    
    /**
     * Returns the current {@link User}
     */
    public User getUser();
    
    
    /**
     * Returns the destination entity to populate while parsing response content
     * 
     * @return the destination {@link IEntity} to populate 
     */
    public IEntity getDestinationEntity();
    
    
    /**
     * Adds a custom header
     * 
     * @param header the Hedaer name
     * @param value the Hedaer value. If {@code null} it removes the header
     */
    public void addHeader(String header, String value);
    
    public void addHeader( Header header );
    
    /**
     * Start the request.
     * 
     * @param async whether to make the request asynchronous or not.
     * 
     * @return the {@link Response} if the request is not asynchronous
     * 
     * @throws ServiceException if any exception remote exception occurs
     * @throws LoginException if any unauthorized exception occurs
     * 
     */
    public Response send(boolean async) throws ServiceException, LoginException, ForbiddenException;
    
    
    /**
     * Start the request passing parameters.
     * (Used with all verbs but GET)
     * 
     * @param async whether to make the request asynchronous or not.
     * @param params {@link List} of {@link NameValuePair} used as request parameters
     * 
     * @return the {@link Response} if the request is not asynchronous
     * 
     * @throws ServiceException if any exception remote exception occurs
     * @throws LoginException if any unauthorized exception occurs
     */
    public Response send(boolean async, List<NameValuePair> params) throws ServiceException, LoginException, ForbiddenException;
    
    
    /**
     * Invokes AudioBox.fm passing an entire {@link HttpEntity}
     * (Used by POST method)
     * 
     * @param async whether to make the request asynchronous or not.
     * @param params the {@link HttpEntity} used as request parameter
     * 
     * @return the {@link Response} if the request is not asynchronous
     * 
     * @throws ServiceException if any exception remote exception occurs
     * @throws LoginException if any unauthorized exception occurs
     */
    public Response send(boolean async, HttpEntity params) throws ServiceException, LoginException, ForbiddenException;
    
    
    /**
     * Invokes AudioBox.fm passing a {@link HttpEntity} as request parameter.
     * The {@link IResponseHandler} is used as custom response handler.
     * 
     * @param async whether to make the request asynchronous or not.
     * @param entity a {@link HttpEntity} used as request parameter
     * @param responseHandler a {@link IResponseHandler} used as custom response handler
     * 
     * @return the {@link Response} if the request is not asynchronous
     * 
     * @throws ServiceException if any exception remote exception occurs
     * @throws LoginException if any unauthorized exception occurs
     * 
     */
    public Response send(boolean async, HttpEntity entity, IResponseHandler responseHandler) throws ServiceException, LoginException, ForbiddenException;
    

    /**
     * Use this method to get the {@link Response} of this request 
     * 
     * @return the {@link Response} of the request
     * 
     * @throws ServiceException if any exception remote exception occurs
     * @throws LoginException if any unauthorized exception occurs
     */
    public Response getResponse() throws ServiceException, LoginException, ForbiddenException;
    
    
    /**
     * Aborts all current pending requests 
     */
    public void abort();
    
    
    /**
     * Returns {@code true} if request is running. {@code false} if not
     * @return boolean
     */
    public boolean isRunning();
    
    
    /**
     * Returns {@code true} if request has been aborted. {@code false} if not
     * @return boolean
     */
    public boolean isAborted();
    
    
    
    /**
     * @return true if the verb used is GET
     */
    public boolean isGET();
    
    
    /**
     * @return true if the verb used is POST
     */
    public boolean isPOST();
    
    
    /**
     * @return true if the verb used is PUT
     */
    public boolean isPUT();
    
    
    /**
     * @return true if the verb used is DELETE
     */
    public boolean isDELETE();
    
  }
  
}
