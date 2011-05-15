package fm.audiobox.interfaces;

import java.util.concurrent.ExecutorService;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Album;
import fm.audiobox.core.models.Artist;
import fm.audiobox.core.models.Genre;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Track;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;

public interface IConfiguration {


  /**
   * Identifies each request and response format type 
   */
  public static enum ContentFormat {
    XML,
    JSON,
    TXT,
    BINARY
  }

  
  
  public boolean hasPlaylist(String token);
  public Playlist getPlaylist(String token);
  public void addPlaylist(Playlist pl);
  
  public boolean hasTrack(String token);
  public Track getTrack(String token);
  public void addTrack(Track tr);
  
  public boolean hasAlbum(String token);
  public Album getAlbum(String token);
  public void addAlbum(Album al);
  
  public boolean hasGenre(String token);
  public Genre getGenre(String token);
  public void addGenre(Genre gr);
  
  public boolean hasArtist(String token);
  public Artist getArtist(String token);
  public void addArtist(Artist ar);
  
  

  /**
   * Sets the default extension for each request
   * @param requestFormat
   */
  public void setRequestFormat(ContentFormat requestFormat);

  /**
   * Returns the default extension for each request
   * @return
   */
  public ContentFormat getRequestFormat();

  /**
   * Retruns current {@link IFactory} associated with this configuration
   * @return {@link IFactoruy}
   */
  public IFactory getFactory();

  /**
   * Sets the application name sent to server as "User-Agent" request header
   * @param appName the application name
   */
  public void setApplicationName(String appName);

  /**
   * Returns the application name sent to server
   * @return
   */
  public String getApplicationName();


  /**
   * Sets the version sent to server as "User-Agent" request header.
   * Default should be 1.0.0
   * 
   * @param major
   * @param minor
   * @param revision
   */
  public void setVersion(int major, int minor, int revision);

  /**
   * Returns the application version sent to server as "User-Agent"
   * @return
   */
  public String getVersion();


  /**
   * Returns the "User-Agent" sent to server
   * @return
   */
  public String getUserAgent();


  /**
   * Returns protocol used for connection
   * @return
   */
  public String getProtocol();

  /**
   * Returns host used for connection
   * @return
   */
  public String getHost();

  /**
   * Returns server port used for connection
   * @return
   */
  public int getPort();


  /**
   * Returns path used for connetion
   * @return
   */
  public String getPath();


  /**
   * Passing true AudioBox will try to use FileSystem to store information as cache
   * @param useChache
   */
  public void setUseCache(boolean useCache);


  /**
   * Returns true if AudioBox is using cache sistem
   * @return
   */
  public boolean isCacheEnabled();


  /**
   * Enables or disables {@code short} request parameter
   * @param shortResponse
   */
  public void setShortResponse(boolean shortResponse);


  /**
   * Returns {@code true} if AudioBox is using the {@code short} request parameter. {@code False} if not
   * @return
   */
  public boolean isShortResponseEnabled();


  /**
   * Sets the default handler for {@link LoginException}
   * @param handler
   */
  public void setDefaultLoginExceptionHandler(ILoginExceptionHandler handler);

  /**
   * Returns the default handler for {@link LoginException}
   * @return
   */
  public ILoginExceptionHandler getDefaultLoginExceptionHandler();
  
  

  /**
   * Sets the default handler for {@link ServiceException}
   * @param handler
   */
  public void setDefaultServiceExceptionHandler(IServiceExceptionHandler handler);

  /**
   * Returns the default handler for {@link ServiceException}
   * @return
   */
  public IServiceExceptionHandler getDefaultServiceExceptionHandler();

  /**
   * Sets the connection method class used for connection
   * @param method
   */
  public void setHttpMethodType(Class<? extends IConnectionMethod> method);

  /**
   * Returns the connection method class used for connection
   * @return
   */
  public Class<? extends IConnectionMethod> getHttpMethodType();

  
  /**
   * Use this method to get the configured {@link ExecutorService}
   * 
   * @return the configured {@link ExecutorService} used for requests
   */
  public ExecutorService getExecutor();
  

  /**
   * Use this method to set the {@link ICacheManager}
   * @param manager the {@link ICacheManager} to set
   */
  public void setCacheManager(ICacheManager manager);
  
  
  /**
   * Use this method to get the configured cache manager
   * @return the {@link ICacheManager} 
   */
  public ICacheManager getCacheManager();
}
