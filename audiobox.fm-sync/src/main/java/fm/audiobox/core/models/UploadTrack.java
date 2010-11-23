
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

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.content.FileBody;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.AudioBoxClient.AudioBoxConnector;


/**
 * Classes that extends {@link Track} and implements upload methods.
 * 
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class UploadTrack extends Track {

    private HttpRequestBase method;
	
	/** Default constructor */
    public UploadTrack() {
        super();
    }

    /** 
     * Setter for <code>fileBody</code> field.
     * 
     * @param fileBody the {@link FileBody} to set
     */
    public void setFileBody( FileBody fileBody ) {
        this.fileBody = fileBody;
    }


    /**
     * Performs the request and sends the file to upload to AudioBox.fm cloud service.
     * 
     * @throws LoginException if any authenticity problem occurs
     * @throws ServiceException if any remote service problem occurs
     */
    public void upload() throws LoginException, ServiceException  {
    	method = this.pConnector.createConnectionMethod(Tracks.END_POINT , null, null, this, HttpPost.METHOD_NAME);
    	String result[] = this.pConnector.request(method, this, false);
    	this.setToken( result[ AudioBoxConnector.RESPONSE_BODY ]  );
    }

    /** {@inheritDoc} */
	@Override
	public String parseBinaryResponse(HttpResponse response) throws IOException {
		if ( this.fileOutputStream == null )
			return super.parsePlainResponse( response.getEntity().getContent() );
		return super.parseBinaryResponse(response);
	}
    
	/**
	 * Getter method for the field <code>method</code>
	 * 
	 * @return the {@link HttpRequestBase} method used to perform the upload.
	 */
    public HttpRequestBase getConnectionMethod(){
    	return this.method;
    }

}
