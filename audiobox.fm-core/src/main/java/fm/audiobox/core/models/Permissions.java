package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IEntity;

public final class Permissions  extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 3196523340942887761L;

  public static final String NAMESPACE = "permissions";
  public static final String TAGNAME = NAMESPACE;
  
  
  private boolean cloud;
  private boolean local;
  private boolean dropbox;
  private boolean soundcloud;
  private boolean youtube;
  private boolean gdrive;
  private boolean skydrive;
  
  private boolean box;
  private boolean lastfm;
  private boolean partner;
  
  
  public Permissions(IConfiguration config) {
    super(config);
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

  public boolean isPartner() {
    return partner;
  }

  public void setPartner(boolean partner) {
    this.partner = partner;
  }


  @Override
  public String getNamespace() {
    return NAMESPACE;
  }

  @Override
  public String getTagName() {
    return TAGNAME;
  }

  
  public boolean isCloud() {
    return cloud;
  }

  public void setCloud(boolean cloud) {
    this.cloud = cloud;
  }
  
  public boolean isLocal() {
    return local;
  }

  public void setLocal(boolean local) {
    this.local = local;
  }

  public boolean isDropbox() {
    return dropbox;
  }

  public void setDropbox(boolean dropbox) {
    this.dropbox = dropbox;
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
  
  
  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    
    if ( tagName.equals("cloud") ){
      return this.getClass().getMethod("setCloud", boolean.class);

    } else if ( tagName.equals("local") ){
      return this.getClass().getMethod("setLocal", boolean.class);

    } else if ( tagName.equals("dropbox") ){
      return this.getClass().getMethod("setDropbox", boolean.class);

    } else if ( tagName.equals("soundcloud") ){
      return this.getClass().getMethod("setSoundcloud", boolean.class);

    } else if ( tagName.equals("youtube") ){
      return this.getClass().getMethod("setYoutube", boolean.class);

    } else if ( tagName.equals("gdrive") ){
      return this.getClass().getMethod("setGdrive", boolean.class);

    } else if ( tagName.equals("skydrive") ){
      return this.getClass().getMethod("setSkydrive", boolean.class);

    } else if ( tagName.equals("box") ){
      return this.getClass().getMethod("setBox", boolean.class);

    } else if ( tagName.equals("lastfm") ){
      return this.getClass().getMethod("setLastfm", boolean.class);

    } else if ( tagName.equals("partner") ){
      return this.getClass().getMethod("setPartner", boolean.class);

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
