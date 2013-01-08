package fm.audiobox.configurations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.interfaces.ICacheManager;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;

/**
 * This is the default cache manager
 * <br/>
 * <b>It stores each data into memory using {@link HashMap}s</b>
 */
public class DefaultCacheManager implements ICacheManager {

  private static final long serialVersionUID = 1L;
  private static Logger log = LoggerFactory.getLogger(DefaultCacheManager.class);

  @SuppressWarnings("unused")
  private IConfiguration configuration;
  
  private Map<String, String> urls = new HashMap<String, String>();
  private Map<String, String> etags = new HashMap<String, String>();
  private Map<String, Response> entities = new HashMap<String,Response>();
  
  public void setConfiguration(IConfiguration config) {
    this.configuration = config;
  }
  
  public String setup(IEntity destEntity, String url, IConnectionMethod request) {
    log.info("setup cache for " + url);
    
    if ( ! urls.containsKey( url) ) {
      urls.put( url, String.valueOf( System.currentTimeMillis() ) );
    }
    
    String ts = urls.get( url );
    if ( etags.containsKey( ts ) ) {
      request.getHttpMethod().setHeader( IConnectionMethod.HTTP_HEADER_IF_NONE_MATCH, etags.get( ts ) );
    }
    
    return ts;
  }
  
  
  public void store(IEntity destEntity, String ecode, String url, Response response, HttpResponse httpResponse) {
    
    String etag = null;
    Header etagTag = httpResponse.getLastHeader( IConnectionMethod.HTTP_HEADER_ETAG );
    if ( etagTag != null ) {
      etag = etagTag.getValue();
    }
      
    if ( etag != null) {
      etags.put(ecode, etag);
      entities.put( ecode, response );
    }
    
  }
  
  
  public Response getResponse(IEntity destEntity, String ecode) {
    Response body = entities.get( ecode );
    return body;
  }
  

  public void clear() throws IOException {
    etags.clear();
    entities.clear();
  }


}
