package fm.audiobox.core.models;

import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;

public abstract class AbstractEntity extends Observable implements IEntity {

  private static Logger log = LoggerFactory.getLogger(AbstractEntity.class);
  
  private IConnector connector;
  private IConfiguration configuration; 
  protected Map<String, Object> properties;
  
  protected String token;
  
  
  public AbstractEntity(IConnector connector, IConfiguration config){
    this.connector = connector;
    this.configuration = config;
    this.properties = new ConcurrentHashMap<String, Object>();
    if ( log.isDebugEnabled() ){
      log.trace("Entity instantiated: " + this.getNamespace() );
    }
  }
  

  @Override
  public String getToken() {
    return this.token;
  }

  public final void setToken(String tk){
    this.token = tk;
  }
  
  /**
   * Returns the {@link IConnector} associated with this {@link IEntity}
   * @return the {@link IConnector}
   */
  protected IConnector getConnector(){
    return this.connector;
  }

  /**
   * Returns the {@link IConfiguration} associated with this {@link IEntity}
   * @return the {@link IConfiguration}
   */
  protected IConfiguration getConfiguration(){
    return this.configuration;
  }

  
  /**
   * Sets a generic property. This method is used for general purposes
   * 
   * @param tagName the tagName found while parsing response content
   * @param value general Object as field value
   */
  public void setProperty(String key, Object value) {
    this.properties.put(key, value);
  }
  
  /**
   * Returns an Object associated with given key
   * 
   * @param key String
   * @return the Object associated with given key
   */
  public Object getProperty(String key) {
    return this.properties.get(key);
  }
  
  
}
