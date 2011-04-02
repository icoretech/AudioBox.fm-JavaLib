package fm.audiobox.configurations;

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

public class DefaultResponseHandler implements ResponseHandler<String[]>, IResponseHandler {

  
  private static final Logger log = LoggerFactory.getLogger(DefaultResponseHandler.class);
  
  public IConfiguration configuration;
  public IEntity destEntity;
  public IResponseHandler responseHandler;
  
  
  public DefaultResponseHandler(IConfiguration config, IEntity destEntity, IResponseHandler responseHandler){
    this.configuration = config;
    this.destEntity = destEntity;
    this.responseHandler = responseHandler != null  ?  responseHandler  :  this;
  }
  
  public static String streamToString(InputStream stream) throws IOException{
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
          
          String body = streamToString(response.getEntity().getContent() );
          throw new LoginException( responseCode, body );
        
        case HttpStatus.SC_PAYMENT_REQUIRED:
          
          throw new LoginException( responseCode, "Unauthorized user plan" );
          
          
        // 50x
        default:
          String message = "";
          
          if ( isXml ){
            fm.audiobox.core.models.Error error = new fm.audiobox.core.models.Error();
            
            try {
              SAXParserFactory spf = SAXParserFactory.newInstance();
              SAXParser sp = spf.newSAXParser();
              XMLReader xr = sp.getXMLReader();
              xr.setContentHandler( new XmlParser(error, configuration) );
              xr.parse( new InputSource( response.getEntity().getContent() ) );
            } catch (ParserConfigurationException e) {
              log.error("An error occurred while instantiating XML Parser", e);
              throw new ServiceException(ServiceException.GENERIC_ERROR, e.getMessage() );
            } catch (SAXException e) {
              log.error("An error occurred while instantiating XML Parser", e);
              throw new ServiceException(ServiceException.GENERIC_ERROR, e.getMessage() );
            } catch (IllegalStateException e) {
              log.error("An error occurred while parsing response", e);
              throw new ServiceException(ServiceException.GENERIC_ERROR, e.getMessage() );
            }
            
            message = error.getMessage();
            
          } else if ( isJson ){
            // TODO: Still not implemented
          
          }else {
            message = streamToString( response.getEntity().getContent() );
          }
            
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

  @Override
  public String parseAsXml(HttpResponse response) throws ServiceException {
    
    try {
      
      SAXParserFactory spf = SAXParserFactory.newInstance();
      SAXParser sp = spf.newSAXParser();
      XMLReader xr = sp.getXMLReader();
      xr.setContentHandler( new XmlParser(destEntity, configuration) );
      xr.parse( new InputSource( response.getEntity().getContent() ) );
      
    } catch (ParserConfigurationException e) {
      log.error("An error occurred while instantiating XML Parser", e);
      throw new ServiceException(ServiceException.GENERIC_ERROR, e.getMessage() );
    } catch (SAXException e) {
      log.error("An error occurred while instantiating XML Parser", e);
      throw new ServiceException(ServiceException.GENERIC_ERROR, e.getMessage() );
    } catch (IllegalStateException e) {
      log.error("An error occurred while parsing response", e);
      throw new ServiceException(ServiceException.GENERIC_ERROR, e.getMessage() );
    } catch (IOException e) {
      log.error("An error occurred while parsing response", e);
      throw new ServiceException(ServiceException.GENERIC_ERROR, e.getMessage() );
    }
    
    return "";
  }

  @Override
  public String parseAsJson(HttpResponse response) throws ServiceException {
    return null;
  }

  @Override
  public String parseAsText(HttpResponse response) throws ServiceException {
    try {
      
      return streamToString( response.getEntity().getContent() );
      
    } catch (IllegalStateException e) {
      log.error("An error occurred while parsing response", e);
      throw new ServiceException(ServiceException.GENERIC_ERROR, e.getMessage() );
      
    } catch (IOException e) {
      log.error("An error occurred while parsing response", e);
      throw new ServiceException(ServiceException.GENERIC_ERROR, e.getMessage() );
    }
    
  }

  @Override
  public String parseAsBinary(HttpResponse response) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String parse(HttpResponse response) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }
  
  
  
  

}
