package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;

public class ArtWorks extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 7759677446413626968L;
  
  public static final String NAMESPACE = "artworks";
  public static final String TAGNAME = NAMESPACE;
  
  public static final String L_FIELD = "l";
  public static final String S_FIELD = "s";
  
  
  private String large;
  private String small;

  private IEntity parent;
  
  public ArtWorks(IConfiguration config) {
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

  
  public String getLarge() {
    return large;
  }

  public void setLarge(String large) {
    this.large = large;
  }

  public String getSmall() {
    return small;
  }

  public void setSmall(String small) {
    this.small = small;
  }
  
  
  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    if ( tagName.equals( L_FIELD ) ){
      return this.getClass().getMethod("setLarge", String.class);
    } else if ( tagName.equals( S_FIELD ) ){
      return this.getClass().getMethod("setSmall", String.class);
    }
    return null;
  }

  @Override
  public String getApiPath() {
    return this.parent.getApiPath() + IConnector.URI_SEPARATOR + this.getNamespace();
  }

  @Override
  public void setParent(IEntity parent) {
    this.parent = parent;
  }

  @Override
  protected void copy(IEntity entity) {
    
  }


}
