package fm.audiobox.core.models;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.audiobox.core.exceptions.ForbiddenException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

public class Player extends AbstractEntity {
  
  private static final Logger log = LoggerFactory.getLogger( Player.class );
  
  private static final String MEDIA_TOKEN_PARAMETER = "media_token";
  private static final String PLAYLIST_TOKEN_PARAMETER = "playlist_token";
  
  public static final String NAMESPACE = "player";
  public static final String TAGNAME = null;
  
  
  public static enum Actions {
    play,
    pause,
    toggle_play,
    next,
    prev,
    stop,
    show_info,
    playlist_switch,
    switch_and_play,
    toggle_shuffle,
    toggle_repeat
  }
  

  public Player(IConfiguration config) {
    super(config);
  }
  
  
  public Player(User user){
    this( user.getConfiguration() );
  }

  
  @Override
  public String getNamespace() {
    return NAMESPACE;
  }


  /**
   * This entity has not tagname
   */
  @Deprecated
  public String getTagName() {
    return null;
  }

  /**
   * This entity has not methods to set
   */
  @Deprecated
  public Method getSetterMethod(String tagName) {
    return null;
  }
  
  
  /**
   * This entity has not methods to get
   */
  @Deprecated
  public Map<String, Method> getGetterMethods() {
    return new HashMap<String, Method>();
  }
  

  @Override
  public String getApiPath() {
    return IConnector.URI_SEPARATOR + NAMESPACE;
  }

  /**
   * This entity has not data to copy
   */
  @Deprecated
  protected void copy(IEntity entity) {
    
  }

  @Override
  public IConnectionMethod load(boolean async) throws ServiceException, LoginException {
    throw new ServiceException("no data to load");
  }

  @Override
  public IConnectionMethod load(boolean async, IResponseHandler responseHandler) throws ServiceException, LoginException {
    throw new ServiceException("no data to load");
  }

  protected List<NameValuePair> toQueryParameters(boolean full) {
    log.warn("No data to serialize");
    return null;
  }

  
  
  public boolean play(MediaFile mediafile) throws ServiceException, LoginException, ForbiddenException {
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair( MEDIA_TOKEN_PARAMETER, mediafile.getToken() ) );
    
    return executeAction(Actions.play, params);
  }
  
  public boolean pause() throws ServiceException, LoginException, ForbiddenException {
    return executeAction(Actions.pause, null);
  }
  
  public boolean togglePlay() throws ServiceException, LoginException, ForbiddenException {
    return executeAction(Actions.toggle_play, null);
  }

  public boolean next() throws ServiceException, LoginException, ForbiddenException {
    return executeAction(Actions.next, null);
  }
  
  public boolean prev() throws ServiceException, LoginException, ForbiddenException {
    return executeAction(Actions.prev, null);
  }
  
  public boolean stop() throws ServiceException, LoginException, ForbiddenException {
    return executeAction(Actions.stop, null);
  }
  
  public boolean toggleShuffle() throws ServiceException, LoginException, ForbiddenException {
    return executeAction(Actions.toggle_shuffle, null);
  }
  
  public boolean toggleRepeat() throws ServiceException, LoginException, ForbiddenException {
    return executeAction(Actions.toggle_repeat, null);
  }
  
  public boolean showInfo(MediaFile mediafile) throws ServiceException, LoginException, ForbiddenException {
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair( MEDIA_TOKEN_PARAMETER, mediafile.getToken() ) );
    
    return executeAction(Actions.show_info, params);
  }
  
  public boolean switchPlaylist(Playlist playlist) throws ServiceException, LoginException, ForbiddenException {
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair( PLAYLIST_TOKEN_PARAMETER, playlist.getToken() ) );
    return executeAction(Actions.playlist_switch, params);
  }
  
  public boolean switchAndPlay(Playlist playlist, MediaFile mediafile) throws ServiceException, LoginException, ForbiddenException {
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    
    params.add(new BasicNameValuePair( PLAYLIST_TOKEN_PARAMETER, playlist.getToken() ) );
    params.add(new BasicNameValuePair( MEDIA_TOKEN_PARAMETER, mediafile.getToken() ) );
    
    return executeAction(Actions.switch_and_play, params);
  }
  
  private boolean executeAction(Player.Actions action, List<NameValuePair> params) throws ServiceException, LoginException, ForbiddenException {
    
    IConnectionMethod request = this.getConnector(IConfiguration.Connectors.RAILS).get(this, IConnector.URI_SEPARATOR + NAMESPACE, action.toString(), params);
    
    return request.send(false).isOK();
  }
  
  
}
