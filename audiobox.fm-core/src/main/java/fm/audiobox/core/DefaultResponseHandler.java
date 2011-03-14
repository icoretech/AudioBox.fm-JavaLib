package fm.audiobox.core;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;
import fm.audiobox.interfaces.IConfiguration.RequestFormat;

public class DefaultResponseHandler implements ResponseHandler<String[]> {

  
  public IConfiguration configuration;
  public IEntity destEntity;
  public IResponseHandler responseHandler;
  
  
  public DefaultResponseHandler(IConfiguration config, IEntity destEntity, IResponseHandler responseHandler){
    this.configuration = config;
    this.destEntity = destEntity;
    this.responseHandler = responseHandler;
  }
  
  private String streamToString(InputStream stream) throws IOException{
    int read;
    byte[] bytes = new byte[ 1024 ];
    StringBuffer sb = new StringBuffer();
    while(  ( read = stream.read( bytes) ) != -1 )
        sb.append( new String( bytes, 0, read ));
    return sb.toString();
  }
  
  
  @Override
  public String[] handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

    int responseCode = response.getStatusLine().getStatusCode();
    long contentLength = response.getEntity().getContentLength();
    Header contentType = response.getEntity().getContentType();
    boolean isXml = contentType.getValue().contains( RequestFormat.XML.toString().toLowerCase() );
    boolean isText = contentType.getValue().contains( "text" );
    boolean isJson = contentType.getValue().contains( RequestFormat.JSON.toString().toLowerCase() );
    boolean isBinary  = contentType.getValue().contains( "audio" ); // TODO: check this code
    String responseString = "";
    
    try {
      switch( responseCode ) {

        // 20*
        case HttpStatus.SC_OK:

          if ( responseHandler != null ){
            
            if ( isXml ) {
              responseString = responseHandler.parseAsXml( response );
            } else if ( isJson ) {
              responseString = responseHandler.parseAsJson( response );
            } else if ( isText ) {
              responseString = responseHandler.parseAsText( response );
            } else if ( isBinary ) {
              responseString = responseHandler.parseAsBinary( response );
            } else {
              responseString = responseHandler.parse( response );
            }
            
          } else if ( contentLength > 0 ){
            
            configuration.getParser().populateEntity( destEntity , response.getEntity().getContent() );
          
          }
          
          break;

        
        case HttpStatus.SC_CREATED:

        // 204 ( used when an uploaded track is still not ready )
        case HttpStatus.SC_NO_CONTENT:
          responseString = "Resource not ready";
          break;

        // 303 ( used by *getStreamUrl* track method )
        case HttpStatus.SC_SEE_OTHER:
          responseString = response.getFirstHeader("Location").getValue();
          break;

        // 401, 403
        case HttpStatus.SC_UNAUTHORIZED:
        case HttpStatus.SC_FORBIDDEN:
        {
          String body = streamToString(response.getEntity().getContent() );
          LoginException loginException = new LoginException( responseCode, body );
          if ( configuration.getDefaultLoginExceptionHandler() != null ){
            configuration.getDefaultLoginExceptionHandler().handle( loginException );
          }
          throw loginException;
        }

        // 50x
        default:

          String message = streamToString( response.getEntity().getContent() );

//            fm.audiobox.core.models.Error error = new fm.audiobox.core.models.Error();
//  
//            try {
//              error.parseXMLResponse( response.getEntity().getContent() );
//              message = error.getMessage();
//              status = error.getStatus();
//            } catch(IOException e) { message = e.getMessage(); }

          throw new ServiceException( responseCode, message );
        }
      
    } finally {

      HttpEntity responseEntity = response.getEntity(); 
      if (responseEntity != null) 
        responseEntity.consumeContent();

    }
    
    return new String[]{ String.valueOf( responseCode ) , responseString };
  }

}
