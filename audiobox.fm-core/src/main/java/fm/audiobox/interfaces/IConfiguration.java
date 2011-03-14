package fm.audiobox.interfaces;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;

public interface IConfiguration {
  
  
  public enum RequestFormat {
    XML,
    JSON,
    TXT
  }
  

  /**
   * Sets the default extension for each request
   * @param requestFormat
   */
  public void setRequestFormat(RequestFormat requestFormat);
  
  /**
   * Returns the default extension for each request
   * @return
   */
  public RequestFormat getRequestFormat();
  
  /**
   * Retruns current {@link IFactory} associated with this configuration
   * @return {@link IFactoruy}
   */
  public IFactory getFactory();
  
  /**
   * Returns {@link IParser} associated with this configuration
   * @return {@link IParser}
   */
  public IParser getParser();

  /**
   * Sets the application name sent to server as "User-Agent" request header
   * @param appName the application name
   */
  public void setApplicationName(String appName);

  /**
   * Returns the application name sent to server
   * @return
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
   * Returns the application version sent to server as "User-Agent"
   * @return
   */
  public String getVersion();
  

  /**
   * Returns the "User-Agent" sent to server
   * @return
   */
  public String getUserAgent();
  
  
  /**
   * Returns protocol used for connection
   * @return
   */
  public String getProtocol();
  
  /**
   * Returns host used for connection
   * @return
   */
  public String getHost();
  
  /**
   * Returns server port used for connection
   * @return
   */
  public int getPort();
  
  
  /**
   * Returns path used for connetion
   * @return
   */
  public String getPath();

  
  /**
   * Passing true AudioBox will try to use FileSystem to store information as cache
   * @param useChache
   */
  public void setUseCache(boolean useCache);
  
  
  /**
   * Returns true if AudioBox is using cache sistem
   * @return
   */
  public boolean isUsingCache();
  
  
  /**
   * Enables or disables {@code short} request parameter
   * @param shortResponse
   */
  public void setShortResponse(boolean shortResponse);
  
  
  /**
   * Returns {@code true} if AudioBox is using the {@code short} request parameter. {@code False} if not
   * @return
   */
  public boolean isUsingShortResponse();
  
  
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
   * Sets the connection method class used for connection
   * @param method
   */
  public void setHttpMethodType(Class<? extends IConnectionMethod> method);
  
  /**
   * Returns the connection method class used for connection
   * @return
   */
  public Class<? extends IConnectionMethod> getHttpMethodType();
  
}
