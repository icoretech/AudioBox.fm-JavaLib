package fm.audiobox.configurations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.AudioBox;
import fm.audiobox.core.models.Plan;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.Profile;
import fm.audiobox.core.models.User;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IFactory;

public class DefaultFactory implements IFactory {

  private static final Logger log = LoggerFactory.getLogger(DefaultFactory.class);
  
  @SuppressWarnings("unchecked")
  private static final Class<? extends IEntity>[] EXCLUDED_EXTENDABLE_CLASSES = new Class[]{ User.class, Plan.class, Profile.class };
  
  /**
   * Default Entities map.
   * Used to store the default entity class
   */
  private static Map<Class<? extends IEntity>, Class<? extends IEntity>> gEntities;
  
  /**
   * Used to store the entity class associated with this {@link AudioBox} instance
   */
  private Map<Class<? extends IEntity>, Class<? extends IEntity>> entities;
  
  /**
   * {@link IConnector} associated with this {@link AudioBox} instance
   */
  private IConnector connector;
  
  static {
    gEntities = new HashMap<Class<? extends IEntity>, Class<? extends IEntity>>();
    gEntities.put( User.class, User.class );
    gEntities.put( Plan.class, Plan.class );
    gEntities.put( Profile.class, Profile.class );
    
    gEntities.put( Playlists.class, Playlists.class );
    // TODO: populate data
  }
  
  
  @SuppressWarnings("unchecked")
  public DefaultFactory(){
    entities = (Map<Class<? extends IEntity>, Class<? extends IEntity>>) 
          ((HashMap<Class<? extends IEntity>, Class<? extends IEntity>>) gEntities).clone();
  }
  
  
  @Override
  public IEntity getEntity(Class<? extends IEntity> className, IConfiguration configuration) {
    
    IEntity entity = null;
    Class<? extends IEntity> klass = entities.get(className);
    if ( klass == null ) {
      log.warn("No IEntity found under key: " + className.getSimpleName() );
      return null;
    }
    
    try {
      Constructor<? extends IEntity> constructor = klass.getConstructor(IConnector.class, IConfiguration.class);
      entity = constructor.newInstance(this.connector, configuration);
    } catch (SecurityException e) {
      log.error("SecurityException while instanciating IEntity under key: " + className.getName(), e);
    } catch (NoSuchMethodException e) {
      log.error("NoSuchMethodException while instanciating IEntity under key: " + className.getName(), e);
    } catch (IllegalArgumentException e) {
      log.error("IllegalArgumentException while instanciating IEntity under key: " + className.getName(), e);
    } catch (InstantiationException e) {
      log.error("InstantiationException while instanciating IEntity under key: " + className.getName(), e);
    } catch (IllegalAccessException e) {
      log.error("IllegalAccessException while instanciating IEntity under key: " + className.getName(), e);
    } catch (InvocationTargetException e) {
      log.error("InvocationTargetException while instanciating IEntity under key: " + className.getName(), e);
    }
    
    return entity;
  }

  @Override
  public void setEntity(Class<? extends IEntity> klass, Class<? extends IEntity> entity) {
    if ( Arrays.asList( EXCLUDED_EXTENDABLE_CLASSES ).contains( klass ) ) {
      log.warn("Cannot use '" + klass.getName() + "' as Entity. Use its default Entity instead");
      return;
    }
    entities.put( klass, entity );
  }

  @Override
  public void setConnector(IConnector connector) {
    this.connector = connector;
  }
  
}
