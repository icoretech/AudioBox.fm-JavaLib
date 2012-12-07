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
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;


/**
 * dropbox: false,
 * gdrive: true,
 * skydrive: false,
 * soundcloud: false,
 * youtube: true,
 * box: false,
 * lastfm: false,
 * twitchtv: false,
 * facebook: false,
 * twitter: false
 */

public final class ExternalTokens extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private static Logger log = LoggerFactory.getLogger(ExternalTokens.class);
  
  public static final String NAMESPACE = "external_tokens";
  public static final String TAGNAME = NAMESPACE;
  
  public static final String DROPBOX = "dropbox";
  public static final String GDRIVE = "gdrive";
  public static final String SKYDRIVE = "skydrive";
  public static final String SOUNDCLOUD = "soundcloud";
  public static final String YOUTUBE = "youtube";
  public static final String BOX = "box";
  public static final String LASTFM = "lastfm";
  public static final String TWITCHTV = "twitchtv";
  public static final String FACEBOOK = "facebook";
  public static final String TWITTER = "twitter";
  
  
  private boolean dropbox = false;
  private boolean gdrive = false;
  private boolean skydrive = false;
  private boolean soundcloud = false;
  private boolean youtube = false;
  private boolean box = false;
  private boolean lastfm = false;
  private boolean twitchtv = false;
  private boolean facebook = false;
  private boolean twitter = false;
  
  
  
  
  private static final Map<String, Method> setterMethods = new HashMap<String, Method>();
  static {
    try {
      setterMethods.put( DROPBOX, ExternalTokens.class.getMethod( "setDropbox" , boolean.class ) );
      setterMethods.put( GDRIVE, ExternalTokens.class.getMethod( "setGdrive" , boolean.class ) );
      setterMethods.put( SKYDRIVE, ExternalTokens.class.getMethod( "setSkydrive" , boolean.class ) );
      setterMethods.put( SOUNDCLOUD, ExternalTokens.class.getMethod( "setSoundcloud" , boolean.class ) );
      setterMethods.put( YOUTUBE, ExternalTokens.class.getMethod( "setYoutube" , boolean.class ) );
      setterMethods.put( BOX, ExternalTokens.class.getMethod( "setBox" , boolean.class ) );
      setterMethods.put( LASTFM, ExternalTokens.class.getMethod( "setLastfm" , boolean.class ) );
      setterMethods.put( TWITCHTV, ExternalTokens.class.getMethod( "setTwitchtv" , boolean.class ) );
      setterMethods.put( FACEBOOK, ExternalTokens.class.getMethod( "setFacebook" , boolean.class ) );
      setterMethods.put( TWITTER, ExternalTokens.class.getMethod( "setTwitter" , boolean.class ) );
    } catch (SecurityException e) {
      log.error("Security error", e);
    } catch (NoSuchMethodException e) {
      log.error("No method found", e);
    }
  }
  
  
  public ExternalTokens(IConfiguration config) {
    super(config);
  }


  @Override
  public String getNamespace() {
    return NAMESPACE;
  }

  @Override
  public String getTagName() {
    return TAGNAME;
  }
  
  
  public boolean isDropbox() {
    return dropbox;
  }


  public void setDropbox(boolean dropbox) {
    this.dropbox = dropbox;
  }


  public boolean isGdrive() {
    return gdrive;
  }


  public void setGdrive(boolean gdrive) {
    this.gdrive = gdrive;
  }


  public boolean isSkydrive() {
    return skydrive;
  }


  public void setSkydrive(boolean skydrive) {
    this.skydrive = skydrive;
  }


  public boolean isSoundcloud() {
    return soundcloud;
  }


  public void setSoundcloud(boolean soundcloud) {
    this.soundcloud = soundcloud;
  }


  public boolean isYoutube() {
    return youtube;
  }


  public void setYoutube(boolean youtube) {
    this.youtube = youtube;
  }


  public boolean isBox() {
    return box;
  }


  public void setBox(boolean box) {
    this.box = box;
  }


  public boolean isLastfm() {
    return lastfm;
  }


  public void setLastfm(boolean lastfm) {
    this.lastfm = lastfm;
  }


  public boolean isTwitchtv() {
    return twitchtv;
  }


  public void setTwitchtv(boolean twitchtv) {
    this.twitchtv = twitchtv;
  }


  public boolean isFacebook() {
    return facebook;
  }


  public void setFacebook(boolean facebook) {
    this.facebook = facebook;
  }


  public boolean isTwitter() {
    return twitter;
  }


  public void setTwitter(boolean twitter) {
    this.twitter = twitter;
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
  public IConnectionMethod load(boolean sync) throws ServiceException, LoginException {
    return this.load(false, null);
  }

  @Override
  public IConnectionMethod load(boolean sync, IResponseHandler responseHandler) throws ServiceException, LoginException {
    throw new ServiceException("method not supported");
  }
  
}
