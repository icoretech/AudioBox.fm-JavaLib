package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;

public class Albums extends AbstractCollectionEntity<Album> implements Serializable {

  private static final long serialVersionUID = 1L;
  
  public static final String NAMESPACE = "albums";
  public static final String TAGNAME = NAMESPACE;
  
  public Albums(IConfiguration config) {
    super(config);
  }

  public String getApiPath() {
    return this.getParent().getApiPath() + IConnector.URI_SEPARATOR + NAMESPACE;
  }

  @Override
  public String getNamespace() {
    return NAMESPACE;
  }

  @Override
  public String getTagName() {
    return TAGNAME;
  }

  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    
    if ( tagName.equals( Album.TAGNAME ) ) {
      return this.getClass().getMethod("add", Album.class);
    }
    
    return null;
  }

  @Override
  public boolean add(Album entity) {
    return super.addEntity(entity);
  }

  @Override
  public String getSubTagName() {
    return Album.TAGNAME;
  }

  @Override
  protected void copy(IEntity entity) {
    
  }
  
  
  
  
}
