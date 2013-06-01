package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

/**
 * This class identifies all {@code permissions} related to this {@link User} instance.
 * <p>
 *  Each flag identifies single permission which user has been enabled to
 * </p>
 */
public final class Permissions extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private static final Logger log = LoggerFactory.getLogger(Permissions.class);

  public static final String NAMESPACE = "permissions";
  public static final String TAGNAME = NAMESPACE;

  public static final String PLAYER = "player";
  public static final String LOCAL = "local";
  public static final String CLOUD = "cloud";
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
  public static final String MUSIXMATCH = "musixmatch";
  public static final String SONGKICK = "songkick";
  public static final String UBUNTU = "ubuntu";

  
  private boolean player = true;
  private boolean local = true;
  private boolean cloud = true;
  private boolean dropbox = true;
  private boolean gdrive = true;
  private boolean skydrive = true;
  private boolean soundcloud = true;
  private boolean youtube = true;
  private boolean box = true;
  private boolean lastfm = true;
  private boolean twitchtv = true;
  private boolean facebook = true;
  private boolean twitter = true;
  private boolean musixmatch = true;
  private boolean songkick = true;
  private boolean ubuntu = true;


  private static final Map<String, Method> setterMethods = new HashMap<String, Method>();
  static {
    try {
      setterMethods.put( PLAYER,  Permissions.class.getMethod( "setPlayer", boolean.class )  );
      setterMethods.put( LOCAL,  Permissions.class.getMethod( "setLocal", boolean.class )  );
      setterMethods.put( CLOUD,  Permissions.class.getMethod( "setCloud", boolean.class )  );
      setterMethods.put( DROPBOX,  Permissions.class.getMethod( "setDropbox", boolean.class )  );
      setterMethods.put( GDRIVE,  Permissions.class.getMethod( "setGdrive", boolean.class )  );
      setterMethods.put( SKYDRIVE,  Permissions.class.getMethod( "setSkydrive", boolean.class )  );
      setterMethods.put( SOUNDCLOUD,  Permissions.class.getMethod( "setSoundcloud", boolean.class )  );
      setterMethods.put( YOUTUBE,  Permissions.class.getMethod( "setYoutube", boolean.class )  );
      setterMethods.put( BOX,  Permissions.class.getMethod( "setBox", boolean.class )  );
      setterMethods.put( LASTFM,  Permissions.class.getMethod( "setLastfm", boolean.class )  );
      setterMethods.put( TWITCHTV,  Permissions.class.getMethod( "setTwitchtv", boolean.class )  );
      setterMethods.put( FACEBOOK,  Permissions.class.getMethod( "setFacebook", boolean.class )  );
      setterMethods.put( TWITTER,  Permissions.class.getMethod( "setTwitter", boolean.class )  );
      setterMethods.put( MUSIXMATCH,  Permissions.class.getMethod( "setMusixmatch", boolean.class )  );
      setterMethods.put( SONGKICK,  Permissions.class.getMethod( "setSongkick", boolean.class )  );
      setterMethods.put( UBUNTU,  Permissions.class.getMethod( "setUbuntu", boolean.class )  );
    } catch (SecurityException e) {
      log.error("Security error", e);
    } catch (NoSuchMethodException e) {
      log.error("No method found", e);
    }
  }


  public Permissions(IConfiguration config) {
    super(config);
  }



  public String getNamespace() {
    return NAMESPACE;
  }

  public String getTagName() {
    return TAGNAME;
  }


  /**
   * @return {@code true} when {@link User} is able to use the Cloud Web Player. {@code false} if not
   */
  public boolean isPlayer() {
    return player;
  }



  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setPlayer(boolean player) {
    this.player = player;
  }


  /**
   * @return {@code true} when {@link User} is able to use the AudioBox.fm Desktop drive. {@code false} if not
   */
  public boolean isLocal() {
    return local;
  }



  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setLocal(boolean local) {
    this.local = local;
  }



  /**
   * @return {@code true} when {@link User} is able to use the AudioBox.fm Cloud drive. {@code false} if not
   */
  public boolean isCloud() {
    return cloud;
  }



  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setCloud(boolean cloud) {
    this.cloud = cloud;
  }



  /**
   * @return {@code true} when {@link User} is able to use the Dropbox drive. {@code false} if not
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
   * @return {@code true} when {@link User} is able to use the GDrive drive. {@code false} if not
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
   * @return {@code true} when {@link User} is able to use the MusixMatch feature. {@code false} if not
   */
  public boolean isMusixmatch() {
    return musixmatch;
  }



  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setMusixmatch(boolean musixmatch) {
    this.musixmatch = musixmatch;
  }


  /**
   * @return {@code true} when {@link User} is able to use the Songkick feature. {@code false} if not
   */
  public boolean isSongkick() {
    return this.songkick;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setSongkick(boolean songkick) {
    this.songkick = songkick;
  }


  /**
   * @return {@code true} when {@link User} is able to use the Skydrive drive. {@code false} if not
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
   * @return {@code true} when {@link User} is able to use the Soundcloud drive. {@code false} if not
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
   * @return {@code true} when {@link User} is able to use the Youtube drive. {@code false} if not
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
   * @return {@code true} when {@link User} is able to use the Box.net drive. {@code false} if not
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
   * @return {@code true} when {@link User} is able to use the Last.fm feature. {@code false} if not
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
   * @return {@code true} when {@link User} is able to use the TwitchTv feature. {@code false} if not
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
   * @return {@code true} when {@link User} is able to use the Facebook feature. {@code false} if not
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
   * @return {@code true} when {@link User} is able to use the Ubuntu feature. {@code false} if not
   */
  public boolean isUbuntu() {
    return ubuntu;
  }


  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setUbuntu(boolean ubuntu) {
    this.ubuntu = ubuntu;
  }


  /**
   * @return {@code true} when {@link User} is able to use the Twitter feature. {@code false} if not
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
    return new ArrayList<NameValuePair>();
  }

}
