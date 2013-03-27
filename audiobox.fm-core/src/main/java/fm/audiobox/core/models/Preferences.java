package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

/**
 * This class contains all user's preferences
 */
public class Preferences extends AbstractEntity implements Serializable {
  
  
  private static final long serialVersionUID = 1L;
  private static Logger log = LoggerFactory.getLogger(Preferences.class);
  
  
  public static final String NAMESPACE = "preferences";
  public static final String TAGNAME = NAMESPACE;
  
  public static final String ACCEPT_EMAILS = "accept_emails";
  public static final String AUTOPLAY = "autoplay";
  public static final String VOLUME_LEVEL = "volume_level";
  public static final String COLOR = "color";
  public static final String TOP_BAR_BG = "top_bar_bg";
  public static final String PREBUFFER = "prebuffer";
  public static final String REPEAT = "repeat";
  public static final String SHUFFLE = "shuffle";
  public static final String DEMUXER = "js_demuxer";
  
  
  private boolean accept_emails = false;
  private boolean autoplay = false;
  private int volume_level = 50;
  private String color = "";
  private String top_bar_bg = "";
  private boolean prebuffer;
  private boolean repeat;
  private boolean shuffle;
  private boolean js_demuxer;
  
  
  private static final Map<String, Method> setterMethods = new HashMap<String, Method>();
  static {
    try {
      setterMethods.put( ACCEPT_EMAILS, Preferences.class.getMethod( "setAcceptEmails", boolean.class )  );
      setterMethods.put( AUTOPLAY, Preferences.class.getMethod( "setAutoplay", boolean.class )  );
      setterMethods.put( VOLUME_LEVEL, Preferences.class.getMethod( "setVolumeLevel", int.class )  );
      setterMethods.put( COLOR, Preferences.class.getMethod( "setColor", String.class )  );
      setterMethods.put( TOP_BAR_BG, Preferences.class.getMethod( "setTopBarBg", String.class )  );
      setterMethods.put( PREBUFFER, Preferences.class.getMethod( "setPrebuffer", boolean.class )  );
      setterMethods.put( REPEAT, Preferences.class.getMethod( "setRepeat", boolean.class )  );
      setterMethods.put( SHUFFLE, Preferences.class.getMethod( "setShuffle", boolean.class )  );
      setterMethods.put( DEMUXER, Preferences.class.getMethod( "setDemuxer", boolean.class )  );
    } catch (SecurityException e) {
      log.error("Security error", e);
    } catch (NoSuchMethodException e) {
      log.error("No method found", e);
    }
  }
  
  
  public Preferences(IConfiguration config) {
    super(config);
  }
  

  public String getNamespace() {
    return NAMESPACE;
  }

  public String getTagName() {
    return TAGNAME;
  }
  
  
  /**
   * @return {@code true} when {@link User} wants to receive emails. {@code false} if not
   */
  public boolean isAcceptEmails() {
    return accept_emails;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setAcceptEmails(boolean accept_emails) {
    this.accept_emails = accept_emails;
  }


  /**
   * @return {@code true} if {@link User} has enabled the autoplay feature in Cloud Web Player. {@code false} if not
   */
  public boolean isAutoplay() {
    return autoplay;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setAutoplay(boolean autoplay) {
    this.autoplay = autoplay;
  }


  /**
   * @return last {@code volume level} set in Cloud Web Player
   */
  public int getVolumeLevel() {
    return volume_level;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setVolumeLevel(int volume_level) {
    this.volume_level = volume_level;
  }


  /**
   * @return the Cloud Web Player {@code color} theme set
   */
  public String getColor() {
    return color;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setColor(String color) {
    this.color = color;
  }


  /**
   * @return the Cloud Web Player top bar {@code background image} set
   */
  public String getTopBarBg() {
    return top_bar_bg;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setTopBarBg(String top_bar_bg) {
    this.top_bar_bg = top_bar_bg;
  }
  
  
  /**
   * @return {@code true} if {@link User} has enabled the {@code prebuffer} feature in Cloud Web Player
   */
  public boolean isPrebuffer() {
    return prebuffer;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setPrebuffer(boolean prebuffer) {
    this.prebuffer = prebuffer;
  }

  /**
   * @return {@code true} if {@link User} has enabled the {@code repeat} feature in Cloud Web Player
   */
  public boolean isRepeat() {
    return repeat;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setRepeat(boolean repeat) {
    this.repeat = repeat;
  }


  /**
   * @return {@code true} if {@link User} has enabled the {@code shuffle} feature in Cloud Web Player
   */
  public boolean isShuffle() {
    return shuffle;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setShuffle(boolean shuffle) {
    this.shuffle = shuffle;
  }

  
  
  /**
   * @return {@code true} if {@link User} has enabled the {@code demuxer} feature in Cloud Web Player
   */
  public boolean isDemuxer() {
    return js_demuxer;
  }


  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setDemuxer(boolean js_demuxer) {
    this.js_demuxer = js_demuxer;
  }


  public Method getSetterMethod(String tagName) {
    if ( setterMethods.containsKey( tagName) ) {
      return setterMethods.get( tagName );
    }
    return null;
  }  
  
  
  public String getApiPath() {
    return null;
  }

  protected void copy(IEntity entity) { }
  
  
  public IConnectionMethod load(boolean async) throws ServiceException, LoginException {
    return this.load(false, null);
  }

  public IConnectionMethod load(boolean async, IResponseHandler responseHandler) throws ServiceException, LoginException {
    throw new ServiceException("method not supported");
  }
  
  
  protected List<NameValuePair> toQueryParameters(boolean all) {
    String prefix = TAGNAME + "[";
    String suffix = "]";
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();

    params.add(new BasicNameValuePair(prefix + ACCEPT_EMAILS + suffix, String.valueOf( this.accept_emails ) )  );
    params.add(new BasicNameValuePair(prefix + AUTOPLAY + suffix, String.valueOf( this.autoplay ) )  );
    params.add(new BasicNameValuePair(prefix + VOLUME_LEVEL + suffix, String.valueOf( this.volume_level ) )  );
    params.add(new BasicNameValuePair(prefix + COLOR + suffix, this.color)  );
    params.add(new BasicNameValuePair(prefix + TOP_BAR_BG + suffix, this.top_bar_bg)  );
    params.add(new BasicNameValuePair(prefix + PREBUFFER + suffix, String.valueOf( this.prebuffer ) )  );
    params.add(new BasicNameValuePair(prefix + REPEAT + suffix, String.valueOf( this.repeat ) )  );
    params.add(new BasicNameValuePair(prefix + SHUFFLE + suffix, String.valueOf( this.shuffle ) )  );
    
    return params;
  }
  
}
