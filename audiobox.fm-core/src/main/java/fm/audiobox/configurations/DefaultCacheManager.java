package fm.audiobox.configurations;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import fm.audiobox.interfaces.ICacheManager;

public class DefaultCacheManager implements ICacheManager {

  private static final long serialVersionUID = 1L;

  Map<String, String> etags = new HashMap<String, String>();
  Map<String, File> cache = new HashMap<String, File>();
  
  @Override
  public String getEtag(String url) {
    return etags.get(url);
  }

  @Override
  public InputStream getBody(String etag) {
    File cachedFile = cache.get(etag);

    return null;
  }

  @Override
  public void store(String url, String etag, InputStream in) {
    
    

  }

}
