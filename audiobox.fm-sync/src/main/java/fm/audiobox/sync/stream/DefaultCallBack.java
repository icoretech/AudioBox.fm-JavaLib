package fm.audiobox.sync.stream;

import fm.audiobox.core.parsers.JParser;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IEntity;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;

import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;

public class DefaultCallBack extends Observable implements IOCallback{

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
