package fm.audiobox.core.observables;

import java.util.EventObject;

public class Event extends EventObject {

  
  private static final long serialVersionUID = 1L;


  public static enum States {
    ENTITY_ADDED,
    ENTITY_REMOVED,
    COLLECTION_CLEARED
  };
  
  
  public States state;
  
  public Event(Object source) {
    super(source);
  }

  
  public Event(Object source, States state) {
    super(source);
    this.state = state;
  }
  
  
}
