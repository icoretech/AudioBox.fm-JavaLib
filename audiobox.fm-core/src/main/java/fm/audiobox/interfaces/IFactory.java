package fm.audiobox.interfaces;

import fm.audiobox.AudioBox;
import fm.audiobox.core.models.AccountStats;
import fm.audiobox.core.models.ExternalTokens;
import fm.audiobox.core.models.Permissions;
import fm.audiobox.core.models.Preferences;
import fm.audiobox.core.models.User;

public interface IFactory {

  
  public static final String[] EXCLUDED_EXTENDABLE_CLASSES = new String[]{ User.TAGNAME, Permissions.TAGNAME, AccountStats.TAGNAME, Preferences.TAGNAME, ExternalTokens.TAGNAME };
  
  /**
   * Returns the right entity associated with its default class
   * 
   * @param tagName default {@link Class}
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
   * Returns {@code true} if an {@link IEntity} has been set
   * 
   * @param tagName the {@link IEntity} tag name
   * 
   * @return {@code true} if an {@link IEntity} has been set
   */
  public boolean containsEntity(String tagName);
  
  /**
   * Adds a global {@link IConnector} associated with the {@link AudioBox} instance
   * <br />
   * <b>NOTE: do not use this method for general purpose</b>
   * 
   * @param server the name of the server. See the configuration file
   * @param connector the global {@link IConnector} associated with this {@link AudioBox} instance
   */
  public void addConnector(IConfiguration.Connectors server, IConnector connector);
 
  
  /**
   * @return the default {@link IConnector connector} associated with the {@link AudioBox} instance
   */
  public IConnector getConnector();
  
  
  /**
   * @return the global {@link IConnector connector} associated with given {@link IConfiguration.Connectors}
   */
  public IConnector getConnector(IConfiguration.Connectors server);
  
}
