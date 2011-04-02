package fm.audiobox.interfaces;

import java.io.InputStream;
import java.io.Serializable;

public interface ICacheManager extends Serializable {


  public String getEtag(String url);
  
  public String getBody(String etag);
  
  public void store(String url, String etag, InputStream in);
  
}
