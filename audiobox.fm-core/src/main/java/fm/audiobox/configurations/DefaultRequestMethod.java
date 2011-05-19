package fm.audiobox.configurations;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.AudioBoxException;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.parsers.ResponseParser;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.ContentFormat;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

public class DefaultRequestMethod implements IConnectionMethod {

  private static Logger log = LoggerFactory.getLogger(DefaultRequestMethod.class);

  private volatile transient HttpClient connector;
  private volatile transient HttpRequestBase method;
  private volatile transient IEntity destEntity;
  private volatile transient IConfiguration configuration;
  private volatile transient Future<Response> futureResponse;


  public DefaultRequestMethod(){
    super();
  }


  @Override
  public void init(IEntity destEntity, HttpRequestBase method, HttpClient connector, IConfiguration config) {
    this.connector = connector;
    this.method = method;
    this.destEntity = destEntity;
    this.configuration = config;
  }


  @Override
  public Response send(boolean async) throws ServiceException, LoginException {
    return send(async, null,null);
  }


  @Override
  public Response send(boolean async, List<NameValuePair> params) throws ServiceException, LoginException {
    HttpEntity entity = null;
    if (  (! isGET() && ! isDELETE() )  && params != null ){
      try {
        entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
      } catch (UnsupportedEncodingException e) {
        log.error("An error occurred while instantiating UrlEncodedFormEntity", e);
      }
    }
    return send(async, entity);
  }


  @Override
  public Response send(boolean async, HttpEntity params) throws ServiceException, LoginException {
    return send(async, params, null);
  }


  @Override
  public Response send(boolean async, HttpEntity params, final IResponseHandler responseHandler) throws ServiceException, LoginException {
    if (   ( ! isGET() && ! isDELETE() )  && params != null ){
      ((HttpEntityEnclosingRequestBase) getHttpMethod() ).setEntity( params );
    }
    
    
    if ( isGET() && configuration.isCacheEnabled() ) {
      String url = getHttpMethod().getRequestLine().getUri();
      String etag = this.configuration.getCacheManager().getEtag(destEntity, url);
      if (etag != null) {
        getHttpMethod().addHeader( IConnectionMethod.HTTP_HEADER_IF_NONE_MATCH,  "\"" + etag + "\"" );
      }
    }
    

    Callable<Response> start = new Callable<Response>() {

      @Override
      public Response call() throws ServiceException, LoginException {
        Response response = null;
        try {

          return connector.execute( getHttpMethod(), new ResponseParser( configuration, DefaultRequestMethod.this, responseHandler), new BasicHttpContext() );

        } catch (ClientProtocolException e) {
          
          response = new Response(ContentFormat.XML, AudioBoxException.GENERIC_ERROR, e.getMessage() );
          log.error("ClientProtocolException thrown while executing request method", e);
          response.setException( new ServiceException(e) );

        } catch (IOException e) {
          
          response = new Response(ContentFormat.XML, AudioBoxException.GENERIC_ERROR, e.getMessage() );
          AudioBoxException responseException = null;
          
          if ( e instanceof LoginException ) {
            
            LoginException le = (LoginException) e;
            if ( configuration.getDefaultLoginExceptionHandler() != null ){
              configuration.getDefaultLoginExceptionHandler().handle( le );
            }
            responseException = le;
            
          } else {
            
            ServiceException se = e instanceof ServiceException ? (ServiceException) e : new ServiceException(e);
            if ( configuration.getDefaultServiceExceptionHandler() != null ){
              configuration.getDefaultServiceExceptionHandler().handle( se );
            }
            responseException = se;
          }
          
          response.setException( responseException );
          
        }
        return response;
      }

    };

    futureResponse = this.configuration.getExecutor().submit( start ); 

    return async ? null : this.getResponse();
  }

  public Response getResponse()  throws ServiceException, LoginException{
    try {
      Response response = futureResponse.get();
      if ( response.getException() != null ) {
        AudioBoxException ex = response.getException();
        if ( ex instanceof LoginException ){
          throw (LoginException) ex;
        } else {
          throw (ServiceException) ex;
        }
      }
      return response;
    } catch (InterruptedException e) {
      log.error(e.getMessage(), e);
    } catch (ExecutionException e) {
      log.error(e.getMessage(), e);
    }
    throw new ServiceException(HttpStatus.SC_PRECONDITION_FAILED, "No response found");
  }

  @Override
  public void abort() {
    futureResponse.cancel(true);
    getHttpMethod().abort();
  }


  @Override
  public HttpRequestBase getHttpMethod() {
    return this.method;
  }

  @Override
  public IEntity getDestinationEntity() {
    return this.destEntity;
  }


  @Override
  public boolean isGET() {
    return getHttpMethod().getMethod().equals( HttpGet.METHOD_NAME );
  }

  @Override
  public boolean isPOST() {
    return getHttpMethod().getMethod().equals( HttpPost.METHOD_NAME );
  }

  @Override
  public boolean isPUT() {
    return getHttpMethod().getMethod().equals( HttpPut.METHOD_NAME );
  }

  @Override
  public boolean isDELETE() {
    return getHttpMethod().getMethod().equals( HttpDelete.METHOD_NAME );
  }

  @Override
  public void setFollowRedirect(boolean followRedirect) {
    getHttpMethod().getParams().setBooleanParameter( ClientPNames.HANDLE_REDIRECTS, true );
  }

}
