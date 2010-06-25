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

package fm.audiobox.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
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
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import fm.audiobox.core.api.Model;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.interfaces.AudioBoxModelLoader;
import fm.audiobox.core.interfaces.CollectionListener;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.User;
import fm.audiobox.core.util.Inflector;

/**
 * AudioBoxClient is the main library class. Every request to AudioBox.fm are made through this object.
 * This class is used mainly to configure every aspect of the library itself.<br/>
 * To populate and get informations  about user library use the {@link User} (or an extended User) model instead.
 *
 * <p>
 * 
 * As many other libraries out there AudioBox.fm-JavaLib allows you to extends default models and use them in place 
 * of default ones.
 * 
 * <p>
 * 
 * For this purpose we embrace the "convention over configuration" phylosophy this is why you will need to store all 
 * your extended models in the same package.
 * 
 * <p>
 * 
 * If you dislike this phylosophy or you have special needs you can bypass it by overwriting the default 
 * {@link AudioBoxModelLoader} providing your implementation.
 * 
 * <p>
 * 
 * In order to make AudioBoxClient load your extended models you will need to provide a full package path where 
 * your classes are stored.<br/> 
 * For instance the default models package is "fm.audiobox.api.models"; here you will find the default {@link Track}
 * model.<br/>
 * If you plan to override the Track model you must inform AudioBoxClient where to find your Track class and this can 
 * be done in two ways:
 * 
 * <ol>
 *  <li>simply setting up the custom package path through {@link AudioBoxClient#setCustomModelsPackage(String) setCustomModelsPackage(String)}.</li>
 *  <li>overwriting the default {@link AudioBoxModelLoader} as told before.</li>
 * </ol>
 * 
 * <p>
 * 
 * The usual execution flow can be demonstrated by the code snippet below: 
 * 
 * <pre>
 * 
 * // This is needed if you plan to extend default models 
 * AudioBoxClient.setCustomModelsPackage("my.custom.models");
 * 
 * // If you extended the {@link User} model AudioBoxClient should be informed before create a new instance 
 * AudioBoxClient.setUserClass(MyUser.class);
 *
 * // Creating the new AudioBoxClient instance 
 * abc = new AudioBoxClient();
 * try {
 *    
 *    // Should perform a login before anything else is done with the AudioBoxClient object
 *    MyUser user = (MyUser) abc.login( "user@email.com" , "password" );
 *    
 *    // Now you can browse your library with calls like this:
 *    Playlists pls = user.getPlaylists();
 *    
 * } catch (LoginException e) {
 *    // Handle {@link LoginException}
 * } catch (ServiceException e) {
 *    // Handle {@link ServiceException}
 * }
 * 
 * 
 * 
 * </pre>
 * 
 * 
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * 
 * @version 0.1
 * 
 */

public class AudioBoxClient {

    /** Message used when the response is succesfully parsed */
    public static final String PROTOCOL = "https";
    public static final String HOST = "audiobox.dev";
    public static final String PORT = "443";
    public static final String API_PREFIX = "/api/";
    public static final String API_SUFFIX = "xml";
    public static final String TRACK_ID_PLACEHOLDER = "[track_id]";
    public static final String API_PATH = PROTOCOL + "://" + HOST + API_PREFIX;

    /** Specifies the models package (default: fm.audiobox.core.models) */
    public static final String DEFAULT_MODELS_PACKAGE = "fm.audiobox.core.models";
    private static final String PATH_PARAMETER = "${path}";
    private static final String TOKEN_PARAMETER = "${token}";
    private static final String ACTION_PARAMETER = "${action}";


    private static long sTimeout = (180 * 1000);
    private static boolean sForceTrust = false;
    private static String sUserAgent = "AudioBox.fm/0.1-alpha (Java; U; " +
    System.getProperty("os.name") + " " +
    System.getProperty("os.arch") + "; " + 
    System.getProperty("user.language") + "; " +
    System.getProperty("java.runtime.version") +  ") " +
    System.getProperty("java.vm.name") + "/" + 
    System.getProperty("java.vm.version") + 
    " AudioBoxClient/0.1";

    private static String sCustomModelsPackage = DEFAULT_MODELS_PACKAGE;
    private static String sApiPath = API_PATH + PATH_PARAMETER + TOKEN_PARAMETER + ACTION_PARAMETER;
    private static Class<? extends User> sUserClass = User.class;
    private static Inflector sI = Inflector.getInstance();
    private static User sUser;
    private static ThreadSafeClientConnManager sCm;
    private static DefaultHttpClient sClient;
    private static HttpRoute sAudioBoxRoute;


    /* ------------------ */
    /* Default Interfaces */
    /* ------------------ */

    /** @see {@link CollectionListener} */
    private static CollectionListener sCollectionListener = new CollectionListener() {
        public void onCollectionReady(int message, Object result) {  }
        public void onItemReady(int item, Object obj) { }
    };

    /** @see {@link AudioBoxModelLoader} */
    private static AudioBoxModelLoader sAudioBoxModelLoader = new AudioBoxModelLoader() {
        @SuppressWarnings("unchecked")
        public Class<? extends Model> getModelClassName(Class<? extends Model> clazz,  String tagName) {
            Class<? extends Model> klass = null;
            String tname = clazz.getCanonicalName();

            if (tagName != null && tagName.trim().length() > 0)
                tname = sI.upperCamelCase( tagName, '-' );

            try {
                klass = (Class<? extends Model>) Class.forName( sCustomModelsPackage  + "." +  tname );
            } catch(ClassNotFoundException e) {
                try {
                    klass = (Class<? extends Model>) Class.forName( DEFAULT_MODELS_PACKAGE + "." +  tname );
                } catch (ClassNotFoundException e1) {
                    // This should never happens...
                    e1.printStackTrace();
                }
            }

            return klass;
        }
    };


    /**
     * Note: When first created, AudioBoxClient instantiate a new {@link User} object needed for authentication 
     * on AudioBox.fm.
     * 
     * <p>
     * 
     * If you wish to overload or override the default User model class you have to specify which is the User
     * class you want to use <b>before</b> AudioBoxClient object is created. 
     * This is done through the {@link AudioBoxClient#setUserClass(Class)} method.
     */

    public AudioBoxClient() { 
        try {
            Class<? extends User> clazz = sUserClass;
            sUser = clazz.cast(clazz.newInstance());
            sAudioBoxRoute = new HttpRoute(new HttpHost(HOST, Integer.parseInt(PORT)));

            httpSetup();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    /**
     * This method should be called before any other call to AudioBox.fm.<br/>
     * It tries to perform a login. If succeeds a User object is returned otherwise a {@link LoginException} is thrown.<br/>
     * This method also checks whether the user is active or not. If not a LoginException is thrown.
     * 
     * <p>
     * 
     * @param username the username to login to AudioBox.fm in form of an e-mail
     * @param password the password to use for authentication
     * 
     * @return {@link User} object
     */

    public User login(String username, String password) throws LoginException, ServiceException {

        Log logger = LogFactory.getLog(AudioBoxClient.class);
        logger.info("Starting AudioBoxClient: " + sUserAgent);

        sUserClass.cast(sUser).setUsername( username );
        sUserClass.cast(sUser).setPassword( password );

        sUser.invoke();

        if ( ! User.ACTIVE_STATE.equalsIgnoreCase( sUserClass.cast(sUser).getState() ))
            throw new LoginException("User is not active: " + sUserClass.cast(sUser).getState(), LoginException.INACTIVE_USER_STATE );

        return sUserClass.cast(sUser);

    }


    /**
     * This method returns the User object used to perform requests to AudioBox.fm.
     * 
     * @return {@link User} object
     */

    public User getUser() {
        return sUser;
    }


    /**
     * Use this method to configure the timeout limit for reqests made against AudioBox.fm.
     * 
     * @param timeout the milliseconds of the timeout limit
     */

    public void setTimeout(long timeout) {
        sTimeout = timeout; 
    }


    /**
     * Returns the requests timeout limit.
     * 
     * @return timeout limit
     */

    public long getTimeout() {
        return sTimeout;
    }


    /**
     * Use this method to override the default {@link CollectionListener}.
     * 
     * @param collectionListener the CollectionListener to use for parser callbacks
     */

    public void setCollectionListener(CollectionListener collectionListener) {
        AudioBoxClient.sCollectionListener = collectionListener;
    }


    /**
     * Use this method to override the default {@link AudioBoxModelLoader}.
     * 
     * @param modelLoader the AudioBoxModelLoader to use for models class loading
     */

    public void setAudioBoxModelLoader(AudioBoxModelLoader modelLoader) {
        AudioBoxClient.sAudioBoxModelLoader = modelLoader;
    }


    /* -------------- */
    /* Static methods */
    /* -------------- */

    /**
     * AudioBox.fm RESTful API uses inflections to distinguish between a single item and a collection.<br/>
     * To preserve the same philosophy we also use an inflector.<br/>
     * To be sure to not waste memory or resources use this method to get the {@link Inflector} instance used by 
     * the parser.
     * 
     * @return the AudioBoxClient {@link Inflector} instance
     */

    public static Inflector getInflector() {
        return AudioBoxClient.sI;
    }


    /**
     * This method will switch SSL certificate validation on or off.
     * You will not need to use this. This method is used for testing purpose only. For this reason this method is 
     * marked as "deprecated".
     * 
     * @param force set or unset the SSL certificate validation (false validates, true skips validation).
     */

    @Deprecated
    public static void setForceTrust(boolean force) {
        AudioBoxClient.sForceTrust = force;
    }


    /**
     * If you need to extend the {@link User} model class you will have to specify which is the User class to load
     * <b>before</b> instantiate a new {@link AudioBoxClient} object.
     * 
     * @param clazz the extended {@link User} class.
     */

    public static void setUserClass(Class<? extends User> clazz) {
        AudioBoxClient.sUserClass = clazz;
    }


    /**
     * Use this method to make AudioBoxClient loads your extended models classes.<br/>
     * 
     * @param customModelsPackage the extended models classes package.
     */

    public static void setCustomModelsPackage(String customModelsPackage) {
        AudioBoxClient.sCustomModelsPackage = customModelsPackage;
    }


    /**
     * Use this method to gather the models package used for models class loading.
     * 
     * @return the custom models package default is {@link AudioBoxClient#DEFAULT_MODELS_PACKAGE}.
     */

    public static String getCustomModelsPackage() {
        return sCustomModelsPackage;
    }


    /**
     * Use this method to get the configured {@link CollectionListener}.
     * 
     * @return the current collection listener AudioBoxClient is using.
     */

    public static CollectionListener getCollectionListener() {
        return sCollectionListener;
    }


    /**
     * Use this method to get the configured {@link AudioBoxModelLoader}.<br/>
     * You can specify a custom model loader through {@link AudioBoxClient#setAudioBoxModelLoader(AudioBoxModelLoader)}.
     * 
     * @return the current model loader AudioBoxClient is using.
     */

    public static AudioBoxModelLoader getAudioBoxModelLoader() {
        return sAudioBoxModelLoader;
    }

    
    /**
     * This method is used by the {@link Model} class by running the {@link Model#invoke()} method.<br/>
     * Avoid direct execution of this method if you don't know what you are doing.
     * 
     * <p>
     * 
     * Some of the parameter may be null other cannot.
     * 
     * @param path the partial url to call. Tipically this is a Model end point ({@link Model#getEndPoint()})
     * @param token the token of the Model if any, may be null or empty ({@link Model#getToken()})
     * @param action the remote action to execute on the model that executes the action (ie. "scrobble")
     * @param target usually reffers the Model that executes the method
     * @param httpVerb the HTTP method to use for the request (ie: GET, PUT, POST and DELETE)
     * 
     * @return the result of the request, may be a response code, such as HTTP OK ("200") or the body of the response.
     */

    public static String execute(String path, String token, String action , Model target, String httpVerb) throws LoginException , ServiceException {
        return execute(path, token, action, target, httpVerb, API_SUFFIX);
    }

    /**
     * This method is used by the {@link Model} class by running the {@link Model#invoke()} method.<br/>
     * Avoid direct execution of this method if you don't know what you are doing.
     * 
     * <p>
     * 
     * Some of the parameter may be null other cannot.
     * 
     * @param path the partial url to call. Tipically this is a Model end point ({@link Model#getEndPoint()})
     * @param token the token of the Model if any, may be null or empty ({@link Model#getToken()})
     * @param action the remote action to execute on the model that executes the action (ie. "scrobble")
     * @param target usually reffers the Model that executes the method
     * @param httpVerb the HTTP method to use for the request (ie: GET, PUT, POST and DELETE)
     * @param format the request format (xml or txt)
     * 
     * @return the result of the request, may be a response code, such as HTTP OK ("200") or the body of the response.
     */

    public static String execute (String path, String token, String action , Model target, String httpVerb, String format) throws LoginException , ServiceException {

        token = ( token == null ) ? "" : token.startsWith("/") ? token : "/".concat(token);
        action = ( action == null ) ? "" : action.startsWith("/") ? action : "/".concat(action);

        // Replace the placeholder with the right values
        String url = sApiPath.replace( PATH_PARAMETER , path ).replace( TOKEN_PARAMETER , token ).replace( ACTION_PARAMETER , action ); 

        httpVerb = httpVerb == null ? HttpGet.METHOD_NAME : httpVerb;

        if (HttpGet.METHOD_NAME.equals(httpVerb) )
            url += "." + format;
        return request( url, target, httpVerb );
    }



    /* --------------- */
    /* Private methods */
    /* --------------- */


    /**
     * This method is used to performs requests to AudioBox.fm service APIs.<br/>
     * Once AudioBox.fm responds the response is parsed through the target {@link Model} only if the response returns
     * with a 200 code.
     * 
     * <p>
     * 
     * If a sream url is requested (tipically from a {@link Track} object, the location for audio streaming is returned.
     * 
     * <p>
     * 
     * Any other case returns a string representing the status code.
     * 
     * @param url the full url where to make the request
     * @param target the model to use to parse the response
     * @param httpVerb the HTTP method to use for the request
     * 
     * @return the response code or the stream Location (in case of a {@link Track} model)
     * 
     */
    
    private static String request(String url, Model target, String httpVerb) throws LoginException, ServiceException {

        if (sUser == null)
            throw new LoginException("Cannot execute API actions without credentials.", LoginException.NO_CREDENTIALS);

        Log log = LogFactory.getLog(AudioBoxClient.class);

        try {

            log.info("Requesting resource: " + url);

            HttpRequestBase method = null; 
            if ( HttpPost.METHOD_NAME.equals( httpVerb ) ) {
                method = new HttpPost(url);
            } else if ( HttpPut.METHOD_NAME.equals( httpVerb ) ) {
                method = new HttpPut(url);
            } else if ( HttpDelete.METHOD_NAME.equals( httpVerb ) ) {
                method = new HttpDelete(url);
            } else {
                method = new HttpGet(url);
            }

            if ( method instanceof HttpPost && target instanceof Track ){
                HttpPost post = ( HttpPost ) method;
                post.setEntity( ((Track)target).getFileEntity() );
            }

            String response = "" ;
            
            HttpResponse resp = sClient.execute(method, new BasicHttpContext());
            response = target.handleResponse( resp, httpVerb );
            
            HttpEntity e = resp.getEntity(); 
            if (e != null) 
                e.consumeContent();
            
            return response;

        } catch( ClientProtocolException e ) {
            throw new ServiceException( "Client protocol exception: " + e.getMessage(), ServiceException.CLIENT_ERROR );
            
        } catch( SocketTimeoutException e ) {
            throw new ServiceException( "Service does not respond: " + e.getMessage(), ServiceException.TIMEOUT_ERROR );
            
        } catch( IOException e ) {
            throw new ServiceException( "IO exception: " + e.getMessage(), ServiceException.SOCKET_ERROR );
            
        } 
    }


    /**
     * When AudioBoxClient is first created a new {@link HttpClient} is configured as well.
     * 
     * <p>
     * 
     * This method provides global HttpClient configuration.
     * 
     */
    
    private static void httpSetup() {

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register( new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register( new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        HttpParams params = new BasicHttpParams();
        params.setParameter(ConnManagerParams.TIMEOUT, sTimeout);
        params.setParameter(ClientPNames.HANDLE_REDIRECTS, false);

        sCm = new ThreadSafeClientConnManager(params, schemeRegistry);
        sClient = new DefaultHttpClient( sCm, params );
        
        // Increase max total connection to 200
        ConnManagerParams.setMaxTotalConnections(params, 200);

        // Increase default max connection per route to 20
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(20);

        // Increase max connections for audiobox.fm:443 to 50
        connPerRoute.setMaxForRoute(sAudioBoxRoute, 50);
        ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);

        if (sForceTrust)
            forceTrustCertificate(sClient);


        sClient.addRequestInterceptor(new HttpRequestInterceptor() {

            public void process( final HttpRequest request,  final HttpContext context) throws HttpException, IOException {
                if (!request.containsHeader("Accept-Encoding")) {
                    request.addHeader("Accept-Encoding", "gzip");
                }
                request.addHeader("Authorization" , "Basic " + sUser.getAuth() );
                request.addHeader("User-Agent", sUserAgent);
            }

        });

        sClient.addResponseInterceptor(new HttpResponseInterceptor() {

            public void process( final HttpResponse response, final HttpContext context) throws HttpException, IOException {
                HttpEntity entity = response.getEntity();
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
        });
        
    }

    /**
     * This method is for internal testing and debugging use only.<br/>
     * Please avoid the use of this method.
     * 
     * <p>
     * 
     * If {@link AudioBoxClient} is configured to accept all certificates this method is called to provide the SSL
     * interfaces that will skips any of the default SSL certificate verifications.
     * 
     * <p>
     * 
     * Note that if {@link NoSuchAlgorithmException} or {@link KeyManagementException} occurs this method fails silently
     * with only warn log message. 
     * 
     * @param client HttpClient to set the SSL interfaces to
     */

    @Deprecated
    private static void forceTrustCertificate(HttpClient client) {

        Log logger = LogFactory.getLog(AudioBoxClient.class);

        TrustManager easyTrustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
            public X509Certificate[] getAcceptedIssuers() { return null; }
        };

        X509HostnameVerifier hnv = new X509HostnameVerifier() {
            public void verify(String arg0, SSLSocket arg1) throws IOException { }
            public void verify(String arg0, X509Certificate arg1) throws SSLException { }
            public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException { }
            public boolean verify(String hostname, SSLSession session) { return false; }
        };

        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] { easyTrustManager }, null);
            SSLSocketFactory sf = new SSLSocketFactory(ctx);
            sf.setHostnameVerifier( hnv );
            Scheme https = new Scheme("https", sf, 443);
            client.getConnectionManager().getSchemeRegistry().register(https);
        } catch (NoSuchAlgorithmException e) {
            logger.warn("Cannot force SSL certificate trust due to 'NoSuchAlgorithmException': " + e.getMessage());
        } catch (KeyManagementException e) {
            logger.warn("Cannot force SSL certificate trust due to 'KeyManagementException': " + e.getMessage());
        }
    }
    
}
