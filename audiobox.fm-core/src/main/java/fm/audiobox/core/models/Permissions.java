package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IEntity;

public class Permissions  extends AbstractEntity implements Serializable {

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
