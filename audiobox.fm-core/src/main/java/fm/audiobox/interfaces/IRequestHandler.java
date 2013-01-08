package fm.audiobox.interfaces;


/**
 * This class should be used as request serializer for sending data to AudioBox.fm server.
 * <br />
 * <b>It is not used yet</b>
 */
public interface IRequestHandler {

  public String serialize(IEntity entity, IConfiguration.ContentFormat format);
  
  public String serializeJson(IEntity entity, IConfiguration.ContentFormat format);
  
  public String serializeXml(IEntity entity, IConfiguration.ContentFormat format);
  
  public String serializeText(IEntity entity, IConfiguration.ContentFormat format);
  
  public String serializeBinary(IEntity entity, IConfiguration.ContentFormat format);
  
}

