package fm.audiobox.core.observables;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Event extends EventObject {

  @SuppressWarnings("unused")
  private static Logger logger = LoggerFactory.getLogger( Event.class );
  
  
  private static final long serialVersionUID = 1L;
  
  private Map<String, Object> properties = new HashMap<String, Object>();

  // It could be used for indexing the entity into collection
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
  
  
  public void addProperty(String key, Object value){
    this.properties.put( key, value );
  }
  
  public Object getProperty(String key){
    
    if ( this.properties.containsKey( key ) ){
      return this.properties.get( key );
    }
    return null;
    
  }
  
}
