package fm.audiobox.core.observers;

import java.util.EventObject;

import fm.audiobox.core.api.Model;

public class Event extends EventObject {

	private static final long serialVersionUID = 1L;

	public enum EventReason {
		Updated,
		Created,
		Destroyed,
		Removed,
		Added,
		Completed
	}
	
	public EventReason reason = EventReason.Updated;
	public Model detail;
	
	public Event(Object source){
		super(source);
	}
	
	
}
