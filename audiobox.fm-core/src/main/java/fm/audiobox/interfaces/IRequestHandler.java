package fm.audiobox.interfaces;

import com.google.gson.JsonElement;


/**
 * This class should be used as request serializer for sending data to AudioBox.fm server.
 * <br />
 * <b>It is not used yet</b>
 */
public interface IRequestHandler {

  public String serialize(IEntity entity, IConfiguration.ContentFormat format);
  
  public JsonElement serializeJson(IEntity entity);
  
  public Object serializeXml(IEntity entity);
  
  public String serializeText(IEntity entity);
  
  public Object serializeBinary(IEntity entity);
  
}

