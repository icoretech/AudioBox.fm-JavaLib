package fm.audiobox.configurations;

import fm.audiobox.interfaces.IConfiguration.ContentFormat;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IRequestHandler;

/**
 * This is the default serializer used for sending data to AudioBox.fm server
 * <br />
 * <b>This class is not used yet</b>
 */
public class DefaultRequestSerializer implements IRequestHandler {

  public String serialize(IEntity entity, ContentFormat format) {
    return null;
  }

  public String serializeJson(IEntity entity, ContentFormat format) {
    return null;
  }

  public String serializeXml(IEntity entity, ContentFormat format) {
    return null;
  }

  public String serializeText(IEntity entity, ContentFormat format) {
    return null;
  }

  public String serializeBinary(IEntity entity, ContentFormat format) {
    return null;
  }

}
