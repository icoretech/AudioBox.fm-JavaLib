package fm.audiobox.configurations;

import fm.audiobox.interfaces.IConfiguration.ContentFormat;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IRequestHandler;

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
