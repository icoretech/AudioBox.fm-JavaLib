package fm.audiobox.configurations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.interfaces.ICacheManager;
import fm.audiobox.interfaces.IEntity;

public class DefaultCacheManager implements ICacheManager {

  private static final long serialVersionUID = 1L;
  private static Logger log = LoggerFactory.getLogger(DefaultCacheManager.class);

  private Map<String, String> entities = new HashMap<String,String>();
  private Map<String, Response> etags = new HashMap<String,Response>();
  
  
  @Override
  public Response getResponse(IEntity destEntity, String etag) {
    Response body = etags.get(etag);
    return body;
  }
  
  
  @Override
  public void store(IEntity destEntity, String etag, Response response) {
    entities.put(destEntity.getTagName(), etag);
    
    try {
      response = new Response( response.getFormat(), response.getStatus(), Response.streamToString(response.getStream() ));
      etags.put( etag, response );
    } catch (IOException e) {
      log.error("An error occurred while storing Reponse", e);
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
