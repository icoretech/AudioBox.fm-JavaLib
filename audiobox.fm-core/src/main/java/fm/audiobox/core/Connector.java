package fm.audiobox.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import fm.audiobox.core.api.Model;
import fm.audiobox.core.api.ModelItem;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Track;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;

/**
   * Connector is the AudioBox http request wrapper.
   * 
   * <p>
   * 
   * Every HTTP request to AudioBox.fm is done through this object and 
   * responses are handled from {@link Model} objects.
   * 
   * <p>
   * 
   * Actually the only configurable parameter is the timeout through the {@link Connector#setTimeout(long)}.
   */
  public class Connector implements Serializable, IConnector {

    private static final long serialVersionUID = -1947929692214926338L;
    private static final Log log = LogFactory.getLog(Connector.class);

    private static final String NAMESPACE_PARAMETER = "${namespace}";
    private static final String TOKEN_PARAMETER = "${token}";
    private static final String ACTION_PARAMETER = "${action}";
    private static final String URI_SEPARATOR = "/";

    /** Get informations from configuration file */
    private final String protocol;
    private final String host;
    private final int port;
    private final String path;

    private String apiPath; 
    private HttpRoute mAudioBoxRoute;
    private ThreadSafeClientConnManager mCm;
    private DefaultHttpClient mClient;
    private UsernamePasswordCredentials mCredentials;
    private BasicScheme mScheme = new BasicScheme();

    private IConfiguration configuration;
    

    /** Default constructor builds http connector */
    private Connector(IConfiguration config) {
      this.configuration = config;
      
      this.protocol = configuration.getProtocol();
      this.host = configuration.getHost();
      this.port = configuration.getPort(); 
      this.path = configuration.getPath();
      
      this.apiPath = this.protocol + "://" + this.host + ":" + this.port + "/" + this.path + "/" + NAMESPACE_PARAMETER + TOKEN_PARAMETER + ACTION_PARAMETER;
      
      buildClient();
    }


    /**
     * This method is used to build the HttpClient to use for connections
     */
    private void buildClient() {

      this.mAudioBoxRoute = new HttpRoute(new HttpHost( this.host, this.port ) );

      SchemeRegistry schemeRegistry = new SchemeRegistry();
      schemeRegistry.register( new Scheme("http", PlainSocketFactory.getSocketFactory(), this.port ));
      schemeRegistry.register( new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

      HttpParams params = new BasicHttpParams();
      //params.setParameter(ClientPNames.HANDLE_REDIRECTS, false);
      HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
      HttpConnectionParams.setSoTimeout(params, 30 * 1000);

      this.mCm = new ThreadSafeClientConnManager(params, schemeRegistry);
      this.mClient = new DefaultHttpClient( this.mCm, params );

      // Increase max total connection to 200
      ConnManagerParams.setMaxTotalConnections(params, 200);

      // Increase default max connection per route to 20
      ConnPerRouteBean connPerRoute = new ConnPerRouteBean(20);

      // Increase max connections for audiobox.fm:443 to 50
      connPerRoute.setMaxForRoute(mAudioBoxRoute, 50);
      ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);

      this.mClient.addRequestInterceptor(new HttpRequestInterceptor() {

        public void process( final HttpRequest request,  final HttpContext context) throws HttpException, IOException {
          if (!request.containsHeader("Accept-Encoding")) {
            request.addHeader("Accept-Encoding", "gzip");
          }
          
          String userAgent = configuration.getUserAgent();
          
          request.addHeader("User-Agent",  userAgent);
          Header hostHeader = request.getFirstHeader("HOST");
          if ( hostHeader.getValue().equals( host ) )
            request.addHeader( mScheme.authenticate(mCredentials,  request) );
        }

      });

      this.mClient.addResponseInterceptor(new HttpResponseInterceptor() {

        public void process( final HttpResponse response, final HttpContext context) throws HttpException, IOException {
          HttpEntity entity = response.getEntity();
          if (entity != null) {
            Header ceheader = entity.getContentEncoding();
            if (ceheader != null) {
              HeaderElement[] codecs = ceheader.getElements();
              for (int i = 0; i < codecs.length; i++) {
                if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                  response.setEntity( new HttpEntityWrapper(entity){
                    @Override
                    public InputStream getContent() throws IOException, IllegalStateException {
                      // the wrapped entity's getContent() decides about repeatability
                      InputStream wrappedin = wrappedEntity.getContent();
                      return new GZIPInputStream(wrappedin);
                    }

                    @Override
                    public long getContentLength() { return -1; }

                  }); 
                  return;
                }
              }
            }
          }
        }
      });

    }


    /**
     * This method is used to close all connections and reinstantiate the HttpClient.
     */
    public void abortAll() {
      this.mCm.shutdown();
      buildClient();
    }


    /**
     * Set up HTTP Basic Authentication credentials for HTTP authenticated requests.
     * 
     * @param credential the basic scheme credentials object to use.
     */
    public void setCredential(UsernamePasswordCredentials credential) {
      this.mCredentials = credential;
    }


    /**
     * Use this method to configure the timeout limit for reqests made against AudioBox.fm.
     * 
     * @param timeout the milliseconds of the timeout limit
     */
    public void setTimeout(long timeout) {
      mClient.getParams().setParameter(ConnManagerParams.TIMEOUT, timeout);
    }


    /**
     * Returns the requests timeout limit.
     * 
     * @return timeout limit
     */
    public long getTimeout() {
      return (Long) mClient.getParams().getParameter(ConnManagerParams.TIMEOUT);
    }


    /**
     * Creates a HttpRequestBase
     * 
     * @param httpVerb the HTTP method to use for the request (ie: GET, PUT, POST and DELETE)
     * @param source usually reffers the Model that invokes method
     * @param dest Model that intercepts the response
     * @param action the remote action to execute on the model that executes the action (ex. "scrobble")
     * @param entity HttpEntity used by POST and PUT method
     * 
     * @return the HttpRequestBase 
     */
    public HttpRequestBase createConnectionMethod(String httpVerb, Model source, String action, HttpEntity entity) {

      httpVerb = httpVerb == null ? HttpGet.METHOD_NAME : httpVerb;

      String
      namespace = source.getEndPoint(),
      token = ( source instanceof ModelItem ) ? ((ModelItem)source).getToken() : null,
          url = buildRequestUrl(namespace, token, action, httpVerb);

      HttpRequestBase method = null;

      if ( HttpPost.METHOD_NAME.equals( httpVerb ) ) {
        method = new HttpPost(url);
      } else if ( HttpPut.METHOD_NAME.equals( httpVerb ) ) {
        method = new HttpPut(url);
      } else if ( HttpDelete.METHOD_NAME.equals( httpVerb ) ) {
        method = new HttpDelete(url);
      } else {
        // TODO: set the entity as querystring
        method = new HttpGet(url);
      }

      if (method instanceof HttpEntityEnclosingRequestBase && entity != null ) {
        ((HttpEntityEnclosingRequestBase) method).setEntity(entity);
      }

      // Default: follow redirects!
      method.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);


     

      log.debug("[ " + httpVerb + " ] Next request will be: " + url);

      return method;
    }



    public String[] get(Model source, Model dest, String action) throws LoginException, ServiceException{
      // TODO: get parameters as entity
      HttpRequestBase method = this.createConnectionMethod( HttpGet.METHOD_NAME, source, action, null);
      return this.request(method, dest);
    }

    public String[] post(Model source, Model dest, String action, HttpEntity entity) throws LoginException, ServiceException{
      HttpRequestBase method = this.createConnectionMethod( HttpPost.METHOD_NAME, source, action, entity);
      return this.request(method, dest);
    }

    public String[] put(Model source, Model dest, String action, HttpEntity entity) throws LoginException, ServiceException{
      HttpRequestBase method = this.createConnectionMethod( HttpPut.METHOD_NAME, source, action, entity);
      return this.request(method, dest);
    }

    public String[] delete(Model source, Model dest, String action) throws LoginException, ServiceException{
      HttpRequestBase method = this.createConnectionMethod( HttpDelete.METHOD_NAME, source, action, null);
      return this.request(method, dest);
    }




    /**
     * This method is used to performs requests to AudioBox.fm service APIs.<br/>
     * Once AudioBox.fm responds the response is parsed through the target {@link Model}.
     * 
     * <p>
     * 
     * If a stream url is requested (tipically from a {@link Track} object), the location for audio streaming is returned.
     * 
     * <p>
     * 
     * Any other case returns a string representing the status code.
     * 
     * @param method the HTTP method to use for the request
     * @param target the model to use to parse the response
     * 
     * @return String array containing the response code at position 0 and the response body at position 1
     * 
     * @throws LoginException if user has not yet logged in
     * @throws ServiceException if the connection to AudioBox.fm throws a {@link SocketTimeoutException} or {@link IOException} occurs.
     */
    public String[] request(HttpRequestBase method, Model target) throws LoginException, ServiceException {

      try {
        // TODO: check this code
        // this.mClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, followRedirect);
        return mClient.execute(method, target, new BasicHttpContext());

      } catch( SocketTimeoutException e ) {
        throw new ServiceException( "Service does not respond: " + e.getMessage(), ServiceException.TIMEOUT_ERROR );

      } catch( ServiceException e ) {
        // Bypass IOException 
        throw e;

      } catch( IOException e ) {
        throw new ServiceException( "IO exception: " + e.getMessage(), ServiceException.SOCKET_ERROR );
      }
    }



    /* --------------- */
    /* Private methods */
    /* --------------- */


    /**
     * Creates the correct url starting from parameters
     * 
     * @param path the partial url to call. Tipically this is a Model end point ({@link Model#getEndPoint()})
     * @param token the token of the Model if any, may be null or empty ({@link Model#getToken()})
     * @param action the remote action to execute on the model that executes the action (ex. "scrobble")
     * @param httpVerb the HTTP method to use for the request (ie: GET, PUT, POST and DELETE)
     * 
     * @return the URL string 
     */
    private String buildRequestUrl(String path, String token, String action, String httpVerb) {
      token = ( ( token == null ) ? "" : URI_SEPARATOR.concat(token) ).trim();
      action = ( ( action == null ) ? "" : URI_SEPARATOR.concat(action) ).trim();

      // Replace placeholders with right values
      String url = mApiPath.replace( PATH_PARAMETER , path ).replace( TOKEN_PARAMETER , token ).replace( ACTION_PARAMETER , action ); 

      if ( HttpGet.METHOD_NAME.equals(httpVerb) )
        url += "." + mRequestFormat.toString().toLowerCase();

      if ( shortlyResponse ){
        url += "?short=true";	// default parameter name and value
      }

      return url;
    }

    
    private String getUrlPath(String endpoint, String token, String action){
      
      endpoint = (endpoint != null ) ? endpoint : "";
      token = (token != null ) ? token : "";
      action = (action != null ) ? action : "";
      
      endpoint = ( ! endpoint.startsWith(URI_SEPARATOR) ) ? URI_SEPARATOR + endpoint : endpoint;
      token = ( ! token.startsWith(URI_SEPARATOR) ) ? URI_SEPARATOR + token : token;
      action = ( ! action.startsWith(URI_SEPARATOR) ) ? URI_SEPARATOR + action : action;
      
      return this.apiPath
              .replace(NAMESPACE_PARAMETER, endpoint)
              .replace(TOKEN_PARAMETER, token)
              .replace(ACTION_PARAMETER, action);
    }
    
    
    @Override
    public IConnectionMethod get(IEntity destEntity, String action, List<NameValuePair> params) {
      
      
      String urlPath = this.getUrlPath(destEntity.getEndpoint(), destEntity.getToken(), action);
      String query = URLEncodedUtils.format(params , "UTF-8");
      
      try {
        URI uri = URIUtils.createURI(this.protocol, this.host, this.port, urlPath, query, configuration.getRequestFormat().toString().toLowerCase() );
        HttpGet method = new HttpGet(uri);
        
        
      } catch (URISyntaxException e) {
        log.error("URI Syntax error", e);
      }
      
      return null;
    }


    @Override
    public IConnectionMethod get(IEntity destEntity, String action) {
      return null;
    }


    @Override
    public IConnectionMethod put(IEntity destEntity, String action) {
      return null;
    }


    @Override
    public IConnectionMethod post(IEntity destEntity, String action) {
      return null;
    }


    @Override
    public IConnectionMethod delete(IEntity destEntity, String action) {
      return null;
    }
    
    
    private class ConnectionMethod implements IConnectionMethod {
      
      private ConnectionMethod(HttpRequestBase method){
        
      }
      
    }
    
    
  }