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
    if ( log.isTraceEnabled() ){
      log.trace("Entity instantiated: " + this.getNamespace() );
    }
  }
  

  @Override
  public String getToken() {
    return this.token;
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
  public IConfiguration getConfiguration(){
    return this.configuration;
  }

  
  /**
   * Copies data from an {@link IEntity}
   * @param entity the {@link IEntity} where copy data from
   */
  protected abstract void copy(IEntity entity);
  
}
