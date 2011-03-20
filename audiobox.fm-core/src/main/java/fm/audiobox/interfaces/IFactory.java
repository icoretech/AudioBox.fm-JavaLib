package fm.audiobox.interfaces;

import fm.audiobox.AudioBox;

public interface IFactory {

  
  /**
   * Returns the right entity associated with its default class
   * 
   * @param klass default {@link Class}
   * @param configuration the {@link IConfiguration} to pass to {@link IEntity} to be instantiated
   * @return the right {@link IEntity}
   */
  public IEntity getEntity(Class<? extends IEntity> klass, IConfiguration configuration);
  
  /**
   * Sets the right entity associated with its default class
   * @param klass default {@link Class}
   * @param entity new {@link Class} to be instantiated
   */
  public void setEntity(Class<? extends IEntity> klass, Class<? extends IEntity> entity);
  
  /**
   * Sets the global {@link IConnector} associated with this {@link AudioBox} instance
   * <b>NOTE: do not use this method for general purpose</b>
   * 
   * @param connector the global {@link IConnector} associated with this {@link AubioBox} instance
   */
  public void setConnector(IConnector connector);
  
}
