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
 * player: true,
 * local: true,
 * cloud: true,
 * dropbox: true,
 * gdrive: true,
 * skydrive: true,
 * soundcloud: true,
 * youtube: true,
 * box: true,
 * lastfm: true,
 * twitchtv: true,
 * facebook: true,
 * twitter: true
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
    } catch (SecurityException e) {
      log.error("Security error", e);
    } catch (NoSuchMethodException e) {
      log.error("No method found", e);
    }
  }


  public Permissions(IConfiguration config) {
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






  public boolean isPlayer() {
    return player;
  }




  public void setPlayer(boolean player) {
    this.player = player;
  }




  public boolean isLocal() {
    return local;
  }




  public void setLocal(boolean local) {
    this.local = local;
  }




  public boolean isCloud() {
    return cloud;
  }




  public void setCloud(boolean cloud) {
    this.cloud = cloud;
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



  public boolean isMusixmatch() {
    return musixmatch;
  }




  public void setMusixmatch(boolean musixmatch) {
    this.musixmatch = musixmatch;
  }



  public boolean isSongkick() {
    return this.songkick;
  }

  public void setSongkick(boolean songkick) {
    this.songkick = songkick;
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
