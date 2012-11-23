package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IEntity;


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
  
  public static final String NAMESPACE = "external_tokens";
  public static final String TAGNAME = NAMESPACE;
  
  
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


  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    
    if ( tagName.equals("dropbox") ) {
      
      return this.getClass().getMethod("setDropbox" , boolean.class);
      
    } else if ( tagName.equals("gdrive") ) {
      
      return this.getClass().getMethod("setGdrive" , boolean.class);
      
    } else if ( tagName.equals("skydrive") ) {
      
      return this.getClass().getMethod("setSkydrive" , boolean.class);
      
    } else if ( tagName.equals("soundcloud") ) {
      
      return this.getClass().getMethod("setSoundcloud" , boolean.class);
      
    } else if ( tagName.equals("youtube") ) {
      
      return this.getClass().getMethod("setYoutube" , boolean.class);
      
    } else if ( tagName.equals("box") ) {
      
      return this.getClass().getMethod("setBox" , boolean.class);
      
    } else if ( tagName.equals("lastfm") ) {
      
      return this.getClass().getMethod("setLastfm" , boolean.class);
      
    } else if ( tagName.equals("twitchtv") ) {
      
      return this.getClass().getMethod("setTwitchtv" , boolean.class);
      
    } else if ( tagName.equals("facebook") ) {
      
      return this.getClass().getMethod("setFacebook" , boolean.class);
      
    } else if ( tagName.equals("twitter") ) {
      
      return this.getClass().getMethod("setTwitter" , boolean.class);
      
    }
    
    return null;
  }
  
  
  @Override
  public String getApiPath() {
    return null;
  }

  @Override
  protected void copy(IEntity entity) { }
  
  
}
