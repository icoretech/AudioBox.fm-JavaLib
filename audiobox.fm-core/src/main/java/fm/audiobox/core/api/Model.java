
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
import java.util.Observable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import fm.audiobox.AudioBox;
import fm.audiobox.AudioBox.Connector;
import fm.audiobox.AudioBox.RequestFormat;
import fm.audiobox.AudioBox.Utils;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.User;


/**
 * Model is an abstract class that provides the RESTful API parser for each 
 * XML AudioBox.fm response.
 * 
 * <p>
 * 
 * It basically transforms XML responses into comfortable Java objects.
 * 
 * <p>
 * 
 * It implements a {@link ResponseHandler} called back from the HttpClient object.
 * 
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public abstract class Model extends Observable implements ResponseHandler<String[]> {

    /** Constant that defines bytes dimention to be read from responses {@link InputStream} */
    protected static final int CHUNK = 4096;

    private static Log log = LogFactory.getLog(Model.class);

    // Default models variables
    protected String pName;
    protected String pEndPoint;
    protected Utils pUtils;


    /**
     * <p>Getter for the end point of this model.</p>
     *
     * @return the model specific API end point.
     */
    public final String getEndPoint(){
        return this.pEndPoint;
    }

    /**
     * <p>Getter for {@link AudioBox.Connector} instance.</p>
     *
     * @return the {@link AudioBox.Connector} object used by this model.
     */
    public Connector getConnector(){
        return this.pUtils.getConnector();
    }
    
    /**
     * <p>Getter for {@link User} instance.</p>
     *
     * @return the {@link User} object used by this model.
     */
    protected User getUser(){
    	return this.pUtils.getUser();
    }

    /**
     * <p>Setter for the {@link AudioBox.Utils Utils} instance.</p>
     *
     * @param utils a {@link AudioBox.Utils} object.
     */
    public final void setUtils(Utils utils){
        this.pUtils = utils;
    }
    
    
    /**
     * This method refreshes current {@link ModelItem} instance
     * executing a new request and populating its fields
     */
    public boolean refresh() throws LoginException, ServiceException {
    	String[] result = this.getConnector().get(this, this, null);
    	return Integer.parseInt( result[ Connector.RESPONSE_CODE ] ) == HttpStatus.SC_OK;
    }
    
    /**
     * <p>Getter for the name of this model, may be vary depending the Model extension.</p>
     *
     * @return the name of the model.
     */
    public String getName() {
        return this.pName;
    }

    /**
     * <p>Setter for the model name: used by the parser.</p>
     *
     * @param name the name of the model.
     */
    public void setName(String name) {
        this.pName = name;
    }

    /** {@inheritDoc} */
    @Override
    public String[] handleResponse(HttpResponse response) throws IOException {

        int responseCode = response.getStatusLine().getStatusCode();
        String responseString = "";

        switch( responseCode ) {

        // 20*
        case HttpStatus.SC_CREATED:
        case HttpStatus.SC_OK:
            
            responseString = this.parseResponse( response );
            break;

        
        case HttpStatus.SC_NO_CONTENT:
            responseString = "Resource not ready";
            break;

            // 30*
        case HttpStatus.SC_SEE_OTHER:
            responseString = response.getFirstHeader("Location").getValue();
            break;

            // 401, 403
        case HttpStatus.SC_UNAUTHORIZED:
        case HttpStatus.SC_FORBIDDEN:
        	{String reason = this._parsePlainResponse(response.getEntity().getContent() );
        	throw new LoginException( responseCode, reason );}
        	
        // 50x
        default:
            
            String message = "";
            int status = responseCode;
            
        	fm.audiobox.core.models.Error error = new fm.audiobox.core.models.Error();
        	
        	try {
        	    error.parseXMLResponse( response.getEntity().getContent() );
        	    message = error.getMessage();
        	    status = error.getStatus();
        	} catch(IOException e) { message = e.getMessage(); }
        	
        	throw new ServiceException( status, message );
        }

        HttpEntity responseEntity = response.getEntity(); 
        if (responseEntity != null) 
            responseEntity.consumeContent();

        return new String[]{ String.valueOf( responseCode ) , responseString };

    }

    /**
     * This method is used to determine the kind of response and parse the HTTP response content if any.
     * 
     * <p>
     * 
     * Returns an empty String if the content type is XML or binary, the body of the response otherwise.
     *
     * @param response the {@link HttpResponse} object
     * 
     * @return the text of the response content  
     * 
     * @throws IOException if the parse process fails for some reason.
     */
    public final String parseResponse( HttpResponse response ) throws IOException {

        Header contentType = response.getEntity().getContentType();
        String rsp = "";
        if ( contentType.getValue().contains( RequestFormat.XML.toString().toLowerCase() ) )
            rsp = this.parseXMLResponse( response.getEntity().getContent() );

        else if ( contentType.getValue().contains( RequestFormat.TEXT.toString().toLowerCase() )  )
            rsp = this.parsePlainResponse( response.getEntity().getContent() );
        
        else
        	rsp = this.parseBinaryResponse( response );

        HttpEntity ent = response.getEntity();
        if (ent != null)
            ent.consumeContent();
        
        return rsp;
    }

    /**
     * This method is used to parse XML responses
     * 
     * @param input the {@link InputStream} of the entity content.
     * 
     * @return an empty String
     * @throws IOException if the parse process fails for some reason.
     */
    protected String parseXMLResponse( InputStream input ) throws IOException {

        String message = null;
        
        try {

            // Instanciate new SaxParser
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();

            /* Get the XMLReader of the SAXParser we created. */
            XMLReader xr = sp.getXMLReader();

            /* Create a new ContentHandler and apply it to the XML-Reader */
            xr.setContentHandler( this.pUtils.getModelParser(this) );

            xr.parse( new InputSource( input ) );

            input.close();

        } catch (ParserConfigurationException e) {
            message = "Parser misconfiguration: " + e.getMessage();
            log.error(message, e);
        } catch (SAXException e) {
            message = "Failed to parse response: " + e.getMessage();
            log.error(message, e);
        }
        
        if (message != null) {
            log.fatal(message);
            throw new IOException(message);
        }
        
        return "";
    }

    /**
     * This method is used to parse plain text responses.
     * 
     * @param input the {@link InputStream} of the entity content.
     * 
     * @return a String representing the body of the response
     */
    protected String parsePlainResponse(InputStream input) throws IOException {
    	return this._parsePlainResponse(input);
    }
    
    
    /**
     * This method implementation does nothing. It must be overridden by each model that should contains
     * a binary response in the body. Tipically {@link Track} objects.
     * 
     * @param response the {@link HttpResponse} object 
     * 
     * @return an empty String
     */
    protected String parseBinaryResponse( HttpResponse response ) throws IOException {
        return "";
    }

    
    
    /**
     * This method is used to parse plain text responses
     * 
     * @param input	the {@link InputStream} of the entity content
     * @return String that represents the response body as text
     * @throws IOException
     */
    private String _parsePlainResponse(InputStream input) throws IOException {
    	int read;
        byte[] bytes = new byte[ CHUNK ];
        StringBuffer sb = new StringBuffer();
        while(  ( read = input.read( bytes) ) != -1 )
            sb.append( new String( bytes, 0, read ));
        return sb.toString().trim();
    }
    
    
    /* --------- */
    /* Overrides */
    /* --------- */


    /** {@inheritDoc} */
    @Override
    public String toString() {
        return this.getName();
    }


}
