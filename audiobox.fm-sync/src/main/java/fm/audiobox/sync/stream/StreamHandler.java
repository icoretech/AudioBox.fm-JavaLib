/***************************************************************************
 *   Copyright (C) 2010 iCoreTech research labs                            *
 *   Contributed code from:                                                *
 *   - Lucio Regina		 						                           *
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
package fm.audiobox.sync.stream;

import io.socket.IOCallback;
import io.socket.SocketIO;

import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.AudioBox;
import fm.audiobox.configurations.DefaultConfiguration;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Action;
import fm.audiobox.core.models.Args;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.ContentFormat;

public class StreamHandler implements Observer{

  private static Logger log = LoggerFactory.getLogger(StreamHandler.class);

  private AudioBox ab;
  private static final String NAME_APP = "StreamHandler";  
  private transient String auth_token;

  private static final String AUTHTOKENPARAM = "x-auth-token";

  private IConfiguration config;
  private SocketIO soket;

  public StreamHandler() {
    this(getDefaultConfiguration());
  }

  public StreamHandler(IConfiguration config) {		
    this.config = config;
    this.config.getFactory().setEntity( Action.TAGNAME, Action.class);
    this.config.getFactory().setEntity( Args.TAGNAME, Args.class);
    ab = new AudioBox(config);
  }

  public void login(String user, String pwd) throws LoginException, ServiceException {
    ab.login(user, pwd);
    this.auth_token = ab.getUser().getAuthToken();
    
  }

  private static IConfiguration getDefaultConfiguration(){
    IConfiguration config = new DefaultConfiguration(NAME_APP);
    config.setRequestFormat(ContentFormat.JSON);
    config.setUseCache(false);

    return config;
  }
  
  public SocketIO getSocketIO() throws MalformedURLException{
    DefaultCallBack cb = new DefaultCallBack( this.config );
    cb.addObserver(this);
    return getSocketIO(cb);
  }

  public SocketIO getSocketIO(IOCallback callback) throws MalformedURLException{
    this.soket = new SocketIO();
    this.soket.addHeader(AUTHTOKENPARAM, this.auth_token);
    String url = this.config.getProtocol(IConfiguration.Connectors.NODE) + "://" + this.config.getHost(IConfiguration.Connectors.NODE) + ":" + this.config.getPort(IConfiguration.Connectors.NODE) + "/";
    
    this.soket.connect( url , callback);
    return this.soket;
  }

  @Override
  public void update(Observable arg0, Object arg1) {
    if( arg1 instanceof Runnable){
      
    }
  }
}
