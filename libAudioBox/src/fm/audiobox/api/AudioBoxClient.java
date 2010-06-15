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

package fm.audiobox.api;

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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import fm.audiobox.api.core.Model;
import fm.audiobox.api.exceptions.LoginException;
import fm.audiobox.api.exceptions.ServiceException;
import fm.audiobox.api.interfaces.AudioBoxModelLoader;
import fm.audiobox.api.interfaces.CollectionListener;
import fm.audiobox.api.models.Track;
import fm.audiobox.api.models.User;
import fm.audiobox.api.util.Inflector;

/**
 * 
 * AudioBoxClient is the main library class.<br/>
 * Use this object to make requests to AudioBox.fm API.<br />
 * Requests are made through Apache httpclient object.
 *
 * <p>
 * 
 * AudioBoxClient libs allows you to extends default models.
 * 
 * <p>
 * 
 * We embrace the "convention over configuration" phylosophy this is why you will need to store all your 
 * extended models in the same package.
 * 
 * <p>
 * 
 * In order to make AudioBoxClient load your extended models you will need to provide a full package path where 
 * your classes are stored.<br/> 
 * For instance the default models package is "fm.audiobox.api.models"; here you will find the default {@link Track}
 * model (example).
 * 
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * 
 * @version 0.1
 * 
 */

public class AudioBoxClient {

    // Internals
    public static final float VERSION = 0.1f;
    public static final String VERSION_NAME = "0.1-alpha";

    /** Message used when the response is succesfully parsed */
    public static final String PROTOCOL = "https";
    public static final String HOST = "audiobox.fm";
    public static final String PORT = "443";
    public static final String API_PREFIX = "/api/";
    public static final String API_SUFFIX = ".xml";
    public static final String TRACK_ID_PLACEHOLDER = "[track_id]";
    public static final String API_PATH = PROTOCOL + "://" + HOST + API_PREFIX;

    /** Specifies the models package (fm.audiobox.api.models) */
    public static final String DEFAULT_MODELS_PACKAGE = "fm.audiobox.api.models";
    private static final String PATH_PARAMETER = "${path}";
    private static final String TOKEN_PARAMETER = "${token}";
    private static final String ACTION_PARAMETER = "${action}";


    private static int sTimeout = (180 * 1000);
    private static boolean sForceTrust = false;
    private static String sUserAgent = "AudioBox.fm/"+ VERSION +" (Java; U; " +
    System.getProperty("os.name") + " " +
    System.getProperty("os.arch") + "; " + 
    System.getProperty("user.language") + "; " +
    System.getProperty("java.runtime.version") +  ") " +
    System.getProperty("java.vm.name") + "/" + 
    System.getProperty("java.vm.version") + 
    " AudioBoxClient/" + VERSION_NAME;

    private static String sCustomModelsPackage = DEFAULT_MODELS_PACKAGE;
    private static String sApiPath = API_PATH + PATH_PARAMETER + TOKEN_PARAMETER + ACTION_PARAMETER;
    private static Class<? extends User> sUserClass = User.class;
    private static Inflector sI = Inflector.getInstance();
    private static User sUser;

    
    /* ------------------ */
    /* Default Interfaces */
    /* ------------------ */

    /** @see {@link CollectionListener} */
    private static CollectionListener sCollectionListener = new CollectionListener() {
        public void onCollectionReady(int message) {  }
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
     * Use this method to configure the timeout limit for the reqests made against AudioBox.fm.
     * 
     * @param timeout the milliseconds of the timeout limit
     */

    public void setTimeout(int timeout) {
        sTimeout = timeout; 
    }


    /**
     * Returns the requests timeout limit.
     * 
     * @return timeout limit
     */

    public int getTimeout() {
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
     * To be sure to not waste memory or resource use this method to get the {@link Inflector} used in the parser.<br/>
     * Keep in mind that this is an util object and should not be overwritten.
     * 
     * @return the AudioBoxClient {@link Inflector} instance
     */

    public static Inflector getInflector() {
        return AudioBoxClient.sI;
    }


    /**
     * This method will set SSL certificate validation on or off.
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
     * @param httpVerb the HTTP method to use for the request (GET, PUT and POST are currently supported)
     * 
     * @return the result of the request, may be a response code, such as HTTP OK ("200") or the response itself.
     */

    public static String execute(String path, String token, String action , Model target, String httpVerb) throws LoginException , ServiceException {

        token = ( token == null ) ? "" : token.startsWith("/") ? token : "/".concat(token);
        action = ( action == null ) ? "" : action.startsWith("/") ? action : "/".concat(action);

        // Replace the placeholder with the right values
        String url = sApiPath.replace( PATH_PARAMETER , path ).replace( TOKEN_PARAMETER , token ).replace( ACTION_PARAMETER , action ); 

        httpVerb = httpVerb == null ? HttpGet.METHOD_NAME : httpVerb;

        if ( HttpGet.METHOD_NAME.equals(httpVerb) )
            url += API_SUFFIX;

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

            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            
            if (sForceTrust)
                forceTrustCertificate(client);

            params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, sTimeout);
            params.setParameter(ClientPNames.HANDLE_REDIRECTS, false);

            HttpRequestBase method = null; 
            if ( HttpPost.METHOD_NAME.equals(httpVerb) ) {
                method = new HttpPost(url);
            } else if ( HttpPut.METHOD_NAME.equals(httpVerb) ) {
                method = new HttpPut(url);
            } else {
                method = new HttpGet(url);
            }

            // Seting up default headers
            method.addHeader("Authorization" , "Basic " + sUser.getAuth());
            method.addHeader("Accept-Encoding", "gzip");
            method.addHeader("User-Agent", sUserAgent);

            HttpResponse resp = client.execute(method);
            HttpEntity entity = resp.getEntity();
            int responseCode = resp.getStatusLine().getStatusCode();
            String response = String.valueOf(responseCode);

            /**
             * We will look at the response code and take decision based upon it.
             * Assumption are done:
             * 
             * - 200: Parsable entity 
             * - 303: Redirect to the right stream url
             * - 401, 403: User cannot access the requested resource
             * 
             * If none of previous code has been returned Service error is thrown.
             */

            switch( responseCode ) {

            case HttpStatus.SC_OK:

                if ( target != null ) {

                    try {

                        // Instanciate new SaxParser from InputStream
                        SAXParserFactory spf = SAXParserFactory.newInstance();
                        SAXParser sp = spf.newSAXParser();

                        /* Get the XMLReader of the SAXParser we created. */
                        XMLReader xr = sp.getXMLReader();

                        /* Create a new ContentHandler and apply it to the XML-Reader */
                        xr.setContentHandler( target );


                        final InputStream is = new GZIPInputStream( entity.getContent() );

                        xr.parse( new InputSource( is ) );

                        is.close();

                    } catch( SAXException e) {
                        throw new ServiceException( "SAX exception: " + e.getMessage() );
                    } catch( ParserConfigurationException e) {
                        throw new ServiceException( "Parser exception: " + e.getMessage() );
                    }
                }
                break;

            case HttpStatus.SC_SEE_OTHER:

                // Return the correct location header
                response = resp.getFirstHeader("Location").getValue();
                break;

            case HttpStatus.SC_FORBIDDEN:
            case HttpStatus.SC_UNAUTHORIZED:
                throw new LoginException("Unauthorized response: " + responseCode, responseCode);

            default:
                throw new ServiceException( "An error occurred", ServiceException.GENERIC_SERVICE_ERROR );
            }

            sCollectionListener.onCollectionReady( CollectionListener.DOCUMENT_PARSED );

            // Free resources
            entity.consumeContent();
            client.getConnectionManager().shutdown();
            
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
