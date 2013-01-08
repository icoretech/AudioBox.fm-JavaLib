package fm.audiobox.core.observables;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is used while emitting events
 * <br />
 * It will contain all event preperties you will need
 */
public class Event extends EventObject {

  @SuppressWarnings("unused")
  private static Logger logger = LoggerFactory.getLogger( Event.class );
  
  
  private static final long serialVersionUID = 1L;
  
  private Map<String, Object> properties = new HashMap<String, Object>();

  public int detail = -1;

  public static enum States {
    ENTITY_ADDED,
    ENTITY_REMOVED,
    COLLECTION_CLEARED,
    ENTITY_REFRESHED,
    CONNECTED,
    DISCONNECTED,
    ERROR,
    START_LOADING,
    END_LOADING
  };
  
  
  public States state;
  
  public Event(Object source) {
    super(source);
  }

  
  public Event(Object source, States state) {
    this(source);
    this.state = state;
  }
  
  /**
   * Use this special method for setting arbitrary property
   * 
   * @param key the {@code key} of the property
   * @param value the {@code value} of the property
   */
  public void addProperty(String key, Object value){
    this.properties.put( key, value );
  }
  
  /**
   * Gets the property
   * 
   * @param key the {@code key} of the property you want to get
   * @return the {@code value} of the property
   */
  public Object getProperty(String key){
    
    if ( this.properties.containsKey( key ) ){
      return this.properties.get( key );
    }
    return null;
    
  }
  
}
