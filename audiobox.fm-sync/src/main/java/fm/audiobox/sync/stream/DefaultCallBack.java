/***************************************************************************
 *   Copyright (C) 2010 iCoreTech research labs                            *
 *   Contributed code from:                                                *
 *   - Lucio Regina                                                        *
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

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;

import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;

import fm.audiobox.core.parsers.JParser;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IEntity;

public class DefaultCallBack extends Observable implements IOCallback {

  private static Logger log = LoggerFactory.getLogger( DefaultCallBack.class );
  private IConfiguration config;

  public DefaultCallBack( IConfiguration config ){
    this.config = config;
  }

  @Override
  public void onDisconnect() {
    log.debug("Connection terminated.");
  }

  @Override
  public void onConnect() {
    log.debug("Connection established.");

  }

  @Override
  public void onMessage(String data, IOAcknowledge ack) {
    log.debug("Server said: " + data );
  }

  @Override
  public void onMessage(JsonElement json, IOAcknowledge ack) {
    JsonElement jobj = json.getAsJsonObject().get("msg_json");
    if( jobj != null && jobj.isJsonPrimitive() ){
      
      String _msg = jobj.getAsString();
      JParser jp = new JParser( this.config );
      IEntity entity = jp.parse( _msg );
      setChanged();
      notifyObservers(entity);
    }
  }

  @Override
  public void on(String event, IOAcknowledge ack, JsonElement... args) {
    // TODO Auto-generated method stub
  }

  @Override
  public void onError(SocketIOException socketIOException) {
    log.debug("an Error occured");
    socketIOException.printStackTrace();
  }
}
