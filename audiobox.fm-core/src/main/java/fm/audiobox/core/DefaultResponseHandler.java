package fm.audiobox.core;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.parsers.XmlParser;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.RequestFormat;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

public class DefaultResponseHandler implements ResponseHandler<String[]> {

  
  private static final Logger log = LoggerFactory.getLogger(DefaultResponseHandler.class);
  
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
    
    // TODO: fix me - When response is gzipped we couldn't know its content-length
    if ( contentLength < 0 ) 
      contentLength = 1;
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
            
            if ( isXml ){
              
              try {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();
                xr.setContentHandler( new XmlParser(destEntity, configuration) );
                xr.parse( new InputSource( response.getEntity().getContent() ) );
              } catch (ParserConfigurationException e) {
                log.error("An error occurred while instantiating XML Parser", e);
                throw new ServiceException(ServiceException.GENERIC_ERROR, e.getMessage());
              } catch (SAXException e) {
                log.error("An error occurred while instantiating XML Parser", e);
                throw new ServiceException(ServiceException.GENERIC_ERROR, e.getMessage());
              }

            }
          
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
    
    String[] result = new String[2];
    result[ IConfiguration.RESPONSE_CODE ] = String.valueOf(responseCode);
    result[ IConfiguration.RESPONSE_BODY ] = responseString;
    return result;
  }

}
