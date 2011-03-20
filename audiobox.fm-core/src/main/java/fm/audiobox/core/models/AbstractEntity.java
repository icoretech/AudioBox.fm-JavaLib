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

  private static final Logger log = LoggerFactory.getLogger(AbstractEntity.class);
  
  private IConnector connector;
  private IConfiguration configuration; 
  protected Map<String, Object> properties;
  
  protected String token;
  
  
  public AbstractEntity(IConnector connector, IConfiguration config){
    this.connector = connector;
    this.configuration = config;
    this.properties = new ConcurrentHashMap<String, Object>();
    if ( log.isInfoEnabled() ){
      log.info("Entity instanciated: " + this.getNamespace() );
    }
  }
  
  @Override
  public String getNamespace() {
    return null;
  }

  @Override
  public String getToken() {
    // TODO Auto-generated method stub
    return null;
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
   * Sets a property value. This method is used by each {@link Entity} while parsing response content
   * 
   * @param tagName the tagName found while parsing response content
   * @param value general Object as field value
   */
  protected void setProperty(String tagName, Object value) {
    this.properties.put(tagName, value);
  }
  
  
  /**
   * Instantiates and returns a new {@link IEntity} object
   * @param entityClass the default {@link Class} that represents the {@link IEntity} to be instantiated 
   * @return requested {@link IEntity}
   */
  protected IEntity instanceChildEntity(Class<? extends IEntity> entityClass ){
    return getConfiguration().getFactory().getEntity(entityClass, getConfiguration() );
  }
  
}
