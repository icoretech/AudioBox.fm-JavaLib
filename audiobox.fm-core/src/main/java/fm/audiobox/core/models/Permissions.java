package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IEntity;

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

  public static final String NAMESPACE = "permissions";
  public static final String TAGNAME = NAMESPACE;
  
  
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




  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    
    if ( tagName.equals("player") ) {

      return this.getClass().getMethod("setPlayer", boolean.class);

    } else if ( tagName.equals("local") ) {

      return this.getClass().getMethod("setLocal", boolean.class);

    } else if ( tagName.equals("cloud") ) {

      return this.getClass().getMethod("setCloud", boolean.class);

    } else if ( tagName.equals("dropbox") ) {

      return this.getClass().getMethod("setDropbox", boolean.class);

    } else if ( tagName.equals("gdrive") ) {

      return this.getClass().getMethod("setGdrive", boolean.class);

    } else if ( tagName.equals("skydrive") ) {

      return this.getClass().getMethod("setSkydrive", boolean.class);

    } else if ( tagName.equals("soundcloud") ) {

      return this.getClass().getMethod("setSoundcloud", boolean.class);

    } else if ( tagName.equals("youtube") ) {

      return this.getClass().getMethod("setYoutube", boolean.class);

    } else if ( tagName.equals("box") ) {

      return this.getClass().getMethod("setBox", boolean.class);

    } else if ( tagName.equals("lastfm") ) {

      return this.getClass().getMethod("setLastfm", boolean.class);

    } else if ( tagName.equals("twitchtv") ) {

      return this.getClass().getMethod("setTwitchtv", boolean.class);

    } else if ( tagName.equals("facebook") ) {

      return this.getClass().getMethod("setFacebook", boolean.class);

    } else if ( tagName.equals("twitter") ) {

      return this.getClass().getMethod("setTwitter", boolean.class);

    } else if ( tagName.equals("musixmatch") ) {

      return this.getClass().getMethod("setMusixmatch", boolean.class);

    }
    
    
    return null;
  }

  @Override
  public String getApiPath() {
    return null;
  }

  @Override
  public void setParent(IEntity parent) { }

  @Override
  protected void copy(IEntity entity) { }
  
  
  
  
}
