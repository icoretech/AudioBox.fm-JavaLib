package fm.audiobox.interfaces;

import java.lang.reflect.Method;

import fm.audiobox.core.observables.Event;


public interface IEntity {

  
  public static final String REQUEST_URL = "reqUrl";
  
  
  /**
   * Returns the namespace path associated with this Entity.
   * <b>This method is used by connector</b>
   * @return {@link String} the namespace
   */
  public String getNamespace();
  
  /**
   * Returns the tag name associated with this Entity
   * <b>This method is used by parser</b>
   * @return
   */
  public String getTagName();
  
  
  /**
   * Returns the unique token associated with this entity
   * @return {@link String} the token
   */
  public String getToken();
  
  
  /**
   * Returns the method name associated with given {@code tagName}
   * <p>This method is normally used by parser</p>
   * <p><b>Do not use this method for general purpose</b></p>
   * 
   * @param tagName the field tagName found while parsing response content
   * @return the method to invoke
   */
  public Method getSetterMethod(String tagName);
  
  
  /**
   * Sets a generic property. This method is used for general purposes
   * 
   * @param tagName the tagName found while parsing response content
   * @param value general Object as field value
   */
  public void setProperty(String key, Object value);
  
  
  /**
   * Returns an Object associated with given key
   * 
   * @param key String
   * @return the Object associated with given key
   */
  public Object getProperty(String key);
  
  /**
   * Returns the {@link IConfiguration} associated with this {@link IEntity}
   * @return the {@link IConfiguration} associated with this {@link IEntity}
   */
  public IConfiguration getConfiguration();
  
  /**
   * Returns the path to identify this {@link IEntity}
   * @return path {@link IEntity}
   */
  public String getApiPath();
  
  /**
   * Sets the parent {@link IEntity}
   * @param parent the {@link IEntity} parent object
   */
  public void setParent(IEntity parent);
  
  
  /**
   * This method should fire the {@link Event.States.START_LOADING} event
   */
  public void startLoading();
  
  /**
   * This method should fire the {@link Event.States.END_LOADING} event
   */
  public void endLoading();
  
}
