
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

import fm.audiobox.core.api.ModelItem;



/**
 * Error class is a {@link ModelItem} specialization for error XML elements.
 * 
 * <p>
 * 
 * Error XML looks like this: 
 * 
 * <pre>
 * {@code
 * <error>
 *   <status>500</name>
 *   <message>This is an error message</token>
 * </error>
 * }
 * </pre>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 */
public class Error extends ModelItem {

    /** The XML tag name for the Error element */
    public static final String TAG_NAME = "error";
    
    protected int status;
    protected String message;
    
    /**
     * <p>Constructor for Error.</p>
     */
    public Error() { }
    
    
    /* ------------------- */
    /* Getters and setters */
    /* ------------------- */
    
    
    /**
     * <p>Setter for the field <code>status</code>.</p>
     *
     * @param status the error status to set
     */
    public void setStatus(String status) {
        this.status = Integer.parseInt(status, 10);
    }
    
    /**
     * <p>Getter for the field <code>status</code>.</p>
     *
     * @return the status
     */
    public int getStatus() {
        return this.status;
    }
    
    
    
    /**
     * <p>Setter for the field <code>message</code>.</p>
     *
     * @param message the error message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * <p>Getter for the field <code>message</code>.</p>
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

}

