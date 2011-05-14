package fm.audiobox.core.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.configurations.DefaultResponseParser;
import fm.audiobox.configurations.Response;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Error;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.ContentFormat;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

public class ResponseParser implements ResponseHandler<Response> {

  private static Logger log = LoggerFactory.getLogger(ResponseParser.class);
  
  private IConnectionMethod method;
  private IConfiguration configuration;
  private IResponseHandler responseHandler;
  private IEntity destEntity;

  
  
  public ResponseParser(IConfiguration config, IConnectionMethod method){
    this(config,method,null);
  }
  
  public ResponseParser(IConfiguration config, IConnectionMethod method, IResponseHandler responseHandler){
    this.method = method;
    this.destEntity = this.method.getDestinationEntity();
    this.configuration = config;
    this.responseHandler = responseHandler != null ? responseHandler : new DefaultResponseParser();
    
    if ( log.isTraceEnabled() ){
      log.trace("ResponseParser instantiated for: " + this.method.getHttpMethod().getRequestLine().getUri() );
    }
  }
  
  
  @Override
  public Response handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
    
    int responseCode = httpResponse.getStatusLine().getStatusCode();
    
    Header contentType = httpResponse.getEntity().getContentType();
    Header etag = httpResponse.getFirstHeader(IConnectionMethod.HTTP_HEADER_ETAG);
    String respondedEtag = etag != null ? etag.getValue().replaceAll("\"","") : null;
    
    boolean isXml = contentType.getValue().contains( ContentFormat.XML.toString().toLowerCase() );
    boolean isText = contentType.getValue().contains( "text" );
    boolean isJson = contentType.getValue().contains( RequestFormat.JSON.toString().toLowerCase() );
    
    ContentFormat format = isXml ? ContentFormat.XML :
                            isJson ? ContentFormat.JSON :
                            isText ? ContentFormat.TXT : ContentFormat.BINARY;
                            
    
    Response response = null;
    
    try {
      switch( responseCode ){
      
        case HttpStatus.SC_OK:
        case HttpStatus.SC_NOT_MODIFIED:
          
          InputStream in = null;
          
          if ( this.configuration.isCacheEnabled() ){
            String cachedEtag = this.configuration.getCacheManager().getEtag(destEntity, this.method.getHttpMethod().getRequestLine().getUri() );
            
            if ( cachedEtag != null ){
              if ( cachedEtag.equals( respondedEtag ) ){
                in = this.configuration.getCacheManager().getBody( this.destEntity, cachedEtag );
              }
            }
            
            if ( in == null && respondedEtag != null ){
              this.configuration.getCacheManager().store( destEntity , respondedEtag, httpResponse.getEntity().getContent() );
              in = this.configuration.getCacheManager().getBody( this.destEntity, respondedEtag );
            }
            
          }
          
          if ( in == null ){
            in = httpResponse.getEntity().getContent();
          }
          
          String content = this.responseHandler.parse( in, destEntity, format);
          response = new Response(responseCode, content);
          break;
          
        case HttpStatus.SC_CREATED:
          response = new Response( responseCode, "Created" );
          break;
          
        case HttpStatus.SC_NO_CONTENT:
          response = new Response( responseCode, "resource not ready" );
          break;
          
        case HttpStatus.SC_SEE_OTHER:
          response = new Response( responseCode, httpResponse.getFirstHeader("Location").getValue() );
          break;
        
        case HttpStatus.SC_PAYMENT_REQUIRED:
          throw new LoginException(responseCode, "Unauthorized user plan");
          
        case HttpStatus.SC_UNAUTHORIZED:
        case HttpStatus.SC_FORBIDDEN:
          throw new LoginException(responseCode, Response.streamToString( httpResponse.getEntity().getContent() ) );
          
          
        default:
          
          if ( isXml || isJson ){
            Error error = new Error( this.configuration );
            if ( isXml ) {
              this.responseHandler.parseAsXml(httpResponse.getEntity().getContent(), error );
            } else {
              this.responseHandler.parseAsJson(httpResponse.getEntity().getContent(), error );
            }
            response = new Response(error.getStatus(), error.getMessage() );
            
          } else {
            
            response = new Response( responseCode, Response.streamToString(httpResponse.getEntity().getContent() ) );
            
          }
          throw new ServiceException(response.getStatus(), response.getBody() );
      
      }
      
    } finally {
      
      if ( httpResponse.getEntity() != null )
        httpResponse.getEntity().consumeContent();
      
    }
    
    
    return response;
  }
  
  
  
}
