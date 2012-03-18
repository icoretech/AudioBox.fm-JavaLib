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
  public IEntity getEntity(String tagName, IConfiguration configuration);
  
  /**
   * Sets the right entity associated with its default class
   * 
   * @param tagName the name of the tag to associate to the class
   * @param entityClass new {@link Class} to be instantiated
   */
  public void setEntity(String tagName, Class<? extends IEntity> entityClass);
  
  /**
   * Returns true if an {@link IEntity} has been set
   * 
   * @param tagName the {@link IEntity} tag name
   * 
   * @return true if an {@link IEntity} has been set
   */
  public boolean containsEntity(String tagName);
  
  /**
   * Adds a global {@link IConnector} associated with the {@link AudioBox} instance
   * <b>NOTE: do not use this method for general purpose</b>
   * 
   * @param server the name of the server. See the configuration file
   * @param connector the global {@link IConnector} associated with this {@link AubioBox} instance
   */
  public void addConnector(IConfiguration.Connectors server, IConnector connector);
 
  
  /**
   * Returns the general global {@link IConnector connector} associated with the {@link AudioBox} instance
   * @return the general {@link IConnector connector} 
   */
  public IConnector getConnector();
  
  
  /**
   * Returns the global {@link IConnector connector} associated with given {@link IConfiguration.Connectors}
   * @return the general {@link IConnector connector} 
   */
  public IConnector getConnector(IConfiguration.Connectors server);
  
}
