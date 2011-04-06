package fm.audiobox.configurations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import fm.audiobox.interfaces.ICacheManager;
import fm.audiobox.interfaces.IEntity;

public class DefaultCacheManager implements ICacheManager {

  private static final long serialVersionUID = 1L;

  private Map<String, String> entities = new HashMap<String,String>();
  private Map<String, String> etags = new HashMap<String,String>();
  
  
  @Override
  public InputStream getBody(IEntity destEntity, String etag) {
    String body = etags.get(etag);
    if ( body != null ){
      return new ByteArrayInputStream( body.getBytes() );
    }
    return null;
  }
  
  
  @Override
  public void store(IEntity destEntity, String etag, InputStream in) {
    
    try {
      entities.put(destEntity.getTagName(), etag);
      etags.put( etag, Response.streamToString(in) );
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }
  
  
  @Override
  public String getEtag(IEntity destEntity, String url) {
    return entities.get( destEntity.getTagName() );
  }


  @Override
  public void clear() throws IOException {
    etags.clear();
    entities.clear();
  }
  

}
