
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

package fm.audiobox.core.interfaces;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import fm.audiobox.core.exceptions.LoginException;


/**
 * <p>ResponseHandler interface.</p>
 *
 * @author Valerio Chiodino
 * @version 0.0.1
 */

public interface ResponseHandler {

    /**
     * <p>handleResponse</p>
     *
     * @param response a {@link org.apache.http.HttpResponse} object.
     * @param restAction a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     * @throws org.apache.http.client.ClientProtocolException if any.
     * @throws java.io.IOException if any.
     * @throws java.lang.IllegalStateException if any.
     * @throws fm.audiobox.core.exceptions.LoginException if any.
     */
    public String handleResponse(HttpResponse response, String restAction) throws ClientProtocolException, IOException, IllegalStateException, LoginException;
    
}
