
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

package fm.audiobox.core.exceptions;


/**
 * This exception is thrown whenever an {@link InstantiationException}, {@link IllegalAccessException} or
 * a {@link ClassNotFoundException} occurs on a Model instantiation.
 *
 * @author Valerio Chiodino
 * @version 0.0.1
 */
public class ModelException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private int errorCode;
	
	/**
	 * <p>Constructor for ModelException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public ModelException(String message) {
		super(message);
	}
	
	/**
	 * <p>Constructor for ModelException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @param code a int.
	 */
	public ModelException(String message, int code) {
	    super(message);
	    errorCode = code;
	}
	
	/**
	 * <p>Getter for the field <code>errorCode</code>.</p>
	 *
	 * @return a int.
	 */
	public int getErrorCode() {
	    return errorCode;
	}
	
}
