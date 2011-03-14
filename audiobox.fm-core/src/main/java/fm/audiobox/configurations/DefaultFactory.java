package fm.audiobox.configurations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.models.User;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IFactory;

public class DefaultFactory implements IFactory {

  private static final Logger log = LoggerFactory.getLogger(DefaultFactory.class);
  
  private final Map<String, Class<? extends IEntity>> gEntities = getDefaultEntities(); 
  
  private Map<String, Class<? extends IEntity>> entities = gEntities; // TODO: check this code (same pointers?)
  private IConnector connector;
  
  
  @Override
  public IEntity getEntity(String tagName, IConfiguration configuration) {
    
    IEntity entity = null;
    Class<? extends IEntity> klass = entities.get(tagName);
    if ( klass == null ) {
      log.warn("No IEntity found under key: " + tagName);
      return null;
    }
    
    try {
      Constructor<? extends IEntity> constructor = klass.getConstructor(IConnector.class, IConfiguration.class);
      entity = constructor.newInstance(this.connector, configuration);
    } catch (SecurityException e) {
      log.error("SecurityException while instanciating IEntity under key: " + tagName, e);
    } catch (NoSuchMethodException e) {
      log.error("NoSuchMethodException while instanciating IEntity under key: " + tagName, e);
    } catch (IllegalArgumentException e) {
      log.error("IllegalArgumentException while instanciating IEntity under key: " + tagName, e);
    } catch (InstantiationException e) {
      log.error("InstantiationException while instanciating IEntity under key: " + tagName, e);
    } catch (IllegalAccessException e) {
      log.error("IllegalAccessException while instanciating IEntity under key: " + tagName, e);
    } catch (InvocationTargetException e) {
      log.error("InvocationTargetException while instanciating IEntity under key: " + tagName, e);
    }
    
    return entity;
  }

  @Override
  public void setEntity(String tagName, Class<? extends IEntity> entity) {
    entities.put( tagName, entity );
  }

  @Override
  public void setConnector(IConnector connector) {
    this.connector = connector;
  }
  
  
  /**
   * Populates a {@link HashMap} with default initial values
   * @return {@link HashMap}
   */
  public static Map<String, Class<? extends IEntity>> getDefaultEntities() {
    
    Map<String, Class<? extends IEntity>> _entities = new HashMap<String, Class<? extends IEntity>>();
    
    _entities.put(User.TAG_NAME, User.class);
    
    return _entities;
  }
  
}
