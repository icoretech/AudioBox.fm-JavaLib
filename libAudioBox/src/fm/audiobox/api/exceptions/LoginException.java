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

package fm.audiobox.api.exceptions;

public class LoginException extends javax.security.auth.login.LoginException {

    public static final int NO_CREDENTIALS = -400;
    public static final int INACTIVE_USER_STATE = -200;
    
	private static final long serialVersionUID = 1L;
	private int errorCode;
	
	public LoginException(String message) {
		super(message);
	}
	
	public LoginException(String message, int code) {
	    super(message);
	    errorCode = code;
	}
	
	public int getErrorCode() {
	    return errorCode;
	}
	
}
