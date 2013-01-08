package fm.audiobox.interfaces;

import java.lang.reflect.Method;

import fm.audiobox.core.observables.Event;


public interface IEntity {


  public static final String REQUEST_URL = "reqUrl";


  /**
   * Returns the namespace path associated with this Entity.
   * <br />
   * <b>This method is used by connector</b>
   * @return the namespace
   */
  public String getNamespace();

  /**
   * Returns the tag name associated with this Entity
   * <br />
   * <b>This method is used by parser</b>
   * @return the entity tag name
   */
  public String getTagName();


  /**
   * Returns the unique token associated with this entity
   * @return the entity token
   */
  public String getToken();


  /**
   * Returns the method name associated with given {@code tagName}
   * <br />
   * <b>This method is used by parser</b>
   * <br />
   * <b>Do not use this method for general purpose</b>
   *
   * @param tagName the field tagName found while parsing response content
   * @return the method to invoke
   */
  @Deprecated
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
   * @return the {@link IConfiguration} associated with this {@link IEntity}
   */
  public IConfiguration getConfiguration();

  /**
   * @return path to identify this {@link IEntity}
   */
  public String getApiPath();

  /**
   * @param the {@link IEntity} parent object
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