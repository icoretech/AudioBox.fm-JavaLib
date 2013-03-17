package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;

public class Albums extends AbstractCollectionEntity<Album> implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private static Logger log = LoggerFactory.getLogger(Albums.class);
  
  public static final String NAMESPACE = "albums";
  public static final String TAGNAME = NAMESPACE;
  
  public Albums(IConfiguration config) {
    super(config);
  }

  public String getApiPath() {
    // use playlists/:token/media_files/albums.json as per 2.0.8 change log
    return this.getParent().getApiPath() + IConnector.URI_SEPARATOR + MediaFiles.NAMESPACE + IConnector.URI_SEPARATOR + NAMESPACE;
  }

  public String getNamespace() {
    return NAMESPACE;
  }

  public String getTagName() {
    return TAGNAME;
  }

  public Method getSetterMethod(String tagName) {
    
    if ( tagName.equals( Album.TAGNAME ) ) {
      try {
        return this.getClass().getMethod("add", Album.class);
      } catch (SecurityException e) {
        log.error("Security error", e);
      } catch (NoSuchMethodException e) {
        log.error("No method found", e);
      }
    }
    
    return null;
  }
  
  
  public boolean add(Album entity) {
    return super.addEntity(entity);
  }

  public String getSubTagName() {
    return Album.TAGNAME;
  }

  protected void copy(IEntity entity) {
    
  }
  
  protected List<NameValuePair> toQueryParameters(boolean all) {
    return new ArrayList<NameValuePair>();
  }
  
}
