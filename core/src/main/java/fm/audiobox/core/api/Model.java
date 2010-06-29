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

package fm.audiobox.core.api;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.interfaces.ResponseHandler;
import fm.audiobox.core.models.AudioBoxClient;
import fm.audiobox.core.models.AudioBoxClient.AudioBoxConnector;
import fm.audiobox.core.util.Inflector;

public abstract class Model extends DefaultHandler implements ResponseHandler {

    private static final String ADD_PREFIX = "add";
    private static final String SET_PREFIX = "set";

    public static final int SAX_ERROR_CODE = -1000;
    public static final int PARSER_CONFIGURATION_ERROR_CODE = -1001;
    public static final int IO_ERROR_CODE = -1002;
    protected static final int CHUNK = 4096;

    private static Log log = LogFactory.getLog(Model.class);

    private boolean mSkipField = false;
    private Stack<Object> mStack;
    private Inflector mInflector = Inflector.getInstance();
    private StringBuffer mStringBuffer = new StringBuffer();

    // Default models variables
    protected String name;
    protected String token;
    protected String endPoint;
    protected AudioBoxConnector connectorS;


    public void setConnector(AudioBoxConnector conn){
    	this.connectorS = conn;
    }
    public AudioBoxConnector getConnector(){
    	return this.connectorS;
    }
    
    public String toString() {
        return this.getName();
    }

    public final void setEndPoint(String endPoint){
        this.endPoint = endPoint;
    }

    public final String getEndPoint(){
        return this.endPoint;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return this.token;
    }

    public final void setToken(String token) {
        this.token = token;
    }

    public final String handleResponse(HttpResponse response, String httpVerb) throws ClientProtocolException, IOException, IllegalStateException, LoginException {

        int responseCode = response.getStatusLine().getStatusCode();
        String responseString = "";//String.valueOf( responseCode );

        switch( responseCode ) {

	        case HttpStatus.SC_OK:
	            InputStream input = response.getEntity().getContent();
	            responseString = this.parseResponse( input , response.getEntity().getContentType() );
	            response.getEntity().consumeContent();
	            break;
	
	        case HttpStatus.SC_SEE_OTHER:
	            responseString = response.getFirstHeader("Location").getValue();
	            break;
	
	        case HttpStatus.SC_FORBIDDEN:
	        case HttpStatus.SC_UNAUTHORIZED:
	            throw new LoginException("Unauthorized user", responseCode);
	
	        default:
	            throw new ServiceException( "An error occurred", ServiceException.GENERIC_SERVICE_ERROR );

        }

        return responseString;
    }

    public String parseResponse( InputStream input, Header contentType ) throws IOException {
    	String response = "";
        try {
        	
        	if ( contentType.getValue().contains(AudioBoxConnector.XML_FORMAT) ){

	            // Instanciate new SaxParser from InputStream
	            SAXParserFactory spf = SAXParserFactory.newInstance();
	            SAXParser sp = spf.newSAXParser();
	
	            /* Get the XMLReader of the SAXParser we created. */
	            XMLReader xr = sp.getXMLReader();
	
	            /* Create a new ContentHandler and apply it to the XML-Reader */
	            xr.setContentHandler( this );
	
	            xr.parse( new InputSource( input ) );
	
	            input.close();
	            
        	} else {
        		/* read response content */
        		int read;
        		byte[] bytes = new byte[ CHUNK ];
        		StringBuffer sb = new StringBuffer();
        		while(  ( read = input.read( bytes) ) != -1 )
        			sb.append( new String( bytes, 0, read ));
        		response =  sb.toString().trim();
        	}

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return response;
    }


//    public void setAudioBoxModelLoader(AudioBoxModelLoader abml) {
//        this.abml = abml;
//    }

//    public void invoke() throws ServiceException, LoginException {
//        AudioBoxClient.execute( this.getEndPoint(), this.getToken(), null, this, HttpGet.METHOD_NAME);
//    }


    /* --------- */
    /* Overrides */
    /* --------- */


    @Override
    public final void startDocument() throws SAXException {
        this.mStack = new Stack<Object>();
        this.mStack.push( this );
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        this.mStack = null;
        super.endDocument();
    }


    @Override
    public final void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        Object peek = this.mStack.peek();

        mSkipField = false;

        try {
            if (localName.trim().length() == 0)
                localName = qName;

            localName =  mInflector.upperCamelCase( localName, '-' );

            String methodPrefix = SET_PREFIX;
            String scm = mInflector.singularize( peek.getClass().getSimpleName() );
            if (!scm.equals( peek.getClass().getSimpleName() ) && localName.equals( scm  ) ) {
                methodPrefix = ADD_PREFIX;
            }

            String methodName =  methodPrefix + localName;
            Method method = null;
            try {
                method = peek.getClass().getMethod(methodName, String.class);
            } catch (NoSuchMethodException e) {
                for (Method m : peek.getClass().getMethods()) {
                    if (m.getName().equals( methodName )) {
                        method = m;
                        break;
                    }
                }
            }


            if (method == null)
                mSkipField = true;

            if ( !mSkipField ) {

                Class<?> argType = method.getParameterTypes()[0];

                if ( ! argType.isPrimitive() && ! argType.equals(String.class) ){

                    Model subClass = null;
                    try {
                        subClass = AudioBoxClient.getModelInstance( mInflector.lowerCamelCase( localName, '-'), this.getConnector() );
                        method.invoke(peek, subClass );

                        this.mStack.push( subClass );
                    } catch (ModelException e) {
                        e.printStackTrace();
                        
                    }
					
                } else {
                    this.mStack.push( method );
                }
            }
        } catch (IllegalArgumentException e) {
            log.error("Illegal Argument Exception @" + localName + ": " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            log.error("Illegal AccessException @" + localName + ": " + e.getMessage());
            e.printStackTrace();
        } catch (SecurityException e) {
            log.error("Security Exception @" + localName + ": " + e.getMessage());
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            log.error("Invocation Target Exception @" + localName + ": " + e.getMessage());
            e.printStackTrace();
        } 

        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if ( !mSkipField ) {

            String _temp = mStringBuffer.toString();
            _temp = _temp.replace("\n","").trim();
            mStringBuffer = new StringBuffer();


            if (this.mStack.peek() instanceof Method) {

                Method method = ( Method ) this.mStack.peek();
                Object _dest = this.mStack.get(  this.mStack.size() - 2 );

                try {
                    if (_temp.trim().length() > 0) {
                        method.invoke(_dest, _temp);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }

            this.mStack.pop();

        }

        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        if ( !mSkipField ) {
            mStringBuffer.append( String.valueOf( ch , start , length ) );
        }

        super.characters(ch, start, length);
    }


}
