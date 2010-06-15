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
import fm.audiobox.api.models.User;
import fm.audiobox.api.util.Inflector;

public class AudioBoxClient {

    // Internals
    public static final float VERSION = 0.1f;
    public static final String VERSION_NAME = "0.1-alpha";
    
    public static final int DOCUMENT_PARSED = -200;
    public static final String PROTOCOL = "https";
    public static final String HOST = "audiobox.fm";
    public static final String PORT = "443";
    public static final String API_PREFIX = "/api/";
    public static final String API_SUFFIX = ".xml";
    public static final String TRACK_ID_PLACEHOLDER = "[track_id]";
    public static final String API_PATH = PROTOCOL + "://" + HOST + API_PREFIX;

    private static final String DEFAULT_MODELS_PACKAGE = "fm.audiobox.api.models";
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

    // Default Interfaces
    protected static CollectionListener cl = new CollectionListener() {
        public void onCollectionReady(int message) {  }
        public void onItemReady(int item, Object obj) { }
    };

    protected static AudioBoxModelLoader abml = new AudioBoxModelLoader() {
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

    public User getUser() {
        return sUser;
    }

    public void setTimeout(int timeout) {
        sTimeout = timeout; 
    }

    public int getTimeout() {
        return sTimeout;
    }

    public void setCollectionListener(CollectionListener collectionListener) {
        cl = collectionListener;
    }

    public void setAudioBoxModelLoader(AudioBoxModelLoader modelLoader) {
        abml = modelLoader;
    }


    /* -------------- */
    /* Static methods */
    /* -------------- */

    public static Inflector getInflector() {
        return AudioBoxClient.sI;
    }
    
    public static void setForceTrust(boolean force) {
        AudioBoxClient.sForceTrust = force;
    }

    public static boolean hasForceTrustEnabled() {
        return sForceTrust;
    }

    /**
     * @param customModelsPackage the customModelsPackage to set
     */
    public static void setUserClass(Class<? extends User> clazz) {
        AudioBoxClient.sUserClass = clazz;
    }

    /**
     * @param customModelsPackage the customModelsPackage to set
     */
    public static void setCustomModelsPackage(String customModelsPackage) {
        AudioBoxClient.sCustomModelsPackage = customModelsPackage;
    }

    /**
     * @return the customModelsPackage
     */
    public static String getCustomModelsPackage() {
        return sCustomModelsPackage;
    }

    public static CollectionListener getCollectionListener() {
        return cl;
    }

    public static AudioBoxModelLoader getAudioBoxModelLoader() {
        return abml;
    }

    public static String execute(String path, String token, String action , Model target, String httpVerb) throws LoginException , ServiceException {

        token = ( token == null ) ? "" : token.startsWith("/") ? token : "/".concat(token);
        action = ( action == null ) ? "" : action.startsWith("/") ? action : "/".concat(action);

        String url = sApiPath.replace( PATH_PARAMETER , path ).replace( TOKEN_PARAMETER , token ).replace( ACTION_PARAMETER , action ); 

        httpVerb = httpVerb == null ? HttpGet.METHOD_NAME : httpVerb;

        if ( HttpGet.METHOD_NAME.equals(httpVerb) )
            url += API_SUFFIX;

        return request( url, target, httpVerb );
    }

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

            method.addHeader("Authorization" , "Basic " + sUser.getAuth());
            method.addHeader("Accept-Encoding", "gzip");
            method.addHeader("User-Agent", sUserAgent);

            HttpResponse resp = client.execute(method);

            int responseCode = resp.getStatusLine().getStatusCode();

            if ( responseCode == HttpStatus.SC_OK ){
                if ( target != null ) {

                    try {

                        // Instanciate new SaxParser from InputStream
                        SAXParserFactory spf = SAXParserFactory.newInstance();
                        SAXParser sp = spf.newSAXParser();

                        /* Get the XMLReader of the SAXParser we created. */
                        XMLReader xr = sp.getXMLReader();

                        /* Create a new ContentHandler and apply it to the XML-Reader */
                        xr.setContentHandler( target );

                        final InputStream is = new GZIPInputStream( resp.getEntity().getContent() );

                        xr.parse( new InputSource( is ) );

                    } catch( SAXException e) {
                        throw new ServiceException( "SAX exception: " + e.getMessage() );
                    } catch( ParserConfigurationException e) {
                        throw new ServiceException( "Parser exception: " + e.getMessage() );
                    }
                }

            } else if ( responseCode == HttpStatus.SC_SEE_OTHER ){

                // Return the correct location header
                return resp.getFirstHeader("Location").getValue();

            } else if ( responseCode == HttpStatus.SC_FORBIDDEN || responseCode == HttpStatus.SC_UNAUTHORIZED ) {

                throw new LoginException("Unauthorized response: " + responseCode, responseCode);

            } else {
                throw new ServiceException( "An error occurred", ServiceException.GENERIC_SERVICE_ERROR );
            }

            cl.onCollectionReady( DOCUMENT_PARSED );

            return String.valueOf(responseCode);

        } catch( ClientProtocolException e ) {
            throw new ServiceException( "Client protocol exception: " + e.getMessage(), ServiceException.CLIENT_ERROR );
        } catch( SocketTimeoutException e ) {
            throw new ServiceException( "Service does not respond: " + e.getMessage(), ServiceException.TIMEOUT_ERROR );
        } catch( IOException e ) {
            e.printStackTrace();
            throw new ServiceException( "IO exception: " + e.getMessage(), ServiceException.SOCKET_ERROR );
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException( "IO exception: " + e.getMessage(), ServiceException.SOCKET_ERROR );
        } catch (KeyManagementException e) {
            throw new ServiceException( "IO exception: " + e.getMessage(), ServiceException.SOCKET_ERROR );
        }
    }

    
    private static void forceTrustCertificate(HttpClient client) throws NoSuchAlgorithmException, KeyManagementException {
        
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

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[0], new TrustManager[] { easyTrustManager }, null);
        SSLSocketFactory sf = new SSLSocketFactory(ctx);
        sf.setHostnameVerifier( hnv );
        Scheme https = new Scheme("https", sf, 443);
        client.getConnectionManager().getSchemeRegistry().register(https);
        
    }

}
