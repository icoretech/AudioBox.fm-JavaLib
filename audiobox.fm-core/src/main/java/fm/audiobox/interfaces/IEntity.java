package fm.audiobox.interfaces;

public interface IEntity {

  
  /**
   * Returns the endpoint path name
   * @return
   */
  public String getEndpoint();
  
  
  
  
  /**
   * Returns the unique name associtated with this entity
   * @return
   */
  public String getName();
  
  
  /**
   * Returns the unique token associated with this entity
   * @return
   */
  public String getToken();
  
  
  
  
}
