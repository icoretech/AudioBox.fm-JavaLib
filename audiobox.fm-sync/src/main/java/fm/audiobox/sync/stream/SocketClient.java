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
import fm.audiobox.core.models.Error;
import fm.audiobox.core.observables.Event;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

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
  
  private Map<String, String> headers = new HashMap<String, String>();
  
  /** for knowing if we were connected, in case of logout and re-login */
  private boolean wasConnected = false;
  
  private boolean connected = false;
  
  private boolean reconnectOnError = false;
  
  private IConfiguration.Connectors server;
  
  public SocketClient(AudioBox abx, IConfiguration.Connectors server) throws ServiceException {
    this.audiobox = abx;
    this.server = server;
    this.configuration = this.audiobox.getConfiguration();
    
    // I'm not sure it is needed
    this.configuration.getFactory().setEntity( Action.TAGNAME, Action.class );
    this.configuration.getFactory().setEntity( Args.TAGNAME, Args.class );
    
    
    // Observer for catching the User status change events (login/logout)
    this.audiobox.addObserver(new Observer() {
      @Override
      public void update(Observable abx, Object evt) {
        // new User has changed its status
        
        Event event = (Event) evt;
        
        log.info("User has changed its status");
        if ( event.state == Event.States.DISCONNECTED ) {
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
        
        } else if ( event.state == Event.States.CONNECTED && wasConnected ){
          // User is logged in, and we were connected.
          // We should reconnect
          try {
            SocketClient.this.connect();
          } catch (ServiceException e) {
            log.error( "An error occurred while connecting to server", e);
            e.fireGlobally();
          }
        }
      }
    });
    
  }


  public void addHeader(String key, String value) {
    this.headers.put(key, value);
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
        url = new URL( urlStr );
        log.info("Server will be " + url.toURI().toString() );
        
      } catch (MalformedURLException e) {
        log.error("Invalid url found");
        throw new ServiceException("No valid URL for server found");
      } catch (URISyntaxException e) {
        log.error("Invalid url found");
        throw new ServiceException("No valid URL for server found");
      }
      
      this.socket = new SocketIO( url );
      if ( this.server == IConfiguration.Connectors.RAILS ) {
        this.socket.setQueryString( "chl=" + this.audiobox.getUser().getCometChannel() );
      }
      this.socket.addHeader( IConnector.X_AUTH_TOKEN_HEADER, this.audiobox.getUser().getAuthToken() );
      
      Set<String> keys = this.headers.keySet();
      for( String k : keys ){
        this.socket.addHeader( k, this.headers.get(k) );
      }
      
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
  
  
  public void setReconnectOnError(boolean value) {
    this.reconnectOnError = value;
  }
  
  public boolean isReconnectOnError() {
    return this.reconnectOnError;
  }
  
  
  public void send(String message) {
    log.warn("not implemented yet");
  }
  
  public void sendEvent(String event, String message) {
    log.warn("not implemented yet");
  }
  
  protected String getServerUrl() {
    String protocol = this.configuration.getProtocol( this.server );
    String host = this.configuration.getHost( this.server );
    String port = "" + this.configuration.getPort( this.server );
    log.info("URL found: " + protocol + "://" + host + ":" + port);
    return protocol + "://" + host + ":" + port;
  }
  
  
  @Override
  public void on(String event, IOAcknowledge ack, JsonElement... args) {
    log.info( "An event emitted " + event );
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
    Event event = new Event(new Object(), Event.States.DISCONNECTED);
    this.setChanged();
    this.notifyObservers( event );
  }


  @Override
  public void onConnect() {
    log.info("Connected!");
    SocketClient.this.connected = true;
    SocketClient.this.wasConnected = true;
    Event event = new Event(new Object(), Event.States.CONNECTED);
    this.setChanged();
    this.notifyObservers( event );
  }


  @Override
  public void onMessage(String data, IOAcknowledge ack) {
    log.info("Text message received" + data);
    log.warn("Not supported yet");
  }


  @Override
  public void onError(SocketIOException ex) {
    log.error( "Error occurs " + ex.getMessage(), ex );
    
    
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
    
    if ( shouldReconnect && this.isReconnectOnError() ){
      log.warn("try to reconnect");
      
      this.connected = false;
      this.wasConnected = false;
      
      try {
        this.connect();
      } catch( Exception e){
        log.error( "Unable to reconnect to the server", e);
      }
      
    } else {
      
      Error error = new Error();
      error.setMessage( ex.getMessage() );
      Event event = new Event( error, Event.States.ERROR );
      this.setChanged();
      this.notifyObservers( event );
      
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
        Event event = new Event(action, Event.States.ENTITY_REFRESHED);
        this.setChanged();
        this.notifyObservers(event);
        
      } else {
        log.error("Invalid message received: " + json.toString() );
      }
    } catch(Exception e) {
      log.error("Error while reveiving message: " + json.toString(), e );
    }
  }

  
  

}
