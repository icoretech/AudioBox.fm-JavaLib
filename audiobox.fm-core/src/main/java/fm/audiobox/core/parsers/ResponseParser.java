package fm.audiobox.core.parsers;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  public ResponseParser(IConfiguration config, IConnectionMethod method) {
    this(config, method, null);
  }

  public ResponseParser(IConfiguration config, IConnectionMethod method, IResponseHandler responseHandler) {
    this.method = method;
    this.destEntity = this.method.getDestinationEntity();
    this.configuration = config;
    this.responseHandler = responseHandler != null ? responseHandler : this.getResponseParser( config.getResponseParser() );

    if (log.isTraceEnabled()) {
      log.trace("ResponseParser instantiated for: " + this.method.getHttpMethod().getRequestLine().getUri());
    }
  }

  @Override
  public Response handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {

    int responseCode = httpResponse.getStatusLine().getStatusCode();

    log.info("Response code: " + responseCode);

    Header etag = httpResponse.getFirstHeader(IConnectionMethod.HTTP_HEADER_ETAG);
    String respondedEtag = etag != null ? etag.getValue().replaceAll("\"", "") : null;

    Response response = null;

    if (this.configuration.isCacheEnabled()) {
      // Try to retrieve response from CacheManager
      response = this.configuration.getCacheManager().getResponse(this.destEntity, respondedEtag);
    }

    if (response == null) {
      // No response found. Build a new Response
      HttpEntity entity = httpResponse.getEntity();
      if (entity == null) {
        // Entity is null. We're assuming we are in case of 304, 201 or 204
        log.warn("No response body found, maybe 204 status?");
      }
      Header contentType = entity != null ? entity.getContentType() : null;
      boolean
        isXml = false, 
        isJson = false,
        // Default value is TEXT
        isText = true;
      
      if (contentType != null){
        isXml = contentType.getValue().contains(ContentFormat.XML.toString().toLowerCase());
        isJson = contentType.getValue().contains(ContentFormat.JSON.toString().toLowerCase());
        isText = contentType.getValue().contains("text");
      }

      ContentFormat format = isXml ? ContentFormat.XML : isJson ? ContentFormat.JSON : isText ? ContentFormat.TXT : ContentFormat.BINARY;

      // Build a new Response
      response = new Response(format, responseCode, entity != null ? entity.getContent() : null );

      if (this.configuration.isCacheEnabled() && responseCode == HttpStatus.SC_OK) {
        /*
         * CacheManager is enabled. So we have to store Response before parsing
         * it. Note: we have to store Response in case of presence of response
         * body ( response code should be 200 )
         */
        this.configuration.getCacheManager().store(this.destEntity, respondedEtag, response);

        /*
         * Retrive Response from CacheManager. In this way we bypass some bug
         * caused by non reparsing httpresponse InputStream
         */
        Response tmpResponse = this.configuration.getCacheManager().getResponse(this.destEntity, respondedEtag);
        if (tmpResponse != null) {
          // CacheManager returned a Response. We have to use that
          response = tmpResponse;
        }
      }

    }

    switch ( responseCode ) {

      case HttpStatus.SC_OK:
      case HttpStatus.SC_NOT_MODIFIED:
      case HttpStatus.SC_ACCEPTED:
        // Try to parse response body
        String content = this.responseHandler.parse(response.getStream(), destEntity, response.getFormat());
        response = new Response(response.getFormat(), responseCode, content);
        break;
  
      // In all other cases new response will be instantiated and returned
      case HttpStatus.SC_CREATED:
  
        response = new Response(response.getFormat(), responseCode, "Created");
        break;
  
      case HttpStatus.SC_NO_CONTENT:
        response = new Response(response.getFormat(), responseCode, "Ok, no content");
        break;
  
      case HttpStatus.SC_SEE_OTHER:
        response = new Response(response.getFormat(), responseCode, httpResponse.getFirstHeader("Location").getValue());
        break;
  
      case HttpStatus.SC_PAYMENT_REQUIRED:
        throw new LoginException(responseCode, "Unauthorized user plan");
  
      case HttpStatus.SC_UNAUTHORIZED:
      case HttpStatus.SC_FORBIDDEN:
        throw new LoginException(responseCode, response.getBody());
  
      default:
        // Assuming we are in case of server error
        if (response.getFormat() == ContentFormat.XML || response.getFormat() == ContentFormat.JSON) {
          Error error = new Error(this.configuration);
          if (response.getFormat() == ContentFormat.XML) {
            this.responseHandler.parseAsXml(response.getStream(), error);
          } else {
            this.responseHandler.parseAsJson(response.getStream(), error);
          }
          response = new Response(response.getFormat(), error.getStatus(), error.getMessage());
  
        } else {
          // default: do nothing
        }
  
        throw new ServiceException(response.getStatus(), response.getBody());
    }

    return response;
  }
  
  
  
  private IResponseHandler getResponseParser(Class<? extends IResponseHandler> klass) {
    try {
      return klass.newInstance();
    } catch (InstantiationException e) {
      log.error("An error occurred while instantiating IResponseHandler class", e);
    } catch (IllegalAccessException e) {
      log.error("An error occurred while accessing to IResponseHandler class", e);
    }
    return null;
  }

}
