package fm.audiobox.interfaces;

import java.io.InputStream;
import java.io.Serializable;

public interface ICacheManager extends Serializable {

  public InputStream getBody(IEntity destEntity, String etag);
  
  public void store(IEntity destEntity, String etag, InputStream in);
  
  public String getEtag(IEntity destEntity, String url);
  
}
