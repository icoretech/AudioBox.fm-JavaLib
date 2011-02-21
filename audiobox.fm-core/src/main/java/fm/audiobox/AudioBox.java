
/***************************************************************************
 *   Copyright (C) 2010 iCoreTech research labs                            *
 *   Contributed code from:                                                *
 *   - Valerio Chiodino - keytwo at keytwo dot net                         *
 *   - Fabio Tunno      - fat at fatshotty dot net                         *
 *                                                                         *
 *   This program is free software: you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation, either version 3 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program. If not, see http://www.gnu.org/licenses/     *
 *                                                                         *
 ***************************************************************************/

package fm.audiobox;


import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.SocketTimeoutException;
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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.api.Model;
import fm.audiobox.core.api.ModelItem;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.ModelFactory;
import fm.audiobox.core.models.ModelFactory.ModelParser;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.User;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;


/**
 * AudioBox is the main library class. Every request to AudioBox.fm should pass through this object.
 * This class is used mainly to configure every aspect of the library itself.<br/>
 * To populate and get informations about user library use the {@link User} (or an extended User) model instead.
 * <p>
 * 
 * Keep in mind that this library provides only the common browsing actions and some other few feature.<br/>
 * AudioBox does not streams, nor play or provide a BitmapFactory for albums covers.
 * 
 * <p>
 *
 * As many other libraries out there AudioBox.fm-JavaLib allows you to extends default models and use them in place
 * of default ones.
 *
 * <p>
 *
 * In order to make AudioBox load your extended models you will need to provide your {@link Model} extension
 * through the {@link AudioBox#setCollectionListenerFor(String, CollectionListener)} method.<br/>
 *
 * <p>
 * 
 * Note that some of the requests, such as the ModelsCollection population requests, can be done asynchronously.<br/>
 * To keep track of the collection building process you can make use of the {@link CollectionListener} object.
 * 
 * <p>
 *
 * The usual execution flow can be demonstrated by the code snippet below:
 *
 * <pre>
 * // Creating the new AudioBox instance
 * abc = new AudioBox();
 *
 * // If you extended the {@link User} model AudioBox should 
 * // be informed before the login take place.
 * AudioBox.setModelClassFor(AudioBox.USER_KEY , MyUser.class );
 * 
 * // Suppose we want to limit requests timeout to 5 seconds
 * abc.getMainConnector().setTimeout( 5000 );
 *  
 * // Now we can try to perform a login...
 * try {
 *
 *    // Should perform a login before anything else is done with 
 *    // the AudioBox object
 *    MyUser user = (MyUser) abc.login( "user@email.com" , "password" );
 *
 *    // To browse user library we have some nice utils methods
 *    // We can get the user's playlists...
 *    Playlists pls = user.getPlaylists();
 *    
 *    // ...and get more details on a specific one
 *    Playlist pl = playlists.get(0);
 *    
 *    // Get playlist's tracks
 *    Tracks trs = pl.getTracks();
 *    
 *    // Track informations
 *    Track tr = trs.get(0);
 *
 * } catch (LoginException e) {
 *    // Handle {@link LoginException}
 * } catch (ServiceException e) {
 *    // Handle {@link ServiceException}
 * }
 * </pre>
 *
 * This is briefly the navigation loop. Moreover each model offer some action that can be performed. To know what a model
 * can do consult the specific model documentation.
 * 
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class AudioBox {

  private static Logger log = LoggerFactory.getLogger(AudioBox.class);

  
  private static final String APP_NAME_PLACEHOLDER = "${APP_NAME}";
  private static final String VERSION_PLACEHOLDER = "${VERSION}";
  

  /** Prefix used to store each property into properties file */
  private static final String PREFIX = "fm.audiobox.";


  private static final String USER_AGENT = 
    "AudioBox.fm; " +
    System.getProperty("os.name") + " " +
    System.getProperty("os.arch") + "; " + 
    System.getProperty("user.language") + "; " +
    System.getProperty("java.runtime.version") +  ") " +
    System.getProperty("java.vm.name") + "/" + 
    System.getProperty("java.vm.version") + " " + 
    APP_NAME_PLACEHOLDER + "/" + VERSION_PLACEHOLDER;

  private IConfiguration configuration;


  /**
   * <p>Constructor for AudioBox.</p>
   * 
   * When is created it instantiate an {@link Connector} too.
   * 
   */
  public AudioBox(IConfiguration config) {
    configuration = config;
  }



  /**
   * <p>Getter method for the default connector Object<p>
   *
   * @return the main {@link Connector} object.
   */
  protected Connector getMainConnector(){
    return this.mUtils.getConnector();
  }

  /**
   * <p>Getter method for the {@link User user} Object<p>
   * 
   * @return the logged in {@link User} instance
   */
  public User getUser(){
    return this.mUtils.getUser();
  }


  /**
   * This method should be called before any other call to AudioBox.fm.<br/>
   * It tries to perform a login. If succeeds a {@link User} object is returned otherwise a 
   * {@link LoginException} is thrown.<br/>
   * This method also checks whether the user is active or not. If not a LoginException is thrown.
   *
   * <p>
   *
   * @param username the username to login to AudioBox.fm in form of an e-mail
   * @param password the password to use for authentication
   * 
   * @return {@link User} object
   * 
   * @throws ModelException if any of the custom model class does not exists or is not instantiable.
   * @throws LoginException if user doesn't exists or is inactive.
   * @throws ServiceException if any connection problem occurs.
   */
  public User login(String username, String password) throws LoginException, ServiceException, ModelException {

    log.info("Starting AudioBox: " + mUserAgent);

    User user = (User) this.mUtils.getModelInstance( ModelFactory.USER_KEY );
    user.setUsername(username);

    this.mUtils.setUser(user);

    this.getMainConnector().setCredential( new UsernamePasswordCredentials(username, password) );

    this.getMainConnector().get(user, user, null);

    return user;
  }


  public class Utils implements Serializable {

    private static final long serialVersionUID = 1L;

    private User mUser;
    private Connector mConnector;

    private void setUser(User user){
      this.mUser = user;
    }

    public User getUser(){
      return mUser;
    }

    public ModelParser getModelParser(Model model){
      return getModelFactory().getModelParser(model, this);
    }

    public Model getModelInstance(String key) throws ModelException {
      Model model = mModelFactory.getModelInstance(key);
      model.setUtils( this );
      return model;
    }

    public Connector getConnector(){
      if ( mConnector == null ){
        this.mConnector = new Connector();
        this.mConnector.setTimeout( 180 * 1000 );
      }
      return this.mConnector;
    }

  }



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

    private static final String NAMESPACE_PARAMETER = "${namespace}";
    private static final String TOKEN_PARAMETER = "${token}";
    private static final String ACTION_PARAMETER = "${action}";
    private static final String URI_SEPARATOR = "/";

    /** Get informations from configuration file */
    private final String PROTOCOL = configuration.getProtocol();
    private final String HOST = configuration.getHost();
    private final String PORT = String.valueOf( configuration.getPort() ); 
    private final String PATH = configuration.getPath();;

    private final String API_PATH = PROTOCOL + "://" + HOST + ":" + PORT + "/" + PATH + "/" + NAMESPACE_PARAMETER + "/" + TOKEN_PARAMETER + "/" + ACTION_PARAMETER;
    private HttpRoute mAudioBoxRoute;
    private ThreadSafeClientConnManager mCm;
    private DefaultHttpClient mClient;
    private UsernamePasswordCredentials mCredentials;
    private BasicScheme mScheme = new BasicScheme();

    private final Log log = LogFactory.getLog(Connector.class);

    /** Default constructor builds http connector */
    private Connector() {
      buildClient();
    }


    /**
     * This method is used to build the HttpClient to use for connections
     */
    private void buildClient() {

      this.mAudioBoxRoute = new HttpRoute(new HttpHost( HOST, Integer.parseInt(PORT) ) );

      SchemeRegistry schemeRegistry = new SchemeRegistry();
      schemeRegistry.register( new Scheme("http", PlainSocketFactory.getSocketFactory(), Integer.parseInt( PORT ) ));
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
          
          String userAgent = USER_AGENT.replace( APP_NAME_PLACEHOLDER , configuration.getApplicationName() ).replace(VERSION_PLACEHOLDER, configuration.getVersion() );
          
          request.addHeader("User-Agent",  userAgent);
          Header hostHeader = request.getFirstHeader("HOST");
          if ( hostHeader.getValue().equals( HOST ) )
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


      //            BasicNameValuePair bnvp = new BasicNameValuePair("a", "b");
      //            UrlEncodedFormEntity uefe = new UrlEncodedFormEntity(null)

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


    @Override
    public IConnectionMethod get(IEntity destEntity, String action, List<NameValuePair> params) {
      
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
      
    }
    
    
  }

}
