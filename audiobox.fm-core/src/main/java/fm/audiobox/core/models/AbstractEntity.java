package fm.audiobox.core.models;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.observables.Event;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;


/**
 * This class represents the single {@link IEntity} abstract implementation
 * <p>
 * It extends {@link Observable} to allow you to add event observer.
 * <br/>
 * Actually it is used for each collection item such as:
 * <ul>
 *  <li>{@link User}</li>
 *  <li>{@link Permissions}</li>
 *  <li>{@link AccountStats}</li>
 *  <li>{@link Preferences}</li>
 *  <li>{@link ExternalTokens}</li>
 *  <li>{@link Album}</li>
 *  <li>{@link Playlist}</li>
 *  <li>{@link MediaFile}</li>
 *  <li>{@link Error}</li>
 * </ul> 
 * </p>
 */
public abstract class AbstractEntity extends Observable implements IEntity {

  private static Logger log = LoggerFactory.getLogger(AbstractEntity.class);
  
  public static final String TOKEN = "token";
  public static final String TOKEN_SHORT = "tk";
  
  private IConfiguration configuration;
  
  /**
   * Dynamic properties associated with this instance
   */
  protected Map<String, Object> properties;

  /**
   * {@code Token} of this {@link IEntity} instance
   * <p>
   *  <b>Note: not all {@link IEntity} has a token</b>
   * </p>
   */
  protected String token;
  
  /**
   * Parent {@link IEntity} which this instance is linked to
   */
  private IEntity parent;

  public AbstractEntity(IConfiguration config) {
    this.configuration = config;
    this.properties = new ConcurrentHashMap<String, Object>();
    if (log.isTraceEnabled()) {
      log.trace("Entity instantiated: " + this.getNamespace());
    }
  }

  public String getToken() {
    return this.token;
  }

  public IEntity getParent() {
    return parent;
  }

  public void setParent(IEntity parent) {
    this.parent = parent;
  }

  public void setProperty(String key, Object value) {
    if (value == null) {
      if (this.properties.containsKey(key)) {
        this.properties.remove(key);
      }
    } else {
      this.properties.put(key, value);
    }
  }

  public Object getProperty(String key) {
    return this.properties.get(key);
  }

  
  /**
   * Sets the token associated with this {@link IEntity} class.
   * <p>
   *  This method should be used by the response parser only
   * </p>
   * @param tk the {@code token} of the class
   */
  public final void setToken(String tk) {
    this.token = tk;
  }

  /**
   * Returns the default {@link IConnector} associated with this {@link IConfiguration}
   *
   * @return the {@link IConnector}
   */
  protected IConnector getConnector() {
    return this.configuration.getFactory().getConnector();
  }

  /**
   * Returns the specified {@link IConnector} associated with this {@link IConfiguration}
   * by given {@link Connectors}
   *
   * @param the specified {@link Connectors connector}
   * @return the {@link IConnector}
   */
  protected IConnector getConnector(IConfiguration.Connectors server) {
    return this.configuration.getFactory().getConnector(server);
  }

  /**
   * Returns the {@link IConfiguration} associated with this {@link IEntity}
   *
   * @return the {@link IConfiguration}
   */
  public IConfiguration getConfiguration() {
    return this.configuration;
  }

  /**
   * Copies data from an {@link IEntity} to another one
   * <p>
   *  <b>Note: this method is not implemented yet</b>
   * </p>
   * @param entity the {@link IEntity} where copy data from
   */
  @Deprecated
  protected abstract void copy(IEntity entity);
  
  
  /**
   * This method invokes {@link AbstractEntity#load(boolean, IResponseHandler)}
   * passing {@code false} as {@code async} switch
   */
  public abstract IConnectionMethod load(boolean async)  throws ServiceException, LoginException;
  
  /**
   * Executes request populating this class.
   * 
   * <p>
   * You can use the returned {@link IConnectionMethod} for adding observers.
   * <p>
   * Note: this method invokes the {@link AbstractCollectionEntity#clear()} before performing the request
   * </p>
   * <p>
   *  <b>Valid {@link IConnectionMethod} is returned if {@code async} is passed to {@code true}</b>
   * </p>
   * 
   * @param async enables or disable asynchronized request
   * @param responseHandler the {@link IResponseHandler} as custom interceptor for this response
   * 
   * @return {@link IConnectionMethod} instance.
   * 
   * @throws ServiceException if any connection error occurrs
   * @throws LoginException if any login error occurrs
   */
  public abstract IConnectionMethod load(boolean async, IResponseHandler responseHandler)  throws ServiceException, LoginException;
  
  
  public void startLoading() {
    this.setChanged();
    Event event = new Event(this);
    event.state = Event.States.START_LOADING;
    this.notifyObservers(event);
  }

  public void endLoading() {
    this.setChanged();
    Event event = new Event(this);
    event.state = Event.States.END_LOADING;
    this.notifyObservers(event);
  }
  
  
  /**
   * Returns a {@link List<NameValuePair>} representing the parameters used
   * as query string associated with this {@link IEntity}
   * 
   * @param full this {@link IEntity} will be fully serialized if {code full} is {@code true}
   * @return {@link List<NameValuePair>} representing the parameters
   */
  protected abstract List<NameValuePair> toQueryParameters(boolean full);
  
  
  /**
   * Serialization of the collection depending to given {@link ContentFormat}
   * @param format the {@link ContentFormat} used for serialization
   * @return the String representation
   */
  public String serialize(IConfiguration.ContentFormat format) {
    return this.getNamespace();
  }
  
  
  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append( this.getTagName() );
    if ( this.token != null && !this.token.equals("") ) {
      buf.append( "(" + this.token + ")" );
    }
    buf.append( "{" );
    List<NameValuePair> params = this.toQueryParameters(false);
    for (int i = 0; i < params.size(); i++ ){
      NameValuePair k = params.get(i);
      buf.append( k.getName() + ": " + k.getValue() );
      if ( i < params.size() -1 ){
        buf.append(", ");
      }
    }
    buf.append( "}");
    return buf.toString();
  }
  
}
