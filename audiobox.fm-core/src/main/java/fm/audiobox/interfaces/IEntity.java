package fm.audiobox.interfaces;

import java.lang.reflect.Method;


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
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException;
  
  
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
  
}
