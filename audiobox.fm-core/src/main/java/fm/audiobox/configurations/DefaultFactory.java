package fm.audiobox.configurations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.AudioBox;
import fm.audiobox.core.models.Album;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.Artist;
import fm.audiobox.core.models.Artists;
import fm.audiobox.core.models.Covers;
import fm.audiobox.core.models.Genre;
import fm.audiobox.core.models.Genres;
import fm.audiobox.core.models.Plan;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.Profile;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.models.User;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IFactory;

public class DefaultFactory implements IFactory {

  private static final Logger log = LoggerFactory.getLogger(DefaultFactory.class);
  
  private static final String[] EXCLUDED_EXTENDABLE_CLASSES = new String[]{ User.TAGNAME, Plan.TAGNAME, Profile.TAGNAME };
  
  /**
   * Default Entities map.
   * Used to store the default entity class
   */
  private static Map<String, Class<? extends IEntity>> gEntities;
  
  /**
   * Used to store the entity class associated with this {@link AudioBox} instance
   */
  private Map<String, Class<? extends IEntity>> entities;
  
  /**
   * {@link IConnector} associated with this {@link AudioBox} instance
   */
  private IConnector connector;
  
  static {
    gEntities = new HashMap<String, Class<? extends IEntity>>();
    gEntities.put( User.TAGNAME, User.class );
    gEntities.put( Plan.TAGNAME, Plan.class );
    gEntities.put( Profile.TAGNAME, Profile.class );
    
    gEntities.put( Playlists.TAGNAME, Playlists.class );
    gEntities.put( Playlist.TAGNAME, Playlist.class );
    
    
    gEntities.put( Genres.TAGNAME, Genres.class );
    gEntities.put( Genre.TAGNAME, Genre.class );
    
    gEntities.put( Albums.TAGNAME, Albums.class );
    gEntities.put( Album.TAGNAME, Album.class );
    gEntities.put( Covers.TAGNAME, Covers.class );
    
    gEntities.put( Artists.TAGNAME, Artists.class );
    gEntities.put( Artist.TAGNAME, Artist.class );
    
    gEntities.put( Tracks.TAGNAME, Tracks.class );
    gEntities.put( Track.TAGNAME, Track.class );
    
  }
  
  
  @SuppressWarnings("unchecked")
  public DefaultFactory(){
    entities = (Map<String, Class<? extends IEntity>>) 
          ((HashMap<String, Class<? extends IEntity>>) gEntities).clone();
  }
  
  
  @Override
  public boolean containsEntity(String tagName) {
    return this.entities.containsKey(tagName);
  }
  
  
  @Override
  public IEntity getEntity(String tagName, IConfiguration configuration) {
    
    IEntity entity = null;
    
    if ( ! this.containsEntity(tagName) ) {
      log.warn("No IEntity found under key: " + tagName );
      return null;
    }
    
    Class<? extends IEntity> klass = this.entities.get(tagName);
    
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
    if ( Arrays.asList( EXCLUDED_EXTENDABLE_CLASSES ).contains( tagName ) ) {
      log.warn("Cannot use '" + tagName + "' as Entity. Use its default Entity instead");
      return;
    }
    entities.put( tagName, entity );
  }

  @Override
  public void setConnector(IConnector connector) {
    this.connector = connector;
  }

}
