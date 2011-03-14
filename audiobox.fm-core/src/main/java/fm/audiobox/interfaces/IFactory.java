package fm.audiobox.interfaces;

import fm.audiobox.AudioBox;

public interface IFactory {

  
  /**
   * Returns the correct entity associated with name
   * @param name
   * @return
   */
  public IEntity getEntity(String tagName, IConfiguration configuration);
  
  /**
   * Sets the correct entity assicated with name
   * @param name
   * @param entity
   */
  public void setEntity(String tagName, Class<? extends IEntity> entity);
  
  /**
   * Sets the global {@link IConnector} associated with this {@link AudioBox} instance
   * <b>NOTE: do not use this method for general purpose</b>
   * 
   * @param connector the global {@link IConnector} associated with this {@link AubioBox} instance
   */
  public void setConnector(IConnector connector);
  
}
