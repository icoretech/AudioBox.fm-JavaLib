package fm.audiobox.configurations;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import fm.audiobox.interfaces.ICacheManager;
import fm.audiobox.interfaces.IEntity;

public class DefaultCacheManager implements ICacheManager {

  private static final long serialVersionUID = 1L;

  Map<String, String> etags = new HashMap<String, String>();
  Map<String, File> cache = new HashMap<String, File>();
  
  
  @Override
  public InputStream getBody(IEntity destEntity, String etag) {
    String str = (String) destEntity.getProperty( "cache" );
    if ( str != null ){
      return new ByteArrayInputStream( str.getBytes() );
    }
    return null;
  }
  
  
  @Override
  public void store(IEntity destEntity, String etag, InputStream in) {
    
    try {
      destEntity.setProperty( "cache", Response.streamToString(in) );
      destEntity.setProperty("etag", etag);
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }
  
  
  @Override
  public String getEtag(IEntity destEntity, String url) {
    return (String) destEntity.getProperty("etag");
  }
  

}
