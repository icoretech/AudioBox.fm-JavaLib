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

public class DefaultResponseHandler implements ResponseHandler<Response>, IResponseHandler {


  private static final Logger log = LoggerFactory.getLogger(DefaultResponseHandler.class);

  public IConfiguration configuration;
  public IEntity destEntity;
  public IResponseHandler responseHandler;


  private Response response;

  public DefaultResponseHandler(IConfiguration config, IEntity destEntity, IResponseHandler responseHandler){
    this.configuration = config;
    this.destEntity = destEntity;
    this.responseHandler = responseHandler != null  ?  responseHandler  :  this;
  }



  @Override
  public Response handleResponse(HttpResponse response) throws ClientProtocolException, IOException {


    int responseCode = response.getStatusLine().getStatusCode();

    this.response = new Response(responseCode, response.getEntity().getContent() );

    long contentLength = response.getEntity().getContentLength();

    // TODO: fix me - When response is gzipped we couldn't know its content-length
    if ( contentLength < 0 ) 
      contentLength = 1;
    Header contentType = response.getEntity().getContentType();
    boolean isXml = contentType.getValue().contains( RequestFormat.XML.toString().toLowerCase() );
    boolean isText = contentType.getValue().contains( "text" );
    boolean isJson = contentType.getValue().contains( RequestFormat.JSON.toString().toLowerCase() );
    boolean isBinary  = contentType.getValue().contains( "audio" ); // TODO: check this code

    try {

      switch( responseCode ) {

      // 20*
      case HttpStatus.SC_OK:

        if ( isXml ) {
          responseHandler.parseAsXml( response, this.response.getStream() );

        } else if ( isJson ) {
          responseHandler.parseAsJson( response, this.response.getStream());

        } else if ( isText ) {
          responseHandler.parseAsText( response, this.response.getStream() );

        } else if ( isBinary ) {
          responseHandler.parseAsBinary( response, this.response.getStream() );

        } else {
          responseString = responseHandler.parse( response, this.response.getStream() );

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

        String body = this.response.getBody();
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
            xr.parse( new InputSource( this.response.getStream() ) );
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
          // TODO: Not implemented yet

        } else {
          message = this.response.getBody();
        }

        throw new ServiceException( responseCode, message );
      }

    } finally {

      HttpEntity responseEntity = response.getEntity(); 
      if (responseEntity != null) 
        responseEntity.consumeContent();

    }

    return this.response;
  }

  @Override
  public String parseAsXml(HttpResponse response, InputStream in) throws ServiceException {

    try {

      SAXParserFactory spf = SAXParserFactory.newInstance();
      SAXParser sp = spf.newSAXParser();
      XMLReader xr = sp.getXMLReader();
      xr.setContentHandler( new XmlParser(destEntity, configuration) );
      xr.parse( new InputSource( in ) );

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
  public String parseAsJson(HttpResponse response, InputStream in) throws ServiceException {
    return null;
  }

  @Override
  public String parseAsText(HttpResponse response, InputStream in) throws ServiceException {
    return this.response.getBody();
  }

  @Override
  public String parseAsBinary(HttpResponse response, InputStream in) throws ServiceException {
    return null;
  }

  @Override
  public String parse(HttpResponse response, InputStream in) throws ServiceException {
    return null;
  }

}




