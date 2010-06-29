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

package fm.audiobox.core.models;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
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
import fm.audiobox.core.api.ModelsCollection;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.interfaces.CollectionListener;
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

    private static Log log = LogFactory.getLog(AudioBoxClient.class);

    /** Specifies the models package (default: fm.audiobox.core.models) */
    public static final String DEFAULT_MODELS_PACKAGE = AudioBoxClient.class.getPackage().getName();
    public static final String TRACK_ID_PLACEHOLDER = "[track_id]";

    
    private User sUser;
    private AudioBoxConnector connector;

    private static Map<String,Class<? extends Model>> mapper = null;
    private String mUserAgent;
    private static Inflector sI = Inflector.getInstance();
    
    public static final String 
    USER_KEY      = User.TAG_NAME,  PROFILE_KEY  = Profile.TAG_NAME, 
    PLAYLISTS_KEY = "Playlists",    PLAYLIST_KEY = Playlist.TAG_NAME,
    GENRES_KEY    = "Genres",       GENRE_KEY    = Genre.TAG_NAME,
    ARTISTS_KEY   = "Artists",      ARTIST_KEY   = Artist.TAG_NAME,
    ALBUMS_KEY    = "Albums",       ALBUM_KEY    = Album.TAG_NAME,
    TRACKS_KEY    = "Tracks",       TRACK_KEY    = Track.TAG_NAME;

    static {
        mapper = new HashMap<String , Class<? extends Model>>();
        mapper.put( USER_KEY,      User.class ); 
        mapper.put( PROFILE_KEY ,  Profile.class );
        mapper.put( PLAYLISTS_KEY, Playlists.class ); 
        mapper.put( PLAYLIST_KEY,  Playlist.class );
        mapper.put( GENRES_KEY,    Genres.class ); 
        mapper.put( GENRE_KEY,     Genre.class );
        mapper.put( ARTISTS_KEY,   Artists.class ); 
        mapper.put( ARTIST_KEY,    Artist.class );
        mapper.put( ALBUMS_KEY,    Albums.class ); 
        mapper.put( ALBUM_KEY ,    Album.class );
        mapper.put( TRACKS_KEY,    Tracks.class ); 
        mapper.put( TRACK_KEY ,    Track.class );
    }


    /* ------------------ */
    /* Default Interfaces */
    /* ------------------ */

    /** @see {@link CollectionListener} */
    private static CollectionListener sCollectionListener = new CollectionListener() {
        public void onCollectionReady(int message, Object result) { }
        public void onItemReady(int item, Object obj) { }
    };


    public AudioBoxClient() {
        
        this.connector = this.new AudioBoxConnector();	// setup connection

        String version = "unattended";

        try {
            Properties ps = new Properties();
            ps.load(AudioBoxClient.class.getResourceAsStream("../config/env.properties"));
            version = ps.getProperty("libaudioboxfm-core.version");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mUserAgent = "AudioBox.fm/" + version + " (Java; U; " +
        System.getProperty("os.name") + " " +
        System.getProperty("os.arch") + "; " + 
        System.getProperty("user.language") + "; " +
        System.getProperty("java.runtime.version") +  ") " +
        System.getProperty("java.vm.name") + "/" + 
        System.getProperty("java.vm.version") + 
        " AudioBoxClient/" + version;
    }


    protected AudioBoxConnector getMainConnector(){
        return this.connector;
    }


    public static void initClass(String key, Class<? extends Model> klass){
        Class<? extends Model> _klass = mapper.get( key );
        if ( _klass != null ){
            mapper.put( key , klass );
        }
    }


    @SuppressWarnings("unchecked")
    public static Model getModelInstance(String key, AudioBoxConnector connector) throws ModelException {
        
        Model model = null;
        Class<? extends Model> klass = mapper.get( key );
        
        if ( klass == null ) {
            String className = DEFAULT_MODELS_PACKAGE + "." + sI.upperCamelCase( key, '-');
            
            try {
                klass = (Class<? extends Model>) Class.forName( className );
            } catch (ClassNotFoundException e) {
                throw new ModelException("No model class found: " + className );
            }
        }

        try {
            
            log.debug("New model instance: " + klass.getName() );
            model = klass.newInstance();
            
        } catch (InstantiationException e) {
            throw new ModelException("Instantiation Exception: " + klass.getName() );
            
        } catch (IllegalAccessException e) {
            throw new ModelException("Illegal Access Exception: " + klass.getName() );
            
        }

        model.setConnector( connector );
        
        if ( model instanceof ModelsCollection )
            ((ModelsCollection)model).setCollectionListener(sCollectionListener);
        return model;
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
     * @throws ModelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */

    public User login(String username, String password) throws LoginException, ServiceException, ModelException {

        log.info("Starting AudioBoxClient: " + mUserAgent);

        this.sUser = (User) getModelInstance( USER_KEY , this.getMainConnector() );
        this.sUser.setUsername(username);
        this.sUser.setPassword(password);

        this.getMainConnector().execute( User.PATH, null, null, this.sUser, null);

        if ( ! User.ACTIVE_STATE.equalsIgnoreCase( this.sUser.getState() ) )
            throw new LoginException("User is not active", LoginException.INACTIVE_USER_STATE );

        return this.sUser;
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
     * Use this method to override the default {@link CollectionListener}.
     * 
     * @param collectionListener the CollectionListener to use for parser callbacks
     */

    public static void setCollectionListener(CollectionListener collectionListener) {
        sCollectionListener = collectionListener;
    }


    /**
     * This method will switch SSL certificate validation on or off.
     * You will not need to use this. This method is used for testing purpose only. For this reason this method is 
     * marked as "deprecated".
     * 
     * @param force set or unset the SSL certificate validation (false validates, true skips validation).
     */

    @Deprecated
    public void setForceTrust(boolean force) {
        this.getMainConnector().setForceTrust(force);
    }


    /**
     * Use this method to get the configured {@link CollectionListener}.
     * 
     * @return the current collection listener AudioBoxClient is using.
     */

    public static CollectionListener getCollectionListener() {
        return sCollectionListener;
    }


    public class AudioBoxConnector implements Serializable {

        private static final long serialVersionUID = -1947929692214926338L;

        private Log log = LogFactory.getLog(AudioBoxConnector.class);

        public static final String TEXT_FORMAT = "txt";
        public static final String XML_FORMAT = "xml";

        public static final int RESPONSE_CODE = 0;
        public static final int RESPONSE_BODY = 1;

        private static final String PATH_PARAMETER = "${path}";
        private static final String TOKEN_PARAMETER = "${token}";
        private static final String ACTION_PARAMETER = "${action}";

        private static final String PROTOCOL = "https";
        private static final String HOST = "audiobox.fm";
        private static final String PORT = "443";
        private static final String API_PREFIX = "/api/";
        public static final String API_PATH = PROTOCOL + "://" + HOST + API_PREFIX;

        private String sApiPath = API_PATH + PATH_PARAMETER + TOKEN_PARAMETER + ACTION_PARAMETER;
        private long sTimeout = (180 * 1000);

        private HttpRoute mAudioBoxRoute;
        private ThreadSafeClientConnManager mCm;
        private DefaultHttpClient mClient;
        private boolean mForceTrust = false;



        private AudioBoxConnector() {

            this.mAudioBoxRoute = new HttpRoute(new HttpHost(HOST, Integer.parseInt(PORT)));
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register( new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register( new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

            HttpParams params = new BasicHttpParams();
            params.setParameter(ConnManagerParams.TIMEOUT, sTimeout);
            params.setParameter(ClientPNames.HANDLE_REDIRECTS, false);

            this.mCm = new ThreadSafeClientConnManager(params, schemeRegistry);
            this.mClient = new DefaultHttpClient( this.mCm, params );

            // Increase max total connection to 200
            ConnManagerParams.setMaxTotalConnections(params, 200);

            // Increase default max connection per route to 20
            ConnPerRouteBean connPerRoute = new ConnPerRouteBean(20);

            // Increase max connections for audiobox.fm:443 to 50
            connPerRoute.setMaxForRoute(mAudioBoxRoute, 50);
            ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);

            if (mForceTrust)
                this.forceTrustCertificate();


            this.mClient.addRequestInterceptor(new HttpRequestInterceptor() {

                public void process( final HttpRequest request,  final HttpContext context) throws HttpException, IOException {
                    if (!request.containsHeader("Accept-Encoding")) {
                        request.addHeader("Accept-Encoding", "gzip");
                    }
                    request.addHeader("Authorization" , "Basic " + sUser.getAuth() );
                    request.addHeader("User-Agent", mUserAgent);
                }

            });

            this.mClient.addResponseInterceptor(new HttpResponseInterceptor() {

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

        public String[] execute(String path, String token, String action , Model target, String httpVerb) throws LoginException , ServiceException {
            return execute(path, token, action, target, httpVerb, XML_FORMAT);
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

        public String[] execute(String path, String token, String action , Model target, String httpVerb, String format) throws LoginException , ServiceException {

            token = ( token == null ) ? "" : token.startsWith("/") ? token : "/".concat(token);
            action = ( action == null ) ? "" : action.startsWith("/") ? action : "/".concat(action);

            // Replace the placeholder with the right values
            String url = sApiPath.replace( PATH_PARAMETER , path ).replace( TOKEN_PARAMETER , token ).replace( ACTION_PARAMETER , action ); 

            httpVerb = httpVerb == null ? HttpGet.METHOD_NAME : httpVerb;

            if (HttpGet.METHOD_NAME.equals(httpVerb) )
                url += "." + format;
            return request( url, target, httpVerb );
        }



        /* ----------------- */
        /* Protected methods */
        /* ----------------- */


        /**
         * This method will switch SSL certificate validation on or off.
         * You will not need to use this. This method is used for testing purpose only. For this reason this method is 
         * marked as "deprecated".
         * 
         * @param force set or unset the SSL certificate validation (false validates, true skips validation).
         */

        @Deprecated
        protected void setForceTrust(boolean force){
            this.mForceTrust = force;
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

        private String[] request(String url, Model target, String httpVerb) throws LoginException, ServiceException {

            if (sUser == null)
                throw new LoginException("Cannot execute API actions without credentials.", LoginException.NO_CREDENTIALS);


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

                HttpResponse resp = mClient.execute(method, new BasicHttpContext());
                String response = null;
                if ( target != null )
                    response = target.handleResponse( resp, httpVerb );

                HttpEntity responseEntity = resp.getEntity(); 
                if (responseEntity != null) responseEntity.consumeContent();

                return new String[]{ String.valueOf( resp.getStatusLine().getStatusCode() ) , response };

            } catch( ClientProtocolException e ) {
                throw new ServiceException( "Client protocol exception: " + e.getMessage(), ServiceException.CLIENT_ERROR );

            } catch( SocketTimeoutException e ) {
                throw new ServiceException( "Service does not respond: " + e.getMessage(), ServiceException.TIMEOUT_ERROR );

            } catch( IOException e ) {
                throw new ServiceException( "IO exception: " + e.getMessage(), ServiceException.SOCKET_ERROR );

            } 
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
        private void forceTrustCertificate() {

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
                this.mClient.getConnectionManager().getSchemeRegistry().register(https);
            } catch (NoSuchAlgorithmException e) {
                log.warn("Cannot force SSL certificate trust due to 'NoSuchAlgorithmException': " + e.getMessage());
            } catch (KeyManagementException e) {
                log.warn("Cannot force SSL certificate trust due to 'KeyManagementException': " + e.getMessage());
            }
        }

    }


}
