package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;



/**
 * accept_emails: true,
 * autoplay: false,
 * volume_level: 85,
 * color: 'audiobox-fm-blue',
 * top_bar_bg: 'default'
 */


public class Preferences extends AbstractEntity implements Serializable {
  
  
  private static final long serialVersionUID = -4286453333519508598L;
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
  
  
  private boolean accept_emails = false;
  private boolean autoplay = false;
  private int volume_level = 50;
  private String color = "";
  private String top_bar_bg = "";
  private boolean prebuffer;
  private boolean repeat;
  private boolean shuffle;
  
  
  private static final Map<String, Method> setterMethods = new HashMap<String, Method>();
  static {
    try {
      setterMethods.put( ACCEPT_EMAILS, Preferences.class.getMethod( "setAccept_emails", boolean.class )  );
      setterMethods.put( AUTOPLAY, Preferences.class.getMethod( "setAutoplay", boolean.class )  );
      setterMethods.put( VOLUME_LEVEL, Preferences.class.getMethod( "setVolume_level", int.class )  );
      setterMethods.put( COLOR, Preferences.class.getMethod( "setColor", String.class )  );
      setterMethods.put( TOP_BAR_BG, Preferences.class.getMethod( "setTop_bar_bg", String.class )  );
      setterMethods.put( PREBUFFER, Preferences.class.getMethod( "setPrebuffer", boolean.class )  );
      setterMethods.put( REPEAT, Preferences.class.getMethod( "setRepeat", boolean.class )  );
      setterMethods.put( SHUFFLE, Preferences.class.getMethod( "setShuffle", boolean.class )  );
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
  
  
  
  public boolean isAcceptEmails() {
    return accept_emails;
  }


  public void setAccept_emails(boolean accept_emails) {
    this.accept_emails = accept_emails;
  }


  public boolean isAutoplay() {
    return autoplay;
  }


  public void setAutoplay(boolean autoplay) {
    this.autoplay = autoplay;
  }


  public int getVolumeLevel() {
    return volume_level;
  }


  public void setVolume_level(int volume_level) {
    this.volume_level = volume_level;
  }


  public String getColor() {
    return color;
  }


  public void setColor(String color) {
    this.color = color;
  }


  public String getTop_bar_bg() {
    return top_bar_bg;
  }


  public void setTop_bar_bg(String top_bar_bg) {
    this.top_bar_bg = top_bar_bg;
  }
  
  


  public boolean isPrebuffer() {
    return prebuffer;
  }


  public void setPrebuffer(boolean prebuffer) {
    this.prebuffer = prebuffer;
  }


  public boolean isRepeat() {
    return repeat;
  }


  public void setRepeat(boolean repeat) {
    this.repeat = repeat;
  }


  public boolean isShuffle() {
    return shuffle;
  }


  public void setShuffle(boolean shuffle) {
    this.shuffle = shuffle;
  }

  
  public Method getSetterMethod(String tagName) {
    if ( setterMethods.containsKey( tagName) ) {
      return setterMethods.get( tagName );
    }
    return null;
  }  
  
  
  @Override
  public String getApiPath() {
    return null;
  }

  @Override
  protected void copy(IEntity entity) { }
  
  
  @Override
  public IConnectionMethod load(boolean async) throws ServiceException, LoginException {
    return this.load(false, null);
  }

  @Override
  public IConnectionMethod load(boolean async, IResponseHandler responseHandler) throws ServiceException, LoginException {
    throw new ServiceException("method not supported");
  }
  
  
}
