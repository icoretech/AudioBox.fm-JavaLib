package fm.audiobox.interfaces;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;


public interface IConnector {
  
  /**
   * Builds {@link HttpMethodBase} using GET method and passing parameters
   * 
   * @param destEntity  {@link IEntity} to populate retriving response content
   * @param action  action to invoke
   * @param params  request parameters to send
   * @return
   */
  public IConnectionMethod get(IEntity destEntity, String action, List<NameValuePair> params);
  
  /**
   * Builds {@link HttpMethodBase} using PUT method
   * @param destEntity  {@link IEntity} to populate retriving response content
   * @param action  action to invoke
   * @return
   */
  public IConnectionMethod put(IEntity destEntity, String action);
  
  /**
   * Builds {@link HttpMethodBase} using POST method
   * @param destEntity  {@link IEntity} to populate retriving response content
   * @param action  action to invoke
   * @return
   */
  public IConnectionMethod post(IEntity destEntity, String action);
  
  /**
   * Builds {@link HttpMethodBase} using DELETE method
   * @param destEntity  {@link IEntity} to populate retriving response content
   * @param action  action to invoke
   * @return
   */
  public IConnectionMethod delete(IEntity destEntity, String action);
  
  
  public interface IConnectionMethod {
    
    public static final String METHOD_GET = HttpGet.METHOD_NAME;
    public static final String METHOD_POST = HttpPost.METHOD_NAME;
    public static final String METHOD_PUT = HttpPut.METHOD_NAME;
    public static final String METHOD_DELETE = HttpDelete.METHOD_NAME;
    
    /**
     * Initialization method. Sets all variables
     * 
     * @param destEntity the {@link IEntity} to pupulate while parsing response content
     * @param method the original {@link HttpRequestBase} used for connection
     * @param connector the original {@link HttpClient} used as connector
     * @param followRedirect when {@code true} force request to follow redirect ( used by download method ) 
     */
    public void init(IEntity destEntity, HttpRequestBase method, HttpClient connector, IConfiguration config);
    
    /**
     * When {@code true} force request to follow redirects ( used by download method )
     * @param followRedirect
     */
    public void setFollowRedirect(boolean followRedirect);
    
    /**
     * Returns the original {@link HttpRequestBase} method
     * @return the original {@link HttpRequestBase} method
     */
    public HttpRequestBase getHttpMethod();
    
    
    /**
     * Returns the destination entity to populate while parsing response content
     * 
     * @return the destination {@link IEntity} to populate 
     */
    public IEntity getDestinationEntity();
    
    /**
     * Invokes server using
     * 
     * @return a String array containing Response status and response body
     * @throws ServiceException
     * @throws LoginException
     */
    public String[] send(boolean async) throws ServiceException, LoginException;
    
    /**
     * Invokes server passing parameters.
     * (Used with all methods exclusing GET)
     * 
     * @param params {@link List} of {@link NameValuePair} used as request parameters
     * @return a String array containing Response status and response body
     */
    public String[] send(boolean async, List<NameValuePair> params) throws ServiceException, LoginException;
    
    /**
     * Invokes server passing an entire {@link HttpEntity}
     * (Used with POST method)
     * 
     * @param destEntity
     * @param params the {@link HttpEntity} used as request parameter
     * @return a String array containing Response status and response body
     */
    public String[] send(boolean async, HttpEntity params) throws ServiceException, LoginException;
    
    /**
     * Invokes server passing a {@link HttpEntity} as request parameter.
     * The {@link IResponseHandler} is used as Response interceptor
     * 
     * @param params a {@link HttpEntity} used as request parameter
     * @param responseHandler a {@link IResponseHandler} used as custom response interceptor
     * @return a String array containing Response status and response body
     * @throws ServiceException
     * @throws LoginException
     */
    public String[] send(boolean async, HttpEntity params, IResponseHandler responseHandler) throws ServiceException, LoginException;
    
    
    /**
     * Cancels current request
     * (I.E. used to cancel an upload 
     */
    public void abort();
    
    public boolean isGET();
    public boolean isPOST();
    public boolean isPUT();
    public boolean isDELETE();
    
  }
  
}
