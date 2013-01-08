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
 * This class indentifies each external token which {@link User} has enabled.
 * <p>
 *  In other words, this class represents each feature user has enabled on his account
 * </p> 
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

  public String getNamespace() {
    return NAMESPACE;
  }

  public String getTagName() {
    return TAGNAME;
  }

  /**
   * @return {@code true} if Dropbox drive is available for this {@link User}. {@code false} if not
   */
  public boolean isDropbox() {
    return dropbox;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setDropbox(boolean dropbox) {
    this.dropbox = dropbox;
  }

  
  /**
   * @return {@code true} if GDrive drive is available for this {@link User}. {@code false} if not
   */
  public boolean isGdrive() {
    return gdrive;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setGdrive(boolean gdrive) {
    this.gdrive = gdrive;
  }

  
  /**
   * @return {@code true} if Skydrive drive is available for this {@link User}. {@code false} if not
   */
  public boolean isSkydrive() {
    return skydrive;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setSkydrive(boolean skydrive) {
    this.skydrive = skydrive;
  }

  /**
   * @return {@code true} if Soundcloud drive is available for this {@link User}. {@code false} if not
   */
  public boolean isSoundcloud() {
    return soundcloud;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setSoundcloud(boolean soundcloud) {
    this.soundcloud = soundcloud;
  }

  
  /**
   * @return {@code true} if Youtube feature is available for this {@link User}. {@code false} if not
   */
  public boolean isYoutube() {
    return youtube;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setYoutube(boolean youtube) {
    this.youtube = youtube;
  }

  
  /**
   * @return {@code true} if Box.net drive is available for this {@link User}. {@code false} if not
   */
  public boolean isBox() {
    return box;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setBox(boolean box) {
    this.box = box;
  }

  
  /**
   * @return {@code true} if Last.fm feature is available for this {@link User}. {@code false} if not
   */
  public boolean isLastfm() {
    return lastfm;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setLastfm(boolean lastfm) {
    this.lastfm = lastfm;
  }

  
  /**
   * @return {@code true} if TwitchTv feature is available for this {@link User}. {@code false} if not
   */
  public boolean isTwitchtv() {
    return twitchtv;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setTwitchtv(boolean twitchtv) {
    this.twitchtv = twitchtv;
  }

  
  /**
   * @return {@code true} if Facebook feature is available for this {@link User}. {@code false} if not
   */
  public boolean isFacebook() {
    return facebook;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setFacebook(boolean facebook) {
    this.facebook = facebook;
  }

  
  /**
   * @return {@code true} if Twitter feature is available for this {@link User}. {@code false} if not
   */
  public boolean isTwitter() {
    return twitter;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setTwitter(boolean twitter) {
    this.twitter = twitter;
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
  
  
  public IConnectionMethod load(boolean sync) throws ServiceException, LoginException {
    return this.load(false, null);
  }

  public IConnectionMethod load(boolean sync, IResponseHandler responseHandler) throws ServiceException, LoginException {
    throw new ServiceException("method not supported");
  }
  
  
  protected List<NameValuePair> toQueryParameters(boolean all) {
    String prefix = TAGNAME + "[";
    String suffix = "]";
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();

    params.add(new BasicNameValuePair(prefix + DROPBOX + suffix, String.valueOf( this.dropbox ) )  );
    params.add(new BasicNameValuePair(prefix + GDRIVE + suffix, String.valueOf( this.gdrive ) )  );
    params.add(new BasicNameValuePair(prefix + SKYDRIVE + suffix, String.valueOf( this.skydrive ) )  );
    params.add(new BasicNameValuePair(prefix + SOUNDCLOUD + suffix, String.valueOf( this.soundcloud ) )  );
    params.add(new BasicNameValuePair(prefix + YOUTUBE + suffix, String.valueOf( this.youtube ) )  );
    params.add(new BasicNameValuePair(prefix + BOX + suffix, String.valueOf( this.box ) )  );
    params.add(new BasicNameValuePair(prefix + LASTFM + suffix, String.valueOf( this.lastfm ) )  );
    params.add(new BasicNameValuePair(prefix + TWITCHTV + suffix, String.valueOf( this.twitchtv ) )  );
    params.add(new BasicNameValuePair(prefix + FACEBOOK + suffix, String.valueOf( this.facebook ) )  );
    params.add(new BasicNameValuePair(prefix + TWITTER + suffix, String.valueOf( this.twitter ) )  );
    
    return params;
  }

}
