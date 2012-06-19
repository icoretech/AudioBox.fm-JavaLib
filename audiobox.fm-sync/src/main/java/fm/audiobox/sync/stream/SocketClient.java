/***************************************************************************
 *   Copyright (C) 2010 iCoreTech research labs                            *
 *   Contributed code from:                                                *
 *   - Lucio Regina		 						                                         *
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

import fm.audiobox.AudioBox;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Action;
import fm.audiobox.core.models.Args;
import fm.audiobox.core.parsers.JParser;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SocketClient extends Observable implements IOCallback {

  private static Logger log = LoggerFactory.getLogger(SocketClient.class);

  
  public static enum EventsTypes {
    MESSAGE
  }
  
  private AudioBox audiobox;
  private IConfiguration configuration;
  private SocketIO socket;
  
  /** for knowing if we were connected, in case of logout and re-login */
  private boolean wasConnected = false;
  
  private boolean connected = false;
  
  public SocketClient(AudioBox abx) throws ServiceException {
    this.audiobox = abx;
    this.configuration = this.audiobox.getConfiguration();
    
    // I'm not sure it is needed
    this.configuration.getFactory().setEntity( Action.TAGNAME, Action.class );
    this.configuration.getFactory().setEntity( Args.TAGNAME, Args.class );
    
    
    // Observer for catching the User status change events (login/logout)
    this.audiobox.addObserver(new Observer() {
      @Override
      public void update(Observable abx, Object usr) {
        // new User has changed its status
        
        log.info("User has changed its status");
        if ( usr == null ){
          // User is no longer logged in
          
          /**
           *  We want to store the oringal state of the socket
           *  because the {@link disconnect} method reset the flag
           */
          boolean originaWasConnected = SocketClient.this.isConnected(); 
          
          SocketClient.this.disconnect();

          if ( originaWasConnected )
            // We were connected, we're storing this flag
            // in order to perform a new connection once User is logged in
            wasConnected = true;
        
        } else if ( wasConnected ){
          // User is logged in, and we were connected.
          // We should reconnect
          try {
            SocketClient.this.connect();
          } catch (ServiceException e) {
            log.error( "An error occurred while connecting to server", e);
            if ( SocketClient.this.audiobox.getConfiguration().getDefaultServiceExceptionHandler() != null )
              SocketClient.this.audiobox.getConfiguration().getDefaultServiceExceptionHandler().handle( e );
          }
        }
      }
    });
    
  }


  /**
   * Connects to Socket server and returns {@code true} it everithing went ok
   * @return boolean
   */
  public void connect() throws ServiceException {
    
    log.info("Trying to connect to server");
    if ( this.isConnected() ){
      this.disconnect();
      
      // restoring interval flag
      this.wasConnected = false;
    }
    
    if ( this.audiobox.getUser() != null ){
      
      String urlStr = this.getServerUrl();
      URL url = null;
      try {
        url = new URL( urlStr + this.audiobox.getUser().getAuthToken() );
        log.info("Server will be " + url.toURI().toString() );
        
      } catch (MalformedURLException e) {
        log.error("Invalid url found");
        throw new ServiceException("No valid URL for server found");
      } catch (URISyntaxException e) {
        log.error("Invalid url found");
        throw new ServiceException("No valid URL for server found");
      }
      
      this.socket = new SocketIO( url );
      this.socket.addHeader(IConnector.X_AUTH_TOKEN_HEADER, this.audiobox.getUser().getAuthToken() );
      this.socket.connect(this);
    } else {
      // User is not logged in.
      // We are preparing for connecting as soon as user will be logged in
      this.wasConnected = true;
    }
  }
  
  
  public boolean isConnected(){
    return this.connected;
  }
  
  
  public void disconnect() {
    if ( this.isConnected() && this.socket != null){
      this.socket.disconnect();
    }
    this.wasConnected = false;
  }
  
  
  protected String getServerUrl() {
    String protocol = this.configuration.getProtocol(IConfiguration.Connectors.DAEMON);
    String host = this.configuration.getHost(IConfiguration.Connectors.DAEMON);
    String port = "" + this.configuration.getPort(IConfiguration.Connectors.DAEMON);
    log.info("URL found: " + protocol + "://" + host + ":" + port);
    return protocol + "://" + host + ":" + port + IConnector.URI_SEPARATOR;
  }


  
  
  @Override
  public void on(String event, IOAcknowledge ack, JsonElement... args) {
    log.info( "An event emitted", args );
    String messageEvent = EventsTypes.MESSAGE.toString().toLowerCase();
    if ( messageEvent.equals( event ) ) {
      log.info( "A message received with " + args.length + " arguments" );
      
      for ( JsonElement json : args ) {
        log.debug("firing actions for '" + json.toString() + "'");
        this.onMessage( json, ack);
      }
    }
  }


  @Override
  public void onDisconnect() {
    log.info("Disconnected!");
    SocketClient.this.connected = false;
  }


  @Override
  public void onConnect() {
    log.info("Connected!");
    SocketClient.this.connected = true;
    SocketClient.this.wasConnected = true;
  }


  @Override
  public void onMessage(String data, IOAcknowledge ack) {
    log.info("Text message received" + data);
  }


  @Override
  public void onError(SocketIOException ex) {
    log.error( "Error occurs" + ex.getMessage(), ex );
    
    
    boolean socketConnected = false;
    try {
      socketConnected = this.socket.isConnected();
    } catch( Exception e ) {
      // Sylently fails
    }
    
    
    boolean shouldReconnect = this.wasConnected;
    
    if ( socketConnected ){
      
      log.warn("Socket is still connected, we want to disconnect just to be sure");
      try {
        
        this.disconnect();
        
      } catch(Exception e){
        // Sylently fails
      }
    }
    
    if ( shouldReconnect ){
      log.warn("try to reconnect");
      
      this.connected = false;
      this.wasConnected = false;
      
      try {
        this.connect();
      } catch( Exception e){
        log.error( "Unable to reconnect to the server", e);
      }
    }
  }


  @Override
  public void onMessage(JsonElement json, IOAcknowledge arg1) {
    
    try {
      log.info("Action received");
  
      // {action: {name: "stream", id: 123, args:{filename: "", rangemin: 0, rangemax: 1000, etag: ""}} }
      
      JsonObject jobj = json.getAsJsonObject();
      if( jobj != null && jobj.isJsonObject() ){
        
        log.debug("message is: " + jobj.toString() );
        
        Action action = new Action(this.configuration);
        JParser jp = new JParser( action );
        
        jp.parse( jobj );
        
        // Message received: notify observers
        // Should we do that via Thread?!
        this.setChanged();
        this.notifyObservers(action);
        
      } else {
        log.error("Invalid message received: " + json.toString() );
      }
    } catch(Exception e) {
      log.error("Error while reveiving message: " + json.toString(), e );
    }
  }

  
  

}
