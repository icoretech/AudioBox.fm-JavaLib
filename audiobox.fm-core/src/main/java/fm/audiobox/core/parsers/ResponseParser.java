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
import fm.audiobox.core.exceptions.ForbiddenException;
import fm.audiobox.core.exceptions.PaymentException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.exceptions.UnauthorizedException;
import fm.audiobox.core.models.Error;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.ContentFormat;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;


/**
 * This class handles all AudioBox.fm response.
 * <br />
 * It works as Response interceptor
 */
public class ResponseParser implements ResponseHandler<Response> {

  private static Logger log = LoggerFactory.getLogger(ResponseParser.class);

  private IConnectionMethod method;

  private IConfiguration configuration;

  private IResponseHandler responseHandler;

  private IEntity destEntity;
  private String ecode;

  public ResponseParser(IConfiguration config, IConnectionMethod method) {
    this(config, method, null);
  }

  public ResponseParser(IConfiguration config, IConnectionMethod method, IResponseHandler responseHandler) {
    this(config, method, responseHandler, null);
  }

  /**
   * Constructor method
   *
   * @param config the {@link IConfiguration} associated with this class
   * @param method the {@link IConnectionMethod} associated with this request
   * @param responseHandler a {@link IResponseHandler} will be use for handling the response body
   * @param ecode the {@code ETag code} used for retrieving response body in case of {@code 304} status code
   */
  public ResponseParser(IConfiguration config, IConnectionMethod method, IResponseHandler responseHandler, String ecode) {
    this.method = method;
    this.destEntity = this.method.getDestinationEntity();
    this.configuration = config;
    this.responseHandler = responseHandler != null ? responseHandler : this.getResponseParser( config.getResponseDeserializer() );
    this.ecode = ecode;

    if (log.isTraceEnabled()) {
      log.trace("ResponseParser instantiated for: " + this.method.getHttpMethod().getRequestLine().getUri());
    }
  }

  @SuppressWarnings("deprecation")
  public Response handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {

    int responseCode = httpResponse.getStatusLine().getStatusCode();
    String url = this.method.getHttpMethod().getURI().toString();

    if ( log.isDebugEnabled() ) {
      log.trace("Response code: " + responseCode + " for " + url);
    } else {
      log.trace("Response code: " + responseCode);
    }

    // No response found. Build a new Response
    HttpEntity entity = httpResponse.getEntity();
    if (entity == null) {
      // Entity is null. We're assuming we are in case of 304, 201 or 204
      log.warn("No response body found, maybe 204 (or 304) status?");
    }
    Header contentType = entity != null ? entity.getContentType() : null;
    boolean
      isXml = false,
      isJson = false,
      // Default value is TEXT
      isText = true,
      cacheEnabled = this.configuration.isCacheEnabled();

    if (contentType != null){
      isXml = contentType.getValue().contains(ContentFormat.XML.toString().toLowerCase());
      isJson = contentType.getValue().contains(ContentFormat.JSON.toString().toLowerCase());
      isText = contentType.getValue().contains("text");
    }

    ContentFormat format = isXml ? ContentFormat.XML : isJson ? ContentFormat.JSON : isText ? ContentFormat.TXT : ContentFormat.BINARY;

    // Build a new Response
    Response response = new Response(format, responseCode, entity != null ? entity.getContent() : null, cacheEnabled );

    switch ( responseCode ) {

      case HttpStatus.SC_NOT_MODIFIED:
        response = this.configuration.getCacheManager().getResponse(destEntity, this.ecode);
        response.setStatus( HttpStatus.SC_NOT_MODIFIED );

      case HttpStatus.SC_CREATED:
      case HttpStatus.SC_OK:
      case HttpStatus.SC_ACCEPTED:

        // Try to parse response body
        if ( destEntity != null ) {
          String text = this.responseHandler.deserialize( response.getStream(), destEntity, response.getFormat() );
          if ( ContentFormat.TXT == format && text != null ) {
            response.setBody( text );
          }
        }

        if ( responseCode != HttpStatus.SC_NOT_MODIFIED && this.method.isGET() && this.configuration.isCacheEnabled() ) {
          this.configuration.getCacheManager().store(this.destEntity, this.ecode, url, response, httpResponse);
        }
        break;

      case HttpStatus.SC_NO_CONTENT:
        response = new Response(response.getFormat(), responseCode, "Ok, no content", cacheEnabled);
        break;

      case HttpStatus.SC_SEE_OTHER:
        response = new Response(response.getFormat(), responseCode, httpResponse.getFirstHeader("Location").getValue(), cacheEnabled);
        break;

      case HttpStatus.SC_PAYMENT_REQUIRED:
        throw new PaymentException("Unauthorized user plan");

      case HttpStatus.SC_FORBIDDEN:
        throw new ForbiddenException(response.getBody());

      case HttpStatus.SC_UNAUTHORIZED:
        throw new UnauthorizedException( response.getBody(), method );

      default:
        // Assuming we are in case of server error
        if (response.getFormat() == ContentFormat.XML || response.getFormat() == ContentFormat.JSON) {
          Error error = new Error(this.configuration);
          if (response.getFormat() == ContentFormat.XML) {
            this.responseHandler.deserializeXml(response.getStream(), error);
          } else {
            this.responseHandler.deserializeJson(response.getStream(), error);
          }
          response = new Response(response.getFormat(), response.getStatus(), error.getMessage(), false);

        } else {
          // default: do nothing
        }
        String body = response.getBody();
        if ( body.length() > 50 ) {
          body = body.substring(0, 49);
        }
        log.warn("Server has returned error: " + body );
        throw new ServiceException(response.getStatus(), body);
    }

    return response;
  }



  private IResponseHandler getResponseParser(Class<? extends IResponseHandler> klass) {
    try {
      return klass.newInstance();
    } catch (InstantiationException e) {
      log.error("An error occurred while instantiating IResponseHandler class: " + e.getMessage());
    } catch (IllegalAccessException e) {
      log.error("An error occurred while accessing to IResponseHandler class: " + e.getMessage());
    }
    return null;
  }

}
