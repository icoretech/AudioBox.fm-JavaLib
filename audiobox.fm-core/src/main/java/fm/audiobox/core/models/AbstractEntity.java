package fm.audiobox.core.models;

import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

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

public abstract class AbstractEntity extends Observable implements IEntity {

  private static Logger log = LoggerFactory.getLogger(AbstractEntity.class);
  
  public static final String TOKEN_FIELD = "token";
  public static final String TOKEN_SHORT_FIELD = "tk";
  
  private IConfiguration configuration; 
  protected Map<String, Object> properties;
  
  protected String token;
  private IEntity parent;
  
  
  public AbstractEntity(IConfiguration config){
    this.configuration = config;
    this.properties = new ConcurrentHashMap<String, Object>();
    if ( log.isTraceEnabled() ){
      log.trace("Entity instantiated: " + this.getNamespace() );
    }
  }
  

  @Override
  public String getToken() {
    return this.token;
  }

  
  public IEntity getParent() {
    return parent;
  }

  public void setParent(IEntity parent) {
    this.parent = parent;
  }


  @Override
  public void setProperty(String key, Object value) {
    if ( value == null ) {
      if ( this.properties.containsKey( key )  ) {
        this.properties.remove(key);
      }
    } else {
      this.properties.put(key, value);
    }
  }
  
  @Override
  public Object getProperty(String key) {
    return this.properties.get(key);
  }
  
  public final void setToken(String tk){
    this.token = tk;
  }
  
  
  
  /**
   * Returns the default {@link IConnector} associated with this {@link IEntity}
   * @return the {@link IConnector}
   */
  protected IConnector getConnector(){
    return this.configuration.getFactory().getConnector();
  }
  
  
  /**
   * Returns the {@link IConnector} associated with this {@link IEntity}
   * @return the {@link IConnector}
   */
  protected IConnector getConnector(IConfiguration.Connectors server){
    return this.configuration.getFactory().getConnector(server);
  }

  /**
   * Returns the {@link IConfiguration} associated with this {@link IEntity}
   * @return the {@link IConfiguration}
   */
  public IConfiguration getConfiguration(){
    return this.configuration;
  }
  
  
  /**
   * Copies data from an {@link IEntity}
   * @param entity the {@link IEntity} where copy data from
   */
  protected abstract void copy(IEntity entity);
  
  
  public abstract IConnectionMethod load(boolean async)  throws ServiceException, LoginException;
  public abstract IConnectionMethod load(boolean async, IResponseHandler responseHandler)  throws ServiceException, LoginException;
  
  
  @Override
  public void startLoading() {
    this.setChanged();
    Event event = new Event(this);
    event.state = Event.States.START_LOADING;
    this.notifyObservers( event );
  }

  @Override
  public void endLoading() {
    this.setChanged();
    Event event = new Event(this);
    event.state = Event.States.END_LOADING;
    this.notifyObservers( event );
  }
  
  
}
