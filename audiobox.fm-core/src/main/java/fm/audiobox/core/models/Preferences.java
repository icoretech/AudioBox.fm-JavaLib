package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;



/**
 * accept_emails: true,
 * autoplay: false,
 * volume_level: 85,
 * color: 'audiobox-fm-blue',
 * top_bar_bg: 'default'
 */


public class Preferences extends AbstractEntity implements Serializable {
  
  
  
  private static final long serialVersionUID = -4286453333519508598L;
  
  
  public static final String NAMESPACE = "preferences";
  public static final String TAGNAME = NAMESPACE;
  
  
  
  private boolean accept_emails = false;
  private boolean autoplay = false;
  private int volume_level = 50;
  private String color = "";
  private String top_bar_bg = "";
  private boolean prebuffer;
  private boolean repeat;
  private boolean shuffle;
  
  
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


  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    
    if ( tagName.equals("accept_emails") ) {
      return this.getClass().getMethod("setAccept_emails", boolean.class);

    } else if ( tagName.equals("autoplay") ) {
      return this.getClass().getMethod("setAutoplay", boolean.class);

    } else if ( tagName.equals("volume_level") ) {
      return this.getClass().getMethod("setVolume_level", int.class);

    } else if ( tagName.equals("color") ) {
      return this.getClass().getMethod("setColor", String.class);

    } else if ( tagName.equals("top_bar_bg") ) {
      return this.getClass().getMethod("setTop_bar_bg", String.class);

    } else if ( tagName.equals("prebuffer") ) {
      return this.getClass().getMethod("setPrebuffer", boolean.class);

    } else if ( tagName.equals("repeat") ) {
      return this.getClass().getMethod("setRepeat", boolean.class);

    } else if ( tagName.equals("shuffle") ) {
      return this.getClass().getMethod("setShuffle", boolean.class);

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
