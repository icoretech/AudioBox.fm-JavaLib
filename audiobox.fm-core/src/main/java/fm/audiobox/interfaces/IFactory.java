package fm.audiobox.interfaces;

public interface IFactory {

  
  /**
   * Returns the correct entity associated with name
   * @param name
   * @return
   */
  public IEntity getEntity(String name);
  
  
  
  /**
   * Sets the correct entity assicated with name
   * @param name
   * @param entity
   */
  public void setEntity(String name, Class<? extends IEntity> entity);
  
  
  
}
