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

public class ServiceException extends java.net.ConnectException {

    public static final int SOCKET_ERROR = 1;
    public static final int CLIENT_ERROR = 2;
    public static final int TIMEOUT_ERROR = 3;
    public static final int GENERIC_SERVICE_ERROR = 4;
    
    private static final long serialVersionUID = 1L;

    private int errorCode;
	
	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(String message, int errorCode) {
	    this.errorCode = errorCode;
	}
	
	public int getErrorCode(){
		return errorCode;
	}
}
