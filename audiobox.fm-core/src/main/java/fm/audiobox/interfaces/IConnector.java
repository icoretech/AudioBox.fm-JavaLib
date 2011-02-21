package fm.audiobox.interfaces;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;


public interface IConnector {

  
  /**
   * Builds {@link HttpMethodBase} using GET method and passing parameters
   * @param destEntity  {@link IEntity} to populate retriving response content
   * @param action  action to invoke
   * @param params  request parameters to send
   * @return
   */
  public IConnectionMethod get(IEntity destEntity, String action, List<NameValuePair> params);
  
  /**
   * Builds {@link HttpMethodBase} using GET method without passing parameters
   * @param destEntity  {@link IEntity} to populate retriving response content
   * @param action  action to invoke
   * @return
   */
  public IConnectionMethod get(IEntity destEntity, String action);
  
  /**
   * Builds {@link HttpMethodBase} using PUT method
   * @param destEntity
   * @param action
   * @return
   */
  public IConnectionMethod put(IEntity destEntity, String action);
  
  /**
   * Builds {@link HttpMethodBase} using POST method
   * @param destEntity
   * @param action
   * @return
   */
  public IConnectionMethod post(IEntity destEntity, String action);
  
  
  
  /**
   * Builds {@link HttpMethodBase} using DELETE method
   * @param destEntity
   * @param action
   * @return
   */
  public IConnectionMethod delete(IEntity destEntity, String action);

  
  
  
  public interface IConnectionMethod {
    
    /**
     * Invokes server
     */
    public void send();
    
    
    /**
     * Invokes server passing parameters.
     * (Used with all methods exclusing GET)
     * 
     * @param params {@link List} of {@link NameValuePair} used as request parameters
     */
    public void send(List<NameValuePair> params);
    
    /**
     * Invokes server passing an entire {@link HttpEntity}
     * (Used with POST method)
     * @param entity
     */
    public void send(HttpEntity entity);
    
    
    /**
     * Cancels current request
     * (I.E. used to cancel an upload 
     */
    public void abort();
    
  }
  
}
